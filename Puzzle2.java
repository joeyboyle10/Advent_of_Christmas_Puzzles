import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Puzzle2 {
    public static void main(String[] args) {
        try {
            String content = Files.readString(Paths.get("input2.txt"));
            String[] lines = content.trim().split(",");

            int count = 0;
            long total = 0L;

            for (String line : lines) {

                String[] nums = line.split("-");
                System.out.println(nums[0] + ":=:" + nums[1]);

                for(long i = Long.parseLong(nums[0]); i <= Long.parseLong(nums[1]); i++) {

                    boolean isInvalid = false;
                   
                    for(int unitLen = 1; unitLen <= Long.toString(i).length() / 2; unitLen++) {

                        if(Long.toString(i).length() % unitLen != 0) {
                            continue;
                        }

                        boolean matches = true;

                        for (int pos = unitLen; pos < Long.toString(i).length(); pos+=unitLen) {
                            if(!Long.toString(i).substring(pos, pos + unitLen).equals(Long.toString(i).substring(0, unitLen))) {
                                matches = false;
                                break;
                            }
                        }

                        if(matches) {
                            isInvalid = true;
                            break;
                        }
                    }

                    if(isInvalid) {
                        System.out.println(i + " is an INVALID number");
                        count++;
                        total += i;
                    }
                }
            }
            System.out.println("Count: " + count);
            System.out.println("--------------------------------");
            System.out.println("Total: " + total);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }   
    }
}