package mst;

import java.util.*;

public class Graph {
    public List<String> nodes;
    public List<Edge> edges;

    public Graph(List<String> nodes, List<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public int getVertexCount() {
        return nodes.size();
    }

    public int getEdgeCount() {
        return edges.size();
    }
}
