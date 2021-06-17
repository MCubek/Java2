package hr.fer.oprpp1.custom.scripting.exec;

/**
 * Value Wrapper helper class for encapsulating objects.
 * Contains useful methods for numberic operations if applicable.
 *
 * @author MatejCubek
 * @project hw03-0036516398
 * @created 29/03/2021
 */
public class ValueWrapper {
    private Object value;

    public ValueWrapper(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void add(Object incValue) {
        var n1 = parseNumber(value);
        var n2 = parseNumber(incValue);

        if (n1 instanceof Integer && n2 instanceof Integer)
            value = n1.intValue() + n2.intValue();
        else
            value = n1.doubleValue() + n2.doubleValue();
    }

    public void subtract(Object decValue) {
        var n1 = parseNumber(value);
        var n2 = parseNumber(decValue);

        if (n1 instanceof Integer && n2 instanceof Integer)
            value = n1.intValue() - n2.intValue();
        else
            value = n1.doubleValue() - n2.doubleValue();
    }

    public void multiply(Object mulValue) {
        var n1 = parseNumber(value);
        var n2 = parseNumber(mulValue);

        if (n1 instanceof Integer && n2 instanceof Integer)
            value = n1.intValue() * n2.intValue();
        else
            value = n1.doubleValue() * n2.doubleValue();
    }

    public void divide(Object divValue) {
        var n1 = parseNumber(value);
        var n2 = parseNumber(divValue);

        if (n1 instanceof Integer && n2 instanceof Integer)
            value = n1.intValue() / n2.intValue();
        else
            value = n1.doubleValue() / n2.doubleValue();
    }

    /**
     * Compares values of two objects that can be converted to numbers.
     *
     * @param withValue other object
     * @return the value 0 if this object is equal to the argument object; a value less than 0 if this object is numerically less than the argument object; and a value greater than 0 if this object is numerically greater than the argument
     */
    public int numCompare(Object withValue) {
        var n1 = parseNumber(value);
        var n2 = parseNumber(withValue);

        if (n1 instanceof Integer int1 && n2 instanceof Integer int2)
            return int1.compareTo(int2);
        return Double.compare(n1.doubleValue(), n2.doubleValue());
    }

    /**
     * Parse object into Number.
     *
     * @param number objecto to make a number
     * @return {@link Number} object of value
     * @throws RuntimeException If object isn't one of required class
     */
    private Number parseNumber(Object number) {
        if (number instanceof ValueWrapper valueWrapper)
            number = valueWrapper.value;
        checkValidArgument(number);
        if (number == null) return 0;

        try {
            if (number instanceof String stringValue) {
                if (stringValue.contains(".") || stringValue.toLowerCase().contains("e"))
                    return Double.parseDouble(stringValue);
                return Integer.parseInt(stringValue);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e.getMessage());
        }

        assert number instanceof Number;
        return (Number) number;
    }

    /**
     * Check if object is valid for operation.
     * Permitted are null, Integer, Double and String
     *
     * @param object object to check
     * @throws RuntimeException If object isnt one of required class
     */
    private void checkValidArgument(Object object) {
        if (! (object == null || object instanceof Integer || object instanceof Double || object instanceof String)) {
            throw new RuntimeException("Operation not permitted on class type " + object.getClass());
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
