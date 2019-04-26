package com.michel.brueger;

import java.util.*;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;

public class MinimumSpanningTrees {
    // The graph to generate always has nodes with ids "1"-"nr_of_nodes"
    private static void addRandomEdge(Graph graph){
        Random rand = new Random();

        int nodes = graph.getNodeCount();
        int first_node = rand.nextInt(nodes) + 1;
        int second_node = rand.nextInt(nodes) + 1;

        if (first_node > second_node){
            int temp = second_node;
            second_node = first_node;
            first_node = temp;
        }

        int edgeCount = graph.getEdgeCount();
        graph.addEdge(Integer.toString(graph.getEdgeCount()), Integer.toString(first_node), Integer.toString(second_node));
        if (graph.getEdgeCount() == edgeCount)
            addRandomEdge(graph);
    }

    public static Graph generateGraph(int numberOfEdges, int numberOfNodes, boolean isConnected, boolean allowMultiEdges){
        Graph generatedGraph = null;

        if (allowMultiEdges)
            generatedGraph = new MultiGraph("Generated Graph " + System.currentTimeMillis());
        else
            generatedGraph = new SingleGraph("Generated Graph " + System.currentTimeMillis());

        // Return empty graph for negative number of edges or zero/negative nodes
        if (numberOfEdges < 0 || numberOfNodes <= 0)
            return generatedGraph;

        // Specified graph can't be generated without multi edges, return empty graph instead
        if (numberOfEdges > numberOfNodes && allowMultiEdges != true)
            return generatedGraph;

        generatedGraph.setAutoCreate(true);
        generatedGraph.setStrict(false);

        Random rand = new Random();

        if (isConnected) {
            // If a connected graph cannot be generated, return empty graph
            if (numberOfEdges < numberOfNodes - 1)
                return generatedGraph;

            // This will hold all generated edges
            ArrayList<Edge> generatedEdges = new ArrayList<>();

            if (numberOfNodes >= 2){
                // Graph has at least 2 nodes
                generatedEdges.add(generatedGraph.addEdge(Integer.toString(generatedEdges.size()), "1", "2"));
            }

            // Add a new node to the graph by adding an edge to a random existing node
            // A random existing edge is chosen to determine this existing node to connect to
            for (int i = 2; i < numberOfNodes; i++){
                int randomIndex = rand.nextInt(generatedEdges.size());
                Edge chosenEdge = generatedEdges.get(randomIndex);

                Node chosenNode;

                if (rand.nextBoolean())
                    chosenNode = chosenEdge.getNode0();
                else
                    chosenNode = chosenEdge.getNode1();

                generatedEdges.add(generatedGraph.addEdge(Integer.toString(generatedEdges.size()), chosenNode.getId(), Integer.toString(i + 1)));
            }

            for (int i = 0; i < numberOfEdges - numberOfNodes + 1; i++){
                addRandomEdge(generatedGraph);
            }
        }
        else{
            for (int i = 0; i < numberOfNodes; i++) {
                generatedGraph.addNode(Integer.toString(i + 1));
            }

            for (int i = 0; i < numberOfEdges; i++){
                addRandomEdge(generatedGraph);
            }
        }

        // Generate integer edge weights from 1-edgeCount*2
        HashSet<Double> usedWeights = new HashSet<>();
        for (Edge edge : generatedGraph.getEachEdge()){
            Double generatedWeight;
            do{
                generatedWeight = ((Integer)rand.nextInt(2 * numberOfEdges)).doubleValue();
            } while(usedWeights.contains(generatedWeight));

            usedWeights.add(generatedWeight);
            edge.addAttribute("weight", generatedWeight);
        }

        return generatedGraph;
    }

    public static ArrayList<Edge> minimumSpanningTreeKruskal(Graph graph, boolean outputRuntime) {
        long start = 0;
        if (outputRuntime)
            start = java.lang.System.currentTimeMillis();
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
        Arrays.sort(edgeArray, Comparator.comparingDouble((Edge edge) -> (Double)edge.getAttribute("weight")));

        Double totalWeight = 0.0;

        // Loop adding new edges
        for(i = 0; i < edgeArray.length; i++) {
            int position1 = edgeArray[i].getNode0().getAttribute("position");
            int position2 = edgeArray[i].getNode1().getAttribute("position");

            if (disjointSet.union(position1, position2)) {
                // edge has merged to sets, add it to the spanning tree
                spanningEdges.add(edgeArray[i]);
                totalWeight += (Double)edgeArray[i].getAttribute("weight");
            }
        }

        graph.setAttribute("minimumSpanningWeight", totalWeight);

        if (outputRuntime)
            System.out.print("Kruskal's Algorithm runtime: " + ((java.lang.System.currentTimeMillis() - start) / 1000.0) + " secs" + "\n");

        return spanningEdges;
    }

    public static ArrayList<Edge> minimumSpanningTreePrim(Graph graph, boolean outputRuntime) {
        return minimumSpanningTreeKruskal(graph, outputRuntime);
    }
}
