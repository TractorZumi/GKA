package com.michel.brueger;

import java.util.*;

import org.graphstream.algorithm.Kruskal;
import org.graphstream.algorithm.Prim;
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

    public static long minimumSpanningTreeKruskalTime(Graph graph, boolean outputRuntime){
        long startTime = System.currentTimeMillis();
        for(int i=0; i<10; i++){
            minimumSpanningTreeKruskal(graph, outputRuntime);
        }
        long endTime = System.currentTimeMillis();
        return  (endTime-startTime)/10;
    }

    public static ArrayList<Edge> minimumSpanningTreeKruskal(Graph graph, boolean outputRuntime) {
        long start = 0;
        //long accessCounter = 0;
        if (outputRuntime){
           start = java.lang.System.currentTimeMillis();
        }
        // Create an Edge Array for fast sorting
        Collection<Edge> edgeSet = graph.getEdgeSet();
        Edge[] edgeArray = new Edge[edgeSet.size()];

        // Create the result list of spanningEdges
        ArrayList<Edge> spanningEdges = new ArrayList<>();

        int i = 0;
        for(Edge edge : edgeSet) {
            if (!edge.hasAttribute("weight")) {
                return spanningEdges;
            }
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

        if (outputRuntime) {
            System.out.print("Kruskal's Algorithm runtime: " + ((java.lang.System.currentTimeMillis() - start) / 1000.0) + " secs" + "\n");
            //System.out.print("Kruskal's Algorithm accesses: " + accessCounter);
        }

        return spanningEdges;
    }

    public static long minimumSpanningTreeKruskalWithCounter(Graph graph, boolean outputRuntime) {
        long start = 0;
        long accessCounter = 0;
        if (outputRuntime)
            start = java.lang.System.currentTimeMillis();
        // Create an Edge Array for fast sorting
        Collection<Edge> edgeSet = graph.getEdgeSet();
        Edge[] edgeArray = new Edge[edgeSet.size()];

        // Create the result list of spanningEdges
        ArrayList<Edge> spanningEdges = new ArrayList<>();

        int i = 0;
        for(Edge edge : edgeSet) {
            if (!edge.hasAttribute("weight")) {
//                return spanningEdges;
                return accessCounter;
            }
            accessCounter += 1;
            edgeArray[i] = edge;
            i++;
        }

        // Create new DisjointSet
        DisjointSet disjointSet = new DisjointSet();

        // Create a single Set for each Node
        for (Node node : graph.getEachNode()) {
            node.setAttribute("position", disjointSet.makeSet());
            accessCounter += 1;
        }

        // Sort the Edges by weight
        Arrays.sort(edgeArray, Comparator.comparingDouble((Edge edge) -> (Double)edge.getAttribute("weight")));

        Double totalWeight = 0.0;

        // Loop adding new edges
        for(i = 0; i < edgeArray.length; i++) {
            int position1 = edgeArray[i].getNode0().getAttribute("position");
            int position2 = edgeArray[i].getNode1().getAttribute("position");
            accessCounter += 2;
            if (disjointSet.union(position1, position2)) {
                // edge has merged to sets, add it to the spanning tree
                spanningEdges.add(edgeArray[i]);
                totalWeight += (Double)edgeArray[i].getAttribute("weight");
            }
        }

        graph.setAttribute("minimumSpanningWeight", totalWeight);

        if (outputRuntime) {
            System.out.print("Kruskal's Algorithm runtime: " + ((java.lang.System.currentTimeMillis() - start) / 1000.0) + " secs" + "\n");
            System.out.print("Kruskal's Algorithm accesses: " + accessCounter + "\n");
        }

//        return spanningEdges;
        return accessCounter;
    }

    public static long minimumSpanningTreePrimTime(Graph graph, boolean outputRuntime){
        long startTime = System.currentTimeMillis();
        for(int i=0; i<10; i++){
            minimumSpanningTreePrim(graph, outputRuntime);
        }
        long endTime = System.currentTimeMillis();
        return (startTime - endTime) / 10;
    }


    public static ArrayList<Edge> minimumSpanningTreePrim(Graph graph, boolean outputRuntime) {
        long start = 0;
        if (outputRuntime)
            start = java.lang.System.currentTimeMillis();

        // Initialize empty list of spanning edges and empty priority queue
        ArrayList<Edge> spanningEdges = new ArrayList<>();
        Double spanningWeight = 0.0;
        Comparator<NodeKeyPair> edgeComparator = Comparator.comparingDouble((NodeKeyPair) -> (Double)NodeKeyPair.getKey());
        PriorityQueue<NodeKeyPair> priorityQueue = new PriorityQueue<>(edgeComparator);

        // Initialize the priority queue with a random node
        Node initialNode = graph.getNodeIterator().next();
        priorityQueue.add(new NodeKeyPair(initialNode, 0));

        // Create dummy edge with weight 0 for the initial node's connectedEdge
        initialNode.setAttribute("connectingEdge", graph.addEdge("dummyEdge", initialNode, initialNode));
        graph.getEdge("dummyEdge").setAttribute("weight", (Double)0.0);

        while(!priorityQueue.isEmpty()){
            // Deque element with minimum key
            NodeKeyPair nodeKeyPair = priorityQueue.remove();
            Node currentNode = nodeKeyPair.getNode();

            if (currentNode.getAttribute("isInSpanningTree") != null)
                continue;

            // Loop all edges, because we allow multi-edges
            Iterator<Edge> connectedEdgesIterator = currentNode.getEdgeIterator();

            // Mark node as added
            currentNode.setAttribute("isInSpanningTree", true);

            while (connectedEdgesIterator.hasNext()){
                Edge edge = connectedEdgesIterator.next();

                Node currentNeighbour = null;
                if (edge.getNode1().getId() == currentNode.getId())
                    currentNeighbour = edge.getNode0();
                else
                    currentNeighbour = edge.getNode1();

                // Check, if neighbour is already in spanning tree
                if (currentNeighbour.getAttribute("isInSpanningTree") == null) {
                    Double weight = edge.getAttribute("weight");

                    Edge oldEdge = currentNeighbour.getAttribute("connectingEdge");

                    // Add node to the queue, if there was no previous edge or the new weight is lower.
                    // Node can now be in the priority queue with multiple edges, but only the one with the
                    // lowest weight will be dequed first. All subsequent pairs will be discarded.
                    if (oldEdge == null || (Double)oldEdge.getAttribute("weight") > weight) {
                        priorityQueue.add(new NodeKeyPair(currentNeighbour, weight));

                        currentNeighbour.setAttribute("connectingEdge", edge);
                    }
                }
            }
            // Add node's connecting edge to the spanning tree
            Edge newEdge = currentNode.getAttribute("connectingEdge");
            spanningEdges.add(newEdge);

          //  System.out.print(currentNode + " ");
           // System.out.print(newEdge + Double.toString(newEdge.getAttribute("weight")) + "\n");

            spanningWeight += (Double)newEdge.getAttribute("weight");
        }

        // Remove the dummy edge from the graph
        spanningEdges.remove(graph.getEdge("dummyEdge"));
        graph.removeEdge("dummyEdge");


        // Add attribute to access mininum spanning weight
        graph.setAttribute("minimumSpanningWeight", spanningWeight);

        if (outputRuntime)
            System.out.print("Prim Algorithm runtime: " + ((java.lang.System.currentTimeMillis() - start) / 1000.0) + " secs" + "\n");

        return spanningEdges;
    }

    public static int minimumSpanningTreePrimDecreaseKeyWithCounter(Graph graph){
        long start = 0;
        start = java.lang.System.currentTimeMillis();

        int accessCounter = 0;

        // Initialize empty list of spanning edges and empty priority queue
        ArrayList<Edge> spanningEdges = new ArrayList<>();
        Double spanningWeight = 0.0;

        NodePriorityQueue priorityQueue = new NodePriorityQueue();

        // Insert each node into the priority queue
        for (Node node : graph.getEachNode()){
            priorityQueue.insert(node, Double.MAX_VALUE);
            accessCounter++;
        }

        while(!priorityQueue.isEmpty()){
            // Deque element with minimum key
            Node currentNode = priorityQueue.removeMinimum();

            // Loop all connected edges, because we allow multi-edges
            Iterator<Edge> connectedEdgesIterator = currentNode.getEdgeIterator();
            accessCounter++;
            // Mark node as added
            currentNode.setAttribute("isInSpanningTree", true);

            while (connectedEdgesIterator.hasNext()){
                accessCounter++;
                Edge edge = connectedEdgesIterator.next();

                Node currentNeighbour = null;
                if (edge.getNode1().getId() == currentNode.getId())
                    currentNeighbour = edge.getNode0();
                else
                    currentNeighbour = edge.getNode1();
                accessCounter++;
                // Check, if neighbour is already in spanning tree
                if (currentNeighbour.getAttribute("isInSpanningTree") == null) {
                    Double weight = edge.getAttribute("weight");
                    accessCounter++;

                    Edge oldEdge = currentNeighbour.getAttribute("connectingEdge");
                    accessCounter++;

                    accessCounter++;
                    if (oldEdge == null || (Double)oldEdge.getAttribute("weight") > weight) {
                        priorityQueue.decreaseKey(currentNeighbour, weight);
                        currentNeighbour.setAttribute("connectingEdge", edge);
                        accessCounter++;
                    }
                }
            }

            accessCounter++;
            // Add node's connecting edge to the spanning tree
            Edge newEdge = currentNode.getAttribute("connectingEdge");

            if (newEdge != null) {
                spanningEdges.add(newEdge);
                spanningWeight += (Double) newEdge.getAttribute("weight");
            }
        }

        accessCounter++;
        // Add attribute to access mininum spanning weight
        graph.setAttribute("minimumSpanningWeight", spanningWeight);

        return accessCounter;
    }

    public static ArrayList<Edge> minimumSpanningTreePrimDecreaseKey(Graph graph, boolean outputRuntime){
        long start = 0;
        if (outputRuntime)
            start = java.lang.System.currentTimeMillis();

        // Initialize empty list of spanning edges and empty priority queue
        ArrayList<Edge> spanningEdges = new ArrayList<>();
        Double spanningWeight = 0.0;

        NodePriorityQueue priorityQueue = new NodePriorityQueue();

        // Insert each node into the priority queue
        for (Node node : graph.getEachNode()){
            priorityQueue.insert(node, Double.MAX_VALUE);
        }

        while(!priorityQueue.isEmpty()){
            // Deque element with minimum key
            Node currentNode = priorityQueue.removeMinimum();

            // Loop all connected edges, because we allow multi-edges
            Iterator<Edge> connectedEdgesIterator = currentNode.getEdgeIterator();

            // Mark node as added
            currentNode.setAttribute("isInSpanningTree", true);

            while (connectedEdgesIterator.hasNext()){
                Edge edge = connectedEdgesIterator.next();

                Node currentNeighbour = null;
                if (edge.getNode1().getId() == currentNode.getId())
                    currentNeighbour = edge.getNode0();
                else
                    currentNeighbour = edge.getNode1();

                // Check, if neighbour is already in spanning tree
                if (currentNeighbour.getAttribute("isInSpanningTree") == null) {
                    Double weight = edge.getAttribute("weight");

                    Edge oldEdge = currentNeighbour.getAttribute("connectingEdge");

                    if (oldEdge == null || (Double)oldEdge.getAttribute("weight") > weight) {
                        priorityQueue.decreaseKey(currentNeighbour, weight);
                        currentNeighbour.setAttribute("connectingEdge", edge);
                    }
                }
            }

            // Add node's connecting edge to the spanning tree
            Edge newEdge = currentNode.getAttribute("connectingEdge");

            if (newEdge != null) {
                spanningEdges.add(newEdge);
                spanningWeight += (Double) newEdge.getAttribute("weight");
            }
        }

        // Add attribute to access mininum spanning weight
        graph.setAttribute("minimumSpanningWeight", spanningWeight);

        if (outputRuntime)
            System.out.print("Prim Algorithm(with decrease key) runtime: " + ((java.lang.System.currentTimeMillis() - start) / 1000.0) + " secs" + "\n");

        return spanningEdges;
    }

    public static Double kruskalGraphstream(Graph graph){
        Kruskal kruskal = new Kruskal();
        kruskal.init(graph);
        long start = java.lang.System.currentTimeMillis();
        kruskal.compute();

        System.out.print("runtime graphstream kruskal: " + (java.lang.System.currentTimeMillis() - start) / 1000.0 + "\n");
        System.out.print("graphstream kruskal weight: " + kruskal.getTreeWeight() + "\n");
        return kruskal.getTreeWeight();
    }

    public static Double primGraphstream(Graph graph){
        Prim prim = new Prim();
        prim.init(graph);
        long start = java.lang.System.currentTimeMillis();
        prim.compute();

        System.out.print("runtime graphstream prim: " + (java.lang.System.currentTimeMillis() - start) / 1000.0 + "\n");
        System.out.print("graphstream prim weight: " + prim.getTreeWeight() + "\n");
        return prim.getTreeWeight();
    }

    public static long minimumSpanningTreePrimWithCounter(Graph graph1, Boolean outputRuntimeBool) {
        return 0;
    }
}
