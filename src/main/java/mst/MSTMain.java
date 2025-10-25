package mst;

import com.google.gson.*;
import java.io.*;
import java.util.*;

public class MSTMain {
    public static void main(String[] args) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonObject inputData = JsonParser.parseReader(
                new FileReader("src/main/resources/input.json")
        ).getAsJsonObject();

        JsonArray graphs = inputData.getAsJsonArray("graphs");
        JsonArray resultsArray = new JsonArray();

        for (JsonElement elem : graphs) {
            JsonObject graphObj = elem.getAsJsonObject();

            List<String> nodes = new ArrayList<>();
            for (JsonElement n : graphObj.getAsJsonArray("nodes")) {
                nodes.add(n.getAsString());
            }

            List<Edge> edges = new ArrayList<>();
            for (JsonElement e : graphObj.getAsJsonArray("edges")) {
                JsonObject eo = e.getAsJsonObject();
                edges.add(new Edge(
                        eo.get("from").getAsString(),
                        eo.get("to").getAsString(),
                        eo.get("weight").getAsInt()
                ));
            }

            Graph graph = new Graph(nodes, edges);

            long startPrim = System.nanoTime();
            MSTResult prim = PrimAlgorithm.run(graph);
            long endPrim = System.nanoTime();

            prim.executionTimeMs = (endPrim - startPrim) / 1_000_000.0;

            long startKruskal = System.nanoTime();
            MSTResult kruskal = KruskalAlgorithm.run(graph);
            long endKruskal = System.nanoTime();

            kruskal.executionTimeMs = (endKruskal - startKruskal) / 1_000_000.0;

            JsonObject result = new JsonObject();
            result.addProperty("graph_id", graphObj.get("id").getAsInt());

            JsonObject stats = new JsonObject();
            stats.addProperty("vertices", graph.getVertexCount());
            stats.addProperty("edges", graph.getEdgeCount());
            result.add("input_stats", stats);

            JsonObject primObj = new JsonObject();
            primObj.add("mst_edges", gson.toJsonTree(prim.mstEdges));
            primObj.addProperty("total_cost", prim.totalCost);
            primObj.addProperty("operations_count", prim.operationsCount);
            primObj.addProperty("execution_time_ms", round(prim.executionTimeMs));
            result.add("prim", primObj);

            JsonObject kruskalObj = new JsonObject();
            kruskalObj.add("mst_edges", gson.toJsonTree(kruskal.mstEdges));
            kruskalObj.addProperty("total_cost", kruskal.totalCost);
            kruskalObj.addProperty("operations_count", kruskal.operationsCount);
            kruskalObj.addProperty("execution_time_ms", round(kruskal.executionTimeMs));
            result.add("kruskal", kruskalObj);

            resultsArray.add(result);
        }

        JsonObject output = new JsonObject();
        output.add("results", resultsArray);

        FileWriter writer = new FileWriter("src/main/resources/output.json");
        gson.toJson(output, writer);
        writer.close();

        System.out.println("Results written to output.json");
    }

    private static double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }
}
