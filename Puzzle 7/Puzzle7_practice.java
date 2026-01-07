import java.io.*;
import java.nio.file.*;

public class Puzzle7_practice {

    public static int path(int grid[][], int row, int col, int cnt) {
        while(row < grid.length) {
            System.out.println("Checking Row: " + row + " Column: " + col);
            if(row + 1 >= grid.length) {
                cnt++;
                System.out.println("Reached bottom at: [" + row + "][" + col + "]");
                return cnt;
            }
            if(grid[row+1][col] == 2) {
                cnt = path(grid, row+1, col-1, cnt) + path(grid, row+1, col+1, cnt);
                System.out.println("Count after recursive calls: " + cnt);
                return cnt;
            }
            row++;
        }
        return cnt;
    }
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

            int cnt = 0;
            System.out.println();
            System.out.println("Looking for S in the first row");
            for (int i = 0; i < grid[0].length; i++) {
                if(grid[0][i] == 1) {
                    System.out.println("Found S at: [" + 0 + "][" + i + "]");
                    System.out.println("Starting path calculation");
                    System.out.println("--------------------------------");
                    cnt = path(grid, 0, i, cnt);
                }
            }

            System.out.println("--------------------------------");
            System.out.println("Count: " + cnt);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}