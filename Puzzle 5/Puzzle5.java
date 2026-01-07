import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Puzzle5 {
    public static void main(String[] args) {
        try {
            String content = Files.readString(Paths.get("input5.txt"));
            String[] lines = content.trim().split("\n");
            List<String> list = new ArrayList<>();

            System.out.println("Lines: " + lines.length);
            for (String line : lines) {
                System.out.println("Line: " + line);
                if(!(line.contains("-") || line.trim().isEmpty())) {
                    list.add(line.trim());
                    System.out.println("Added: " + line.trim());
                }
            }
            System.out.println("List: " + list);
            
            Long total = 0L;
            for (String line : lines) {
                if(line.contains("-")) {
                    Long start = Long.parseLong(line.trim().split("-")[0]);
                    Long end = Long.parseLong(line.trim().split("-")[1]);

                    Iterator<String> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        String item = iterator.next();
                        System.out.println("Start: " + start + " End: " + end + " Item: " + item);
                        if(Long.parseLong(item) >= start && Long.parseLong(item) <= end) {
                            System.out.println("Found: " + item +" in range " + start + " to " + end);
                            iterator.remove();
                            total++;
                        }
                    }
                }
            }
            System.out.println("--------------------------------");
            System.out.println("Total: " + total);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
