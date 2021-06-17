package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * ComplexPolynomial koji modelira polinom nad kompleksim brojevima, prema predlošku u nastavku.
 * Radi se o polinomu f(z) oblika zn*zn+zn-1*zn-1+...+z2*z2+z1*z+z0,
 * gdje su z0 do zn koeficijenti koji pišu uz odgovarajuće potencije od z (i zadaje ih korisnik kroz konstruktor).
 * Primjetite, radi se o polinomu n-tog stupnja (što još zovemo red – engl. polinom order).
 * Svi koeficijenti zadaju se kao kompleksni brojevi, a i sam z je kompleksan broj.
 *
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 09/12/2020
 */
public class ComplexPolynomial {
    private final List<Complex> factors;

    public ComplexPolynomial(Complex... factors) {
        this.factors = Arrays.asList(factors);
    }

    /**
     * Metoda vraća red polinoma
     *
     * @return red polinoma
     */
    public short order() {
        return (short) (factors.size() - 1);
    }

    /**
     * Metoda množi polinome i vraća novi polinom
     *
     * @param polynomial drugi polinom
     * @return pomnozeni polinomi
     */
    public ComplexPolynomial multiply(ComplexPolynomial polynomial) {
        int finalOrder = order() + polynomial.order();
        Complex[] array = new Complex[finalOrder + 1];
        Arrays.fill(array, Complex.ZERO);

        for (int i = 0; i < factors.size(); i++) {
            for (int j = 0; j < polynomial.factors.size(); j++) {
                int order = finalOrder - (order() - i) - (polynomial.order() - j);
                Complex value = factors.get(i).multiply(polynomial.factors.get(j));

                if (array[order] == null) {
                    array[order] = value;
                } else {
                    array[order] = array[order].add(value);
                }
            }
        }

        return new ComplexPolynomial(array);
    }

    /**
     * Metoda derivira polinom
     *
     * @return novi derivirani polinom
     */
    public ComplexPolynomial derive() {
        Complex[] array = new Complex[order()];

        for (int i = 0; i < factors.size() - 1; i++) {
            array[i] = factors.get(i + 1).multiply(new Complex(i + 1, 0));
        }

        return new ComplexPolynomial(array);
    }

    /**
     * Metoda izracunava vrijednost za z
     *
     * @param z kompleksan broj
     * @return novi kompleksan broj
     */
    public Complex apply(Complex z) {
        Complex result = Complex.ZERO;

        for (int i = 0; i < factors.size(); i++) {
            result = result.add(factors.get(i).multiply(z.power(i)));
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = factors.size() - 1; i >= 0; i--) {
            sb.append(String.format("(%s)", factors.get(i).toString()));
            if (i != 0) {
                sb.append(String.format("*z^%d+", i));
            }
        }

        return sb.toString();
    }
}
