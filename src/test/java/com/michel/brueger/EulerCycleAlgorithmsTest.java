package com.michel.brueger;

import org.graphstream.graph.Graph;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EulerCycleAlgorithmsTest {
    Graph eulercycle1, eulercycle2, eulerpath, genEuler31, genEuler41, genEuler51;

    @BeforeEach
    void setUp() throws IOException {
        eulercycle1 = GraphUtilities.createGraphFromFile("src/main/files/eulercycle1.graph");
        eulercycle2 = GraphUtilities.createGraphFromFile("src/main/files/eulercycle2.graph");
        eulerpath = GraphUtilities.createGraphFromFile("src/main/files/eulerpath.graph");
        genEuler31 = GraphUtilities.createGraphFromFile("src/main/files/genEuler31.graph");
        genEuler41 = GraphUtilities.createGraphFromFile("src/main/files/genEuler41.graph");
        genEuler51 = GraphUtilities.createGraphFromFile("src/main/files/genEuler51.graph");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void generateEulerCycleFleury() {
        assertEquals(3, EulerCycleAlgorithms.generateEulerCycleFleury(genEuler31).size());
        assertEquals(6, EulerCycleAlgorithms.generateEulerCycleFleury(genEuler41).size());
        assertEquals(6, EulerCycleAlgorithms.generateEulerCycleFleury(genEuler51).size());
        assertEquals(12, EulerCycleAlgorithms.generateEulerCycleFleury(eulercycle1).size());
        assertEquals(11, EulerCycleAlgorithms.generateEulerCycleFleury(eulercycle2).size());
        assertEquals(7, EulerCycleAlgorithms.generateEulerCycleFleury(eulerpath).size());
    }

    @Test
    void generateEulerCycleHierholzer() {
        assertEquals(3, EulerCycleAlgorithms.generateEulerCycleHierholzer(genEuler31).size());
        assertEquals(6, EulerCycleAlgorithms.generateEulerCycleHierholzer(genEuler41).size());
        assertEquals(6, EulerCycleAlgorithms.generateEulerCycleHierholzer(genEuler51).size());
        assertEquals(12, EulerCycleAlgorithms.generateEulerCycleHierholzer(eulercycle1).size());
        assertEquals(11, EulerCycleAlgorithms.generateEulerCycleHierholzer(eulercycle2).size());
        assertNotEquals(7, EulerCycleAlgorithms.generateEulerCycleHierholzer(eulerpath).size());
    }

    @Test
    void isEulerCycle() {
        assertTrue(EulerCycleAlgorithms.isEulerCycle(genEuler31, EulerCycleAlgorithms.generateEulerCycleFleury(genEuler31)));
        assertTrue(EulerCycleAlgorithms.isEulerCycle(genEuler41, EulerCycleAlgorithms.generateEulerCycleFleury(genEuler41)));
        assertTrue(EulerCycleAlgorithms.isEulerCycle(genEuler51, EulerCycleAlgorithms.generateEulerCycleFleury(genEuler51)));
        assertTrue(EulerCycleAlgorithms.isEulerCycle(eulercycle1, EulerCycleAlgorithms.generateEulerCycleFleury(eulercycle1)));
        assertTrue(EulerCycleAlgorithms.isEulerCycle(eulercycle2, EulerCycleAlgorithms.generateEulerCycleFleury(eulercycle2)));
        assertFalse(EulerCycleAlgorithms.isEulerCycle(eulerpath, EulerCycleAlgorithms.generateEulerCycleFleury(eulerpath)));

        assertTrue(EulerCycleAlgorithms.isEulerCycle(genEuler31, EulerCycleAlgorithms.generateEulerCycleHierholzer(genEuler31)));
        assertTrue(EulerCycleAlgorithms.isEulerCycle(genEuler41, EulerCycleAlgorithms.generateEulerCycleHierholzer(genEuler41)));
        assertTrue(EulerCycleAlgorithms.isEulerCycle(genEuler51, EulerCycleAlgorithms.generateEulerCycleHierholzer(genEuler51)));
        assertTrue(EulerCycleAlgorithms.isEulerCycle(eulercycle1, EulerCycleAlgorithms.generateEulerCycleHierholzer(eulercycle1)));
        assertTrue(EulerCycleAlgorithms.isEulerCycle(eulercycle2, EulerCycleAlgorithms.generateEulerCycleHierholzer(eulercycle2)));
        assertFalse(EulerCycleAlgorithms.isEulerCycle(eulerpath, EulerCycleAlgorithms.generateEulerCycleHierholzer(eulerpath)));


        for(int i=1; i<=20; i++){
            Graph testGraph = Eulergenerator.generateEulergraph(i*100);
            assertTrue(EulerCycleAlgorithms.isEulerCycle(testGraph, EulerCycleAlgorithms.generateEulerCycleFleury(testGraph)));
            assertTrue(EulerCycleAlgorithms.isEulerCycle(testGraph, EulerCycleAlgorithms.generateEulerCycleHierholzer(testGraph)));
        }

//        System.out.println(EulerCycleAlgorithms.generateEulerCycleFleury(genEuler51));
    }
}