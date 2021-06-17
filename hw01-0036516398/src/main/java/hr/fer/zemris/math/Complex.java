package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.*;
import static java.lang.Math.PI;

/**
 * Razred nepromijenjivih kompleksnih brojeva
 *
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 09/12/2020
 */
public class Complex {
    private final double real;
    private final double imaginary;

    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE = new Complex(1, 0);
    public static final Complex ONE_NEG = new Complex(- 1, 0);
    public static final Complex IM = new Complex(0, 1);
    public static final Complex IM_NEG = new Complex(0, - 1);

    private static final double EXPONENT = 1E-10;

    /**
     * Konstruktor koji stvara kompleksni broj s ulaznim parametrima
     *
     * @param real      realni dio
     * @param imaginary imaginarni dio
     */
    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * Podrazumijevani konstruktor koji stvara kompleksni broj <code>0+0i</code>
     */
    public Complex() {
        this(0, 0);
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    /**
     * Metoda računa modul kompleksnog broja
     *
     * @return modul
     */
    public double module() {
        return Math.hypot(real, imaginary);
    }

    /**
     * Metoda mnozi broj s onim zadanim u parametru i vraća novi kompleksni broj s rezultatom
     *
     * @param c drugi kompleksni broj
     * @return novi kompleksni broj pomnozen
     * @throws NullPointerException ako je predan null
     */
    public Complex multiply(Complex c) {
        Objects.requireNonNull(c);
        return new Complex(real * c.real - imaginary * c.imaginary, real * c.imaginary + imaginary * c.real);
    }

    /**
     * Metoda dijeli broj s onim zadanim u parametru i vraća novi kompleksni broj s rezultatom
     *
     * @param c drugi kompleksni broj
     * @return novi kompleksni broj podijeljen
     * @throws NullPointerException ako je predan null
     */
    public Complex divide(Complex c) {
        Objects.requireNonNull(c);
        double divisor = c.real * c.real + c.imaginary * c.imaginary;

        double realPart = (real * c.real + imaginary * c.imaginary) / divisor;
        double imaginaryPart = (imaginary * c.real - real * c.imaginary) / divisor;
        return new Complex(realPart, imaginaryPart);
    }

    /**
     * Metoda zbraja dva kompleksna broja i vraća novi kompleksni broj s rezultatom
     *
     * @param c drugi kompleksni broj
     * @return novi zbrojeni kompleksni broj
     * @throws NullPointerException ako je predan null
     */
    public Complex add(Complex c) {
        Objects.requireNonNull(c);
        return new Complex(real + c.real, imaginary + c.imaginary);
    }

    /**
     * Metoda oduzima dva kompleksna broja i vraća novi kompleksni broj s rezultatom
     *
     * @param c drugi kompleksni broj
     * @return novi oduzeti kompleksni broj
     * @throws NullPointerException ako je predan null
     */
    public Complex sub(Complex c) {
        Objects.requireNonNull(c);
        return new Complex(real - c.real, imaginary - c.imaginary);
    }

    /**
     * Metoda racuna negaciju od kompleksnog broja i vraca novi negiran
     *
     * @return novi kompleksni broj negiran
     */
    public Complex negate() {
        return new Complex(- real, - imaginary);
    }

    /**
     * Metoda racuna n-tu potenciju kompleksnog broja i vraca novi kompleksni broj
     *
     * @param n potencija kompleksnog broja
     * @return novi kompleksni broj koji je potencija
     * @throws IllegalArgumentException ako je predan eksponent manji od 0
     */
    public Complex power(int n) {
        if (n < 0) throw new IllegalArgumentException("Exponent must be >=0!");
        double angle = getAngle();
        double magnitude = module();
        return Complex.fromMagnitudeAndAngle(pow(magnitude, n), n * angle);
    }

    private double getAngle() {
        double angle;
        if (real == 0 || imaginary == 0) {
            if (imaginary == 0) return real >= 0 ? 0 : PI;
            return imaginary >= 0 ? PI / 2 : 3 * PI / 2;
        }
        angle = atan(imaginary / real);

        if (real < 0)
            angle = angle + PI;

        if (angle < 0)
            angle += 2 * PI;

        return angle;
    }

    private static Complex fromMagnitudeAndAngle(double magnitude, double angle) {
        return new Complex(magnitude * cos(angle), magnitude * sin(angle));
    }

    /**
     * Metoda racuna n-ti korijen kompleksnog broja i vraca novi kompleksni broj
     *
     * @param n koji korijen kompleksnog broja
     * @return nova lista korijena novih kompleksnih brojeva
     * @throws IllegalArgumentException ako je predan eksponent manji ili jednak 0
     */
    public List<Complex> root(int n) {
        if (n <= 0) throw new IllegalArgumentException("Root must be > 0!");
        double angle = getAngle();
        double magnitude = module();

        List<Complex> list = new ArrayList<>(n);

        for (int i = 0; i < n; i++)
            list.add(Complex.fromMagnitudeAndAngle(pow(magnitude, 1.0 / n), (angle + 2 * i * PI) / n));

        return list;
    }

    /**
     * Metoda stvara novi kompleksni broj iz Stringa
     * Npr. (1.2+i2.1), (-2.5), (i), (+22-i12)
     *
     * @param s string koji predstavlja kompleksan broj
     * @return novi kompleksan broj
     */
    public static Complex parse(String s) {
        String string = s.strip();

        if (string.matches(".*(\\+\\+|--|\\+-|-\\+).*") || ! string.matches("[\\d.i+\\- ]+"))
            throw new IllegalArgumentException("String is doesnt match required form");

        double real = 0.0, imaginary = 0.0;

        Pattern pattern = Pattern.compile("[+\\-]? *i?\\d+\\.?\\d*");
        Matcher matcher = pattern.matcher(s);
        Pattern patternOnlyI = Pattern.compile("-i[^\\d]+|\\+i[^\\d]+|\\A-?i\\z");
        Matcher matcherOnlyI = patternOnlyI.matcher(s);

        String value;
        int counter = 0;

        while (matcher.find()) {
            counter++;
            value = matcher.group();
            if (value.contains("i")) {
                imaginary = Double.parseDouble(value.replace("i", "").replace(" ",""));
            } else {
                real = Double.parseDouble(value.replace(" ",""));
            }
        }

        while (matcherOnlyI.find()) {
            counter++;
            value = matcherOnlyI.group();
            imaginary = Double.parseDouble(value.replace("i", "1.0"));
        }

        if (counter > 2)
            throw new IllegalArgumentException("String is invalid, contains more then 2 numbers!");

        if (counter == 0)
            throw new IllegalArgumentException("String is invalid, contains 0 numbers!");

        return new Complex(real, imaginary);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(String.format(Locale.ROOT, "%.1f", real));

        if (imaginary >= 0)
            stringBuilder.append("+");
        else
            stringBuilder.append("-");
        stringBuilder.append("i").append(String.format(Locale.ROOT, "%.1f", Math.abs(imaginary)));

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Complex that = (Complex) o;

        if (Math.abs(that.real - real) >= EXPONENT) return false;
        return Math.abs(that.imaginary - imaginary) <= EXPONENT;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(real);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(imaginary);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
