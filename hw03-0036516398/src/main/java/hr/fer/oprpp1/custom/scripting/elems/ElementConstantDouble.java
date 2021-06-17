package hr.fer.oprpp1.custom.scripting.elems;

public class ElementConstantDouble implements Element {
    private final double value;

    public ElementConstantDouble(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String asText() {
        return String.valueOf(value);
    }
}
