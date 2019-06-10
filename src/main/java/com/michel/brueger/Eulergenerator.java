package com.michel.brueger;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import scala.util.Random;

import java.util.ArrayList;

public class Eulergenerator {

    public static Graph generateEulergraph(int numberOfNodes){
        Graph resultGraph = null;
        resultGraph = new SingleGraph("Generated Graph " + System.currentTimeMillis());

        // Return empty graph for zero/negative nodes
        if (numberOfNodes <= 0) return resultGraph;

        resultGraph.setAutoCreate(true);
        resultGraph.setStrict(false);

        // list of unconnected nodes
        ArrayList<Node> nodesList = new ArrayList<>();

        for(int i=0; i<numberOfNodes; i++){
            String nodeId = "Node" + i;
            resultGraph.addNode(nodeId);
            Node node = resultGraph.getNode(nodeId);
            node.setAttribute("connected", false);
            nodesList.add(node);
            System.out.println("NodeId = " + nodeId + " // NodeIndex = " + resultGraph.getNode(nodeId).getIndex() + " // Connected = " + resultGraph.getNode(nodeId).getAttribute("connected"));
        }

        Random random = new Random();
        int randomIndex1 = random.nextInt(numberOfNodes);
        int randomIndex2 = random.nextInt(numberOfNodes);
        while(randomIndex1 == randomIndex2){
            randomIndex2 = random.nextInt(numberOfNodes);
        }

        Node source, target;
        ArrayList<Node> connectedNodesList = new ArrayList<>();
        Integer edgeCounter = 0;

        // chose new random unconnected node and connect to random connected node until there are no more unconnected nodes
        // connects all nodes / spanning tree
        Integer nodesListSize = nodesList.size();
//        for(int i=0; i<nodesListSize; i++){
        while (nodesList.size() > 0) {
            if (connectedNodesList.size()==0) {
                randomIndex1 = random.nextInt(nodesList.size());
                randomIndex2 = random.nextInt(nodesList.size());
                while(randomIndex1 == randomIndex2){
                    randomIndex2 = random.nextInt(nodesList.size());
                }
                source = resultGraph.getNode(nodesList.get(randomIndex1).getIndex());
                target = resultGraph.getNode(nodesList.get(randomIndex2).getIndex());
                System.out.println("source 59 = " + source);
                System.out.println("target 60 = " + target);
                System.out.println("61--------");
                connectedNodesList.add(source);
                nodesList.remove(Math.max(randomIndex1, randomIndex2));
                nodesList.remove(Math.min(randomIndex1, randomIndex2));
                System.out.println("connectedNodesList 63 = " + connectedNodesList);
            } else {
                randomIndex1 = random.nextInt(connectedNodesList.size());
                System.out.println("Nodeslist 68 = " + nodesList);
                randomIndex2 = random.nextInt(nodesList.size());
//                randomIndex2 = random.nextInt(nodesListSize);
                source = resultGraph.getNode(connectedNodesList.get(randomIndex1).getIndex());
                target = resultGraph.getNode(nodesList.get(randomIndex2).getIndex());
                nodesList.remove(randomIndex2);
                System.out.println("source 73 = " + source);
                System.out.println("target 74 = " + target);
                System.out.println("75--------");
            }
            resultGraph.addEdge("Edge"+edgeCounter, source, target);
            edgeCounter++;
            connectedNodesList.add(target);
//            nodesList.remove(randomIndex2);
            System.out.println("connectedNodesList 81 = " + connectedNodesList);
        }

        // create list of all nodes with odd degrees
        ArrayList<Node> oddDegreeNodesList = new ArrayList<>();
        for(Node node : resultGraph.getNodeSet()){
            if (node.getDegree() % 2 != 0) {
                oddDegreeNodesList.add(node);
            }
        }

        // choose 2 random nodes with odd degrees and connect them
//        for(int i=0; i<oddDegreeNodesList.size(); i++){
        while (oddDegreeNodesList.size() > 0){
            ArrayList<Node> oddDegreeNodesListClone = (ArrayList<Node>) oddDegreeNodesList.clone();
            System.out.println("oddDegreeNodesList 95 = " + oddDegreeNodesList);

            // special Case: last two odd nodes are already connected, then connect to third node none of them is already connected to
            if(oddDegreeNodesList.size()==2 && checkIfEdgeAlreadyExists(resultGraph, resultGraph.getNode(oddDegreeNodesList.get(0).getIndex()), resultGraph.getNode(oddDegreeNodesList.get(1).getIndex())) )
            {
               Node node1 = resultGraph.getNode(oddDegreeNodesList.get(0).getIndex());
               Node node2 = resultGraph.getNode(oddDegreeNodesList.get(1).getIndex());
               ArrayList<Node> possibleNodesList = new ArrayList<>();
               for(Node node : resultGraph.getNodeSet()){
                   if(!(checkIfEdgeAlreadyExists(resultGraph, node, node1)) && !(checkIfEdgeAlreadyExists(resultGraph, node, node2))){
                       possibleNodesList.add(node);
                       System.out.println("possibleNodesList intern 108 = " + possibleNodesList);
                   }
               }
                System.out.println("possibleNodesList extern 111 = " + possibleNodesList);

               if(possibleNodesList.size()==0){
                   String nodeId = "Node"+resultGraph.getNodeSet().size();
                   resultGraph.addNode(nodeId);
                   target = resultGraph.getNode(nodeId);
                   resultGraph.addEdge("Edge"+edgeCounter, node1, target);
                   edgeCounter++;
                   resultGraph.addEdge("Edge"+edgeCounter, node2, target);
                   edgeCounter++;
                   oddDegreeNodesList.clear();
               } else {
                   randomIndex1 = random.nextInt(possibleNodesList.size());
                   target = resultGraph.getNode(possibleNodesList.get(randomIndex1).getIndex());
                   System.out.println(" 124 node1 = " + node1 + " // node2 = " + node2 + " // target = " + target);
                   resultGraph.addEdge("Edge" + edgeCounter, node1, target);
                   System.out.println("126 edge added");
                   edgeCounter++;
                   resultGraph.addEdge("Edge" + edgeCounter, node2, target);
                   System.out.println("129 edge added");
                   edgeCounter++;
                   oddDegreeNodesList.clear();
               }
            } else {
                randomIndex1 = random.nextInt(oddDegreeNodesList.size());
                source = resultGraph.getNode(oddDegreeNodesList.get(randomIndex1).getIndex());
                System.out.println("source 136 = " + source);
                randomIndex2 = random.nextInt(oddDegreeNodesList.size());
                while(randomIndex2 == randomIndex1){
                    randomIndex2 = random.nextInt(oddDegreeNodesList.size());
                }
//                while (oddDegreeNodesList.size() > 2) {
//                if (oddDegreeNodesList.size() > 2) {
                    // reduce cloneList until node found without existing edge to source
                    while (checkIfEdgeAlreadyExists(resultGraph, source, resultGraph.getNode(oddDegreeNodesListClone.get(randomIndex2).getIndex()))) {
                        oddDegreeNodesListClone.remove(randomIndex2);
                        randomIndex2 = random.nextInt(oddDegreeNodesListClone.size());
                    }
//                }
                target = resultGraph.getNode(oddDegreeNodesListClone.get(randomIndex2).getIndex());
                System.out.println("target 150 = " + target);

                resultGraph.addEdge("Edge" + edgeCounter, source, target);
                edgeCounter++;
                System.out.println("154 edge added");
                oddDegreeNodesList.remove(Math.max(randomIndex1, randomIndex2));
                oddDegreeNodesList.remove(Math.min(randomIndex1, randomIndex2));
            }
        }

        return resultGraph;

    }


    public static boolean checkIfEdgeAlreadyExists(Graph graph, Node nodeA, Node nodeB){
        for(Edge edge : graph.getEdgeSet()){
            if((edge.getNode0() == nodeA && edge.getNode1()==nodeB) || edge.getNode0() == nodeB && edge.getNode1() == nodeA){
                return true;
            }
        }
        return false;
    }

}
