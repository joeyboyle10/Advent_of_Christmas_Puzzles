import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;

public class Puzzle6 {
    public static void main(String[] args) {
        try {
            String content = Files.readString(Paths.get("input6.txt"));
            String[] lines = content.trim().split("\n");
            List<String[]> columns = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.trim().split("\\s+");
                columns.add(parts);
            }

            Long result = 0L;
            Long total = 0L;
            System.out.println("Length: " + columns.get(lines.length - 1).length);
            for (int i = 0; i < columns.get(lines.length - 1).length; i++) {
                switch (columns.get(lines.length - 1)[i]) {
                    case "*":
                        result = Long.parseLong(columns.get(0)[i]) * Long.parseLong(columns.get(1)[i]) * Long.parseLong(columns.get(2)[i]) * Long.parseLong(columns.get(3)[i]);
                        System.out.println("Column: " + i + " Result: " + result + " = " + columns.get(0)[i] + " * "
                        + columns.get(1)[i] + " * " + columns.get(2)[i] + " * " + columns.get(3)[i]);
                        total += result;
                        break;
                    case "+":
                        result = Long.parseLong(columns.get(0)[i]) + Long.parseLong(columns.get(1)[i]) + Long.parseLong(columns.get(2)[i]) + Long.parseLong(columns.get(3)[i]);
                        System.out.println("Column: " + i + " Result: " + result + " = " + columns.get(0)[i] + " + "
                        + columns.get(1)[i] + " + " + columns.get(2)[i] + " + " + columns.get(3)[i]);
                        total += result;
                        break;
                }
            }
            System.out.println("--------------------------------");
            System.out.println("Total: " + total);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
