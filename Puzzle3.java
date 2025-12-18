import java.io.*;
import java.nio.file.*;

public class Puzzle3 {
    public static void main(String[] args) {
        try {
            final int digits = 12;

            String content = Files.readString(Paths.get("input3.txt"));
            String[] lines = content.trim().split("\n");

            long total = 0L;
            for (String line : lines) {
                System.out.println(line);
                
                String [] P = new String[digits];
                int start = 0;
                int end = line.length() - digits + 1;
                for (int i = 0; i < digits; i++) {
                    int j = start;
                    P[i] = Integer.toString(j);
                    while(j < end) {
                        if(line.charAt(Integer.parseInt(P[i])) < line.charAt(j)) {
                            P[i] = Integer.toString(j);
                        }
                        j++;
                    }
                    start = Integer.parseInt(P[i]) + 1;
                    end++;
                }
                
                String LineTotal = "";
                for (int i = 0; i < digits; i++) {
                    LineTotal += line.charAt(Integer.parseInt(P[i]));
                }

                total += Long.parseLong(LineTotal);
                System.out.println("LineTotal: " + LineTotal);
                System.out.println("Total: " + total);
            }

            System.out.println("--------------------------------");
            System.out.println("Total: " + total);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
