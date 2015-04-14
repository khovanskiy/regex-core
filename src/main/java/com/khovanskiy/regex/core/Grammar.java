package com.khovanskiy.regex.core;

import java.util.*;

public class Grammar {

    public final static String EPSILON = "";
    public final static String EOF = "$";

    public static class Rule {
        private String left;
        private String right;

        public Rule(String left, String right) {
            this.left = left;
            this.right = right;
        }
    }

    private Set<String> terminals = new HashSet<>();
    private Set<String> nonterminals = new HashSet<>();
    private List<Rule> rules = new ArrayList<>();
    private Map<String, Set<String>> first = new HashMap<>();
    private Map<String, Set<String>> follow = new HashMap<>();
    private String start;

    /**
     *
     * @param start start nonterminal
     */
    public Grammar(String start) {
        this.start = start;
    }

    /**
     * Adds rule (A -> B) to grammar
     *
     * @param left A
     * @param right B
     */
    public void add(String left, String right) {
        for (int i = 0; i < left.length(); ++i) {
            char c = left.charAt(i);
            if (isNonTerminal(c)) {
                nonterminals.add(c + "");
            }
        }
        for (int i = 0; i < right.length(); ++i) {
            char c = right.charAt(i);
            if (isNonTerminal(c)) {
                nonterminals.add(c + "");
            } else {
                terminals.add(c + "");
            }
        }
        rules.add(new Rule(left, right));
    }

    public boolean isNonTerminal(char c) {
        return c >= 'A' && c <= 'Z';
    }

    public boolean isNonTerminal(String c) {
        if (c.length() != 1) {
            return false;
        }
        return isNonTerminal(c.charAt(0));
    }

    public boolean isTerminal(char c) {
        return !isNonTerminal(c);
    }

    public boolean isTerminal(String c) {
        if (c.length() != 1) {
            return false;
        }
        return isTerminal(c.charAt(0));
    }

    /**
     * Gets FIRST set
     *
     * @return FIRST set
     */
    public Map<String, Set<String>> getFirstSet() {
        first.clear();
        // 1st part
        for (String term : terminals) {
            HashSet<String> set = new HashSet<>();
            set.add(term);
            first.put(term, set);
        }
        for (String nonterm : nonterminals) {
            first.put(nonterm, new HashSet<String>());
        }
        // 2nd part
        for (Rule rule : rules) {
            if (rule.right.equals(EPSILON)) {
                first.get(rule.left).add(EPSILON);
            }
        }
        // 3rd part
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Rule rule : rules) {
                String name = rule.left;
                String production = rule.right;
                for (int i = 0; i < production.length(); ++i) {
                    String c = production.charAt(i) + "";
                    if (first.get(c).contains(EPSILON)) {
                        for (String cur : first.get(c)) {
                            if (!first.get(name).contains(cur)) {
                                first.get(name).add(cur);
                                changed = true;
                            }
                        }
                        if (i == production.length() - 1) {
                            if (!first.get(name).contains(EPSILON)) {
                                first.get(name).add(EPSILON);
                                changed = true;
                            }
                        }
                    } else {
                        for (String element : first.get(c)) {
                            if (!first.get(name).contains(element)) {
                                first.get(name).add(element);
                                changed = true;
                            }
                        }
                        break;
                    }
                }
            }
        }
        return first;
    }

    /**
     * Gets FOLLOW set
     *
     * @return FOLLOW set
     */
    public Map<String, Set<String>> getFollowSet() {
        getFirstSet();
        follow.clear();
        for (String nonterm : nonterminals) {
            follow.put(nonterm, new HashSet<String>());
        }
        follow.get(start).add(EOF);
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Rule rule : rules) {
                String name = rule.left;
                String production = rule.right;
                for (int i = 0; i < production.length() - 1; i++) {
                    if (nonterminals.contains(production.charAt(i) + "")) {
                        for (String cur : first.get(production.charAt(i + 1) + "")) {
                            if (!cur.equals(EPSILON) && !follow.get(production.charAt(i) + "").contains(cur)) {
                                follow.get(production.charAt(i) + "").add(cur);
                                changed = true;
                            }
                        }
                    }
                }
                if (production.length() == 0) {
                    continue;
                }
                int i = production.length() - 1;
                if (nonterminals.contains(production.charAt(i) + "")) {
                    for (String cur : follow.get(name)) {
                        if (!cur.equals(EPSILON) && !follow.get(production.charAt(i) + "").contains(cur)) {
                            follow.get(production.charAt(i) + "").add(cur);
                            changed = true;
                        }
                    }
                }
                if(first.get(production.charAt(production.length() - 1) + "").contains(EPSILON)) {
                    i = production.length() - 2;
                    if (nonterminals.contains(production.charAt(i) + "")) {
                        for (String cur : follow.get(production.charAt(i + 1) + "")) {
                            if (!cur.equals(EPSILON) && !follow.get(production.charAt(i) + "").contains(cur)) {
                                follow.get(production.charAt(i) + "").add(cur);
                                changed = true;
                            }
                        }
                    }
                }
            }
        }
        return follow;
    }

    public void buildTable() {
        getFollowSet();
        for (Rule rule : rules) {

        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Rule rule : rules) {
            sb.append(rule.left + " -> " + rule.right + "\n");
        }
        return sb.toString();
    }
}
