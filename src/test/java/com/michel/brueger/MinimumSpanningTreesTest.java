package com.michel.brueger;


import org.graphstream.graph.Graph;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;


import static org.junit.jupiter.api.Assertions.*;

class MinimumSpanningTreesTest {
    Graph graph03;
    Graph graph04;
    Graph graph05;
    Graph graph07;
    Graph graph08;
    Graph graph10;
    Graph graph11;
    Graph graphUltimo1;
//    Graph big25E1V;
//    Graph big12E05V;
//    Graph big6E025V;



    @BeforeEach
    void setUp() throws IOException {
        graph03 = GraphUtilities.createGraphFromFile("src/main/files/graph03.graph");
        graph04 = GraphUtilities.createGraphFromFile("src/main/files/graph04.graph");
        graph05 = GraphUtilities.createGraphFromFile("src/main/files/graph05.graph");
        graph07 = GraphUtilities.createGraphFromFile("src/main/files/graph07.graph");
        graph08 = GraphUtilities.createGraphFromFile("src/main/files/graph08.graph");
        graph10 = GraphUtilities.createGraphFromFile("src/main/files/graph10.graph");
        graph11 = GraphUtilities.createGraphFromFile("src/main/files/graph11.graph");
        graphUltimo1 = GraphUtilities.createGraphFromFile("src/main/files/graphUltimo1.graph");
//        big25E1V = GraphUtilities.createGraphFromFile("src/main/files/big25E1V.graph");
//        big12E05V = GraphUtilities.createGraphFromFile("src/main/files/big12E05V.graph");
//        big6E025V = GraphUtilities.createGraphFromFile("src/main/files/big5E025V");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void minimumSpanningTreeKruskal() {
        MinimumSpanningTrees.minimumSpanningTreeKruskal(graph04, false);
        assertEquals(MinimumSpanningTrees.kruskalGraphstream(graph04), graph04.getAttribute("minimumSpanningWeight"));
        MinimumSpanningTrees.minimumSpanningTreeKruskal(graph05, false);
        assertEquals(MinimumSpanningTrees.kruskalGraphstream(graph05), graph05.getAttribute("minimumSpanningWeight"));
        MinimumSpanningTrees.minimumSpanningTreeKruskal(graph07, false);
        assertEquals(MinimumSpanningTrees.kruskalGraphstream(graph07), graph07.getAttribute("minimumSpanningWeight"));
        MinimumSpanningTrees.minimumSpanningTreeKruskal(graph08, false);
        assertEquals(MinimumSpanningTrees.kruskalGraphstream(graph08), graph08.getAttribute("minimumSpanningWeight"));
        MinimumSpanningTrees.minimumSpanningTreeKruskal(graph10, false);
        assertEquals(MinimumSpanningTrees.kruskalGraphstream(graph10), graph10.getAttribute("minimumSpanningWeight"));
    }

