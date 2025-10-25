package mst;

import com.google.gson.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class CSVExporter {

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        JsonObject data = JsonParser.parseReader(new FileReader("src/main/resources/output.json")).getAsJsonObject();

        JsonArray results = data.getAsJsonArray("results");

        StringBuilder csv = new StringBuilder();
        csv.append("Graph_ID,Vertices,Edges,Kruskal_Cost,Prim_Cost,All_Costs_Match,")
                .append("Kruskal_Edges,Prim_Edges,Edges_OK,")
                .append("Kruskal_Time_ms,Prim_Time_ms,")
                .append("Kruskal_Ops,Prim_Ops\n");

        for (JsonElement elem : results) {
            JsonObject obj = elem.getAsJsonObject();

            int graphId = obj.get("graph_id").getAsInt();

            JsonObject prim = obj.getAsJsonObject("prim");
            JsonObject kruskal = obj.getAsJsonObject("kruskal");

            int vertices = obj.getAsJsonObject("input_stats").get("vertices").getAsInt();
            int edges = obj.getAsJsonObject("input_stats").get("edges").getAsInt();

            double primCost = prim.get("total_cost").getAsDouble();
            double kruskalCost = kruskal.get("total_cost").getAsDouble();
            boolean costsMatch = Math.abs(primCost - kruskalCost) < 1e-6;

            int primEdges = prim.getAsJsonArray("mst_edges").size();
            int kruskalEdges = kruskal.getAsJsonArray("mst_edges").size();
            boolean edgesOk = (primEdges == kruskalEdges && primEdges == vertices - 1);

            double primTime = prim.get("execution_time_ms").getAsDouble();
            double kruskalTime = kruskal.get("execution_time_ms").getAsDouble();

            int primOps = prim.get("operations_count").getAsInt();
            int kruskalOps = kruskal.get("operations_count").getAsInt();

            csv.append(String.format(Locale.US,
                    "%d,%d,%d,%.2f,%.2f,%s,%d,%d,%s,%.3f,%.3f,%d,%d\n",
                    graphId, vertices, edges,
                    kruskalCost, primCost,
                    costsMatch ? "YES" : "NO",
                    kruskalEdges, primEdges,
                    edgesOk ? "YES" : "NO",
                    kruskalTime, primTime,
                    kruskalOps, primOps
            ));
        }

        try (FileWriter writer = new FileWriter("src/main/resources/output.csv")) {
            writer.write(csv.toString());
        }

        System.out.println("CSV file created successfully: src/main/resources/output.csv");
    }
}


