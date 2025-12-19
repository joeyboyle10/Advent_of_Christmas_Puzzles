import java.io.*;
import java.nio.file.*;

public class Puzzle7_2 {
    public static void main(String[] args) {
        try {
            String content = Files.readString(Paths.get("input7.txt"));
            String[] lines = content.trim().split("\\r?\\n");
            
            int rows = lines.length;
            int cols = lines[0].length();
            
            long[] pathCount = new long[cols];
            
            for (int col = 0; col < cols; col++) {
                if (lines[0].charAt(col) == 'S') {
                    pathCount[col] = 1;
                }
            }
            
            for (int row = 1; row < rows; row++) {
                long[] newPathCount = new long[cols];
                
                for (int col = 0; col < cols; col++) {
                    if (pathCount[col] > 0) {

                        char nextCell = lines[row].charAt(col);
                        
                        if (nextCell == '^') {

                            if (col - 1 >= 0) {
                                newPathCount[col - 1] += pathCount[col];
                            }
                            if (col + 1 < cols) {
                                newPathCount[col + 1] += pathCount[col];
                            }
                        } else {

                            newPathCount[col] += pathCount[col];
                        }
                    }
                }
                
                pathCount = newPathCount;
            }
            
            long totalPaths = 0;
            for (long count : pathCount) {
                totalPaths += count;
            }
            
            System.out.println("Total paths: " + totalPaths);
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}