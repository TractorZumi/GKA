package com.michel.brueger;

import org.graphstream.graph.Graph;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Big25E1VTest {
    Graph big25E1V;
    @BeforeEach
    void setUp() throws IOException {
        big25E1V = GraphUtilities.createGraphFromFile("src/main/files/big25E1V.graph");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void minimumSpanningTreeKruskal() {
        MinimumSpanningTrees.minimumSpanningTreeKruskal(big25E1V, false);
        assertEquals(MinimumSpanningTrees.kruskalGraphstream(big25E1V), big25E1V.getAttribute("minimumSpanningWeight"));
    }

    @Test
    void primVsKruskal() {
        MinimumSpanningTrees.minimumSpanningTreePrim(big25E1V, false);
        Double prim25E1VWeight = big25E1V.getAttribute("minimumSpanningWeight");
        MinimumSpanningTrees.minimumSpanningTreeKruskal(big25E1V, false);
        Double kruskal25E1VWeight = big25E1V.getAttribute("minimumSpanningWeight");
        assertEquals(prim25E1VWeight, kruskal25E1VWeight);
    }

    @Test
    void minimumSpanningTreePrim() {
        MinimumSpanningTrees.minimumSpanningTreePrim(big25E1V, false);
        assertEquals(MinimumSpanningTrees.primGraphstream(big25E1V), big25E1V.getAttribute("minimumSpanningWeight"));
    }


    @Test
    void minimumSpanningTreePrimDecreaseKey() {
        MinimumSpanningTrees.minimumSpanningTreePrimDecreaseKey(big25E1V, false);
        assertEquals(MinimumSpanningTrees.primGraphstream(big25E1V), big25E1V.getAttribute("minimumSpanningWeight"));
    }
}