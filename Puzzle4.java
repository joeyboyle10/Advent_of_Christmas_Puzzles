import java.io.*;
import java.nio.file.*;

public class Puzzle4 {
    public static void main(String[] args) {
        try {
            String content = Files.readString(Paths.get("input4.txt"));
            String[] lines = content.trim().split("\n");

            int [][] grid = new int[141][141];
            for(int i = 0; i < grid.length; i++) {
                for(int j = 0; j < grid[i].length; j++) {
                    if (i == 0 || j == 0 || i == grid.length - 1 || j == grid[i].length - 1) {
                        grid[i][j] = 0;
                    }
                }
            }

            int row = 1;
            for (String line : lines) {
                int col = 1;
                while(row < grid.length - 1 && col < line.length() + 1) {
                    if(line.charAt(col - 1) == '@') {
                        grid[row][col] = 1;
                    }
                    col++;
                }
                row++;
            }
            
            boolean isAccessible = true;
            int totalCount = 0;
            while(isAccessible) {
                isAccessible = false;
                int unitCount = 0;
                for(int i = 1; i < grid.length - 1; i++) {
                    for(int j = 1; j < grid[i].length - 1; j++) {
                        if(grid[i-1][j-1] == 1) unitCount++;
                        if(grid[i-1][j] == 1) unitCount++;
                        if(grid[i-1][j+1] == 1) unitCount++;
                        if(grid[i][j-1] == 1) unitCount++;
                        if(grid[i][j+1] == 1) unitCount++;
                        if(grid[i+1][j-1] == 1) unitCount++;
                        if(grid[i+1][j] == 1) unitCount++;
                        if(grid[i+1][j+1] == 1) unitCount++;
                        if(unitCount < 4 && grid[i][j] == 1) {
                            totalCount++;
                            isAccessible = true;
                            System.out.println("Grid: [" + i + "][" + j + "] = " + grid[i][j]);
                            grid[i][j] = 0;
                        }
                        unitCount = 0;
                    }
                }
            }
        
            System.out.println("Total Count: " + totalCount);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}