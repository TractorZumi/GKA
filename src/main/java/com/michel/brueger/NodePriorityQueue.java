package com.michel.brueger;

import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class NodePriorityQueue {
    public NodePriorityQueue(){
        nodeKeyPairHeap = new ArrayList<>();
        indexIntoHeap = new HashMap<>();
    }

    public boolean isEmpty(){
        return nodeKeyPairHeap.isEmpty();
    }

    public void insert(Node node, Double key){
        // Add new NodeKeyPair into the heap
        NodeKeyPair newPair = new NodeKeyPair(node, key);
        indexIntoHeap.put(node, nodeKeyPairHeap.size());
        nodeKeyPairHeap.add(newPair);

        // Rebuild heap property
        upHeap(nodeKeyPairHeap.size() - 1);
    }

    // Remove element with the smallest key
    public Node removeMinimum(){
        if (nodeKeyPairHeap.isEmpty())
            throw new IllegalStateException("Remove minimum called on empty queue.");

        Node minimum = nodeKeyPairHeap.get(0).getNode();
        int currentSize = nodeKeyPairHeap.size();

        if (currentSize == 1) {
            nodeKeyPairHeap.remove(0);
            return minimum;
        }

        // Put last element as the new root, both subtrees remain min heaps
        nodeKeyPairHeap.set(0, nodeKeyPairHeap.get(currentSize - 1));
        nodeKeyPairHeap.remove(currentSize - 1);

        indexIntoHeap.replace(nodeKeyPairHeap.get(0).getNode(), 0);

        // Heapify starting from the root
        heapify(0);

        return minimum;
    }

    // New key assumed to be smaller than old key, upHeap can always be used
    public void decreaseKey(Node node, Double newKey){
        int index = indexIntoHeap.get(node);

        nodeKeyPairHeap.get(index).setKey(newKey);

        upHeap(index);
    }

    public void printHeap(String message){
        System.out.print(message + "\n");
        int j = 0;
        for (int i = 0; i < nodeKeyPairHeap.size(); i++){
            System.out.print(nodeKeyPairHeap.get(i).getNode().getId() + ": " + nodeKeyPairHeap.get(i).getKey() + " ");
        }
        System.out.print("\n");
    }

    // Rebuild heap property from root element with both subtrees being min heaps
    private void heapify(int i)
    {
        int leftChild = leftChild(i);
        int rightChild = rightChild(i);
        int currentSize = nodeKeyPairHeap.size();

        Double smallestKey = nodeKeyPairHeap.get(i).getKey();
        boolean continueRecursion = false;

        if (leftChild < currentSize && nodeKeyPairHeap.get(leftChild).getKey() < smallestKey){
            continueRecursion = true;
            smallestKey = nodeKeyPairHeap.get(leftChild).getKey();
        }

        if (rightChild < currentSize && nodeKeyPairHeap.get(rightChild).getKey() < smallestKey){
            swapNodes(i, rightChild);
            heapify(rightChild);
        }
        else if (continueRecursion){
            swapNodes(i, leftChild);
            heapify(leftChild);
        }
    }

    // Rebuild heap property by bubbling up one element
    private void upHeap(int i){
        int parent = parent(i);

        while (i > 0 && nodeKeyPairHeap.get(parent).getKey() > nodeKeyPairHeap.get(i).getKey()){
            swapNodes(i, parent);
            i = parent;
            parent = parent(i);
        }
    }

    private void swapNodes(int i, int j){
        NodeKeyPair nodeKeyPairAtI = nodeKeyPairHeap.get(i);
        NodeKeyPair nodeKeyPairAtJ = nodeKeyPairHeap.get(j);
        indexIntoHeap.replace(nodeKeyPairAtI.getNode(), j);
        indexIntoHeap.replace(nodeKeyPairAtJ.getNode(), i);
        nodeKeyPairHeap.set(i, nodeKeyPairAtJ);
        nodeKeyPairHeap.set(j, nodeKeyPairAtI);
    }

    private int parent(int i){
        return (i - 1) / 2;
    }

    private int leftChild(int i){
        return 2 * i + 1;
    }

    private int rightChild(int i){
        return 2 * i + 2;
    }

    // List holding the full data
    ArrayList<NodeKeyPair> nodeKeyPairHeap;

    // Index into the array list, used for decreaseKey
    HashMap<Node, Integer> indexIntoHeap;
}
