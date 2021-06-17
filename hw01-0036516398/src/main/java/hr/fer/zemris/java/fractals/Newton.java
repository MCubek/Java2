package hr.fer.zemris.java.fractals;

import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Razred koji ilustrira pomoćne metode računanja faktala iz Newton-Raphsonove iteracije.
 * Razred također sadrži statičke metode koje služe generiranju takvih fraktala
 * i one su javno dostupne.
 *
 * @author MatejCubek
 * @project hw01-0036516398
 * @created 12/03/2021
 */
class Newton {

    /**
     * Metoda koja sa pomocu konzole korisnika trazi korijene i stvara
     * objekt <code>ComplexRootedPolynomial</code> kojega i vraća.
     *
     * @return objekt <code>ComplexRootedPolynomial</code>
     */
    static ComplexRootedPolynomial newtonCommandLine() {
        System.out.print("""
                Welcome to Newton-Raphson iteration-based fractal viewer.
                Please enter at least two roots, one root per line. Enter 'done' when done.
                """);

        Scanner scanner = new Scanner(System.in);
        int numRoots = 1;
        List<Complex> roots = new ArrayList<>();

        while (true) {
            System.out.printf("Root %d> ", numRoots);
            String input = scanner.nextLine();

            if (input.equals("done")) {
                if (numRoots < 3) {
                    System.out.println("Please enter at least two roots!");
                } else {
                    break;
                }
            }
            if (input.equals("")) {
                roots.add(Complex.ONE);
                roots.add(Complex.ONE_NEG);
                roots.add(Complex.IM);
                roots.add(Complex.IM_NEG);
                break;
            }

            try {
                Complex complex = Complex.parse(input);

                roots.add(complex);
                numRoots++;

            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
        scanner.close();
        System.out.println("Image of fractal will appear shortly. Thank you.");

        return new ComplexRootedPolynomial(Complex.ONE, roots.toArray(new Complex[0]));
    }

    /**
     * Metoda preslikava zadane vrijednosti u kompleksni broj s njihovom vrijednosti
     * na trenutno vidljivoj ravnini.
     *
     * @return novi preslikani kompleksni broj.
     */
    static Complex mapToComplexPlain(int x, int y, double reMin, double imMin, double reMax, double imMax,
                                            int width, int height) {
        double real = x / (width - 1.0) * (reMax - reMin) + reMin;
        double imaginary = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;
        return new Complex(real, imaginary);
    }

    /**
     * Metoda računa potreben vrijednosti i sprema ih u data polje kako bi se mogao vizualno prikazati fraktal.
     * Metoda omogućava zadavanje minimalne i maksimalne linije kao potporu moguće paralelnosti izvođenja.
     */
    static void calculate(double reMin, double reMax, double imMin, double imMax, int width,
                                 int height, int iterationsLimit, double rootDistance, double threshold,
                                 int yMin, int yMax, short[] data, AtomicBoolean cancel,
                                 ComplexRootedPolynomial complexRootedPolynomial, ComplexPolynomial complexPolynomial,
                                 ComplexPolynomial derived) {

        int offset = yMin * width;

        for (int y = yMin; y <= yMax && ! cancel.get(); y++) {
            for (int x = 0; x < width; x++) {
                Complex complex = mapToComplexPlain(x, y, reMin, imMin, reMax, imMax, width, height);

                double module;
                int iterations = 0;

                Complex old;

                do {
                    Complex numerator = complexPolynomial.apply(complex);
                    Complex denominator = derived.apply(complex);

                    old = new Complex(complex.getReal(), complex.getImaginary());

                    complex = complex.sub(numerator.divide(denominator));

                    iterations++;
                    module = old.sub(complex).module();
                } while (module > threshold && iterations < iterationsLimit);

                int index = complexRootedPolynomial.indexOfClosestRootFor(complex, rootDistance);
                data[offset++] = (short) (index + 1);
            }
        }
    }

}
