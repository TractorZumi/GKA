package com.michel.brueger;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ArrayDeque;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

/**
 *
 * @author Jonathan Aldag, Michel Brüger
 * Utility class for creating and representing Graph objects from .graph files and for computing and
 * representing shortest path from Node to Node.
 *
 */
public final class GraphUtilities {
    private static String fileEncoding = "UTF-8";

    public static void useUTF8() {
        fileEncoding = "UTF-8";
    }

    public static void useWindows1252() {
        fileEncoding = "Cp1252";
    }

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

//		int currentDepth = 0;

        boolean toNodeFound = false;
        while(!openNodes.isEmpty() && !toNodeFound) {
            Node currentNode = openNodes.poll();

            System.out.print("new current node: " + currentNode.getId() + "\n");

            Iterator<Node> neighbours = currentNode.getNeighborNodeIterator();
            while(neighbours.hasNext() && !toNodeFound) {
                Node neighbourNode = neighbours.next();

                if (currentNode.hasEdgeToward(neighbourNode))
                {
                    if (!neighbourNode.hasAttribute("depth")) {
                        System.out.print("looking at neighbour: " + neighbourNode.getId() + "\n");

//						neighbourNode.addAttribute("depth", currentDepth + 1);
                        neighbourNode.addAttribute("depth", (Integer)currentNode.getAttribute("depth") + 1);
                        neighbourNode.addAttribute("parent", currentNode);

                        if (neighbourNode.getId().equals(toNode))
                            toNodeFound = true;
                        else
                            openNodes.add(neighbourNode);
                    }

                }
            }
//			currentDepth++;
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

        // Null nicht bei "kein Pfad" zurück,  besser 1 Node

//		for (Edge edge: graph.getEachEdge()) {
//			edge.removeAttribute("parent");
//			edge.removeAttribute("depth");
//		}

        Iterator<Node> iter = graph.getNodeIterator();
        while(iter.hasNext()){
            Node n = iter.next();
            n.removeAttribute("depth");
            n.removeAttribute("parent");
        }
//
//		ArrayList<Edge> reversedPath = new ArrayList<>();
//		if(!(path == null)) {
//			for(Edge edge:path) {
//				reversedPath.add(0, edge);
//			}
//			path = reversedPath;
//		}


        return path;													// umgedreht
    }

    public static void colorInEdges(ArrayList<Edge> edges, Graph graph, String color)
    {
        // color in the edges in red and beautify the graph

        if (edges == null)
            return;

        graph.addAttribute("ui.stylesheet", "edge.path { size: 2px; stroke-color: red; fill-color:" + color + "; stroke-width: 1px; stroke-mode: plain;}node {\r\n" +
                "	size: 14px, 10px;text-size: 13;text-alignment: right;text-background-mode:rounded-box;text-background-color:yellow;}");

        for (Edge edge: edges) {
            edge.addAttribute("ui.class", "path");
        }

    }

    public static void applyBetterGraphics(Graph graph){
        // Enable advanced viewer
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        // Enable higher quality graphics
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
    }

