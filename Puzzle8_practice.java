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

public class Puzzle8_practice {
    final static int NUM_CONNECTIONS = 10;
    final static int CIRCUIT_SIZE_FOR_BIGGEST = 3;
    public static void main(String[] args) {
        try {
            String content = Files.readString(Paths.get("input8.test.txt"));
            String[] lines = content.trim().split("\\r?\\n");
            int grid[][] = new int[lines.length][3];

            for (int row = 0; row < lines.length; row++) {
                String[] coords = lines[row].split(",");
                grid[row][0] = Integer.parseInt(coords[0]);
                grid[row][1] = Integer.parseInt(coords[1]);
                grid[row][2] = Integer.parseInt(coords[2]);
            }
            System.out.println("Rows: " + grid.length + " Columns: " + grid[0].length);

            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    System.out.print(grid[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println("--------------------------------");

            double distance = Math.sqrt(Math.pow(grid[0][0] - grid[1][0], 2)
            + Math.pow(grid[0][1] - grid[1][1], 2)
            + Math.pow(grid[0][2] - grid[1][2], 2)) + 1;
            
            List<Circuit> solution = new ArrayList<>();
            
            for(int k = 0; k < NUM_CONNECTIONS; k++) {
                System.out.println("Finding connection " + k + " of " + NUM_CONNECTIONS);
                double leastDistance = Double.MAX_VALUE;
                Circuit tempCircuit = new Circuit();
                tempCircuit.connections = new ArrayList<>();

                Connection tempConnection = new Connection();
                for (int i = 0; i < grid.length - 1; i++) {
                    for (int j = i + 1; j < grid.length; j++) {

                        Point start = new Point();
                        Point end = new Point();
                        start.x = grid[i][0];
                        start.y = grid[i][1];
                        start.z = grid[i][2];
                        end.x = grid[j][0];
                        end.y = grid[j][1];
                        end.z = grid[j][2];
                        distance = Math.sqrt(Math.pow(start.x - end.x, 2)
                        + Math.pow(start.y - end.y, 2)
                        + Math.pow(start.z - end.z, 2));

                        boolean alreadyExists = false;
                        for(Connection c : tempCircuit.connections) {
                            if(c.start.x == start.x && c.start.y == start.y && c.start.z == start.z && c.end.x == end.x && c.end.y == end.y && c.end.z == end.z) {
                                alreadyExists = true;
                                break;
                            }
                        }
                        for(Circuit cir : solution) {
                            for(Connection cc : cir.connections) {
                                if(cc.start.x == start.x && cc.start.y == start.y && cc.start.z == start.z && cc.end.x == end.x && cc.end.y == end.y && cc.end.z == end.z) {
                                    alreadyExists = true;
                                    break;
                                }
                            }
                        }
                        if(alreadyExists) {
                            continue;
                        }

                        if(distance < leastDistance) {
                            tempConnection.start = start;
                            tempConnection.end = end;
                            tempConnection.distance = distance;

                            tempCircuit.connections.clear();
                            tempCircuit.connections.add(tempConnection);
                            leastDistance = distance;
                        }
                    }
                }

                if(solution.isEmpty()) {
                    System.out.println("Adding first circuit");
                    Circuit firstCircuit = new Circuit();
                    firstCircuit.connections = new ArrayList<>();
                    firstCircuit.connections.add(tempConnection);
                    solution.add(firstCircuit);
                    continue;
                } else {
                    List<Circuit> matchingCircuits = new ArrayList<>();
                    for(Circuit cir : solution) {
                        boolean matches = false;
                        for(Connection cc : cir.connections) {
                            if(cc.start.x == tempConnection.start.x && cc.start.y == tempConnection.start.y && cc.start.z == tempConnection.start.z
                                || cc.end.x == tempConnection.end.x && cc.end.y == tempConnection.end.y && cc.end.z == tempConnection.end.z
                                || cc.start.x == tempConnection.end.x && cc.start.y == tempConnection.end.y && cc.start.z == tempConnection.end.z
                                || cc.end.x == tempConnection.start.x && cc.end.y == tempConnection.start.y && cc.end.z == tempConnection.start.z) {
                                matches = true;
                                break;
                            }
                        }
                        if(matches) {
                            matchingCircuits.add(cir);
                        }
                    }

                    if(matchingCircuits.isEmpty()) {
                        Circuit newCircuit = new Circuit();
                        newCircuit.connections = new ArrayList<>();
                        newCircuit.connections.add(tempConnection);
                        solution.add(newCircuit);
                    } else if(matchingCircuits.size() == 1) {
                        matchingCircuits.get(0).connections.add(tempConnection);
                    } else {
                        Circuit mergedCircuit = matchingCircuits.get(0);
                        mergedCircuit.connections.add(tempConnection);
                        
                        for(int m = 1; m < matchingCircuits.size(); m++) {
                            Circuit toMerge = matchingCircuits.get(m);
                            mergedCircuit.connections.addAll(toMerge.connections);
                            solution.remove(toMerge);
                        }
                    }
                }
            }
            System.out.println("--------------------------------");

            for(int i = 0; i < grid.length; i++) {
                boolean found = false;
                for(Circuit cir : solution) {
                    for(Connection cc : cir.connections) {
                        if((cc.start.x == grid[i][0] && cc.start.y == grid[i][1] && cc.start.z == grid[i][2])
                            || (cc.end.x == grid[i][0] && cc.end.y == grid[i][1] && cc.end.z == grid[i][2])) {
                            found = true;
                            break;
                        }
                    }
                    if(found) break;
                }
                if(!found) {
                    Circuit singlePoint = new Circuit();
                    singlePoint.connections = new ArrayList<>();
                    solution.add(singlePoint);
                }
            }

            /*O(N)
            System.out.println("Solution: " + solution.size());
            for(Circuit c : solution) {
                List<String> uniquePoints = new ArrayList<>();
                for(Connection cc : c.connections) {
                    String startKey = cc.start.x + "," + cc.start.y + "," + cc.start.z;
                    String endKey = cc.end.x + "," + cc.end.y + "," + cc.end.z;
                    if(!uniquePoints.contains(startKey)) uniquePoints.add(startKey);
                    if(!uniquePoints.contains(endKey)) uniquePoints.add(endKey);
                }
                int junctionBoxes = uniquePoints.isEmpty() ? 1 : uniquePoints.size();
                System.out.println("Circuit with " + junctionBoxes + " junction boxes:");
                System.out.print(c.toString());
            }*/

            //O(1)
            System.out.println("Solution: " + solution.size() + " circuits");
            for (Circuit c : solution) {
                Set<String> uniquePoints = new HashSet<>();

                for (Connection cc : c.connections) {
                    uniquePoints.add(cc.start.x + "," + cc.start.y + "," + cc.start.z);
                    uniquePoints.add(cc.end.x + "," + cc.end.y + "," + cc.end.z);
                }
                c.junctionBoxes = uniquePoints.isEmpty() ? 1 : uniquePoints.size();

                System.out.println("Circuit with " + c.junctionBoxes + " junction boxes");
                //System.out.println(c.toString());
            }
            
            System.out.println("Sorting " + solution.size() + " circuits...");
            solution.sort((a, b) -> Integer.compare(a.junctionBoxes, b.junctionBoxes));
            System.out.print("Biggest 3 circuits: ");
            for(int i = 0; i < CIRCUIT_SIZE_FOR_BIGGEST; i++) {
                System.out.print(solution.get(solution.size() - i - 1).junctionBoxes + " ");
            }
            System.out.println();
            System.out.print("Answer: ");
            int answer = 1;
            for(int i = 0; i < CIRCUIT_SIZE_FOR_BIGGEST; i++) {
                if(i < CIRCUIT_SIZE_FOR_BIGGEST - 1) {
                    System.out.print(solution.get(solution.size() - i - 1).junctionBoxes + " * ");
                } else {
                    System.out.print(solution.get(solution.size() - i - 1).junctionBoxes + " = ");
                }
                answer *= solution.get(solution.size() - i - 1).junctionBoxes;
            }
            System.out.println(answer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}