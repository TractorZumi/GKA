package com.michel.brueger;

public class Graph {
    Boolean directed = false;

    public Graph(){
    }

    public Boolean getDirected() { return directed; }
    public void setDirected() { directed=true; }
    public void setUndirected() {directed = false; }

}
