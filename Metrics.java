package algo.common;

public final class Metrics {
    public long comparisons;
    public long swaps;
    public long allocations;
    private int recursionDepth;
    public int maxRecursionDepth;
    private long startNanos;
    public long elapsedNanos;

    public void startTimer() { startNanos = System.nanoTime(); }
    public void stopTimer()  { elapsedNanos = System.nanoTime() - startNanos; }

    public void onEnter() {
        recursionDepth++;
        if (recursionDepth > maxRecursionDepth) maxRecursionDepth = recursionDepth;
    }
    public void onExit()  { recursionDepth--; }

    public void addAlloc(long units) { allocations += units; }

    @Override public String toString() {
        return "Metrics{comparisons=" + comparisons +
                ", swaps=" + swaps +
                ", allocations=" + allocations +
                ", maxRecursionDepth=" + maxRecursionDepth +
                ", elapsedNanos=" + elapsedNanos + "}";
    }
}