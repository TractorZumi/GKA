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
    Graph graph08;
    Graph graph10;
    Graph graph11;
    Graph graphUltimo1;



    @BeforeEach
    void setUp() throws IOException {
        graph03 = GraphUtilities.createGraphFromFile("src/main/files/graph03.graph");
        graph04 = GraphUtilities.createGraphFromFile("src/main/files/graph04.graph");
        graph08 = GraphUtilities.createGraphFromFile("src/main/files/graph08.graph");
        graph10 = GraphUtilities.createGraphFromFile("src/main/files/graph10.graph");
        graph11 = GraphUtilities.createGraphFromFile("src/main/files/graph11.graph");
        graphUltimo1 = GraphUtilities.createGraphFromFile("src/main/files/graphUltimo1.graph");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void minimumSpanningTreeKruskal() {
        MinimumSpanningTrees.minimumSpanningTreeKruskal(graph03, false);
        assertEquals((Object)MinimumSpanningTrees.kruskalGraphstream(graph03), (Object) graph03.getAttribute("minimumSpanningWeight"));
        MinimumSpanningTrees.minimumSpanningTreeKruskal(graph04, false);
        assertEquals(9, (Object) graph04.getAttribute("minimumSpanningWeight"));
        assertEquals(15, MinimumSpanningTrees.minimumSpanningTreeKruskal(graph08, false).size());
        assertEquals(11, MinimumSpanningTrees.minimumSpanningTreeKruskal(graph10, false).size());
        assertEquals(21, MinimumSpanningTrees.minimumSpanningTreeKruskal(graph11, false).size());
        assertEquals(4, MinimumSpanningTrees.minimumSpanningTreeKruskal(graphUltimo1, false).size());
    }

    @Test
    void minimumSpanningTreePrim() {
    }
}