    /**
     * Creates Graph object from GraphStream library, according to .graph file found at location specified by filePath argument.
     * @param filePath : String representing location of .graph file in the filesystem
     * @return : Graph object of GraphStream library including all nodes, edges and their attributes as given by the .graph file
     * @throws IOException : If filePath is not correct, FileNotFoundException is thrown
     */
    public static Graph createGraphFromFile(String filePath) throws IOException  {
        Graph graph = new MultiGraph(filePath);

        // Allow multi-edges
        graph.setStrict(false);
        // addEdges now automatically creates the appropriate nodes
        graph.setAutoCreate(true);

        // Setup counter for unique edge id
        int id_counter = 0;

        // Use a bufferedReader to enable UTF-8/Cp1252 encoding
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), fileEncoding));
        // autoclosable try-withresources
        String line;

        // Check whether the graph is directed or undirected

        boolean directed = false;

        // Save pointer into stream
        bufferedReader.mark(1000);

        line = bufferedReader.readLine();

        line = line.replaceAll("\\s","");

        if (line.equals("#directed;")) {
            directed = true;
            System.out.print("Directed graph detected.\n");
        }
        else
            // reset bufferedReader to the first line
            bufferedReader.reset();

        // Compile the regex pattern
        Pattern pattern = Pattern.compile
                ("(?<node1>[^\\s,]+)((\\s)+:(\\s)+(?<attr1>[0-9]+))?(,(?<node2>[^\\s]+)((\\s)+:(\\s)+(?<attr2>[0-9]+))?((\\s)+\\((?<edge>[^\\s]+)\\))?((\\s)+::(\\s)+(?<weight>[0-9]+))?)?;|\\s*");
        //("(?<node1>[^\\s,]+)((\\s)+:(\\s)+(?<attr1>[1-9]+))?(,(?<node2>[^\\s]+)((\\s)+:(\\s)+(?<attr2>[1-9]+))?)?;|\\s*");

        while ((line = bufferedReader.readLine()) != null) {
            // Create Matcher object for the read line
            Matcher matcher = pattern.matcher(line);

            // Debugging
            System.out.print(line);

            if (matcher.matches())
                System.out.print(" matches: ");
            else
                System.out.print(" doesn't match: ");
            // End Debugging

            try {
                // Debugging
                System.out.print(matcher.group("node1") + " ");
                System.out.print(matcher.group("node2") + " ");
                System.out.print(matcher.group("attr1") + " ");;
                System.out.print(matcher.group("attr2") + " ");
                System.out.print(matcher.group("edge")  + " ");
                System.out.print(matcher.group("weight")  + "\n");
                // End Debugging

                String node1 = matcher.group("node1");

                if (node1 != null) {
                    // Line is not empty
                    String node2 = matcher.group("node2");

                    if (node2 != null) {
                        // Current line describes an edge
                        String edgeId = String.valueOf(id_counter);
                        graph.addEdge(edgeId, node1, node2, directed);
                        id_counter++;

                        // Apply attr2
                        String attr2 = matcher.group("attr2");
                        if (attr2 != null) {
                            graph.getNode(node2).addAttribute("value", attr2);
                            graph.getNode(node2).addAttribute("ui.label", node2 + ": " + attr2);
                        }
                        else
                            graph.getNode(node2).addAttribute("ui.label", node2);

                        // Apply (non-unique) edge name
                        Edge currentEdge = graph.getEdge(edgeId);
                        String edgeName = matcher.group("edge");
                        if (edgeName != null) {
                            currentEdge.addAttribute("name", edgeName);
                            currentEdge.addAttribute("ui.label", edgeName);
                        }

                        // Apply weight
                        String edgeWeight = matcher.group("weight");
                        if (edgeWeight != null) {
                            currentEdge.addAttribute("weight", edgeWeight);
                            if (currentEdge.hasAttribute("ui.label"))
                                currentEdge.setAttribute("ui.label", currentEdge.getAttribute("ui.label") + ": " + edgeWeight);
                            else
                                currentEdge.addAttribute("ui.label", edgeWeight);
                        }
                    }
                    else {
                        // Current line just describes a single node
                        graph.addNode(node1);
                    }

                    // Apply attr1
                    String attr1 = matcher.group("attr1");
                    if (attr1 != null) {
                        graph.getNode(node1).addAttribute("value", attr1);
                        graph.getNode(node1).addAttribute("ui.label", node1 + ": " + attr1);
                    }
                    else
                        graph.getNode(node1).addAttribute("ui.label", node1);

                }
            }
            catch(IllegalStateException e)
            {
                System.out.print("\nIgnored line '" + line + "' during parsing: wrong syntax\n");
            }
        }

        bufferedReader.close();


        return graph;
    }
}