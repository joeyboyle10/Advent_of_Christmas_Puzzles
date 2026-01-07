import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;


public class Puzzle6_2 {
    public static void main(String[] args) {
        try {
            String content = Files.readString(Paths.get("input6.txt"));
            String[] lines = content.split("\\r?\\n");
            int colSize = lines[lines.length - 1].toCharArray().length;
            char[][] grid = new char[lines.length][colSize];

            for (int i = 0; i < lines.length; i++) {
                for (int j = 0; j < colSize; j++) {
                    grid[i][j] = lines[i].charAt(j);
                }
            }

            char operator = grid[grid.length - 1][0];
            List<Long> results = new ArrayList<>();
            Long chunkTotal = (operator == '+') ? 0L : 1L;
            String line = "";

            for (int col = 0; col < grid[0].length; col++) { 
                for(int row = 0; row < grid.length - 1; row++) {
                    if(grid[row][col] == ' ') {
                        line += String.valueOf(" ");
                    } else {
                        line += String.valueOf(grid[row][col]);
                    }
                }

                if(!line.trim().isEmpty()) {
                    switch(operator) {
                        case '+':
                            chunkTotal += Long.parseLong(line.trim());
                            break;
                        case '*':
                            chunkTotal *= Long.parseLong(line.trim());
                            break;
                    }
                }
                line = "";

                if(col + 1 >= grid[0].length) {
                    results.add(chunkTotal);
                    break;
                }
                
                if(grid[grid.length - 1][col + 1] == '+' || grid[grid.length - 1][col + 1] == '*') {
                    results.add(chunkTotal);
                    operator = grid[grid.length - 1][col + 1];
                    chunkTotal = (operator == '+') ? 0L : 1L;
                }
            }
            System.out.println("Results: " + results);
            System.out.println("Total: " + results.stream().mapToLong(Long::longValue).sum());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}