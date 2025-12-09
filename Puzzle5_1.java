import java.io.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Puzzle5_1 {
    public static void main(String[] args) {
        try {   
            String content = Files.readString(Paths.get("input5.txt"));
            String[] lines = content.trim().split("\n");
            String[] a = new String[lines.length];
            List<String> list = new ArrayList<>();

            for (String line : lines) {
                if(!(line.contains("-") || line.trim().isEmpty())) {
                    list.add(line.trim());
                }
            }

            BigInteger total = new BigInteger("0");
            for (String line : lines) {
                if(line.contains("-")) {
                    BigInteger start = new BigInteger(line.trim().split("-")[0]);
                    BigInteger end = new BigInteger(line.trim().split("-")[1]);
                    BigInteger diff = end.subtract(start).add(BigInteger.ONE);
                    total = total.add(diff);
                    System.out.println("Start: " + start + " End: " + end + " Diff: " + diff + " Total: " + total);
                    
                }
            }
            System.out.println("--------------------------------");
            System.out.println("Total: " + total);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
