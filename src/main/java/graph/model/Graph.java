package graph.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Graph representation with adjacency lists.
 * Supports loading from JSON files.
 */
public class Graph {
    private final int n;
    private final List<List<Edge>> adj;
    private final List<List<Edge>> revAdj;
    private final String weightModel;
    
    public Graph(int n, String weightModel) {
        this.n = n;
        this.weightModel = weightModel;
        this.adj = new ArrayList<>();
        this.revAdj = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
            revAdj.add(new ArrayList<>());
        }
    }
    
    public void addEdge(int u, int v, int weight) {
        adj.get(u).add(new Edge(u, v, weight));
        revAdj.get(v).add(new Edge(v, u, weight));
    }
    
    public List<Edge> getNeighbors(int u) {
        return adj.get(u);
    }
    
    public List<Edge> getReverseNeighbors(int u) {
        return revAdj.get(u);
    }
    
    public int getVertexCount() {
        return n;
    }
    
    public int getEdgeCount() {
        int count = 0;
        for (List<Edge> edges : adj) {
            count += edges.size();
        }
        return count;
    }
    
    /**
     * Load graph from JSON file.
     * Expected format:
     * {
     *   "directed": true,
     *   "n": 8,
     *   "edges": [{"u": 0, "v": 1, "w": 3}, ...],
     *   "source": 0,
     *   "weight_model": "edge"
     * }
     */
    public static GraphData fromJsonFile(String filename) throws IOException {
        try (FileReader reader = new FileReader(filename)) {
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            
            int n = json.get("n").getAsInt();
            String weightModel = json.has("weight_model") ? 
                               json.get("weight_model").getAsString() : "edge";
            
            Graph graph = new Graph(n, weightModel);
            
            JsonArray edges = json.getAsJsonArray("edges");
            for (int i = 0; i < edges.size(); i++) {
                JsonObject edge = edges.get(i).getAsJsonObject();
                int u = edge.get("u").getAsInt();
                int v = edge.get("v").getAsInt();
                int w = edge.get("w").getAsInt();
                graph.addEdge(u, v, w);
            }
            
            Integer source = json.has("source") ? json.get("source").getAsInt() : null;
            
            return new GraphData(graph, source);
        }
    }
    
    public static class GraphData {
        public final Graph graph;
        public final Integer source;
        
        public GraphData(Graph graph, Integer source) {
            this.graph = graph;
            this.source = source;
        }
    }
    
    public static class Edge {
        public final int from;
        public final int to;
        public final int weight;
        
        public Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }
}
