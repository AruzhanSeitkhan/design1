package algo.pair;

public record Point2D(double x, double y) {
    @Override public String toString() { return "(" + x + "," + y + ")"; }
}
