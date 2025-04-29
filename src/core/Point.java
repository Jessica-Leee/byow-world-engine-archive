package core;

public class Point {
    private final int x;
    private final int y;
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public int distanceTo(Point other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }
}
