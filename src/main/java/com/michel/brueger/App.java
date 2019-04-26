package com.michel.brueger;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;

import java.io.IOException;
import java.util.ArrayList;

import org.graphstream.algorithm.Kruskal;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;

public class App 
{
    public static void testKruskall() throws IOException {
        GraphUtilities.useWindows1252();
        Graph graph1 = GraphUtilities.createGraphFromFile("src/main/files/genGraph.graph");

        MinimumSpanningTrees.kruskalGraphstream(graph1 );

        ArrayList<Edge> tree = MinimumSpanningTrees.minimumSpanningTreeKruskal(graph1, true);
        System.out.print("own kruskal: ");
        System.out.print((Double)graph1.getAttribute("minimumSpanningWeight"));
        GraphUtilities.colorInEdges(tree, graph1, "red");
        System.out.print(tree.size() == graph1.getNodeCount() - 1);

        GraphUtilities.applyBetterGraphics(graph1);
        graph1.display();
    }

    public static void testGraphGenerator(){
        int nrEdges = 100;// max: 350000
        Graph graph = MinimumSpanningTrees.generateGraph(nrEdges + 20, nrEdges, false, true );
        GraphUtilities.applyBetterGraphics(graph);
        GraphUtilities.labelGraph(graph);

        try {
            GraphUtilities.createFileFromGraph("src/main/files/genGraph.graph", graph);
        }
        catch(IOException e){}
    }


    public static void main( String[] args ) throws IOException {
        //testCreateFile();

        testGraphGenerator();
        testKruskall();
    }
}
