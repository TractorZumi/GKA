package com.michel.brueger;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import scala.util.Random;

import java.util.ArrayList;

public class Eulergenerator {

    public static Graph generateEulergraph(int numberOfNodes){
        Graph resultGraph = null;
        resultGraph = new SingleGraph("Generated Graph " + System.currentTimeMillis());

        // Return empty graph for negative number of edges or zero/negative nodes
        if (numberOfNodes <= 0) return resultGraph;

        resultGraph.setAutoCreate(true);
        resultGraph.setStrict(false);

        for(int i=0; i<numberOfNodes; i++){
            resultGraph.addNode("Node"+i);
        }

        ArrayList<Node> nodesList = new ArrayList<>();
        nodesList.addAll(resultGraph.getNodeSet());

        Random random = new Random();
        int randomIndex1 = random.nextInt(numberOfNodes);
        int randomIndex2 = random.nextInt(numberOfNodes);
        while(randomIndex1 == randomIndex2){
            randomIndex2 = random.nextInt(numberOfNodes);
            nodesList.remove(randomIndex1);
            nodesList.remove(randomIndex2);
        }
        resultGraph.addEdge("Edge"+0, randomIndex1, randomIndex2);
        int connectedComponentSize = 2;
        int randomCoCoIndex;
        for(int i=0; i<numberOfNodes-2; i++){
            randomIndex2 = random.nextInt(numberOfNodes-2-i);

            randomCoCoIndex = random.nextInt(connectedComponentSize);
            int edgeIndex = i+1;
            resultGraph.addEdge("Edge" + edgeIndex, randomCoCoIndex, randomIndex2);
            connectedComponentSize++;
            //TODO: besser mit Attributen an den Nodes als alles mit doofen Listen
        }

        ArrayList<Node>
        for(Node node : resultGraph.getNodeSet()){
            if(node.getDegree() % 2 != 0){

            }
        }


        return resultGraph;
    }

}
