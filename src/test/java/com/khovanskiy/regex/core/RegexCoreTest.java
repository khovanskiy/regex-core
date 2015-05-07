package com.khovanskiy.regex.core;

import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

public class RegexCoreTest {

    private final RegexParser parser = new RegexParser();

    @Test (expected = ParseException.class)
    public void parseException1() throws IOException, ParseException {
        parser.parse("(");
    }

    @Test (expected = ParseException.class)
    public void parseException2() throws IOException, ParseException {
        parser.parse("(a|*)");
    }

    @Test (expected = ParseException.class)
    public void parseException3() throws IOException, ParseException {
        parser.parse("(a|c)|");
    }

    @Test
    public void singleChar() throws IOException, ParseException {
        parser.parse("c");
    }

    @Test
    public void alteration() throws IOException, ParseException {
        parser.parse("a|b");
    }

    @Test
    public void closure() throws IOException, ParseException {
        parser.parse("a*");
    }

    @Test
    public void brackets() throws IOException, ParseException {
        parser.parse("(((a)))");
    }

    @Test
    public void word() throws IOException, ParseException {
        parser.parse("abc");
    }

    @Test
    public void example1() throws IOException, ParseException {
        parser.parse("(((c|c)*abc)|((c|c)*abc))*");
    }
}
