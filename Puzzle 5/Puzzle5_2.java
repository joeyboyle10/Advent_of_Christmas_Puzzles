import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Puzzle5_2 {

    static class Range {
        long start;
        long end;

        Range(String line) {
            String[] parts = line.trim().split("-");
            start = Long.parseLong(parts[0]);
            end = Long.parseLong(parts[1]);
        }

        boolean contains(long value) {
            return value >= start && value <= end;
        }

        @Override
        public String toString() {
            return start + "-" + end;
        }
    }
    public static void main(String[] args) {
        try {   
            String content = Files.readString(Paths.get("input5.txt"));
            String[] lines = content.trim().split("\n");
            List<Range> ranges = new ArrayList<>();
            
            for (String line : lines) {
                if(line.contains("-")) {
                    long linestart = Long.parseLong(line.trim().split("-")[0]);
                    long lineend = Long.parseLong(line.trim().split("-")[1]);
                    System.out.println("Line: " + linestart + " - " + lineend);

                    if(linestart > lineend) {
                        System.out.println("Skipping range: " + linestart + "-" + lineend);
                        continue;
                    }

                    boolean merged = false;
                    for(Range range : ranges) {
                        if(linestart >= range.start && linestart <= range.end) {
                            range.end = (range.end > lineend) ? range.end : lineend;
                            System.out.println("Updating range: " + range.start + "-" + range.end);
                            merged = true;
                            break;
                        }
                        if(lineend >= range.start && lineend <= range.end) {
                            range.start = (range.start < linestart) ? range.start : linestart;
                            System.out.println("Updating range: " + range.start + "-" + range.end); 
                            merged = true;
                            break;
                        }
                        if(linestart <= range.start && lineend >= range.end) {
                            range.start = linestart;
                            range.end = lineend;
                            System.out.println("Updating range: " + range.start + "-" + range.end);
                            merged = true;
                            break;
                        }
                    }

                    if(!merged) {
                        System.out.println("Adding range: " + linestart + "-" + lineend);
                        ranges.add(new Range(linestart + "-" + lineend));
                    }
                }
            }

            boolean changed = true;
            while (changed) {
                changed = false;
                for (int i = 0; i < ranges.size(); i++) {
                    for (int j = i + 1; j < ranges.size(); j++) {
                        Range a = ranges.get(i);
                        Range b = ranges.get(j);
                        if (a.start <= b.end && a.end >= b.start) {
                            a.start = Math.min(a.start, b.start);
                            a.end = Math.max(a.end, b.end);
                            ranges.remove(j);
                            changed = true;
                            break;
                        }
                    }
                    if (changed) break;
                }
            }

            Long total = 0L;
            for(Range range : ranges) {
                total += range.end - range.start + 1;
            }
            System.out.println("Total: " + total);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}