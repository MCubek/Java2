package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

public class ElementVariable implements Element {
    private final String name;

    public ElementVariable(String name) {
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
