package com.michel.brueger;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.Graphs;


import java.text.SimpleDateFormat;
import java.util.*;

public class EulerCycleAlgorithms {

    public static void labelIndex(ArrayList<Edge> edges)
    {
        for (int i = 0; i < edges.size(); i++){
            edges.get(i).setAttribute("ui.label", Integer.toString(i));
        }
    }

    public static void debug(String message){
        System.out.print(message + "\n");
    }


    public static int depthFirstSearchCount(Node node, HashSet<String> visited){
        int count = 1;
        visited.add(node.getId());

        Iterator<Node> neighbourNodes = node.getNeighborNodeIterator();

        while (neighbourNodes.hasNext()) {
            Node neighbour = neighbourNodes.next();
            if (!visited.contains(neighbour.getId()))
                count += depthFirstSearchCount(neighbour, visited);
        }

        return count;
    }

    // Removing and adding the edge doesn't work, attributes would be lost
    public static boolean isBridge(Graph graph, Edge edge){

        // Do a DFS for one of the nodes
        HashSet<String> visited = new HashSet<>();
        int firstCount = depthFirstSearchCount(edge.getNode0(), visited);

        // Clone graph and remove the edge
        Graph modifiedGraph = Graphs.clone(graph);

        modifiedGraph.removeEdge(edge.getId());

        // Get the equivalent starting node in the cloned graph
        Node nodeInModifiedGraph = modifiedGraph.getNode(edge.getNode0().getId());

        // Do a second DFS
        visited.clear();
        int secondCount = depthFirstSearchCount(nodeInModifiedGraph, visited);

        return firstCount > secondCount;
    }

    public static ArrayList<Edge> generateEulerCycleFleury(Graph graph) {
        ArrayList<Edge> tour = new ArrayList<>();
        Graph currentGraph = Graphs.clone(graph);

        int nodeCount = currentGraph.getNodeCount();
        int edgeCount = currentGraph.getEdgeCount();

        // If the graph has no vertex, return empty list
        if (nodeCount == 0)
            return tour;

        // Check, if there are vertices with an odd degree
        ArrayList<Node> nodesWithOddDegree = new ArrayList<>();
        for (Node node : currentGraph.getEachNode()){
            if (node.getDegree() % 2 == 1)
                nodesWithOddDegree.add(node);
        }

        Node currentNode = null;
        int nrNodesWithOddDegree = nodesWithOddDegree.size();

        Random rand = new Random();
        if (nrNodesWithOddDegree == 2) {
            currentNode = nodesWithOddDegree.get(rand.nextInt(nrNodesWithOddDegree));
        }
        else if (nrNodesWithOddDegree == 0) {
            currentNode = graph.getNode(rand.nextInt(nodeCount));
        }
        else
            return new ArrayList<>(); // graph has no euler tour

        while (tour.size() < edgeCount) {
            Iterator<Edge> connectedEdgesIterator = currentNode.getEdgeIterator();

            boolean found = false;
            Edge nextEdge = null;
            Edge bridgeEdge = null;

            while (connectedEdgesIterator.hasNext() && !found) {
                nextEdge = connectedEdgesIterator.next();

                if (isBridge(currentGraph, nextEdge))
                    bridgeEdge = nextEdge;
                else
                    found = true;
            }

            if (!found)
                if(bridgeEdge != null) {
                    nextEdge = bridgeEdge;
                }
                else
                    return new ArrayList<>();// graph has no cycle

            String id = "";
            // Go to next node
            if (nextEdge.getNode1().getId() == currentNode.getId())
                id = nextEdge.getNode0().getId();
            else
                id = nextEdge.getNode1().getId();

            // Remove the edge from the current graph and add the equivalent edge of the original graph to the cycle
            // This creates new node instances
            currentGraph.removeEdge(nextEdge.getId());

            currentNode = currentGraph.getNode(id);

            tour.add(graph.getEdge(nextEdge.getId()));
        }

        return tour;
    }

