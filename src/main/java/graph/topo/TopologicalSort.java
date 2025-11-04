package graph.topo;

import graph.model.Graph;
import graph.model.Graph.Edge;
import graph.metrics.Metrics;
import java.util.*;

/**
 * Topological sorting for DAGs.
 * Implements both DFS and Kahn's algorithm.
 */
public class TopologicalSort {
    private final Graph graph;
    private final Metrics metrics;
    
    public TopologicalSort(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }
    
    /**
     * DFS-based topological sort.
     * Time: O(V + E)
     */
    public List<Integer> sortDFS() {
        metrics.startTimer();
        
        int n = graph.getVertexCount();
        boolean[] visited = new boolean[n];
        Stack<Integer> stack = new Stack<>();
        
        for (int v = 0; v < n; v++) {
            if (!visited[v]) {
                dfs(v, visited, stack);
            }
        }
        
        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
            metrics.increment("pop_ops");
        }
        
        metrics.stopTimer();
        return result;
    }
    
    private void dfs(int u, boolean[] visited, Stack<Integer> stack) {
        visited[u] = true;
        metrics.increment("dfs_visits");
        
        for (Edge edge : graph.getNeighbors(u)) {
            metrics.increment("edges_explored");
            if (!visited[edge.to]) {
                dfs(edge.to, visited, stack);
            }
        }
        
        stack.push(u);
        metrics.increment("push_ops");
    }
    
    /**
     * Kahn's algorithm (BFS-based).
     * Time: O(V + E)
     */
    public List<Integer> sortKahn() {
        metrics.startTimer();
        
        int n = graph.getVertexCount();
        int[] inDegree = new int[n];
        
        // Calculate in-degrees
        for (int u = 0; u < n; u++) {
            for (Edge edge : graph.getNeighbors(u)) {
                inDegree[edge.to]++;
            }
        }
        
        // Initialize queue with zero in-degree vertices
        Queue<Integer> queue = new LinkedList<>();
        for (int v = 0; v < n; v++) {
            if (inDegree[v] == 0) {
                queue.offer(v);
                metrics.increment("push_ops");
            }
        }
        
        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            metrics.increment("pop_ops");
            result.add(u);
            
            for (Edge edge : graph.getNeighbors(u)) {
                metrics.increment("edges_explored");
                inDegree[edge.to]--;
                if (inDegree[edge.to] == 0) {
                    queue.offer(edge.to);
                    metrics.increment("push_ops");
                }
            }
        }
        
        metrics.stopTimer();
        
        if (result.size() != n) {
            return new ArrayList<>(); // Cycle detected
        }
        return result;
    }
}
