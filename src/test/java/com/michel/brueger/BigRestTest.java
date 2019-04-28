package com.michel.brueger;

import org.graphstream.graph.Graph;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BigRestTest {
    Graph big12E05V;
    Graph big6E025V;

    @BeforeEach
    void setUp() throws IOException {
        big12E05V = GraphUtilities.createGraphFromFile("src/main/files/big12E05V.graph");
        big6E025V = GraphUtilities.createGraphFromFile("src/main/files/big6E025V.graph");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void minimumSpanningTreeKruskal() {
        MinimumSpanningTrees.minimumSpanningTreeKruskal(big12E05V, false);
        assertEquals(MinimumSpanningTrees.kruskalGraphstream(big12E05V), big12E05V.getAttribute("minimumSpanningWeight"));
        MinimumSpanningTrees.minimumSpanningTreeKruskal(big6E025V, false);
        assertEquals(MinimumSpanningTrees.kruskalGraphstream(big6E025V), big6E025V.getAttribute("minimumSpanningWeight"));
    }

    @Test
    void primVsKruskal() {
        MinimumSpanningTrees.minimumSpanningTreePrim(big12E05V, false);
        Double prim12E05VWeight = big12E05V.getAttribute("minimumSpanningWeight");
        MinimumSpanningTrees.minimumSpanningTreeKruskal(big12E05V, false);
        Double kruskal25E05VWeight = big12E05V.getAttribute("minimumSpanningWeight");
        assertEquals(prim12E05VWeight, kruskal25E05VWeight);
        MinimumSpanningTrees.minimumSpanningTreePrim(big6E025V, false);
        Double prim6E025VWeight = big6E025V.getAttribute("minimumSpanningWeight");
        MinimumSpanningTrees.minimumSpanningTreeKruskal(big6E025V, false);
        Double kruskal6E025VWeight = big6E025V.getAttribute("minimumSpanningWeight");
        assertEquals(prim6E025VWeight, kruskal6E025VWeight);
    }

    @Test
    void minimumSpanningTreePrim() {
        MinimumSpanningTrees.minimumSpanningTreePrim(big12E05V, false);
        assertEquals(MinimumSpanningTrees.primGraphstream(big12E05V), big12E05V.getAttribute("minimumSpanningWeight"));
        MinimumSpanningTrees.minimumSpanningTreePrim(big6E025V, false);
        assertEquals(MinimumSpanningTrees.primGraphstream(big6E025V), big6E025V.getAttribute("minimumSpanningWeight"));
    }

    @Test
    void minimumSpanningTreePrimDecreaseKey() {
        MinimumSpanningTrees.minimumSpanningTreePrimDecreaseKey(big12E05V, false);
        assertEquals(MinimumSpanningTrees.primGraphstream(big12E05V), big12E05V.getAttribute("minimumSpanningWeight"));
        MinimumSpanningTrees.minimumSpanningTreePrimDecreaseKey(big6E025V, false);
        assertEquals(MinimumSpanningTrees.primGraphstream(big6E025V), big6E025V.getAttribute("minimumSpanningWeight"));
    }
}