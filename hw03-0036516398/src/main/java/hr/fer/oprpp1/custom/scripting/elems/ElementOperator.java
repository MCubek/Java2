package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

public class ElementOperator implements Element {
    private final String symbol;

    public ElementOperator(String symbol) {
        this.symbol = Objects.requireNonNull(symbol);
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String asText() {
        return symbol;
    }
}
