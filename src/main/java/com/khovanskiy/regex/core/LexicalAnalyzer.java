package com.khovanskiy.regex.core;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

/**
 * @author Victor Khovanskiy
 */
public class LexicalAnalyzer extends InputStream {
    private enum Mode {
        READ, END, ERROR
    }
    private int currentPosition = -1;
    private int currentChar;
    private Token currentToken;
    private InputStream is;
    private Mode mode = Mode.READ;

    public LexicalAnalyzer(InputStream is) {
        this.is = is;
    }

    /**
     * Gets current position
     *
     * @return current position
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Gets current token
     *
     * @return current token
     */
    public Token getCurrentToken() {
        return currentToken;
    }

    /**
     * Gets current value of token
     *
     * @return current value
     */
    public String getCurrentValue() {
        return (char)currentChar+ "";
    }
    /**
     * Reads and returns next token
     *
     * @return next token
     */
    public Token nextToken() throws ParseException {
        if (mode == Mode.END) {
            currentToken = Token.END;
            mode = Mode.ERROR;
            return currentToken;
        } else if (mode == Mode.ERROR) {
            throw new ParseException("End of stream", currentPosition);
        }
        while (isBlank(nextChar()));
        switch (currentChar) {
            case '(':
                currentToken = Token.OPEN_BRACKET;
                break;
            case ')':
                currentToken = Token.CLOSE_BRACKET;
                break;
            case '*':
                currentToken = Token.ASTERISK;
                break;
            case '|':
                currentToken = Token.OR;
                break;
            default:
                if (!isLetter(currentChar)) {
                    throw new ParseException("Illegal character : \"" + (char) currentChar + "\"; expected non-letter character after " + currentToken + " token; position = " + currentPosition, currentPosition);
                }
                currentToken = Token.CHAR;
        }
        try {
            if (available() == 0) {
                mode = Mode.END;
            }
        } catch (IOException e) {
        }
        return currentToken;
    }

    /**
     * Returns {@code true} if the lexical analyzer has more tokens.
     *
     * @return {@code true} if the lexical analyzer has more token
     */
    public boolean hasNext() {
        try {
            return available() > 0 || mode == Mode.END;
        } catch (IOException e) {
            return false;
        }
    }

    private int nextChar() throws ParseException {
        ++currentPosition;
        try {
            currentChar = is.read();
        } catch (IOException e) {
            throw new ParseException(e.getMessage(), currentPosition);
        }
        return currentChar;
    }

    @Override
    public int available() throws IOException {
        return is.available();
    }

    @Override
    public synchronized void reset() throws IOException {
        is.reset();
    }

    @Override
    public synchronized void mark(int readlimit) {
        is.mark(readlimit);
    }

    @Override
    public boolean markSupported() {
        return is.markSupported();
    }

    @Override
    public int read() throws IOException {
        return is.read();
    }

    @Override
    public void close() throws IOException {
        is.close();
    }

    private boolean isLetter(int c) {
        return c >= 'a' && c <= 'z';
    }

    private boolean isBlank(int c) {
        return c == '\r' || c == '\n';
    }
}
