package com.michel.brueger;

import org.graphstream.graph.Node;

public class NodeKeyPair {
    public NodeKeyPair(Node node, double key){
        this.node = node;
        this.key = key;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public double getKey() {
        return key;
    }

    public void setKey(double value) {
        this.key = value;
    }

    private Node node;
    private double key;
}
