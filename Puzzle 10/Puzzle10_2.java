import java.io.*;
import java.nio.file.*;
import java.util.*;

//IN PROGRESS
public class Puzzle10_2 {

    static class Button {
        List<Integer> values = new ArrayList<>();

        Button(String str) {
            String temp = str.substring(1, str.length() - 1);
            String[] numbers = temp.split(",");
            for (String number : numbers) {
                values.add(Integer.parseInt(number));
            }
        }

        public String toString() {
            return values.toString();
        }
    }

    public static void main(String[] args) {
        try {
            String content = Files.readString(Paths.get("input10.txt"));
            String[] lines = content.trim().split("\n");

            int totalPresses = 0;

            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                
                int endOfTarget = line.indexOf("]");
                String targetStr = line.substring(1, endOfTarget);
                int[] target = new int[targetStr.length()];
                for (int j = 0; j < targetStr.length(); j++) {
                    target[j] = (targetStr.charAt(j) == '#') ? 1 : 0;
                }
                
                int start = endOfTarget + 1;
                int end = line.indexOf("{") - 1;
                String buttonValues = line.substring(start, end).trim();
                String[] buttonStrings = buttonValues.split(" ");
                List<Button> buttons = new ArrayList<>();
                for (String bs : buttonStrings) {
                    buttons.add(new Button(bs));
                }
                
                int minPresses = findMinPresses(buttons, target);
                System.out.println("Machine " + (i+1) + ": " + minPresses + " presses");
                totalPresses += minPresses;
            }
            
            System.out.println("Total: " + totalPresses);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public static int findMinPresses(List<Button> buttons, int[] target) {
        Queue<int[]> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        
        int[] start = new int[target.length];
        queue.add(start);
        visited.add(Arrays.toString(start));
        
        Queue<Integer> presses = new LinkedList<>();
        presses.add(0);
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currentPresses = presses.poll();
            
            if (Arrays.equals(current, target)) {
                return currentPresses;
            }
            
            for (Button button : buttons) {
                int[] next = Arrays.copyOf(current, current.length);
                for (int idx : button.values) {
                    next[idx] = 1 - next[idx];
                }
                
                String nextKey = Arrays.toString(next);
                if (!visited.contains(nextKey)) {
                    visited.add(nextKey);
                    queue.add(next);
                    presses.add(currentPresses + 1);
                }
            }
        }
        
        return -1;
    }
}