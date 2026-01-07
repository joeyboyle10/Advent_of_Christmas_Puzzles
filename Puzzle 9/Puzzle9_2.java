import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;

public class Puzzle9_2 {
    
    static class Point {
        int x;
        int y;

        @Override
        public boolean equals(Object obj) {
            if(this == obj) return true;
            if(obj == null || getClass() != obj.getClass()) return false;
            Point point = (Point) obj;
            return x == point.x && y == point.y;
        }
    }
    public static void main(String[] args) {
        
        try {
            String content = Files.readString(Paths.get("input9.txt"));
            String[] lines = content.trim().split("\\r?\\n");

            List<Point> points = new ArrayList<>();

            for(int i = 0; i < lines.length; i++) {
                String[] parts = lines[i].split(",");
                Point point = new Point();
                point.x = Integer.parseInt(parts[0]);
                point.y = Integer.parseInt(parts[1]);
                System.out.println("Point: (" + point.x + ", " + point.y + ")");
                points.add(point);
            }

            long largestArea = 0;
            List<Point> largestAreaPoints = new ArrayList<>();


            for(int i = 0; i < points.size() - 1; i++) {
                for(int j = i + 1; j < points.size(); j++) {
                    Point point1 = points.get(i);
                    Point point2 = points.get(j);

                    if(point1.x == point2.x || point1.y == point2.y) {
                        continue;
                    }

                    Point point3 = new Point();
                    point3.x = point1.x;
                    point3.y = point2.y;
                    Point point4 = new Point();
                    point4.x = point2.x;
                    point4.y = point1.y;

                    System.out.println("--------------------------------");
                    System.out.println("POINT1: " + point1.x + ", " + point1.y + " and POINT2: " + point2.x + ", " + point2.y);
                    System.out.println("Point3: " + point3.x + ", " + point3.y + " and Point4: " + point4.x + ", " + point4.y);

                    boolean point3IsValid = false;
                    if(points.contains(point3)) {
                        point3IsValid = true;
                    } else {
                        for(int k = 0; k < points.size(); k++) {
                            Point a = points.get(k);
                            Point b = points.get((k+1) % points.size());

                            if(a.x == b.x && point3.x == a.x &&
                                point3.y >= Math.min(a.y, b.y) && point3.y <= Math.max(a.y, b.y)) {
                                point3IsValid = true;
                            }

                            if(a.y == b.y && point3.y == a.y &&
                                point3.x >= Math.min(a.x, b.x) && point3.x <= Math.max(a.x, b.x)) {
                                point3IsValid = true;
                            }
                        }

                        if(!point3IsValid) {
                            int crossings = 0;
                            for(int k = 0; k < points.size(); k++) {
                                Point a = points.get(k);
                                Point b = points.get((k+1) % points.size());
                                if((a.y > point3.y) != (b.y > point3.y)) {
                                    double xIntersect = (double)(b.x - a.x) * (point3.y - a.y) / (b.y - a.y) + a.x;
                                    if(point3.x < xIntersect) {
                                        crossings++;
                                    }
                                }
                            }
                            point3IsValid = crossings % 2 == 1;
                        }
                    }

                    boolean point4IsValid = false;
                    if(points.contains(point4)) {
                        point4IsValid = true;
                    } else {
                        for(int k = 0; k < points.size(); k++) {
                            Point a = points.get(k);
                            Point b = points.get((k+1) % points.size());

                            if(a.x == b.x && point4.x == a.x &&
                                point4.y >= Math.min(a.y, b.y) && point4.y <= Math.max(a.y, b.y)) {
                                    point4IsValid = true;
                                }
                            if(a.y == b.y && point4.y == a.y &&
                                point4.x >= Math.min(a.x, b.x) && point4.x <= Math.max(a.x, b.x)) {
                                    point4IsValid = true;
                            }
                        }

                        if (!point4IsValid) {
                            int crossings = 0;
                            for(int k = 0; k < points.size(); k++) {
                                Point a = points.get(k);
                                Point b = points.get((k+1) % points.size());
                                if((a.y > point4.y) != (b.y > point4.y)) {
                                    double xIntersect = (double)(b.x - a.x) * (point4.y - a.y) / (b.y - a.y) + a.x;
                                    if(point4.x < xIntersect) {
                                        crossings++;
                                    }
                                }
                            }
                            point4IsValid = crossings % 2 == 1;
                        }
                    }

                    if(point3IsValid && point4IsValid) {
                        int minX = Math.min(point1.x, point2.x);
                        int maxX = Math.max(point1.x, point2.x);
                        int minY = Math.min(point1.y, point2.y);
                        int maxY = Math.max(point1.y, point2.y);
                        
                        boolean rectangleIsValid = true;
                        
                        for (int k = 0; k < points.size(); k++) {
                            Point a = points.get(k);
                            Point b = points.get((k + 1) % points.size());
                            
                            if (a.x == b.x) {
                                if (a.x > minX && a.x < maxX) {
                                    int edgeMinY = Math.min(a.y, b.y);
                                    int edgeMaxY = Math.max(a.y, b.y);
                                    
                                    if (edgeMaxY > minY && edgeMinY < maxY) {
                                        rectangleIsValid = false;
                                        break;
                                    }
                                }
                            }
                            if (a.y == b.y) {
                                if (a.y > minY && a.y < maxY) {
                                    int edgeMinX = Math.min(a.x, b.x);
                                    int edgeMaxX = Math.max(a.x, b.x);

                                    if (edgeMaxX > minX && edgeMinX < maxX) {
                                        rectangleIsValid = false;
                                        break;
                                    }
                                }
                            }
                        }
                        
                        if (rectangleIsValid) {
                            long area = (1L * (maxX - minX + 1)) * (maxY - minY + 1);
                            System.out.println("Area: " + area);

                            if(area > largestArea) {
                                largestArea = area;
                                largestAreaPoints.clear();
                                largestAreaPoints.add(point1);
                                largestAreaPoints.add(point2);
                                System.out.println("New largest area: " + largestArea + " at Point1: " + point1.x + ", " + point1.y + " and Point2: " + point2.x + ", " + point2.y);
                            }
                        }
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
