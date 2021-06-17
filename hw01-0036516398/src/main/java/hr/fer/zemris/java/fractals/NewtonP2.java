package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.io.Serial;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Razred koji ilustrira faktale iz Newton-Raphsonove iteracije.
 * Fraktali se prikazuju korištenjem više dretvi radi poboljšanih
 * performanci koristeći rekuzrivnost i ForkJoinPool.
 *
 * @author MatejCubek
 * @project hw01-0036516398
 * @created 12/03/2021
 */
public class NewtonP2 {
    /**
     * Main metoda koja prima argumente, pokrece program komandne linije gdje se ispituju korijeni
     * i iscrtava fraktal.
     * <p>
     * Dopusteni argumenti za minimalni broj staza (onaj broj kada se kreće slijedno računati) su <code>--mintracks=</code> ili <code>-wm</code>
     * Argument mora biti veći od 2.
     * Dopušteno je pozvati bez argumenata te se onda koristi podrazumijevana vrijednost od 16.
     *
     * @param args argumenti
     */
    public static void main(String[] args) {

        int minTracks = 16;
        boolean minTracksSet = false;

        //Parsiranje argumenata
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.matches("--mintracks=\\d+")) {
                if (minTracksSet) throw new IllegalArgumentException("Duplicate argument!");
                minTracksSet = true;
                int number = Integer.parseInt(arg.replace("--mintracks=", ""));
                if (number > 2)
                    minTracks = number;
            } else if (arg.equals("-m")) {
                if (minTracksSet) throw new IllegalArgumentException("Duplicate argument!");
                minTracksSet = true;
                int number = Integer.parseInt(args[++ i]);
                if (number > 2)
                    minTracks = number;
            } else {
                throw new IllegalArgumentException(String.format("Argument %s is not valid!", arg));
            }
        }
        var polynomial = Newton.newtonCommandLine();
        FractalViewer.show(new BetterFractalProducer(polynomial, minTracks));
    }

    /**
     * Implementacija sučelja IFractalProducer koji uz paralelizaciju ForkJoinPoolom
     * crta Newtonov fraktal.
     */
    private static class BetterFractalProducer implements IFractalProducer {

        private ForkJoinPool pool;

        private final ComplexRootedPolynomial complexRootedPolynomial;
        private final ComplexPolynomial complexPolynomial;
        private final ComplexPolynomial derived;

        private static final double THRESHOLD = 1E-3;
        private static final int ITERATIONS = 16 * 16 + 16;
        private static final double ROOT_DISTANCE = 0.002;

        private final int minTracks;

        public BetterFractalProducer(ComplexRootedPolynomial complexRootedPolynomial, int minTracks) {
            this.complexRootedPolynomial = complexRootedPolynomial;
            this.complexPolynomial = complexRootedPolynomial.toComplexPolynomial();
            this.derived = complexPolynomial.derive();
            this.minTracks = minTracks;
        }

        @Override
        public void setup() {
            pool = new ForkJoinPool();
            System.out.printf("MinTracks: %d\n", minTracks);
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            short[] data = new short[width * height];

            /*
                Klasa rekurzivne akcije za paralelizaciju
             */
            class RecursiveNewton extends RecursiveAction {
                @Serial
                private static final long serialVersionUID = 1L;
                private final int yMin;
                private final int yMax;

                public RecursiveNewton(int yMin, int yMax) {
                    this.yMin = yMin;
                    this.yMax = yMax;
                }

                @Override
                protected void compute() {
                    int tracks = yMax - yMin;
                    if (tracks <= minTracks) {
                        computeDirect();
                        return;
                    }

                    //Računanje sredine
                    int middleLow = (yMax + yMin) / 2;
                    int middleHigh = middleLow + 1;

                    RecursiveNewton one = new RecursiveNewton(yMin, middleLow);
                    RecursiveNewton two = new RecursiveNewton(middleHigh, yMax);
                    invokeAll(one, two);
                }

                //Direktno računanje fraktala
                private void computeDirect() {
                    Newton.calculate(reMin, reMax, imMin, imMax, width, height, ITERATIONS, ROOT_DISTANCE,
                            THRESHOLD, yMin, yMax, data, cancel,
                            complexRootedPolynomial, complexPolynomial, derived);
                }
            }

            RecursiveNewton work = new RecursiveNewton(0, height - 1);

            pool.invoke(work);

            observer.acceptResult(data, (short) (complexPolynomial.order() + 1), requestNo);
        }

        @Override
        public void close() {
            pool.shutdown();
        }
    }

}
