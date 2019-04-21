package com.michel.brueger;

public class DisjointSetElement {
    private int position;
    private int parent;
    private int size;

    public DisjointSetElement(int position, int parent, int size) {
        this.position = position;
        this.parent = parent;
        this.size = size;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int id) {
        this.position = id;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
