import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

public class Puzzle10_practice {

    static class Machine {
        int [] machine = new int[0];
        Map<Integer, List<Button>> map = new HashMap<>();
        List<Button> buttons = new ArrayList<>();
        Map<String, Integer> mapOfMachines = new HashMap<String, Integer>();

        Machine(List<Button> buttons) {
            this.buttons = buttons;
            this.machine = new int[buttons.size()];
        }

        Machine(String[] buttons) {
            for(String button : buttons) {
                this.buttons.add(new Button(button));
            }
            System.out.println("Buttons: " + this.buttons.toString());
        }

        public String toString() {
            return Arrays.toString(machine);
        }
    }

    static class Button {
        List<Integer> values = new ArrayList<>();

        Button(List<Integer> values) {
            this.values = values;
        }

        Button(String str) {
            String temp = str.substring(1, str.length() - 1);
            String[] numbers = temp.split(",");
            for (String number : numbers) {
                values.add(Integer.parseInt(number));
            }
            System.out.println("Buttons: " + values);
        }

        public String toString() {
            return values.toString();
        }
    }

    public static void main(String[] args) {
        try {
            String content = Files.readString(Paths.get("input10.txt"));
            String[] lines = content.trim().split("\n");

            for (String line : lines) {
                System.out.println(line);
            }

            int start, end = 0;
            int endOfMachine = 0;
            for(int i = 1; i < 2; i++) {
                String line = lines[i];
                System.out.println("Line: " + line);
                start = line.indexOf("]") + 1;
                end = line.indexOf("{") - 1;
                endOfMachine = line.indexOf("]");
                String buttonValues = line.substring(start, end).trim();
                System.out.println("buttonValues: " + buttonValues);
                String newMachine = line.substring(1, endOfMachine).trim();
                System.out.println("New Machine: " + newMachine);
                String[] buttonValuesArray = buttonValues.split(" ");

                Machine mach = new Machine(buttonValuesArray);
                mach.machine = new int[newMachine.length()];
                for(int j = 0; j < newMachine.length(); j++) {
                    if(newMachine.charAt(j) == '.') {
                        mach.machine[j] = 0;
                    } else {
                        mach.machine[j] = 1;
                    }
                }
                System.out.println("Machine: " + mach.toString());

                Map<Integer, List<Button>> map = new HashMap<>();
                System.out.println("--------------------------------");
                System.out.println("Machine buttons: " + mach.buttons.toString());
                for(int a = 0; a < mach.machine.length; a++) {
                    List<Button> tempButtons = new ArrayList<>();
                    for(int b = 0; b < mach.buttons.size(); b++) {
                        if(mach.buttons.get(b).values.contains(a)) {
                            tempButtons.add(mach.buttons.get(b));
                        }
                    }
                    map.put(a, tempButtons);
                }
                System.out.println("Map: " + map.toString());


                List<String> Routes = new ArrayList<>();
                List<String> smallestRoute = new ArrayList<>();

                Machine tempMach = new Machine(mach.buttons);
                tempMach.machine = new int[newMachine.length()];
                System.out.println("Temp Machine: " + tempMach.toString());
                System.out.println("Machine: " + Arrays.toString(mach.machine));

                System.out.println("--------------------------------");
                System.out.println("--------------------------------");

                checkMachine(map, mach, tempMach, tempMach.mapOfMachines, Routes, smallestRoute);
                System.out.println("Smallest Route: " + (smallestRoute.size() - 1));
                System.out.println("Smallest Route: " + smallestRoute.toString());
                System.out.println("--------------------------------");
            }   
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public static void checkMachine(Map<Integer, List<Button>> map, Machine mach, Machine tempMach, Map<String, Integer> mapOfMachines, List<String> Routes, List<String> smallestRoute) {
        /*System.out.println("--------------------------------");
        System.out.println("Checking MACH: " + mach.toString());
        System.out.println("Temp Machine Box: " + tempMach.toString());
        System.out.println("Map of Buttons: " + map.toString());
        System.out.println("Map of Machines: " + mapOfMachines.toString());
        System.out.println("--------------------------------");*/

        if(Arrays.equals(tempMach.machine, mach.machine)) {
            System.out.println("Machine is the same, returning");
            Routes.add(Arrays.toString(tempMach.machine));
            System.out.println("Routes: " + Routes.toString());
            if(smallestRoute.isEmpty() || Routes.size() < smallestRoute.size()) {
                smallestRoute.clear();
                smallestRoute.addAll(Routes);
                System.out.println("Smallest Route: " + smallestRoute.toString());
            }
            return;
        }

        String stateKey = Arrays.toString(tempMach.machine);

        if(mapOfMachines.containsKey(stateKey)) {
            System.out.println("Already visited this state, returning");
            System.out.println("Routes: " + Routes.size());
            return;
        }

        mapOfMachines.put(stateKey, 1);
        Routes.add(stateKey);

        for(int i = 0; i < tempMach.machine.length; i++) {
            if(tempMach.machine[i] != mach.machine[i]) {
                for(int j = 0; j < map.get(i).size(); j++) {
                    int[] savedState = Arrays.copyOf(tempMach.machine, tempMach.machine.length);
                    
                    tempMach = isPressed(map.get(i).get(j).values, tempMach);
                    checkMachine(map, mach, tempMach, mapOfMachines, Routes, smallestRoute);
                    
                    tempMach.machine = savedState;
                }
            }
        }
    }

    public static Machine isPressed(List<Integer> x, Machine m) {
        System.out.println("X: " + x.toString());
        System.out.println("Machine Before: " + m.toString()); 
        System.out.println("--------------------------------");
        if(x.isEmpty()) {
            return m;
        }
        m.mapOfMachines.put(Arrays.toString(m.machine), 1);
        for(int i = 0; i < x.size(); i++) {
            if(m.machine[x.get(i)] == 1) {
                m.machine[x.get(i)] = 0;
            } else {
                m.machine[x.get(i)] = 1;
            }
        }
        System.out.println("Machine After: " + m.toString());
        return m;
    }
}
