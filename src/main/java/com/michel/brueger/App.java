package com.michel.brueger;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        GraphUtilities.useWindows1252();
        Graph graph1 = GraphUtilities.createGraphFromFile("src/main/files/graphUltimo1.graph");

        // commit this

        ArrayList<Edge> path = GraphUtilities.breadthFirstSearch(graph1, "v1", "v6");

        GraphUtilities.applyBetterGraphics(graph1);

        GraphUtilities.colorInEdges(path, graph1, "red");

        System.out.print("path: " + path);
        graph1.display();
    }
}
