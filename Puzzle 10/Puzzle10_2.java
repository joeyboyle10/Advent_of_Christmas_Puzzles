import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Puzzle10_2 {

    public static void main(String[] args) {
        try {
            String content = Files.readString(Paths.get("Puzzle 10/input10.txt"));
            String[] lines = content.trim().split("\\r?\\n");

            long totalPresses = 0;

            for (int machineIndex = 0; machineIndex < lines.length; machineIndex++) {
                Machine machine = Machine.parse(lines[machineIndex]);
                long minPresses = machine.findMinimumPresses();
                
                System.out.println("Machine " + (machineIndex + 1) + ": " + minPresses);
                totalPresses += minPresses;
            }
            
            System.out.println("Total: " + totalPresses);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

class Machine {
    private final int[][] buttonEffects;
    private final int[] targetValues;
    private final int numCounters;
    private final int numButtons;
    
    private Machine(int[][] buttonEffects, int[] targetValues) {
        this.buttonEffects = buttonEffects;
        this.targetValues = targetValues;
        this.numCounters = targetValues.length;
        this.numButtons = buttonEffects.length > 0 ? buttonEffects[0].length : 0;
    }
    
    static Machine parse(String line) {
        int braceStart = line.indexOf("{");
        int braceEnd = line.indexOf("}");
        String[] targetParts = line.substring(braceStart + 1, braceEnd).split(",");
        
        int[] targets = new int[targetParts.length];
        for (int i = 0; i < targetParts.length; i++) {
            targets[i] = Integer.parseInt(targetParts[i].trim());
        }
        
        int indicatorEnd = line.indexOf("]");
        String buttonSection = line.substring(indicatorEnd + 1, braceStart - 1).trim();
        
        List<int[]> buttons = new ArrayList<>();
        int index = 0;
        while (index < buttonSection.length()) {
            if (buttonSection.charAt(index) == '(') {
                int closeIndex = buttonSection.indexOf(')', index);
                String inside = buttonSection.substring(index + 1, closeIndex);
                String[] nums = inside.split(",");
                int[] countersAffected = new int[nums.length];
                for (int i = 0; i < nums.length; i++) {
                    countersAffected[i] = Integer.parseInt(nums[i].trim());
                }
                buttons.add(countersAffected);
                index = closeIndex + 1;
            } else {
                index++;
            }
        }
        
        int[][] effects = new int[targets.length][buttons.size()];
        for (int buttonIndex = 0; buttonIndex < buttons.size(); buttonIndex++) {
            for (int counter : buttons.get(buttonIndex)) {
                effects[counter][buttonIndex] = 1;
            }
        }
        
        return new Machine(effects, targets);
    }
    
    long findMinimumPresses() {
        LinearSystem system = new LinearSystem(buttonEffects, targetValues, numButtons, numCounters);
        return system.solveForMinimumSum();
    }
}

class Rational {
    final long numerator;
    final long denominator;
    
    Rational(long value) {
        this.numerator = value;
        this.denominator = 1;
    }
    
    Rational(long num, long den) {
        if (den < 0) {
            num = -num;
            den = -den;
        }
        long gcd = gcd(Math.abs(num), Math.abs(den));
        this.numerator = num / gcd;
        this.denominator = den / gcd;
    }
    
    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    Rational add(Rational other) {
        return new Rational(
            numerator * other.denominator + other.numerator * denominator,
            denominator * other.denominator
        );
    }
    
    Rational subtract(Rational other) {
        return new Rational(
            numerator * other.denominator - other.numerator * denominator,
            denominator * other.denominator
        );
    }
    
    Rational multiply(Rational other) {
        return new Rational(numerator * other.numerator, denominator * other.denominator);
    }
    
    Rational divide(Rational other) {
        return new Rational(numerator * other.denominator, denominator * other.numerator);
    }
    
    boolean isZero() {
        return numerator == 0;
    }
    
    boolean isNonNegativeInteger() {
        return denominator == 1 && numerator >= 0;
    }
    
    long asLong() {
        return numerator;
    }
}

class LinearSystem {
    private final Rational[][] augmentedMatrix;
    private final int numVariables;
    private final int numEquations;
    private final int[] targetValues;
    
    private int[] pivotColumns;
    private int rank;
    
    LinearSystem(int[][] coefficients, int[] targets, int numVars, int numEqs) {
        this.numVariables = numVars;
        this.numEquations = numEqs;
        this.targetValues = targets;
        
        this.augmentedMatrix = new Rational[numEqs][numVars + 1];
        for (int row = 0; row < numEqs; row++) {
            for (int col = 0; col < numVars; col++) {
                augmentedMatrix[row][col] = new Rational(coefficients[row][col]);
            }
            augmentedMatrix[row][numVars] = new Rational(targets[row]);
        }
    }
    
    long solveForMinimumSum() {
        reduceToRowEchelonForm();
        
        if (isInconsistent()) {
            return -1;
        }
        
        int numFreeVariables = numVariables - rank;
        
        if (numFreeVariables == 0) {
            return extractUniqueSolution();
        }
        
        return searchFreeVariables();
    }
    
    private void reduceToRowEchelonForm() {
        pivotColumns = new int[Math.min(numEquations, numVariables)];
        Arrays.fill(pivotColumns, -1);
        int currentPivotRow = 0;
        
        for (int col = 0; col < numVariables && currentPivotRow < numEquations; col++) {
            int pivotRow = findPivotRow(col, currentPivotRow);
            if (pivotRow < 0) continue;
            
            swapRows(currentPivotRow, pivotRow);
            pivotColumns[currentPivotRow] = col;
            
            normalizeRow(currentPivotRow, col);
            eliminateColumn(currentPivotRow, col);
            
            currentPivotRow++;
        }
        
        rank = currentPivotRow;
    }
    
    private int findPivotRow(int column, int startRow) {
        for (int row = startRow; row < numEquations; row++) {
            if (!augmentedMatrix[row][column].isZero()) {
                return row;
            }
        }
        return -1;
    }
    
    private void swapRows(int row1, int row2) {
        Rational[] temp = augmentedMatrix[row1];
        augmentedMatrix[row1] = augmentedMatrix[row2];
        augmentedMatrix[row2] = temp;
    }
    
    private void normalizeRow(int row, int pivotColumn) {
        Rational pivotValue = augmentedMatrix[row][pivotColumn];
        for (int col = 0; col <= numVariables; col++) {
            augmentedMatrix[row][col] = augmentedMatrix[row][col].divide(pivotValue);
        }
    }
    
    private void eliminateColumn(int pivotRow, int pivotColumn) {
        for (int row = 0; row < numEquations; row++) {
            if (row != pivotRow && !augmentedMatrix[row][pivotColumn].isZero()) {
                Rational factor = augmentedMatrix[row][pivotColumn];
                for (int col = 0; col <= numVariables; col++) {
                    augmentedMatrix[row][col] = augmentedMatrix[row][col]
                        .subtract(factor.multiply(augmentedMatrix[pivotRow][col]));
                }
            }
        }
    }
    
    private boolean isInconsistent() {
        for (int row = rank; row < numEquations; row++) {
            if (!augmentedMatrix[row][numVariables].isZero()) {
                return true;
            }
        }
        return false;
    }
    
    private long extractUniqueSolution() {
        long total = 0;
        for (int i = 0; i < rank; i++) {
            Rational value = augmentedMatrix[i][numVariables];
            if (!value.isNonNegativeInteger()) {
                return -1;
            }
            total += value.asLong();
        }
        return total;
    }
    
    private long searchFreeVariables() {
        int[] freeVariableIndices = findFreeVariableIndices();
        int maxValue = findMaxTarget();
        long[] variableValues = new long[numVariables];
        
        return searchRecursively(freeVariableIndices, 0, variableValues, maxValue, Long.MAX_VALUE);
    }
    
    private int[] findFreeVariableIndices() {
        Set<Integer> pivotColumnSet = new HashSet<>();
        for (int i = 0; i < rank; i++) {
            if (pivotColumns[i] >= 0) {
                pivotColumnSet.add(pivotColumns[i]);
            }
        }
        
        int numFree = numVariables - rank;
        int[] freeIndices = new int[numFree];
        int index = 0;
        for (int col = 0; col < numVariables; col++) {
            if (!pivotColumnSet.contains(col)) {
                freeIndices[index++] = col;
            }
        }
        return freeIndices;
    }
    
    private int findMaxTarget() {
        int max = 0;
        for (int target : targetValues) {
            max = Math.max(max, target);
        }
        return max;
    }
    
    private long searchRecursively(int[] freeIndices, int depth, long[] values, int maxValue, long bestSoFar) {
        if (depth == freeIndices.length) {
            return evaluateSolution(freeIndices, values, bestSoFar);
        }
        
        int freeVariable = freeIndices[depth];
        long best = bestSoFar;
        
        for (int value = 0; value <= maxValue; value++) {
            values[freeVariable] = value;
            long result = searchRecursively(freeIndices, depth + 1, values, maxValue, best);
            if (result < best) {
                best = result;
            }
        }
        values[freeVariable] = 0;
        
        return best;
    }
    
    private long evaluateSolution(int[] freeIndices, long[] values, long bestSoFar) {
        long total = 0;
        
        for (int freeIndex : freeIndices) {
            total += values[freeIndex];
        }
        if (total >= bestSoFar) {
            return Long.MAX_VALUE;
        }
        
        for (int i = 0; i < rank; i++) {
            int pivotColumn = pivotColumns[i];
            if (pivotColumn < 0) continue;
            
            Rational computedValue = augmentedMatrix[i][numVariables];
            for (int col = 0; col < numVariables; col++) {
                if (col != pivotColumn) {
                    computedValue = computedValue.subtract(
                        augmentedMatrix[i][col].multiply(new Rational(values[col]))
                    );
                }
            }
            
            if (!computedValue.isNonNegativeInteger()) {
                return Long.MAX_VALUE;
            }
            
            values[pivotColumn] = computedValue.asLong();
            total += computedValue.asLong();
            
            if (total >= bestSoFar) {
                return Long.MAX_VALUE;
            }
        }
        
        return total;
    }
}
