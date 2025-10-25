package mst;
import java.util.*;

public class PrimAlgorithm {

    public static MSTResult run(Graph graph) {
        long startTime = System.nanoTime();
        int operations = 0;

        Map<String, Boolean> visited = new HashMap<>();
        for (String node : graph.nodes) visited.put(node, false);

        List<Edge> mstEdges = new ArrayList<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>();

        String start = graph.nodes.get(0);
        visited.put(start, true);

        for (Edge e : graph.edges)
            if (e.from.equals(start) || e.to.equals(start))
                pq.add(e);

        while (!pq.isEmpty() && mstEdges.size() < graph.nodes.size() - 1) {
            Edge edge = pq.poll();
            operations++;

            String nextNode = visited.get(edge.from) ? edge.to : edge.from;

            if (visited.get(nextNode)) continue;
            visited.put(nextNode, true);
            mstEdges.add(edge);

            for (Edge e : graph.edges) {
                if (visited.get(e.from) && !visited.get(e.to)) pq.add(e);
                if (visited.get(e.to) && !visited.get(e.from)) pq.add(e);
            }
        }

        long endTime = System.nanoTime();
        double execTimeMs = (endTime - startTime) / 1_000_000.0;
        int totalCost = mstEdges.stream().mapToInt(e -> e.weight).sum();

        return new MSTResult("Prim", mstEdges, totalCost, graph.getVertexCount(), graph.getEdgeCount(), operations, execTimeMs);
    }
}

