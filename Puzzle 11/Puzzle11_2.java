import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Puzzle11_2 {

    public static void main(String[] args) {
        try {
            String content = Files.readString(Paths.get("Puzzle 11/input11.txt"));
            String[] lines = content.trim().split("\\r?\\n");

            Map<String, List<String>> graph = new HashMap<>();
            Set<String> allNodes = new HashSet<>();
            
            for (String line : lines) {
                String[] parts = line.split(": ");
                String name = parts[0];
                List<String> deps = Arrays.asList(parts[1].split(" "));
                graph.put(name, deps);
                allNodes.add(name);
                allNodes.addAll(deps);
            }

            List<String> topoOrder = topologicalSort(graph, allNodes);
            
            Map<String, Long> toOut = new HashMap<>();
            Map<String, Long> viaDac = new HashMap<>();
            Map<String, Long> viaFft = new HashMap<>();
            Map<String, Long> viaBoth = new HashMap<>();
            
            for (String node : topoOrder) {
                if (node.equals("out")) {
                    toOut.put(node, 1L);
                    viaDac.put(node, 0L);
                    viaFft.put(node, 0L);
                    viaBoth.put(node, 0L);
                } else {
                    long sumToOut = 0, sumViaDac = 0, sumViaFft = 0, sumViaBoth = 0;
                    
                    if (graph.containsKey(node)) {
                        for (String child : graph.get(node)) {
                            sumToOut += toOut.getOrDefault(child, 0L);
                            sumViaDac += viaDac.getOrDefault(child, 0L);
                            sumViaFft += viaFft.getOrDefault(child, 0L);
                            sumViaBoth += viaBoth.getOrDefault(child, 0L);
                        }
                    }
                    
                    toOut.put(node, sumToOut);
                    
                    if (node.equals("dac")) {
                        viaDac.put(node, sumToOut);
                        viaFft.put(node, sumViaFft);
                        viaBoth.put(node, sumViaFft);
                    } else if (node.equals("fft")) {
                        viaDac.put(node, sumViaDac);
                        viaFft.put(node, sumToOut);
                        viaBoth.put(node, sumViaDac);
                    } else {
                        viaDac.put(node, sumViaDac);
                        viaFft.put(node, sumViaFft);
                        viaBoth.put(node, sumViaBoth);
                    }
                }
            }
            
            System.out.println("Paths through both dac and fft: " + viaBoth.get("svr"));
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    static List<String> topologicalSort(Map<String, List<String>> graph, Set<String> allNodes) {
        List<String> order = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        
        for (String node : allNodes) {
            if (!visited.contains(node)) {
                dfs(node, graph, visited, order);
            }
        }
        
        return order;
    }
    
    static void dfs(String node, Map<String, List<String>> graph, Set<String> visited, List<String> order) {
        visited.add(node);
        
        if (graph.containsKey(node)) {
            for (String child : graph.get(node)) {
                if (!visited.contains(child)) {
                    dfs(child, graph, visited, order);
                }
            }
        }
        
        order.add(node);
    }
}