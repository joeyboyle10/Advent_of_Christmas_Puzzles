import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;

class Point {
    int x;
    int y;
}

public class Puzzle9_1 {
    public static void main(String[] args) {
        try {
            String content = Files.readString(Paths.get("input9.txt"));
            String[] lines = content.trim().split("\\r?\\n");

            List<Point> points = new ArrayList<>();

            for(int i = 0; i < lines.length; i++) {
                String[] parts = lines[i].split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                Point point = new Point();
                point.x = x;
                point.y = y;
                System.out.println("Point: " + point.x + ", " + point.y);
                points.add(point);
            }

            //COMPUTING LARGEST AREAS
            long largestArea = 0;
            List<Point> largestAreaPoints = new ArrayList<>();
            for(int i = 0; i < points.size() - 1; i++) {
                for(int j = i + 1; j < points.size(); j++) {
                    Point point1 = points.get(i);
                    Point point2 = points.get(j);
                    
                    long area = (1L * Math.max(point1.x,point2.x) - Math.min(point1.x,point2.x) + 1) * (Math.max(point1.y,point2.y) - Math.min(point1.y,point2.y) + 1);
                    if(area > largestArea) {
                        System.out.println("Row: " + i + " and Row: " + j);
                        largestArea = area;
                        System.out.println("New largest area: " + largestArea + " at Point1: " + point1.x + ", " + point1.y + " and Point2: " + point2.x + ", " + point2.y);
                        largestAreaPoints.clear();
                        largestAreaPoints.add(point1);
                        largestAreaPoints.add(point2);
                    }
                }
            }
            System.out.println("Largest area: " + largestArea + " found at: ");
            for(Point point : largestAreaPoints) {
                System.out.println("Point: " + point.x + ", " + point.y);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
