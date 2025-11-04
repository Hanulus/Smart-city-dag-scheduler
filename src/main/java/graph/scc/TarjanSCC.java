package graph.scc;

import graph.model.Graph;
import graph.model.Graph.Edge;
import graph.metrics.Metrics;
import java.util.*;

/**
 * Tarjan's algorithm for finding Strongly Connected Components.
 * Time: O(V + E), Space: O(V)
 */
public class TarjanSCC {
    private final Graph graph;
    private final Metrics metrics;
    private final int[] disc;
    private final int[] low;
    private final boolean[] onStack;
    private final Stack<Integer> stack;
    private final List<List<Integer>> sccs;
    private int time;
    
    public TarjanSCC(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
        int n = graph.getVertexCount();
        this.disc = new int[n];
        this.low = new int[n];
        this.onStack = new boolean[n];
        this.stack = new Stack<>();
        this.sccs = new ArrayList<>();
        Arrays.fill(disc, -1);
    }
    
    /**
     * Find all SCCs using Tarjan's algorithm.
     */
    public List<List<Integer>> findSCCs() {
        metrics.startTimer();
        
        for (int v = 0; v < graph.getVertexCount(); v++) {
            if (disc[v] == -1) {
                dfs(v);
            }
        }
        
        metrics.stopTimer();
        return sccs;
    }
    
    private void dfs(int u) {
        disc[u] = low[u] = time++;
        stack.push(u);
        onStack[u] = true;
        metrics.increment("dfs_visits");
        metrics.increment("push_ops");
        
        for (Edge edge : graph.getNeighbors(u)) {
            int v = edge.to;
            metrics.increment("edges_explored");
            
            if (disc[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
        
        // Found SCC root
        if (low[u] == disc[u]) {
            List<Integer> scc = new ArrayList<>();
            int v;
            do {
                v = stack.pop();
                metrics.increment("pop_ops");
                onStack[v] = false;
                scc.add(v);
            } while (v != u);
            sccs.add(scc);
        }
    }
    
    public static String formatSCCs(List<List<Integer>> sccs) {
        StringBuilder sb = new StringBuilder();
        sb.append("Found ").append(sccs.size()).append(" SCC(s):\n");
        for (int i = 0; i < sccs.size(); i++) {
            sb.append("  Component ").append(i)
              .append(" (size ").append(sccs.get(i).size()).append("): ")
              .append(sccs.get(i)).append("\n");
        }
        return sb.toString();
    }
}
