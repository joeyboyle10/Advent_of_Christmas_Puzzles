import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Puzzle11_1 {

    static class Device {
        String name;
        List<String> dependents = new ArrayList<>();

        Device() {}

        Device(String name) {
            this.name = name;
        }

        Device(String name, List<String> dependents) {
            this.name = name;
            this.dependents = dependents;
        }

        public String toString() {
            return "Device: " + name + " Dependents: " + dependents.toString();
        }
    }
    public static void main(String[] args) {
        try {
            String content = Files.readString(Paths.get("Puzzle 11/input11.txt"));
            String[] lines = content.trim().split("\\r?\\n");

            List<Device> devices = new ArrayList<>();
            for (String line : lines) {
                String[] parts = line.split(": ");
                String name = parts[0];
                List<String> dependents = Arrays.asList(parts[1].split(" "));
                Device device = new Device(name, dependents);
                devices.add(device);
                System.out.println(device.toString());
            }

            Map<String, List<String>> graph = new HashMap<>();
            for (Device device : devices) {
                graph.put(device.name, device.dependents);
            }

            int pathCount = countPaths("you", "out", graph, new HashSet<>());
            System.out.println("Path Count: " + pathCount);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public static int countPaths(String current, String target, Map<String, List<String>> graph, Set<String> path) {
        if(current.equals(target)) {
            return 1;
        }
        if(!graph.containsKey(current)) {
            return 0;
        }
        path.add(current);
        int count = 0;
        for (String next : graph.get(current)) {
            if (!path.contains(next)) {
                count += countPaths(next, target, graph, path);
            }
        }
        path.remove(current);
        return count;
    }
}