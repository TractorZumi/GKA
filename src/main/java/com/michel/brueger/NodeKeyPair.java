package com.michel.brueger;

import org.graphstream.graph.Node;

public class NodeKeyPair {
    public NodeKeyPair(Node node, double key){
        this.node = node;
        this.key = key;
        this.index = -1;
    }
    public NodeKeyPair(Node node, double key, int index){
        this.node = node;
        this.key = key;
        this.index = index;
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private int index;
}
