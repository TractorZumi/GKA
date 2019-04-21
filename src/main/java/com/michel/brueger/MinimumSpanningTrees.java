package com.michel.brueger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import org.graphstream.graph.*;

// Returns a list of edges, adds an attribute "minimumSpanningWeight" to the graph object
public class MinimumSpanningTrees {
    public static ArrayList<Edge> minimumSpanningTreeKruskal(Graph graph) {
        // Create an Edge Array for fast sorting
        Collection<Edge> edgeSet = graph.getEdgeSet();
        Edge[] edgeArray = new Edge[edgeSet.size()];

        // Create the result list of spanningEdges
        ArrayList<Edge> spanningEdges = new ArrayList<>();

        int i = 0;
        for(Edge edge : edgeSet) {
            if (!edge.hasAttribute("weight"))
                return spanningEdges;

            edgeArray[i] = edge;
            i++;
        }

        // Create new DisjointSet
        DisjointSet disjointSet = new DisjointSet();

        // Create a single Set for each Node
        for (Node node : graph.getEachNode()) {
            node.setAttribute("position", disjointSet.makeSet());
        }

        // Sort the Edges by weight
        Arrays.sort(edgeArray, Comparator.comparingInt((Edge edge) -> (Integer)edge.getAttribute("weight")));

        int totalWeight = 0;

        // Loop adding new edges
        for(i = 0; i < edgeArray.length; i++) {
            int position1 = edgeArray[i].getNode0().getAttribute("position");
            int position2 = edgeArray[i].getNode1().getAttribute("position");

            if (disjointSet.union(position1, position2)) {
                // edge has merged to sets, add it to the spanning tree
                spanningEdges.add(edgeArray[i]);
                totalWeight += (Integer)edgeArray[i].getAttribute("weight");
            }
        }

        graph.setAttribute("minimumSpanningWeight", totalWeight);
        return spanningEdges;
    }

    public static Graph minimumSpanningTreePrim(Graph graph) {
        return null;
    }
}
