package hr.fer.zemris.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 09/12/2020
 */
class ComplexRootedPolynomialTest {
    @Test
    void testComplexRootedPolynomial() {
        ComplexRootedPolynomial crp = new ComplexRootedPolynomial(new Complex(2, 0), Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG
        );

        assertEquals("(2.0+i0.0)*(z-(1.0+i0.0))*(z-(-1.0+i0.0))*(z-(0.0+i1.0))*(z-(0.0-i1.0))", crp.toString());
    }

    @Test
    void testComplexPolynomials() {
        ComplexRootedPolynomial crp = new ComplexRootedPolynomial(new Complex(2, 0), Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG
        );
        ComplexPolynomial cp = crp.toComplexPolynomial();

        assertEquals("(2.0+i0.0)*z^4+(0.0+i0.0)*z^3+(0.0+i0.0)*z^2+(0.0+i0.0)*z^1+(-2.0+i0.0)", cp.toString());
    }

    @Test
    void testDerived() {
        ComplexRootedPolynomial crp = new ComplexRootedPolynomial(new Complex(2, 0), Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG
        );
        ComplexPolynomial cp = crp.toComplexPolynomial();

        assertEquals("(8.0+i0.0)*z^3+(0.0+i0.0)*z^2+(0.0+i0.0)*z^1+(0.0+i0.0)", cp.derive().toString());
    }

    @Test
    void testComplexRootedApply() {
        ComplexRootedPolynomial crp = new ComplexRootedPolynomial(new Complex(5, 1), new Complex(2, 2));
        assertEquals(new Complex(10, - 24), crp.apply(new Complex(3, - 3)));
    }
}