package com.michel.brueger;
import java.util.ArrayList;

// DisjointSet Data-structure using an internal array and positions(instead of Ids and a HashMap?)
public class DisjointSet {
    ArrayList<DisjointSetElement> disjointSet;

    public DisjointSet() {
        disjointSet = new ArrayList<>();
    }

    public int makeSet() {
        int id = disjointSet.size();
        disjointSet.add(new DisjointSetElement(id, id, 1));
        return id;
    }

    // Find using path compression
    public int find(int position) {
        DisjointSetElement element = disjointSet.get(position);

        int parent = element.getParent();

    // path compression
        if(parent != position) {
            element.setParent(find(parent));
            return element.getParent();
        }
        else
            return parent;
    }

    // Union by size
    public boolean union(int position1, int position2) {   // position = id
        int root1 = find(position1);
        int root2 = find(position2);

        if (root1 == root2)  // dann sind die Knoten schon irgendwie verbunden
            return false;

        DisjointSetElement rootElement1 = disjointSet.get(root1);
        DisjointSetElement rootElement2 = disjointSet.get(root2);

        // kleinerer Baum wird an Wurzel des größeren Baumes gehängt (verhindert Entartung)
        if (rootElement1.getSize() > rootElement2.getSize()) {
            rootElement2.setParent(root1);
            rootElement1.setSize(rootElement1.getSize() + rootElement2.getSize());
        }
        else {
            rootElement1.setParent(root2);
            rootElement2.setSize(rootElement1.getSize() + rootElement2.getSize());
        }

        return true;
    }
}





