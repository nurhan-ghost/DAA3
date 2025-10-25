package mst;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class MSTTest {

    static Graph graph;
    static MSTResult prim;
    static MSTResult kruskal;

    @BeforeAll
    static void setup() {
        System.out.println("Setting up test graph...");

        List<String> nodes = Arrays.asList("A", "B", "C", "D", "E");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 2),
                new Edge("A", "C", 4),
                new Edge("B", "C", 1),
                new Edge("B", "D", 8),
                new Edge("C", "E", 3),
                new Edge("D", "E", 2)
        );

        graph = new Graph(nodes, edges);

        prim = PrimAlgorithm.run(graph);
        kruskal = KruskalAlgorithm.run(graph);

        System.out.println("Test graph setup complete!\n");
    }

    @Test
    void testTotalCostEquality() {
        System.out.println("Running testTotalCostEquality...");
        assertEquals(prim.totalCost, kruskal.totalCost,
                "Prim and Kruskal MST total costs must be equal");
        System.out.println("testTotalCostEquality passed! (Cost = " + prim.totalCost + ")\n");
    }

    @Test
    void testEdgesCount() {
        System.out.println("Running testEdgesCount...");
        int expectedEdges = graph.getVertexCount() - 1;
        assertEquals(expectedEdges, prim.mstEdges.size(),
                "Prim's MST must have V - 1 edges");
        assertEquals(expectedEdges, kruskal.mstEdges.size(),
                "Kruskal's MST must have V - 1 edges");
        System.out.println("testEdgesCount passed! (Edges = " + expectedEdges + ")\n");
    }

    @Test
    void testNonNegativeMetrics() {
        System.out.println("Running testNonNegativeMetrics...");
        assertTrue(prim.executionTimeMs >= 0);
        assertTrue(kruskal.executionTimeMs >= 0);
        assertTrue(prim.operationsCount >= 0);
        assertTrue(kruskal.operationsCount >= 0);
        System.out.println("testNonNegativeMetrics passed!\n");
    }

    @Test
    void testAcyclicAndConnected() {
        System.out.println("Running testAcyclicAndConnected...");
        assertTrue(isConnected(graph, prim.mstEdges), "Prim MST should connect all vertices");
        assertTrue(isConnected(graph, kruskal.mstEdges), "Kruskal MST should connect all vertices");
        System.out.println("testAcyclicAndConnected passed!\n");
    }

    private boolean isConnected(Graph g, List<Edge> edges) {
        Set<String> visited = new HashSet<>();
        Map<String, List<String>> adj = new HashMap<>();

        for (String v : g.nodes)
            adj.put(v, new ArrayList<>());

        for (Edge e : edges) {
            adj.get(e.from).add(e.to);
            adj.get(e.to).add(e.from);
        }

        dfs(g.nodes.get(0), adj, visited);
        return visited.size() == g.nodes.size();
    }

    private void dfs(String node, Map<String, List<String>> adj, Set<String> visited) {
        visited.add(node);
        for (String nei : adj.get(node))
            if (!visited.contains(nei)) dfs(nei, adj, visited);
    }
}
