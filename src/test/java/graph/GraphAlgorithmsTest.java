package graph;

import graph.dagsp.DAGShortestPath;
import graph.model.Graph;
import graph.metrics.Metrics;
import graph.scc.CondensationGraph;
import graph.scc.TarjanSCC;
import graph.topo.TopologicalSort;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for all graph algorithms.
 */
public class GraphAlgorithmsTest {
    
    // ===== SCC Tests =====
    
    @Test
    public void testSimpleDAG() {
        Graph g = new Graph(3, "edge");
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        
        TarjanSCC tarjan = new TarjanSCC(g, new Metrics());
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        assertEquals(3, sccs.size()); // Each vertex is its own SCC
    }
    
    @Test
    public void testSingleCycle() {
        Graph g = new Graph(3, "edge");
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        
        TarjanSCC tarjan = new TarjanSCC(g, new Metrics());
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        assertEquals(1, sccs.size());
        assertEquals(3, sccs.get(0).size());
    }
    
    @Test
    public void testMultipleSCCs() {
        Graph g = new Graph(4, "edge");
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 2, 1);
        
        TarjanSCC tarjan = new TarjanSCC(g, new Metrics());
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        assertEquals(2, sccs.size());
    }
    
    // ===== Condensation Tests =====
    
    @Test
    public void testCondensationDAG() {
        Graph g = new Graph(4, "edge");
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 3, 1);
        
        TarjanSCC tarjan = new TarjanSCC(g, new Metrics());
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        CondensationGraph cond = new CondensationGraph(g, sccs);
        Graph condensation = cond.getGraph();
        
        assertTrue(condensation.getVertexCount() < g.getVertexCount());
        assertTrue(condensation.getEdgeCount() > 0);
    }
    
    // ===== Topological Sort Tests =====
    
    @Test
    public void testTopoSortDFS() {
        Graph g = new Graph(4, "edge");
        g.addEdge(0, 1, 1);
        g.addEdge(0, 2, 1);
        g.addEdge(1, 3, 1);
        g.addEdge(2, 3, 1);
        
        TopologicalSort topo = new TopologicalSort(g, new Metrics());
        List<Integer> order = topo.sortDFS();
        
        assertEquals(4, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(0) < order.indexOf(2));
        assertTrue(order.indexOf(1) < order.indexOf(3));
        assertTrue(order.indexOf(2) < order.indexOf(3));
    }
    
    @Test
    public void testTopoSortKahn() {
        Graph g = new Graph(3, "edge");
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        
        TopologicalSort topo = new TopologicalSort(g, new Metrics());
        List<Integer> order = topo.sortKahn();
        
        assertEquals(3, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(1) < order.indexOf(2));
    }
    
    @Test
    public void testCycleDetection() {
        Graph g = new Graph(3, "edge");
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        
        TopologicalSort topo = new TopologicalSort(g, new Metrics());
        List<Integer> order = topo.sortKahn();
        
        assertTrue(order.isEmpty()); // Cycle detected
    }
    
    // ===== DAG Shortest Path Tests =====
    
    @Test
    public void testShortestPath() {
        Graph g = new Graph(4, "edge");
        g.addEdge(0, 1, 1);
        g.addEdge(0, 2, 4);
        g.addEdge(1, 3, 2);
        g.addEdge(2, 3, 1);
        
        DAGShortestPath sp = new DAGShortestPath(g, new Metrics());
        int[] dist = sp.shortestPaths(0);
        
        assertEquals(0, dist[0]);
        assertEquals(1, dist[1]);
        assertEquals(4, dist[2]);
        assertEquals(3, dist[3]); // via 0->1->3
    }
    
    @Test
    public void testCriticalPath() {
        Graph g = new Graph(5, "edge");
        g.addEdge(0, 1, 3);
        g.addEdge(0, 2, 2);
        g.addEdge(1, 3, 4);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 4, 2);
        
        DAGShortestPath cp = new DAGShortestPath(g, new Metrics());
        DAGShortestPath.PathResult result = cp.criticalPath();
        
        assertEquals(9, result.pathLength); // 0->1->3->4
        assertFalse(result.path.isEmpty());
    }
    
    @Test
    public void testSingleVertex() {
        Graph g = new Graph(1, "edge");
        
        TarjanSCC tarjan = new TarjanSCC(g, new Metrics());
        assertEquals(1, tarjan.findSCCs().size());
        
        TopologicalSort topo = new TopologicalSort(g, new Metrics());
        assertEquals(1, topo.sortDFS().size());
        
        DAGShortestPath sp = new DAGShortestPath(g, new Metrics());
        int[] dist = sp.shortestPaths(0);
        assertEquals(0, dist[0]);
    }
}
