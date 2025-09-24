package algo.pair;

import algo.common.Metrics;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ClosestPairTest {

    @Test
    void tiny() {
        Point2D[] pts = {
                new Point2D(0,0),
                new Point2D(3,4),
                new Point2D(1,1)
        };
        Metrics m = new Metrics();
        ClosestPair.Result r = ClosestPair.closest(pts, m);
        assertEquals(Math.hypot(1,1), r.dist, 1e-9);
    }

    @Test
    void randomVsBruteSmall() {
        Random rnd = new Random(123);
        for (int n = 2; n <= 200; n += 7) {
            Point2D[] pts = new Point2D[n];
            for (int i = 0; i < n; i++) {
                pts[i] = new Point2D(rnd.nextDouble()*1000.0, rnd.nextDouble()*1000.0);
            }
            Metrics fastM = new Metrics();
            ClosestPair.Result fast = ClosestPair.closest(pts, fastM);


            double brute = Double.POSITIVE_INFINITY;
            for (int i = 0; i < n; i++) {
                for (int j = i+1; j < n; j++) {
                    double d = Math.hypot(pts[i].x()-pts[j].x(), pts[i].y()-pts[j].y());
                    if (d < brute) brute = d;
                }
            }
            assertEquals(brute, fast.dist, 1e-9, "n=" + n);
        }
    }
}
