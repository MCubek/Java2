package hr.fer.oprpp1.custom.scripting.lexer;

import hr.fer.oprpp1.custom.scripting.elems.Element;

import java.util.Objects;

/**
 * Razred tokena
 *
 * @author matej
 */
public class Token {
    private final TokenType type;
    private final Element value;

    public Token(TokenType type, Element value) {
        this.type = Objects.requireNonNull(type);
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public Element getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (type != token.type) return false;
        return Objects.equals(value, token.value);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
