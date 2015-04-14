package com.khovanskiy.regex.core;

import java.util.Arrays;
import java.util.List;

/**
 * @author Victor Khovanskiy
 */
public class Tree {

    private String node;
    private List<Tree> children;

    public Tree(String node, Tree... children) {
        this.node = node;
        this.children = Arrays.asList(children);
    }

    public Tree(String node) {
        this.node = node;
    }

    @Override
    public String toString() {
        if (children == null) {
            return "" + node + "";
        }
        return node + ":" + Arrays.toString(children.toArray())+ "";
    }
}
