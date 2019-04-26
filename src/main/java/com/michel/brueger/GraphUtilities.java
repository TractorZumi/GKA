package com.michel.brueger;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;


import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public final class GraphUtilities {
    private static String fileEncoding = "UTF-8";

    public static void useUTF8() {
        fileEncoding = "UTF-8";
    }

    public static void useWindows1252() {
        fileEncoding = "Cp1252";
    }


    public static void colorInEdges(ArrayList<Edge> edges, Graph graph, String color)
    {
        // color in the edges in red and beautify the graph

        graph.addAttribute("ui.stylesheet", "edge.path { size: 2px; stroke-color: red; fill-color:" + color + "; stroke-width: 1px; stroke-mode: plain;}node {\r\n" +
                "	size: 14px, 10px;text-size: 13;text-alignment: right;text-background-mode:rounded-box;text-background-color:yellow;}");

        if (edges == null)
            return;

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

    public static boolean createFileFromGraph(String filePath, Graph graph) throws IOException{
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, false), fileEncoding))){
            StringBuilder result = new StringBuilder();
            boolean hasDirected = false;
            boolean hasUndirected = false;

            HashSet<String> nodes = new HashSet<>();
            for(Edge edge : graph.getEachEdge()) {
                Node node0 = edge.getNode0();
                Node node1 = edge.getNode1();

                nodes.add(node0.getId());
                nodes.add(node1.getId());

                result.append(node0.getId());
                if (node0.hasAttribute("value")) {
                    result.append(" : " + node0.getAttribute("value") + ",");
                }
                result.append(",");

                result.append(node1.getId());
                if (node1.hasAttribute("value")) {
                    result.append(" : " + node1.getAttribute("value"));
                }

                if(edge.hasAttribute("name"))
                    result.append(" (" + edge.getAttribute("name") + ")");

                if (edge.hasAttribute("weight"))
                    result.append(" :: " + Double.toString((Double)edge.getAttribute("weight")));

                result.append(";\n");

                if(edge.isDirected())
                    hasDirected = true;
                else
                    hasUndirected = true;
            }

            for (Node node : graph.getEachNode()) {
                if(!nodes.contains(node.getId())) {
                    result.append(node.getId() + ";\n");
                }
            }

            String trueResult = result.toString();
            if (hasDirected) {
                if (hasUndirected)
                    // the current format does not support mixed graphs
                    return false;
                else
                    trueResult = "#directed;\n" + result;
            }

            if (trueResult.length() > 0)
                writer.write(trueResult.substring(0, trueResult.length() - 1));
        }

        return true;
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
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), fileEncoding)))
        {
            String line;

            // Check whether the graph is directed or undirected

            boolean directed = false;

            // Save pointer into stream
            bufferedReader.mark(1000);

            line = bufferedReader.readLine();

            if (line != null) {
                line = line.replaceAll("\\s", "");

                if (line.equals("#directed;")) {
                    directed = true;
                    //System.out.print("Directed graph detected.\n");
                } else
                    // reset bufferedReader to the first line
                    bufferedReader.reset();
            }
            // Compile the regex pattern
            Pattern pattern = Pattern.compile
                    //("(?<node1>[^\\s,]+)((\\s)+:(\\s)+(?<attr1>[0-9]+))?(,(?<node2>[^\\s]+)((\\s)+:(\\s)+(?<attr2>[0-9]+))?((\\s)+\\((?<edge>[^\\s]+)\\))?((\\s)+::(\\s)+(?<weight>[0-9]+))?)?;|\\s*");
                            ("(?<node1>[^\\s,]+)((\\s)+:(\\s)+(?<attr1>[0-9]+))?(,(?<node2>[^\\s]+)((\\s)+:(\\s)+(?<attr2>[0-9]+))?((\\s)+\\((?<edge>[^\\s]+)\\))?((\\s)+::(\\s)+(?<weight>[0-9]+(\\.)?[0-9]*))?)?;|\\s*");

            while ((line = bufferedReader.readLine()) != null) {
                // Create Matcher object for the read line
                Matcher matcher = pattern.matcher(line);

                // Debugging

              matcher.matches();
              // End Debugging

                try {
                    // Debugging
                    /*
                    System.out.print(matcher.group("node1") + " ");
                    System.out.print(matcher.group("node2") + " ");
                    System.out.print(matcher.group("attr1") + " ");;
                    System.out.print(matcher.group("attr2") + " ");
                    System.out.print(matcher.group("edge")  + " ");
                    System.out.print(matcher.group("weight")  + "\n");
                    */

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
                                currentEdge.addAttribute("weight", Double.parseDouble(edgeWeight));
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

            //   bufferedReader.close();
        }

        return graph;
    }

    public static boolean isEqual(Graph graph1, Graph graph2){
        if (graph1.getNodeCount() != graph2.getNodeCount())
            return false;

        if (graph1.getEdgeCount() != graph2.getEdgeCount())
            return false;

        HashSet<Edge> mappedEdges1 = new HashSet<>();

        // For each edge in graph2, find a identical edge in graph1 and mark it as mapped
        for (Edge edge2 : graph2.getEachEdge()) {
            boolean mapped = false;
            //System.out.print("Looking at " + edge2.getNode0().getId() + "->" + edge2.getNode1().getId() + "\n");
            for (Edge edge1 : graph1.getEachEdge()){

                // was not already mapped
                if (mappedEdges1.contains(edge1) || mapped)
                    continue;
               // System.out.print("Trying to match with " + edge1.getNode0().getId() + "->" + edge1.getNode1().getId() + "\n");

                // is also directed/undirected and has same node0 and node1(can be swapped if undirected)
                if (edge1.isDirected() && edge2.isDirected()) {
                    if (!(edge1.getNode0().getId().equals(edge2.getNode0().getId()) && edge1.getNode1().getId().equals(edge2.getNode1().getId()))) {
                        continue;
                    }
                } else if (!edge1.isDirected() && !edge2.isDirected()){
                    if (!((edge1.getNode0().getId().equals(edge2.getNode0().getId()) && edge1.getNode1().getId().equals(edge2.getNode1().getId())) ||
                        (edge1.getNode1().getId().equals(edge2.getNode0().getId()) && edge1.getNode0().getId().equals(edge2.getNode1().getId()))))
                        continue;
                }
                else {
                    continue;
                }

                // same name
                if (edge2.hasAttribute("name") && edge1.hasAttribute("name")) {
                    if (!edge2.getAttribute("name").equals(edge1.getAttribute("name")))
                        continue;
                }
                else if (edge2.hasAttribute("name") || edge1.hasAttribute("name"))
                    continue;

                // has same weight
                if (edge2.hasAttribute("weight") && edge1.hasAttribute("weight")) {
                    if (!edge2.getAttribute("weight").equals(edge1.getAttribute("weight")))
                        continue;
                }
                else if (edge2.hasAttribute("weight") || edge1.hasAttribute("weight"))
                        continue;

                mappedEdges1.add(edge1);
                mapped = true;
               // System.out.print("matched\n");
            }
        }

        if (mappedEdges1.size() != graph1.getEdgeCount())
            return false;

        // For each node, find one with the same unique id
        for (Node node : graph1.getEachNode()){
            Node node2 = graph2.getNode(node.getId());

            if (node2 == null)
                return false;

            if (node.hasAttribute("name")){
                if (!node2.hasAttribute("name"))
                    return false;

                if (node2.getAttribute("name") != node.getAttribute("name"))
                    return false;
            }

            if (node.hasAttribute("value")){
                if (!node2.hasAttribute("value"))
                    return false;

                if (node2.getAttribute("value") != node.getAttribute("value"))
                    return false;
            }
        }

        return true;
    }

    public static void labelGraph(Graph graph){
        for (Edge edge : graph.getEachEdge()){
            if (edge.hasAttribute("weight"))
                edge.addAttribute("ui.label", (Double)edge.getAttribute("weight"));
            else
                edge.addAttribute("ui.label", "no weight");
        }
        for (Node node : graph.getEachNode()){
            node.addAttribute("ui.label", node.getId());
        }
    }
}



