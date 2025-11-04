package graph.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Unit tests for Graph data structure.
 */
public class GraphTest {
    
    @Test
    public void testGraphCreation() {
        Graph graph = new Graph(5, true, "edge");
        
        assertEquals(5, graph.getVertexCount());
        assertEquals(0, graph.getEdgeCount());
        assertTrue(graph.isDirected());
        assertEquals("edge", graph.getWeightModel());
    }
    
    @Test
    public void testAddEdge() {
        Graph graph = new Graph(3, true, "edge");
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 3);
        
        assertEquals(2, graph.getEdgeCount());
        
        List<Edge> neighbors0 = graph.getNeighbors(0);
        assertEquals(1, neighbors0.size());
        assertEquals(1, neighbors0.get(0).to);
        assertEquals(5, neighbors0.get(0).weight);
    }
    
    @Test
    public void testReverseGraph() {
        Graph graph = new Graph(3, true, "edge");
        graph.addEdge(0, 1, 5);
        
        List<Edge> reverseNeighbors = graph.getReverseNeighbors(1);
        assertEquals(1, reverseNeighbors.size());
        assertEquals(0, reverseNeighbors.get(0).to);
    }
    
    @Test
    public void testUndirectedGraph() {
        Graph graph = new Graph(3, false, "edge");
        graph.addEdge(0, 1, 5);
        
        // In undirected graph, edge should exist in both directions
        assertEquals(1, graph.getNeighbors(0).size());
        assertEquals(1, graph.getNeighbors(1).size());
        
        // Edge count should be 1 (not 2) for undirected
        assertEquals(1, graph.getEdgeCount());
    }
    
    @Test
    public void testInvalidVertex() {
        Graph graph = new Graph(3, true, "edge");
        
        assertThrows(IllegalArgumentException.class, () -> {
            graph.addEdge(-1, 1, 5);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            graph.addEdge(0, 5, 5);
        });
    }
    
    @Test
    public void testGetAllVertices() {
        Graph graph = new Graph(4, true, "edge");
        List<Integer> vertices = graph.getAllVertices();
        
        assertEquals(4, vertices.size());
        assertTrue(vertices.contains(0));
        assertTrue(vertices.contains(1));
        assertTrue(vertices.contains(2));
        assertTrue(vertices.contains(3));
    }
    
    @Test
    public void testEmptyGraph() {
        Graph graph = new Graph(3, true, "edge");
        
        assertEquals(3, graph.getVertexCount());
        assertEquals(0, graph.getEdgeCount());
        
        for (int v = 0; v < 3; v++) {
            assertTrue(graph.getNeighbors(v).isEmpty());
        }
    }
    
    @Test
    public void testToString() {
        Graph graph = new Graph(3, true, "edge");
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 3);
        
        String str = graph.toString();
        assertTrue(str.contains("Graph"));
        assertTrue(str.contains("n=3"));
        assertTrue(str.contains("edges=2"));
    }
}
