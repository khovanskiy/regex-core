package com.khovanskiy.regex.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

/**
 * @author Victor Khovanskiy
 */
public class RegexParser {

    private LexicalAnalyzer lex;

    public Tree parse(InputStream is) throws ParseException, IOException {
        lex = new LexicalAnalyzer(is);
        lex.nextToken();
        Tree tree = R();
        lex.close();
        return tree;
    }

    public Tree parse(String in) throws ParseException, IOException {
        return parse(new ByteArrayInputStream(in.getBytes()));
    }

    private Tree R() throws ParseException {
        //System.out.println("R " + lex.getCurrentToken());
        switch (lex.getCurrentToken()) {
            case CHAR:
            case OPEN_BRACKET:
                return new Tree("R", T(), D());
            default:
                throw new ParseException("Unexpected char", lex.getCurrentPosition());
        }
    }

    private Tree D() throws ParseException {
        //System.out.println("D " + lex.getCurrentToken());
        switch (lex.getCurrentToken()) {
            case OR:
                lex.nextToken();
                return new Tree("D", new Tree("|"), T(), D());
            case CLOSE_BRACKET:
            case END:
                return new Tree("D", new Tree("eps"));
            default:
                throw new ParseException("Unexpected char", lex.getCurrentPosition());
        }
    }

    private Tree T() throws ParseException {
        //System.out.println("T " + lex.getCurrentToken());
        switch (lex.getCurrentToken()) {
            case CHAR:
            case OPEN_BRACKET:
                return new Tree("T", F(), P());
            default:
                throw new ParseException("Unexpected char", lex.getCurrentPosition());
        }
    }

    private Tree P() throws ParseException {
        //System.out.println("P " + lex.getCurrentToken());
        switch (lex.getCurrentToken()) {
            case CHAR:
            case OPEN_BRACKET:
                return new Tree("P", F(), P());
            case CLOSE_BRACKET:
            case OR:
            case END:
                return new Tree("P", new Tree("eps"));
            default:
                throw new ParseException("Unexpected char", lex.getCurrentPosition());
        }
    }

    private Tree F() throws ParseException {
        //System.out.println("F " + lex.getCurrentToken());
        switch (lex.getCurrentToken()) {
            case CHAR:
            case OPEN_BRACKET:
                return new Tree("F", N(), W());
            default:
                throw new ParseException("Unexpected char", lex.getCurrentPosition());
        }
    }

    private Tree W() throws ParseException {
        //System.out.println("W " + lex.getCurrentToken());
        switch (lex.getCurrentToken()) {
            case ASTERISK:
                lex.nextToken();
                return new Tree("W", new Tree("*"), W());
            case CHAR:
            case OPEN_BRACKET:
            case CLOSE_BRACKET:
            case OR:
            case END:
                return new Tree("W", new Tree("eps"));
            default:
                throw new ParseException("Unexpected char", lex.getCurrentPosition());
        }
    }

    private Tree N() throws ParseException {
        //System.out.println("N " + lex.getCurrentToken());
        switch (lex.getCurrentToken()) {
            case OPEN_BRACKET: {
                lex.nextToken();
                Tree temp = R();
                lex.nextToken();
                return new Tree("N", new Tree("("), temp, new Tree(")"));
            }
            case CHAR: {
                Tree temp = new Tree(lex.getCurrentValue());
                lex.nextToken();
                return new Tree("N", temp);
            }
            default:
                throw new ParseException("Unexpected char", lex.getCurrentPosition());
        }
    }
}
