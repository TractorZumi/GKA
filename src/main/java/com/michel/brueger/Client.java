package com.michel.brueger;

import org.graphstream.graph.Graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    Scanner scanner1 = new Scanner(System.in);
    List<String> parametersList;
    Boolean outputRuntimeBool;

    public void run(){

        while (true) {
            System.out.println("################################################# \n" +
                    "Was möchten Sie tun? \n" +
                    "1: Graph generieren und in Datei speichern\n" +
//                "2: Graph aus Datei einlesen \n" +
                    "2: Kruskal auf eingelesenen Graph anwenden \n" +
                    "3: Kruskal mit Zeitmessung \n" +
                    "4: Kruskal mit Graph-Zugriffen \n" +
                    "5: Prim auf eingelesenen Graph anwenden \n" +
                    "6: Prim mit Zeitmessungen\n" +
                    "7: Prim mit Graph-Zugriffen");

            int eingabe = scanner1.nextInt();

            switch(eingabe) {
                case 1:
                    generateGraphDialog();
                    break;
                case 2:
                    parametersList = mstDialog();
                    outputRuntimeBool = false;
                    if(parametersList.get(1).equals("ja")) outputRuntimeBool = true;
                    try {
                        Graph graph1 = GraphUtilities.createGraphFromFile(parametersList.get(0));
                        MinimumSpanningTrees.minimumSpanningTreeKruskal(graph1, outputRuntimeBool);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    parametersList = mstDialog();
                    outputRuntimeBool = false;
                    if(parametersList.get(1).equals("ja")) outputRuntimeBool = true;
                    try {
                        Graph graph1 = GraphUtilities.createGraphFromFile(parametersList.get(0));
                        System.out.println(MinimumSpanningTrees.minimumSpanningTreeKruskalTime(graph1, outputRuntimeBool));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    parametersList = mstDialog();
                    outputRuntimeBool = false;
                    if(parametersList.get(1).equals("ja")) outputRuntimeBool = true;
                    try {
                        Graph graph1 = GraphUtilities.createGraphFromFile(parametersList.get(0));
//                        System.out.println(MinimumSpanningTrees.minimumSpanningTreeKruskalWithCounter(graph1, true));
                        MinimumSpanningTrees.minimumSpanningTreeKruskalWithCounter(graph1, outputRuntimeBool);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    parametersList = mstDialog();
                    outputRuntimeBool = false;
                    if (parametersList.get(1).equals("ja")) outputRuntimeBool = true;
                    try {
                        Graph graph1 = GraphUtilities.createGraphFromFile(parametersList.get(0));
                        MinimumSpanningTrees.minimumSpanningTreePrim(graph1, outputRuntimeBool);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    parametersList = mstDialog();
                    outputRuntimeBool = false;
                    if (parametersList.get(1).equals("ja")) outputRuntimeBool = true;
                    try {
                        Graph graph1 = GraphUtilities.createGraphFromFile(parametersList.get(0));
                        System.out.println(MinimumSpanningTrees.minimumSpanningTreePrimTime(graph1, outputRuntimeBool));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    parametersList = mstDialog();
                    outputRuntimeBool = false;
                    if (parametersList.get(1).equals("ja")) outputRuntimeBool = true;
                    try {
                        Graph graph1 = GraphUtilities.createGraphFromFile(parametersList.get(0));
                        System.out.println(MinimumSpanningTrees.minimumSpanningTreePrimWithCounter(graph1, outputRuntimeBool));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }                    break;
                default:
                    System.out.println("Ungültige Eingabe! Bitte geben Sie eine Zahl zwischen 1 und 8 ein.");
            }
        }



    }

    private List<String> mstDialog() {
        List<String> resultList = new ArrayList<>();
        System.out.println("Auf welche .graph-Datei soll Kruskal angewendet werden? (nur Name, ohne .graph");
        String filename = scanner1.next();
        String path = "src/main/files/" + filename + ".graph";
        resultList.add(path);
        System.out.println("Möchten Sie Output zur Laufzeit? (ja oder nein, Standard ist nein)");
        String outputRuntimeString = scanner1.next();
        resultList.add(outputRuntimeString);
        return resultList;
    }

    private void generateGraphDialog() {
        System.out.println("Wie soll die Graph-Datei heißen?");
        String filename = scanner1.next();
        String path = "src/main/files/" + filename + ".graph";
//                    GraphUtilities.createFileFromGraph("src/main/files/genGraph1800kv2.graph", graph);
        System.out.println("Wieviele Kanten soll der Graph beinhalten?");
        int nrOfEdges = scanner1.nextInt();
        System.out.println("Wieviele Knoten soll der Graph beinhalten?");
        int nrOfNodes = scanner1.nextInt();
        System.out.println("Soll es ein zusammenhängender Graph sein? (ja oder nein, Standard ist nein)");
        String connectedGraphString = scanner1.next();
        boolean connectedGraphBool = false;
        if (connectedGraphString.equals("ja")) connectedGraphBool = true;
        System.out.println("Sollen Multiedges erlaubt sein? (ja oder nein, Standard ist nein)");
        String multiGraphString = scanner1.next();
        boolean multiGraphBool = false;
        if(multiGraphString.equals("ja")) multiGraphBool = true;
        Graph generatedGraph = MinimumSpanningTrees.generateGraph(nrOfEdges,nrOfNodes,connectedGraphBool,multiGraphBool);
        try {
            GraphUtilities.createFileFromGraph(path, generatedGraph);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // für tests anzahl kanten = anzahl knoten-1
}
