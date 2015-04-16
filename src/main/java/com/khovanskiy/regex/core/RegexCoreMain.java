package com.khovanskiy.regex.core;

import com.martiansoftware.jsap.*;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

public class RegexCoreMain {
    public static void main(String[] args) throws Exception {

        Grammar grammar = new Grammar("R");
        /*grammar.add("R", "T");
        grammar.add("R", "R|T");
        grammar.add("T", Grammar.EPSILON);
        grammar.add("T", "TF");
        grammar.add("F", "F*");
        grammar.add("F", "N");
        grammar.add("N", "(R)");
        grammar.add("N", "c");*/

        grammar.add("R","TD");
        grammar.add("D", "|TD");
        grammar.add("D", Grammar.EPSILON);
        grammar.add("T", "FP");
        grammar.add("P", "FP");
        grammar.add("P", Grammar.EPSILON);
        grammar.add("F", "NW");
        grammar.add("W", "*W");
        grammar.add("W", Grammar.EPSILON);
        grammar.add("N", "(R)");
        grammar.add("N", "c");

        SimpleJSAP jsap = new SimpleJSAP("regex-core", "Parse and build tree with regex grammar",
                new Parameter[] {
                        new FlaggedOption("input").setRequired(JSAP.REQUIRED).setShortFlag('i').setHelp("Input"),
                        new FlaggedOption("output").setStringParser(JSAP.STRING_PARSER).setRequired(JSAP.REQUIRED).setShortFlag('o').setHelp("Filename of output where visualization will be saved")
                });

        JSAPResult bundle = jsap.parse(args);
        if (!bundle.success()) {
            System.exit(-1);
        }

        System.out.println(grammar);

        RegexParser parser = new RegexParser();
        Tree tree = parser.parse(bundle.getString("input"));
        System.out.println(tree);

        TreeVisualizer visualizer = new TreeVisualizer(tree, bundle.getString("output"));
        visualizer.render();

        /*System.out.println("FIRST set");
        for (Map.Entry<String, Set<String>> entry : grammar.getFirstSet().entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        System.out.println("FOLLOW set");
        for (Map.Entry<String, Set<String>> entry : grammar.getFollowSet().entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }*/



        /*String example = "(((c|c)*abc)|((c|c)*abc))*";
        InputStream is = new ByteArrayInputStream(example.getBytes());
        LexicalAnalyzer lex = new LexicalAnalyzer(is);

        while (lex.hasNext()) {
            System.out.println("Token " + lex.nextToken());
        }

        RegexParser parser = new RegexParser();
        Tree tree = parser.parse(is);
        System.out.println(tree);

        TreeVisualizer visualizer = new TreeVisualizer(tree, "output.html");
        visualizer.render();*/
    }
}
