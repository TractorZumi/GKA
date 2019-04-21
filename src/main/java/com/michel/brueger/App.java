package com.michel.brueger;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;

import java.io.IOException;
import java.util.ArrayList;

import org.graphstream.algorithm.Kruskal;

public class App 
{
    public static void main( String[] args ) throws IOException {

        GraphUtilities.useWindows1252();
        Graph graph1 = GraphUtilities.createGraphFromFile("src/main/files/graph05.graph");

        Kruskal kruskal = new Kruskal();

        kruskal.init(graph1);
        kruskal.compute();

        ArrayList<Edge> tree = MinimumSpanningTrees.minimumSpanningTreeKruskal(graph1);

        GraphUtilities.colorInEdges(tree, graph1, "red");
        GraphUtilities.applyBetterGraphics(graph1);
        graph1.display();
        System.out.print((Integer)graph1.getAttribute("minimumSpanningWeight"));
    }
}
