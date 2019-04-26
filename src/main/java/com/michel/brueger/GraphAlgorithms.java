package com.michel.brueger;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

public class GraphAlgorithms {
    /**
     *
     * @param graph : Graph object from GraphStream library containing fromNode and toNode
     * @param fromNode : Node object from GraphStream library. Start of shortest path to find.
     * @param toNode : Node object from GraphStream library. Finish of shortest path to find.
     * @return : List of Edges (GraphStream) representing one of the shortest paths form fromNode to toNode.
     */

    public static ArrayList<Edge> breadthFirstSearch(Graph graph, String fromNode, String toNode) {
        // If the starting node is equal to the ending node, return an empty list of edges
        if (fromNode.equals(toNode))
            return new ArrayList<Edge>();
        if (graph.getNode(fromNode) == null || graph.getNode(toNode) == null)
            return null;

        ArrayDeque<Node> openNodes = new ArrayDeque<Node>();

        Node startingNode = graph.getNode(fromNode);
        openNodes.add(startingNode);

        startingNode.setAttribute("depth", 0);
        startingNode.setAttribute("parent", (Object)null);

        boolean toNodeFound = false;
        while(!openNodes.isEmpty() && !toNodeFound) {
            Node currentNode = openNodes.poll();

            // System.out.print("new current node: " + currentNode.getId() + "\n");

            Iterator<Node> neighbours = currentNode.getNeighborNodeIterator();
            while(neighbours.hasNext() && !toNodeFound) {
                Node neighbourNode = neighbours.next();

                if (currentNode.hasEdgeToward(neighbourNode))
                {
                    if (!neighbourNode.hasAttribute("depth")) {
                        //System.out.print("looking at neighbour: " + neighbourNode.getId() + "\n");

                        neighbourNode.addAttribute("depth", (Integer)currentNode.getAttribute("depth") + 1);
                        neighbourNode.addAttribute("parent", currentNode);
                        neighbourNode.addAttribute("ui.label", neighbourNode.getAttribute("ui.label") + " depth: " + ((Integer)currentNode.getAttribute("depth") + 1) + " parent: " + currentNode);
                        if (neighbourNode.getId().equals(toNode))
                            toNodeFound = true;
                        else
                            openNodes.add(neighbourNode);
                    }
                }
            }
        }

        // Build path from end node to start node using the parent attribute

        ArrayList<Edge> path = null;

        if (toNodeFound) {
            path = new ArrayList<Edge>();

            Node nextNode = graph.getNode(toNode);
            Node parent = ((Node)nextNode.getAttribute("parent"));

            System.out.print("current: " + nextNode + " with parent: " + parent + "\n");

            while (!nextNode.getId().equals(fromNode)){
                path.add(0, nextNode.getEdgeFrom(parent));
                nextNode = parent;
                parent = ((Node)nextNode.getAttribute("parent"));
                System.out.print("current: " + nextNode + " with parent: " + parent + "\n");
            }
        }

        Iterator<Node> iter = graph.getNodeIterator();
        while(iter.hasNext()){
            Node n = iter.next();
            n.removeAttribute("depth");
            n.removeAttribute("parent");
        }

        return path;
    }
}