    @Test
    void primVsKruskal() {
        MinimumSpanningTrees.minimumSpanningTreePrim(graph04, false);
        MinimumSpanningTrees.minimumSpanningTreePrim(graph05, false);
        MinimumSpanningTrees.minimumSpanningTreePrim(graph07, false);
        MinimumSpanningTrees.minimumSpanningTreePrim(graph08, false);
        MinimumSpanningTrees.minimumSpanningTreePrim(graph10, false);
//        MinimumSpanningTrees.minimumSpanningTreePrim(big25E1V, false);
//        MinimumSpanningTrees.minimumSpanningTreePrim(big12E05V, false);
//        MinimumSpanningTrees.minimumSpanningTreePrim(big6E025V, false);
        Double prim04Weight = graph04.getAttribute("minimumSpanningWeight");
        Double prim05Weight = graph05.getAttribute("minimumSpanningWeight");
        Double prim07Weight = graph07.getAttribute("minimumSpanningWeight");
        Double prim08Weight = graph08.getAttribute("minimumSpanningWeight");
        Double prim10Weight = graph10.getAttribute("minimumSpanningWeight");
//        Double prim25E1VWeight = big25E1V.getAttribute("minimumSpanningWeight");
        MinimumSpanningTrees.minimumSpanningTreeKruskal(graph04, false);
        MinimumSpanningTrees.minimumSpanningTreeKruskal(graph05, false);
        MinimumSpanningTrees.minimumSpanningTreeKruskal(graph07, false);
        MinimumSpanningTrees.minimumSpanningTreeKruskal(graph08, false);
        MinimumSpanningTrees.minimumSpanningTreeKruskal(graph10, false);
//        MinimumSpanningTrees.minimumSpanningTreeKruskal(big25E1V, false);
        Double kruskal04Weight = graph04.getAttribute("minimumSpanningWeight");
        Double kruskal05Weight = graph05.getAttribute("minimumSpanningWeight");
        Double kruskal07Weight = graph07.getAttribute("minimumSpanningWeight");
        Double kruskal08Weight = graph08.getAttribute("minimumSpanningWeight");
        Double kruskal10Weight = graph10.getAttribute("minimumSpanningWeight");
//        Double kruskal25E1VWeight = big25E1V.getAttribute("minimumSpanningWeight");


        assertEquals(prim04Weight, kruskal04Weight);
        assertEquals(prim05Weight, kruskal05Weight);
        assertEquals(prim07Weight, kruskal07Weight);
        assertEquals(prim08Weight, kruskal08Weight);
        assertEquals(prim10Weight, kruskal10Weight);


    }

    @Test
    void minimumSpanningTreePrim() {
        MinimumSpanningTrees.minimumSpanningTreePrim(graph04, false);
        assertEquals(MinimumSpanningTrees.primGraphstream(graph04), graph04.getAttribute("minimumSpanningWeight"));
        MinimumSpanningTrees.minimumSpanningTreePrim(graph05, false);
        assertEquals(MinimumSpanningTrees.primGraphstream(graph05), graph05.getAttribute("minimumSpanningWeight"));
        MinimumSpanningTrees.minimumSpanningTreePrim(graph07, false);
        assertEquals(MinimumSpanningTrees.primGraphstream(graph07), graph07.getAttribute("minimumSpanningWeight"));
        MinimumSpanningTrees.minimumSpanningTreePrim(graph08, false);
        assertEquals(MinimumSpanningTrees.primGraphstream(graph08), graph08.getAttribute("minimumSpanningWeight"));
        MinimumSpanningTrees.minimumSpanningTreePrim(graph10, false);
        assertEquals(MinimumSpanningTrees.primGraphstream(graph10), graph10.getAttribute("minimumSpanningWeight"));
    }


    @Test
    void minimumSpanningTreePrimDecreaseKey() {
        MinimumSpanningTrees.minimumSpanningTreePrimDecreaseKey(graph04, false);
        assertEquals(MinimumSpanningTrees.primGraphstream(graph04), graph04.getAttribute("minimumSpanningWeight"));
        MinimumSpanningTrees.minimumSpanningTreePrimDecreaseKey(graph05, false);
        assertEquals(MinimumSpanningTrees.primGraphstream(graph05), graph05.getAttribute("minimumSpanningWeight"));
        MinimumSpanningTrees.minimumSpanningTreePrimDecreaseKey(graph07, false);
        assertEquals(MinimumSpanningTrees.primGraphstream(graph07), graph07.getAttribute("minimumSpanningWeight"));
        MinimumSpanningTrees.minimumSpanningTreePrimDecreaseKey(graph08, false);
        assertEquals(MinimumSpanningTrees.primGraphstream(graph08), graph08.getAttribute("minimumSpanningWeight"));
        MinimumSpanningTrees.minimumSpanningTreePrimDecreaseKey(graph10, false);
        assertEquals(MinimumSpanningTrees.primGraphstream(graph10), graph10.getAttribute("minimumSpanningWeight"));
    }
}