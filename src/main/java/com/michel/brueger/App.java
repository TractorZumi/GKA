package com.michel.brueger;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;

import java.io.IOException;
import java.util.ArrayList;

import org.graphstream.algorithm.Kruskal;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;

public class App 
{
    public static void testKruskal() throws IOException {
        GraphUtilities.useWindows1252();
        Graph graph1 = GraphUtilities.createGraphFromFile("src/main/files/graph04.graph");

        MinimumSpanningTrees.kruskalGraphstream(graph1 );

        ArrayList<Edge> tree = MinimumSpanningTrees.minimumSpanningTreeKruskal(graph1, true);
        System.out.print("own kruskal: ");
        System.out.print((Double)graph1.getAttribute("minimumSpanningWeight"));
        GraphUtilities.colorInEdges(tree, graph1, "red");
        System.out.print(tree.size() == graph1.getNodeCount() - 1);

        GraphUtilities.applyBetterGraphics(graph1);
        graph1.display();
    }

    public static void testPrim() throws IOException {
        GraphUtilities.useWindows1252();
        Graph graph1 = GraphUtilities.createGraphFromFile("src/main/files/big6E025V.graph");

        MinimumSpanningTrees.primGraphstream(graph1);

        ArrayList<Edge> tree = MinimumSpanningTrees.minimumSpanningTreePrim(graph1, true);

        System.out.print("own prim: ");
        System.out.print((Double)graph1.getAttribute("minimumSpanningWeight"));
        GraphUtilities.colorInEdges(tree, graph1, "red");

        System.out.print("Correct edge number: " + (tree.size() == graph1.getNodeCount() - 1));

        GraphUtilities.applyBetterGraphics(graph1);

      //  graph1.display();
    }

    public static void testPrimDecreaseKey() throws IOException {
        GraphUtilities.useWindows1252();
        Graph graph1 = GraphUtilities.createGraphFromFile("src/main/files/big6E025V.graph");

        MinimumSpanningTrees.primGraphstream(graph1);

        ArrayList<Edge> tree = MinimumSpanningTrees.minimumSpanningTreePrimDecreaseKey(graph1, true);

        System.out.print("own prim: ");
        System.out.print((Double)graph1.getAttribute("minimumSpanningWeight") + "\n");
        GraphUtilities.colorInEdges(tree, graph1, "red");

        System.out.print("Correct edge number: " + (tree.size() == graph1.getNodeCount() - 1));

        GraphUtilities.applyBetterGraphics(graph1);

       // graph1.display();
    }

    public static void testGraphGenerator(){
        int nrEdges = 100000;// max: 350000
        Graph graph = MinimumSpanningTrees.generateGraph(nrEdges + 100000, nrEdges, true, true );
      //  System.out.print(graph.getNodeCount() + "\n" + graph.getEdgeCount());
        GraphUtilities.applyBetterGraphics(graph);
        GraphUtilities.labelGraph(graph);

        try {
            GraphUtilities.createFileFromGraph("src/main/files/genGraph.graph", graph);
        }
        catch(IOException e){}
    }


    public static void main( String[] args ) throws IOException {
            //testGraphGenerator();
//            testKruskal();
//            testPrimDecreaseKey();
//            testPrim();

//            Client client = new Client();
//            client.run();
        Eulergenerator.generateEulergraph(8);


    }
}
