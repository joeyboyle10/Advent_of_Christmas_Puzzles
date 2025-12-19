import java.io.*;
import java.nio.file.*;

public class Puzzle7_1 {
    public static void main(String[] args) {
        try {
            String content = Files.readString(Paths.get("input7.test.txt"));
            String[] lines = content.trim().split("\\r?\\n");
            
            int grid[][] = new int[lines.length][lines[0].length()];
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    grid[i][j] = (lines[i].charAt(j) == '^' ) ? 2 : 0;
                    if (lines[i].charAt(j) == 'S') {
                        grid[i][j] = 1;
                    }
                }
            }
            System.out.println("Grid: ");

            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    System.out.print(grid[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------------");

            int cnt = 0;
            for (int row = 1; row < grid.length; row++) {
                System.out.println("Row Before: ");
                for (int j = 0; j < grid[row].length; j++) {
                    System.out.print(grid[row][j]);
                }
                System.out.println();

                for (int col = 1; col < grid[row].length; col++) {
                    if (grid[row-1][col] == 1) {
                        if (grid[row][col] == 2) {
                            grid[row][col-1] = 1;
                            grid[row][col+1] = 1;
                            cnt++;
                        } else {
                            grid[row][col] = 1;
                        }
                        
                    } else {
                        continue;
                    }
                    
                }

                System.out.println("Row After: ");
                for (int j = 0; j < grid[row].length; j++) {
                    System.out.print(grid[row][j]);
                }
                System.out.println();
                System.out.println("--------------------------------");
            }
            System.out.println("Grid: ");
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    System.out.print(grid[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------------");
            System.out.println("Count: " + cnt);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}