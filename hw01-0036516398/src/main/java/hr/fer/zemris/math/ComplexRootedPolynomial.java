package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ComplexRootedPolynomial koji modelira polinom nad kompleksim brojevima, prema predlošku u nastavku.
 * Radi se o polinomu f(z) oblika z0*(z-z1)*(z-z2)*...*(z-zn), gdje su z1 do zn njegove nultočke a z0 konstanta
 * (sve njih zadaje korisnik kroz konstruktor).
 * Primjetite, radi se o polinomu n-tog stupnja (kada biste izmnožili zagrade).
 * Svi zi zadaju se kao kompleksni brojevi, a i sam z je kompleksan broj.
 *
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 09/12/2020
 */
public class ComplexRootedPolynomial {
    private final Complex constant;
    private final List<Complex> roots;

    public ComplexRootedPolynomial(Complex constant, Complex... roots) {
        this.constant = Objects.requireNonNull(constant);
        this.roots = Arrays.asList(roots);
    }

    /**
     * Metoda racuna vrijednost polinoma za zadani broj
     *
     * @param z zadani broj
     * @return vrijednost polinoma
     */
    public Complex apply(Complex z) {
        Complex result = constant;

        for (Complex root : roots) {
            result = result.multiply(z.sub(root));
        }

        return result;
    }

    public ComplexPolynomial toComplexPolynomial() {
        ComplexPolynomial complexPolynomial = new ComplexPolynomial(constant);

        for (Complex root : roots) {
            complexPolynomial = complexPolynomial.multiply(new ComplexPolynomial(root.negate(), Complex.ONE));
        }

        return complexPolynomial;
    }

    @Override
    public String toString() {
        return String.format("(%s)", constant.toString()) + roots.stream()
                .map(Complex::toString)
                .collect(Collectors.joining("))*(z-(", "*(z-(", "))"));
    }

    /**
     * Trazi index najblizeg korjena za kompleksni broj z koji je unutrar intervala.
     * Ako nema takvog broj vraca -1.
     * Prvi korijen ima index 0 drugi 1 itd.
     *
     * @param z         korijen koji trazimo
     * @param threshold maksimalni index
     * @return index korijena
     */
    public int indexOfClosestRootFor(Complex z, double threshold) {
        double distance = threshold;
        int index = - 1;

        for (int i = 0, rootsSize = roots.size(); i < rootsSize; i++) {
            Complex root = roots.get(i);
            double calculatedDistance = z.sub(root).module();

            if (calculatedDistance < distance) {
                index = i;
                distance = calculatedDistance;
            }
        }
        return index;
    }
}
