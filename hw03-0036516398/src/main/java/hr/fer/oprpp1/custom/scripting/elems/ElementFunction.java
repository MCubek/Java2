package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

public class ElementFunction implements Element {
    private final String name;

    public ElementFunction(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public String asText() {
        return name;
    }
}
