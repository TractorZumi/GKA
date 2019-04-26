package com.michel.brueger;

import org.graphstream.graph.Graph;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;

public class GraphUtilitiesTest {

    Graph graph1, graph2, graph3, graph4, graph5, graph6, graph7, graph8,
            graph9, graph10, graph11, graph22, graphUltimo1;

    @Before
    public void setUp() throws Exception {
        graph1 = GraphUtilities.createGraphFromFile("src/main/files/graph01.graph");
        graph2 = GraphUtilities.createGraphFromFile("src/main/files/graph02.graph");
        graph3 = GraphUtilities.createGraphFromFile("src/main/files/graph03.graph");
        graph4 = GraphUtilities.createGraphFromFile("src/main/files/graph04.graph");
        graph5 = GraphUtilities.createGraphFromFile("src/main/files/graph05.graph");
        graph6 = GraphUtilities.createGraphFromFile("src/main/files/graph06.graph");
        graph7 = GraphUtilities.createGraphFromFile("src/main/files/graph07.graph");
        graph8 = GraphUtilities.createGraphFromFile("src/main/files/graph08.graph");
        graph9 = GraphUtilities.createGraphFromFile("src/main/files/graph09.graph");
        graph10 = GraphUtilities.createGraphFromFile("src/main/files/graph10.graph");
        graph11 = GraphUtilities.createGraphFromFile("src/main/files/graph11.graph");
        graphUltimo1 = GraphUtilities.createGraphFromFile("src/main/files/graphUltimo1.graph");
    }

    @Test
    public void testBreadthFirstSearch() throws InterruptedException {
        assertEquals(1, GraphAlgorithms.breadthFirstSearch(graph1, "a", "b").size());
        assertEquals(2, GraphAlgorithms.breadthFirstSearch(graph1, "j", "d").size());
        assertEquals(0, GraphAlgorithms.breadthFirstSearch(graph1, "a", "a").size());
        assertNull(GraphAlgorithms.breadthFirstSearch(graph1, "c", "i"));
        assertEquals(1, GraphAlgorithms.breadthFirstSearch(graph2, "a", "b").size());
        assertEquals(2, GraphAlgorithms.breadthFirstSearch(graph2, "j", "d").size());
        assertEquals(0, GraphAlgorithms.breadthFirstSearch(graph2, "a", "a").size());
        assertEquals(1, GraphAlgorithms.breadthFirstSearch(graph3, "Cuxhaven", "Hannover").size());
        assertEquals(2, GraphAlgorithms.breadthFirstSearch(graph3, "Hannover", "Cuxhaven").size());
        assertEquals(0, GraphAlgorithms.breadthFirstSearch(graph3, "Hamburg", "Hamburg").size());
        assertNull(GraphAlgorithms.breadthFirstSearch(graph3, "Hamburg", "Münster"));
        assertEquals(1, GraphAlgorithms.breadthFirstSearch(graph6, "1", "5").size());
        assertEquals(2, GraphAlgorithms.breadthFirstSearch(graph6, "1", "2").size());
        assertEquals(0, GraphAlgorithms.breadthFirstSearch(graph6, "5", "5").size());
        assertNull(GraphAlgorithms.breadthFirstSearch(graph6, "5", "11"));
    }

    @Test
    public void testCreateGraphFromFile() throws IOException{
        assertEquals(12, graph1.getNodeCount());
        assertEquals(39, graph1.getEdgeCount());
        assertEquals(11, graph2.getNodeCount());
        assertEquals(38, graph2.getEdgeCount());
        assertFalse(graph2.getEdge(0).isDirected());
        assertEquals(22, graph3.getNodeCount());
        assertEquals(40, graph3.getEdgeCount());
        assertTrue(graph3.getEdge(0).isDirected());
        assertEquals(10, graph4.getNodeCount());
        assertEquals(23, graph4.getEdgeCount());
        assertFalse(graph4.getEdge(0).isDirected());
        assertEquals(7, graph5.getNodeCount());
        assertEquals(20, graph5.getEdgeCount());
        assertFalse(graph5.getEdge(0).isDirected());
        assertEquals(11, graph6.getNodeCount());
        assertEquals(15, graph6.getEdgeCount());
        assertFalse(graph6.getEdge(0).isDirected()); 	//semicolon missing in .graph file
        assertEquals(10, graph7.getNodeCount());
        assertEquals(21, graph7.getEdgeCount());         //line 1 ignored because of whitespace in node2
        assertFalse(graph7.getEdge(0).isDirected());
        assertEquals(16, graph8.getNodeCount());
        assertEquals(15, graph8.getEdgeCount());
        assertFalse(graph8.getEdge(0).isDirected());
        assertEquals(12, graph9.getNodeCount());
        assertEquals(36, graph9.getEdgeCount());			//line 5 ignored (a,b b,j;)
        assertFalse(graph9.getEdge(0).isDirected());
        assertEquals(12, graph10.getNodeCount());
        assertEquals(26, graph10.getEdgeCount());
        assertFalse(graph10.getEdge(0).isDirected());
        assertEquals(22, graph11.getNodeCount());
        assertEquals(40, graph11.getEdgeCount());
        assertTrue(graph11.getEdge(0).isDirected());
        assertEquals(6, graphUltimo1.getNodeCount());
        assertEquals(5, graphUltimo1.getEdgeCount());
        assertTrue(graphUltimo1.getEdge(0).isDirected());
//        assertEquals(Integer.valueOf(1), Integer.valueOf(graphUltimo1.getNode("v1").getAttribute("value")));
//        assertEquals(Integer.valueOf(2), Integer.valueOf(graphUltimo1.getNode("v2").getAttribute("value")));
//        assertEquals(Integer.valueOf(3), Integer.valueOf(graphUltimo1.getNode("v3").getAttribute("value")));
//        assertEquals(Integer.valueOf(6), Integer.valueOf(graphUltimo1.getNode("v6").getAttribute("value")));
//        assertEquals(Integer.valueOf(7), Integer.valueOf(graphUltimo1.getNode("v7").getAttribute("value")));
//        assertEquals(Integer.valueOf(8), Integer.valueOf(graphUltimo1.getNode("v8").getAttribute("value")));
        assertEquals("Kante1-2", graphUltimo1.getEdge(0).getAttribute("name"));
        assertEquals("Kante1-3", graphUltimo1.getEdge(1).getAttribute("name"));
        assertEquals("Kante1-7", graphUltimo1.getEdge(2).getAttribute("name"));
        assertEquals("Kante2-3", graphUltimo1.getEdge(3).getAttribute("name"));
        assertEquals("Kante2-6", graphUltimo1.getEdge(4).getAttribute("name"));
        assertEquals((Object)5.0, (Object)graphUltimo1.getEdge(0).getAttribute("weight"));
        assertEquals((Object)7.0, (Object)graphUltimo1.getEdge(1).getAttribute("weight"));
        assertEquals((Object)6.0,(Object)graphUltimo1.getEdge(2).getAttribute("weight"));
        assertEquals((Object)4.0, (Object)graphUltimo1.getEdge(3).getAttribute("weight"));
        assertEquals((Object)3.0, (Object)graphUltimo1.getEdge(4).getAttribute("weight"));
    }

    @Test(expected= FileNotFoundException.class)
    public void testCreateGraphFromFileThrowsFileNotFoundException() throws IOException {
        graph22 = GraphUtilities.createGraphFromFile("src/graph22.graph");
    }

}