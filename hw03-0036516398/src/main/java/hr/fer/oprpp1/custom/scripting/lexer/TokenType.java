package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Emumeracije za sve različite vrste tokena
 *
 * @author matej
 */
public enum TokenType {
    /**
     * Kraj dokumenta
     */
    EOF,
    /**
     * identifikator varijable
     */
    VARIABLE,
    STRING,
    /**
     * Text izvan tokena
     */
    TEXT,
    FUNCTION,
    /**
     * Operator +,-,*,/
     */
    OPERATOR,
    INTEGER,
    DOUBLE,
    /**
     * Početak taga
     */
    TAGSTART,
    /**
     * Kraj taga
     */
    TAGEND,
    /**
     * Tag
     */
    TAGNAME
}
