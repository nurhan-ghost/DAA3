package mst;

import java.util.*;

public class KruskalAlgorithm {

    public static MSTResult run(Graph graph) {
        long startTime = System.nanoTime();
        int operations = 0;

        List<Edge> mstEdges = new ArrayList<>();
        Collections.sort(graph.edges);
        Map<String, String> parent = new HashMap<>();

        for (String node : graph.nodes) parent.put(node, node);

        for (Edge edge : graph.edges) {
            operations++;
            String root1 = find(parent, edge.from);
            String root2 = find(parent, edge.to);

            if (!root1.equals(root2)) {
                mstEdges.add(edge);
                parent.put(root1, root2);
            }

            if (mstEdges.size() == graph.nodes.size() - 1) break;
        }

        long endTime = System.nanoTime();
        double execTimeMs = (endTime - startTime) / 1_000_000.0;
        int totalCost = mstEdges.stream().mapToInt(e -> e.weight).sum();

        return new MSTResult("Kruskal", mstEdges, totalCost, graph.getVertexCount(), graph.getEdgeCount(), operations, execTimeMs);
    }

    private static String find(Map<String, String> parent, String node) {
        if (!parent.get(node).equals(node))
            parent.put(node, find(parent, parent.get(node)));
        return parent.get(node);
    }
}

