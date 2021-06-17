package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Razred koji ilustrira faktale iz Newton-Raphsonove iteracije.
 * Fraktali se prikazuju korištenjem više dretvi radi poboljšanih
 * performanci.
 *
 * @author MatejCubek
 * @project hw01-0036516398
 * @created 12/03/2021
 */
public class NewtonP1 {
    /**
     * Main metoda koja prima argumente, pokrece program komandne linije gdje se ispituju korijeni
     * i iscrtava fraktal.
     * <p>
     * Dopusteni argumenti su <code>--workers=</code> ili <code>-w</code>, te
     * <code>--tracks</code> ili <code>-t</code>.
     * Dopušteno je pozvati bez argumenata te se onda koriste podrazumijevane vrijednosti ovisne o računalu.
     *
     * @param args argumenti
     */
    public static void main(String[] args) {

        int workers = Runtime.getRuntime().availableProcessors();
        int tracks = workers * 4;
        boolean workersSet = false;
        boolean tracksSet = false;

        //Parsiranje argumenata
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.matches("--workers=\\d+")) {
                if (workersSet) throw new IllegalArgumentException("Duplicate argument!");
                workersSet = true;
                int number = Integer.parseInt(arg.replace("--workers=", ""));
                if (number > 0)
                    workers = number;
            } else if (arg.matches("--tracks=\\d+")) {
                if (tracksSet) throw new IllegalArgumentException("Duplicate argument!");
                tracksSet = true;
                int number = Integer.parseInt(arg.replace("--tracks=", ""));
                if (number > 0)
                    tracks = number;
            } else if (arg.equals("-w")) {
                if (workersSet) throw new IllegalArgumentException("Duplicate argument!");
                workersSet = true;
                int number = Integer.parseInt(args[++ i]);
                if (number > 0)
                    workers = number;
            } else if (arg.equals("-t")) {
                if (tracksSet) throw new IllegalArgumentException("Duplicate argument!");
                tracksSet = true;
                int number = Integer.parseInt(args[++ i]);
                if (number > 0)
                    tracks = number;
            } else {
                throw new IllegalArgumentException(String.format("Argument %s is not valid!", arg));
            }
        }
        var polynomial = Newton.newtonCommandLine();
        FractalViewer.show(new BetterFractalProducer(polynomial, workers, tracks));
    }

    /**
     * Implementacija sučelja IFractalProducer koji uz paralelizaciju ExecutorServiceom
     * crta Newtonov fraktal.
     */
    private static class BetterFractalProducer implements IFractalProducer {

        private ExecutorService pool;

        private final ComplexRootedPolynomial complexRootedPolynomial;
        private final ComplexPolynomial complexPolynomial;
        private final ComplexPolynomial derived;

        private static final double THRESHOLD = 1E-3;
        private static final int ITERATIONS = 16 * 16 + 16;
        private static final double ROOT_DISTANCE = 0.002;

        private final int workers;
        private final int tracks;

        public BetterFractalProducer(ComplexRootedPolynomial complexRootedPolynomial, int workers, int tracks) {
            this.complexRootedPolynomial = complexRootedPolynomial;
            this.complexPolynomial = complexRootedPolynomial.toComplexPolynomial();
            this.derived = complexPolynomial.derive();
            this.workers = workers;
            this.tracks = tracks;
        }

        @Override
        public void setup() {
            pool = Executors.newFixedThreadPool(workers);
            System.out.printf("Threads: %d\n", workers);
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {

            short[] data = new short[width * height];

            /*
              Staticka klasa koja demonstrira posao racunanja linija ilustracije fraktala.
              Klasa implementira sučelje <code>Runnable</code>
             */
            class ParallelNewton implements Runnable {
                private final int yMin;
                private final int yMax;

                public ParallelNewton(int yMin, int yMax) {
                    this.yMin = yMin;
                    this.yMax = yMax;
                }

                @Override
                public void run() {
                    Newton.calculate(reMin, reMax, imMin, imMax, width, height, ITERATIONS, ROOT_DISTANCE,
                            THRESHOLD, yMin, yMax, data, cancel,
                            complexRootedPolynomial, complexPolynomial, derived);
                }
            }
            //Racunanje broja traka za svaku dretvu i ukupnih
            int currentTrackNumber = Math.min(tracks, height);
            System.out.printf("Tracks: %d\n", currentTrackNumber);
            int numberOfLinesPerTrack = height / currentTrackNumber;

            //Stvaranje poslova i slanje poolu dretvi
            List<Future<?>> results = new ArrayList<>();
            for (int i = 0; i < currentTrackNumber; i++) {
                int yMin = i * numberOfLinesPerTrack;
                int yMax = (i + 1) * numberOfLinesPerTrack - 1;
                if (i == currentTrackNumber - 1) {
                    yMax = height - 1;
                }

                ParallelNewton work = new ParallelNewton(yMin, yMax);
                results.add(pool.submit(work));
            }

            //Cekanje da sve zavrse
            for (Future<?> future : results) {
                while (true) {
                    try {
                        future.get();
                        break;
                    } catch (InterruptedException ignore) {

                    } catch (ExecutionException executionException) {
                        throw new RuntimeException(executionException.getMessage());
                    }
                }
            }
            //Slanje rezultata
            observer.acceptResult(data, (short) (complexPolynomial.order() + 1), requestNo);
        }

        @Override
        public void close() {
            pool.shutdown();
        }
    }


}