    // If the graph is not fully connected or has no cycle, an empty list is returned
    public static ArrayList<Edge> generateEulerCycleHierholzer(Graph graph){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

        ArrayList<Edge> cycle = new ArrayList<>();
        ArrayList<Node> nodesInCycle = new ArrayList<>();

        int nodeCount = graph.getNodeCount();

        // If the graph has no vertex, return empty list
        if (nodeCount == 0)
            return cycle;

        // Pick a random vertex from the graph
        Random rand = new Random();
        Node currentNode = graph.getNode(rand.nextInt(nodeCount));
        nodesInCycle.add(currentNode);
        // Save number of edges
        int edgeCount = graph.getEdgeCount();

        // If the graph has no edge, return empty list
        if (edgeCount == 0)
            return new ArrayList<>();

        // Find cycle beginning at the current vertex
        Edge currentEdge = currentNode.getEdgeIterator().next();

        // Initial vertex has no edge attached, graph cannot be fully connected, so terminate

        if (currentEdge == null)
            return new ArrayList<>();

        // Index after which new edges are added to the cycle
        int startingIndex = -1;

        // Every loop creates a new sub-cycle
        while(true) {
            Node startingNode = currentNode;
            boolean startingNodeFound = false;

            while (!startingNodeFound) {
                // Get the current neighbour of the vertex
                Node currentNeighbour = null;
                if (currentEdge.getNode1().getId() == currentNode.getId())
                    currentNeighbour = currentEdge.getNode0();
                else
                    currentNeighbour = currentEdge.getNode1();

                debug("next Node: " + currentNeighbour.getId());
                debug("next Edge: " + currentEdge);

                currentEdge.setAttribute("inCycle", timeStamp);

                cycle.add(startingIndex + 1, currentEdge);
                nodesInCycle.add(startingIndex + 2, currentNeighbour);
                startingIndex++;

                debug(nodesInCycle.toString());

                // Check, if we found the starting Node
                if (currentNeighbour.getId() == startingNode.getId())
                    startingNodeFound = true;

                if (startingNodeFound)
                    continue;

                // continue with the neighbour as currentNode
                currentNode = currentNeighbour;

                // pick the next random edge to form the cycle
                Iterator<Edge> connectedEdgesIterator = currentNode.getEdgeIterator();

                boolean edgeFound = false;
                while (connectedEdgesIterator.hasNext() && !edgeFound) {
                    currentEdge = connectedEdgesIterator.next();
                    if (currentEdge.getAttribute("inCycle") != timeStamp){
                        edgeFound = true;
                    }
                }

                // sub-cycle could not be build, therefore graph has no euler-cycle, return empty list
                if (!edgeFound)
                    return new ArrayList<>();
            }

            // if all edges are marked, terminate
            if (cycle.size() == edgeCount)
                return cycle;

            // Find next vertex with an unused edge in the cycle, set currentNode and currentEdge
            boolean found = false;

            HashMap<String, Boolean> wasChecked = new HashMap<>();
            // debug("Find new edge in these nodes: " + nodesInCycle.toString());
            for (int i = 0; i < nodesInCycle.size() && !found; i++){
                currentNode = nodesInCycle.get(i);
                debug(currentNode.toString());

                if (wasChecked.get(currentNode.getId()) != null)
                    continue;

                wasChecked.put(currentNode.getId(), true);

                Iterator<Edge> connectedEdgesIterator = currentNode.getEdgeIterator();

                while (connectedEdgesIterator.hasNext() && !found){
                    currentEdge = connectedEdgesIterator.next();

                    // Index of the incoming edge at currentNode
                    startingIndex = i - 1;

                    if (currentEdge.getAttribute("inCycle") != timeStamp) {
                        found = true;
                    }
                }
            }

            // Terminate if we didn't find one, because graph doesn't have cycle
            if (!found)
                return new ArrayList<>();

            // Otherwise, the loop continues to build a new sub-cycle starting at currentNode with currentEdge,
            // which must be added after startingIndex
        }
    }

    public static boolean isEulerCycle(Graph graph, ArrayList<Edge> edges){
        if (graph.getEdgeCount() != edges.size())
            return false; // number of edges in cycle does not match number of edges in graph
        else if (edges.size() == 0)
            return true; // graph has no edges

        if (edges.size() == 1)
            return false; // Can't be cycle


        HashSet<String> visitedEdges = new HashSet<>();

        // Has at least 2 edges
        Node edge1node0 = edges.get(0).getNode0();
        Node edge1node1 = edges.get(0).getNode1();

        visitedEdges.add(edges.get(0).getId());

        if (visitedEdges.contains(edges.get(1).getId()))
            return false;//First and second edge are identical

        visitedEdges.add(edges.get(1).getId());

        Node edge2node0 = edges.get(1).getNode0();
        Node edge2node1 = edges.get(1).getNode1();

        Node startingNode = null;
        Node currentNode = null;

        if (edge1node0.getId() == edge2node0.getId()){
            startingNode = edge1node1;
            currentNode = edge2node1;
        }
        else if (edge1node0.getId() == edge2node1.getId()){
            startingNode = edge1node1;
            currentNode = edge2node0;
        }
        else if(edge1node1.getId() == edge2node0.getId()){
            startingNode = edge1node0;
            currentNode = edge2node1;
        }
        else if (edge1node1.getId() == edge2node1.getId()){
            startingNode = edge1node0;
            currentNode = edge2node0;
        }
        else
            return false; // edges do not match

        //debug(edges.get(0).toString());
        //  debug(edges.get(1).toString());

        // debug(startingNode.toString());
        // debug(currentNode.toString());
        for (int i = 2; i < edges.size(); i++){
            // current Node must be in the next edge
            Edge currentEdge = edges.get(i);

            // debug("current node: " + currentNode.toString());
            //debug("next edge: " + currentEdge.toString());

            // Kante kommt mehrfach vor
            if (visitedEdges.contains(currentEdge.getId()))
                return false;

            visitedEdges.add(currentEdge.getId());

            // Kanten hängen nicht zusammen (ergeben keinen Pfad)
            if (currentEdge.getNode0().getId() == currentNode.getId()){
                currentNode = currentEdge.getNode1();
            }
            else if (currentEdge.getNode1().getId() == currentNode.getId()) {
                currentNode = currentEdge.getNode0();
            }
            else
                return false;// currentNode was not found in next edge
        }

        // Only if the last Node looked at is the startingNode, return true
        return currentNode == startingNode;
    }
}
