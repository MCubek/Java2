package hr.fer.oprpp1.custom.scripting.lexer;

import hr.fer.oprpp1.custom.scripting.elems.*;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lekser koji ulazni dokument razbija na tokene.
 *
 * @author matej
 */
public class SmartScriptLexer {
    private final String data;
    private int currentPos;
    private Token currentToken;
    private LexerState lexerState;

    public SmartScriptLexer(String data) {
        this.data = Objects.requireNonNull(data);
        this.currentPos = 0;
        lexerState = LexerState.TEXT;
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    /**
     * Metoda izlučuje sljedeći token, postavlja ga kao trenutnog
     * i vraća ga.
     *
     * @return idući token
     * @throws SmartScriptLexerException ako dođe do problema kod tokenizacije
     */
    public Token nextToken() {
        extractNextToken();
        return getCurrentToken();
    }

    /**
     * Privatna metoda koja iz dokumenta -> niza char
     * izvlači svaki token jedan po jedan i sprema ga u klasu.
     *
     * @throws SmartScriptLexerException ako dođe do greške
     */
    private void extractNextToken() {
        //Nema više tokena
        if (currentToken != null && currentToken.getType() == TokenType.EOF)
            throw new SmartScriptLexerException("No tokens available.");

        //Generiraj EOF ako nema znakova
        if (currentPos >= data.length()) {
            currentToken = new Token(TokenType.EOF, null);
            return;
        }

        char character;

        if (lexerState == LexerState.TEXT) {
            StringBuilder sb = new StringBuilder();
            boolean escaped = false;

            while (currentPos < data.length()) {
                character = data.charAt(currentPos);

                //Prvi escape
                if (! escaped && character == '\\') {
                    escaped = true;
                    currentPos++;
                    continue;
                }

                //Escapean je \
                if (escaped && character == '\\') {
                    escaped = false;
                    sb.append(character);
                    currentPos++;
                    continue;

                } else if (escaped && character == '{') {
                    escaped = false;
                    sb.append(character);
                    currentPos++;
                    continue;

                } else if (escaped) throw new SmartScriptLexerException("Wrongly escaped.");

                //Počeo je tagirani block
                if (data.startsWith("{$",currentPos)) {
                    lexerState = LexerState.TAG;

                    //Nije bilo texta
                    if (sb.length() == 0) {
                        extractNextToken();
                    } else {
                        currentToken = new Token(TokenType.TEXT, new ElementString(sb.toString()));
                    }
                    //Vrati do sada procitan text
                    return;
                }

                sb.append(character);
                currentPos++;

            }
            currentToken = new Token(TokenType.TEXT, new ElementString(sb.toString()));

        } else {
            //Početak tag
            if (data.startsWith("{$", currentPos)) {
                currentToken = new Token(TokenType.TAGSTART, null);
                currentPos += 2;
                return;
            }

            skipBlanks();
            character = data.charAt(currentPos);

            //Kraj tag
            if (data.startsWith("$}", currentPos)) {
                currentToken = new Token(TokenType.TAGEND, null);
                currentPos += 2;
                lexerState = LexerState.TEXT;
                return;
            } else if (character == '$') throw new SmartScriptLexerException("Tag ending is not valid!");

            //TAGS
            if (data.startsWith("=", currentPos)) {
                currentPos++;
                currentToken = new Token(TokenType.TAGNAME, new ElementVariable("="));
                return;
            }
            if (data.toUpperCase().startsWith("FOR", currentPos)) {
                currentPos += 3;
                currentToken = new Token(TokenType.TAGNAME, new ElementVariable("FOR"));
                return;
            }
            if (data.toUpperCase().startsWith("END", currentPos)) {
                currentPos += 3;
                currentToken = new Token(TokenType.TAGNAME, new ElementVariable("END"));
                return;
            }

            int endOfLineIndex = data.indexOf("\r", currentPos);
            if (endOfLineIndex == - 1) endOfLineIndex = data.length();

            //Funkcija
            if (character == '@') {
                currentPos++;
                Matcher m = Pattern.compile("\\A([a-zA-Z]+\\w*)[ $\\r\\n].*",Pattern.UNICODE_CHARACTER_CLASS).matcher(data.substring(currentPos));
                if (! m.find())
                    throw new SmartScriptLexerException("Function incorrectly defined.");

                String name = m.group(1);
                currentPos += name.length();
                currentToken = new Token(TokenType.FUNCTION, new ElementFunction(name));
                return;
            }

            //Varijabla
            if (Character.isLetter(character)) {
                Matcher m = Pattern.compile("\\A([a-zA-Z]\\w*)[ $].*",Pattern.UNICODE_CHARACTER_CLASS).matcher(data.substring(currentPos, endOfLineIndex));
                if (! m.find())
                    throw new SmartScriptLexerException("Variable incorrectly defined.");

                String name = m.group(1);
                currentPos += name.length();
                currentToken = new Token(TokenType.VARIABLE, new ElementVariable(name));
                return;
            }

            //Integer
            if (data.substring(currentPos, endOfLineIndex).matches("\\A[+-]?\\d+[ $].*")) {
                Matcher m = Pattern.compile("\\A([+-]?\\d+)[ $].*").matcher(data.substring(currentPos, endOfLineIndex));
                if (! m.find()) {
                    throw new SmartScriptLexerException("Integer is incorrectly defined.");
                }

                String valueStr = m.group(1);
                currentPos += valueStr.length();
                currentToken = new Token(TokenType.INTEGER, new ElementConstantInteger(Integer.parseInt(valueStr)));
                return;
            }

            //Double
            if (data.substring(currentPos, endOfLineIndex).matches("\\A[+-]?\\d+\\.\\d+[ $].*")) {
                Matcher m = Pattern.compile("\\A([+-]?\\d+\\.\\d+)[ $].*").matcher(data.substring(currentPos, endOfLineIndex));
                if (! m.find()) {
                    throw new SmartScriptLexerException("Double is incorrectly defined.");
                }

                String valueStr = m.group(1);
                currentPos += valueStr.length();
                currentToken = new Token(TokenType.DOUBLE, new ElementConstantDouble(Double.parseDouble(valueStr)));
                return;
            }

            //String unutar taga
            //Može kroz više redaka
            if (character == '"') {
                var substring = data.substring(currentPos);
//                var oldRegex = "\\A\"([=.\\w_/\"\\\\\\s]+)\".*\\$}.*";
                Matcher m = Pattern.compile("\"(([^\"\\\\]|(\\\\\")|(\\\\\\\\)|(\\\\n)|(\\\\t)|(\\\\r))+)\"",Pattern.UNICODE_CHARACTER_CLASS).matcher(substring);
                if (! m.find()) {
                    throw new SmartScriptLexerException("String inside of tag incorrectly defined.");
                }

                String stringValue = m.group(1);
                currentPos += stringValue.length() + 2;
                stringValue = stringValue.replace("\\r", "\r").replace("\\n","\n");
                currentToken = new Token(TokenType.STRING, new ElementString(stringValue));
                return;
            }

            //Operator
            if (isOperator(character)) {
                currentPos++;
                currentToken = new Token(TokenType.OPERATOR, new ElementOperator(String.valueOf(character)));
                return;
            }

            throw new SmartScriptLexerException("Lexer has failed to produce a token!");

        }

    }

    /**
     * Privatna metoda koja preskače sve praznine u nizu char
     */
    private void skipBlanks() {
        while (currentPos < data.length()) {
            char c = data.charAt(currentPos);
            if (Character.isWhitespace(c)) {
                currentPos++;
                continue;
            }
            break;
        }
    }

    /**
     * Privatna metoda koja testira je li character operator
     * Dopušteni operatori su '+','-','*','/','^'
     *
     * @param character znak koji isprobavamo je li operator
     * @return istina ako je znak operator, laz inace
     */
    private boolean isOperator(char character) {
        return character == '+' || character == '-' || character == '*' || character == '/' || character == '^';
    }

    /**
     * Stanja lexera
     */
    private enum LexerState {
        TEXT,
        TAG
    }
}
