import java.io.*;
import java.nio.file.*;
import java.util.*;

class Point {
    int x, y, z;
    public String toString() { return "(" + x + "," + y + "," + z + ")"; }
}

class Connection {
    Point start;
    Point end;
    double distance;

    public String toString() {
        return "(" + start.toString() + " to " + end.toString() + ") "
        + " distance " + String.format("%.2f", distance);
    }
}

class Circuit {
    List<Connection> connections = new ArrayList<>();
    int junctionBoxes = 0;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<String> uniquePoints = new ArrayList<>();
        for(Connection cc : this.connections) {
            String startKey = cc.start.x + "," + cc.start.y + "," + cc.start.z;
            String endKey = cc.end.x + "," + cc.end.y + "," + cc.end.z;
            if(!uniquePoints.contains(startKey)) uniquePoints.add(startKey);
            if(!uniquePoints.contains(endKey)) uniquePoints.add(endKey);
        }
        for(String p : uniquePoints) {
            sb.append(p).append("\n");
        }
        return sb.toString();
    }
}

public class Puzzle8_2 {
    public static void main(String[] args) {
        try {
            String content = Files.readString(Paths.get("input8.txt"));
            String[] lines = content.trim().split("\\r?\\n");
            int[][] grid = new int[lines.length][3];

            for (int row = 0; row < lines.length; row++) {
                String[] coords = lines[row].split(",");
                grid[row][0] = Integer.parseInt(coords[0]);
                grid[row][1] = Integer.parseInt(coords[1]);
                grid[row][2] = Integer.parseInt(coords[2]);
            }
            System.out.println("Points: " + grid.length);

            System.out.println("Computing all distances...");
            List<Connection> allConnections = new ArrayList<>();
            for (int i = 0; i < grid.length - 1; i++) {
                for (int j = i + 1; j < grid.length; j++) {
                    Connection c = new Connection();
                    c.start = new Point();
                    c.start.x = grid[i][0]; c.start.y = grid[i][1]; c.start.z = grid[i][2];
                    c.end = new Point();
                    c.end.x = grid[j][0]; c.end.y = grid[j][1]; c.end.z = grid[j][2];
                    c.distance = Math.sqrt(Math.pow(c.start.x - c.end.x, 2)
                        + Math.pow(c.start.y - c.end.y, 2)
                        + Math.pow(c.start.z - c.end.z, 2));
                    allConnections.add(c);
                }
            }

            System.out.println("Sorting " + allConnections.size() + " connections...");
            allConnections.sort((a, b) -> Double.compare(a.distance, b.distance));

            List<Circuit> solution = new ArrayList<>();
            
            for (Connection tempConnection : allConnections) {
                List<Circuit> matchingCircuits = new ArrayList<>();
                for (Circuit cir : solution) {
                    for (Connection cc : cir.connections) {
                        if ((cc.start.x == tempConnection.start.x && cc.start.y == tempConnection.start.y && cc.start.z == tempConnection.start.z)
                            || (cc.end.x == tempConnection.end.x && cc.end.y == tempConnection.end.y && cc.end.z == tempConnection.end.z)
                            || (cc.start.x == tempConnection.end.x && cc.start.y == tempConnection.end.y && cc.start.z == tempConnection.end.z)
                            || (cc.end.x == tempConnection.start.x && cc.end.y == tempConnection.start.y && cc.end.z == tempConnection.start.z)) {
                            matchingCircuits.add(cir);
                            break;
                        }
                    }
                }
                
                if (matchingCircuits.isEmpty()) {
                    Circuit newCircuit = new Circuit();
                    newCircuit.connections = new ArrayList<>();
                    newCircuit.connections.add(tempConnection);
                    solution.add(newCircuit);
                } else if (matchingCircuits.size() == 1) {
                    matchingCircuits.get(0).connections.add(tempConnection);
                } else {
                    Circuit merged = matchingCircuits.get(0);
                    merged.connections.add(tempConnection);
                    for (int m = 1; m < matchingCircuits.size(); m++) {
                        merged.connections.addAll(matchingCircuits.get(m).connections);
                        solution.remove(matchingCircuits.get(m));
                    }
                }
                
                Set<String> allPoints = new HashSet<>();
                for (Circuit cir : solution) {
                    for (Connection cc : cir.connections) {
                        allPoints.add(cc.start.x + "," + cc.start.y + "," + cc.start.z);
                        allPoints.add(cc.end.x + "," + cc.end.y + "," + cc.end.z);
                    }
                }
                
                if (allPoints.size() == grid.length && solution.size() == 1) {
                    System.out.println("All merged! Last connection: " + 
                        tempConnection.start.x + "," + tempConnection.start.y + "," + tempConnection.start.z + " to " +
                        tempConnection.end.x + "," + tempConnection.end.y + "," + tempConnection.end.z);
                    System.out.println("Answer: " + tempConnection.start.x + " * " + tempConnection.end.x + " = " + 
                        ((long)tempConnection.start.x * tempConnection.end.x));
                    break;
                }
            }

            for (int i = 0; i < grid.length; i++) {
                boolean found = false;
                outer:
                for (Circuit cir : solution) {
                    for (Connection cc : cir.connections) {
                        if ((cc.start.x == grid[i][0] && cc.start.y == grid[i][1] && cc.start.z == grid[i][2])
                            || (cc.end.x == grid[i][0] && cc.end.y == grid[i][1] && cc.end.z == grid[i][2])) {
                            found = true;
                            break outer;
                        }
                    }
                }
                if (!found) {
                    Circuit singlePoint = new Circuit();
                    singlePoint.connections = new ArrayList<>();
                    solution.add(singlePoint);
                }
            }

            System.out.println("Solution: " + solution.size() + " circuits");
            for (Circuit c : solution) {
                Set<String> uniquePoints = new HashSet<>();
                for (Connection cc : c.connections) {
                    uniquePoints.add(cc.start.x + "," + cc.start.y + "," + cc.start.z);
                    uniquePoints.add(cc.end.x + "," + cc.end.y + "," + cc.end.z);
                }
                int junctionBoxes = uniquePoints.isEmpty() ? 1 : uniquePoints.size();
                System.out.println("Circuit with " + junctionBoxes + " junction boxes");
                System.out.print(c.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}