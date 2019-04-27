package com.michel.brueger;

import org.graphstream.graph.Graph;

import java.io.IOException;
import java.util.Scanner;

public class Client {
    Scanner scanner1 = new Scanner(System.in);

    public void run(){

        System.out.println("Was möchten Sie tun? \n" +
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
//                    System.out.println("eingabe war 2");
//                    minimumSpanningTreeKruskal(Graph graph, boolean outputRuntime)
//                    generateGraphDialog();
                    System.out.println("Auf welche .graph-Datei soll Kruskal angewendet werden? (nur Name, ohne .graph");
                    String filename = scanner1.next();
                    String path = "src/main/files/" + filename + ".graph";
                    try {
                        Graph graph1 = GraphUtilities.createGraphFromFile("path");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    MinimumSpanningTrees.minimumSpanningTreeKruskal(graph)
                    break;
                case 3:
                    System.out.println("eingabe war 3");
                    break;
                case 4:
                    System.out.println("eingabe war 4");
                    break;
                case 5:
                    System.out.println("eingabe war 5");
                    break;
                case 6:
                    System.out.println("eingabe war 6");
                    break;
                case 7:
                    System.out.println("eingabe war 7");
                    break;
                case 8:
                    System.out.println("eingabe war 8");
                    break;
                default:
                    System.out.println("Ungültige Eingabe! Bitte geben Sie eine Zahl zwischen 1 und 8 ein.");
            }


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

}
