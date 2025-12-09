import java.util.*;
import java.io.*;
import java.nio.file.*;


public class Puzzle1 {
    public static void main(String[] args) {

        try {
            // Read the input file
            String content = Files.readString(Paths.get("input1.txt"));
            String[] lines = content.trim().split("\n");
            
            // Process the input
            int dial = 50;
            int clicks = 0;

            for (String line : lines) {
                System.out.println(line);
                int input = Integer.parseInt(line.substring(1));

                if (line.startsWith("R")) {
                    dial += input;
                    clicks += dial / 100;
                    dial = dial % 100;
                } else if (line.startsWith("L")) {
                    if (dial > 0 && input >= dial) {
                        clicks += (input - dial) / 100 + 1;
                    } else if (dial == 0){
                        clicks += input / 100;
                    }
                    dial -= input;
                    dial = ((dial % 100) + 100) % 100;
                }
            }
            System.out.println("--------------------------------");
            System.out.println("Dial: " + dial);
            System.out.println("Clicks: " + clicks);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}