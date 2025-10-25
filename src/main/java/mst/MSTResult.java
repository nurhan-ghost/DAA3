package mst;

import java.util.List;

public class MSTResult {
    public String algorithm;
    public List<Edge> mstEdges;
    public int totalCost;
    public int vertices;
    public int edges;
    public int operationsCount;
    public double executionTimeMs;

    public MSTResult(String algorithm, List<Edge> mstEdges, int totalCost, int vertices, int edges,
                     int operationsCount, double executionTimeMs) {
        this.algorithm = algorithm;
        this.mstEdges = mstEdges;
        this.totalCost = totalCost;
        this.vertices = vertices;
        this.edges = edges;
        this.operationsCount = operationsCount;
        this.executionTimeMs = executionTimeMs;
    }
}

