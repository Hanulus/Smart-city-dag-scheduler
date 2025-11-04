package graph.dagsp;

import graph.model.Graph;
import graph.model.Graph.Edge;
import graph.metrics.Metrics;
import graph.topo.TopologicalSort;
import java.util.*;

/**
 * Shortest and longest paths in DAGs.
 * Time: O(V + E)
 */
public class DAGShortestPath {
    private final Graph graph;
    private final Metrics metrics;
    private static final int INF = Integer.MAX_VALUE / 2;
    
    public DAGShortestPath(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }
    
    /**
     * Compute shortest paths from source.
     */
    public int[] shortestPaths(int source) {
        metrics.startTimer();
        
        int n = graph.getVertexCount();
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[source] = 0;
        
        // Get topological order
        TopologicalSort topo = new TopologicalSort(graph, new Metrics());
        List<Integer> order = topo.sortDFS();
        
        if (order.isEmpty()) {
            throw new IllegalStateException("Graph has cycles!");
        }
        
        // Relax edges in topological order
        for (int u : order) {
            if (dist[u] != INF) {
                for (Edge edge : graph.getNeighbors(u)) {
                    metrics.increment("relaxations");
                    if (dist[u] + edge.weight < dist[edge.to]) {
                        dist[edge.to] = dist[u] + edge.weight;
                    }
                }
            }
        }
        
        metrics.stopTimer();
        return dist;
    }
    
    /**
     * Compute longest paths (critical path).
     */
    public PathResult criticalPath() {
        metrics.startTimer();
        
        int n = graph.getVertexCount();
        int[] dist = new int[n];
        int[] parent = new int[n];
        Arrays.fill(dist, -INF);
        Arrays.fill(parent, -1);
        
        // Find sources (in-degree 0)
        int[] inDegree = new int[n];
        for (int u = 0; u < n; u++) {
            for (Edge edge : graph.getNeighbors(u)) {
                inDegree[edge.to]++;
            }
        }
        
        for (int v = 0; v < n; v++) {
            if (inDegree[v] == 0) {
                dist[v] = 0;
            }
        }
        
        // Get topological order
        TopologicalSort topo = new TopologicalSort(graph, new Metrics());
        List<Integer> order = topo.sortDFS();
        
        // Maximize distances
        for (int u : order) {
            if (dist[u] != -INF) {
                for (Edge edge : graph.getNeighbors(u)) {
                    metrics.increment("relaxations");
                    if (dist[u] + edge.weight > dist[edge.to]) {
                        dist[edge.to] = dist[u] + edge.weight;
                        parent[edge.to] = u;
                    }
                }
            }
        }
        
        // Find longest path
        int maxDist = -INF;
        int endVertex = -1;
        for (int v = 0; v < n; v++) {
            if (dist[v] > maxDist && dist[v] != -INF) {
                maxDist = dist[v];
                endVertex = v;
            }
        }
        
        // Reconstruct path
        List<Integer> path = new ArrayList<>();
        if (endVertex != -1) {
            int curr = endVertex;
            while (curr != -1) {
                path.add(curr);
                curr = parent[curr];
            }
            Collections.reverse(path);
        }
        
        metrics.stopTimer();
        return new PathResult(path, maxDist == -INF ? 0 : maxDist);
    }
    
    public static class PathResult {
        public final List<Integer> path;
        public final int pathLength;
        
        public PathResult(List<Integer> path, int pathLength) {
            this.path = path;
            this.pathLength = pathLength;
        }
        
        @Override
        public String toString() {
            return "Path: " + path + ", Length: " + pathLength;
        }
    }
}
