package com.michel.brueger;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;

import java.io.IOException;
import java.util.ArrayList;

import org.graphstream.algorithm.Kruskal;

public class App 
{
    public static void testKruskall() throws IOException {
        GraphUtilities.useWindows1252();
        Graph graph1 = GraphUtilities.createGraphFromFile("src/main/files/genGraph150nodes100edges.graph");

        Kruskal kruskal = new Kruskal();

        kruskal.init(graph1);
        kruskal.compute();

        System.out.print("graphstream kruskal: " + kruskal.getTreeWeight() + "\n");

        ArrayList<Edge> tree = MinimumSpanningTrees.minimumSpanningTreeKruskal(graph1);
        System.out.print("own kruskal: ");
        System.out.print((Double)graph1.getAttribute("minimumSpanningWeight"));
        GraphUtilities.colorInEdges(tree, graph1, "red");

        GraphUtilities.applyBetterGraphics(graph1);
        graph1.display();
    }

    public static void testGraphGenerator(){
        Graph graph = GraphUtilities.generateGraph(150, 100, true, true );
        GraphUtilities.applyBetterGraphics(graph);
        GraphUtilities.labelGraph(graph);

        try {
            GraphUtilities.createFileFromGraph("src/main/files/genGraph150nodes100edges.graph", graph);
        }
        catch(IOException e){}
        graph.display();
    }

    public static void main( String[] args ) throws IOException {
        //testKruskall();

        testGraphGenerator();
    }
}
