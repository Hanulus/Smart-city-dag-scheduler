package graph.scc;

import graph.model.Graph;
import graph.model.Graph.Edge;
import java.util.*;

/**
 * Builds condensation graph (DAG) from SCCs.
 * Each SCC becomes a single vertex.
 */
public class CondensationGraph {
    private final Graph condensation;
    private final int[] componentId;
    
    public CondensationGraph(Graph original, List<List<Integer>> sccs) {
        int numComponents = sccs.size();
        this.condensation = new Graph(numComponents, "edge");
        this.componentId = new int[original.getVertexCount()];
        
        // Map vertices to component IDs
        for (int i = 0; i < sccs.size(); i++) {
            for (int v : sccs.get(i)) {
                componentId[v] = i;
            }
        }
        
        // Add inter-component edges
        Set<String> added = new HashSet<>();
        for (int u = 0; u < original.getVertexCount(); u++) {
            int compU = componentId[u];
            for (Edge edge : original.getNeighbors(u)) {
                int compV = componentId[edge.to];
                if (compU != compV) {
                    String key = compU + "->" + compV;
                    if (!added.contains(key)) {
                        condensation.addEdge(compU, compV, edge.weight);
                        added.add(key);
                    }
                }
            }
        }
    }
    
    public Graph getGraph() {
        return condensation;
    }
    
    public int getComponentId(int vertex) {
        return componentId[vertex];
    }
    
    @Override
    public String toString() {
        return "Condensation: " + condensation.getVertexCount() + " nodes, " + 
               condensation.getEdgeCount() + " edges";
    }
}
