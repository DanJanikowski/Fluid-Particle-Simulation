import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by douwz on 1/30/2017.
 */
public class Grid {

    public static int w, h;
    public ArrayList<Dot> dots;
    private Random rnd = new Random();

    public Grid(int m, int n) {
        w = m;
        h = n;
        dots = new ArrayList<>();
        for (int i = 0; i < 100000; i++)
            dots.add(new Dot(rnd.nextInt(100) + 10, rnd.nextInt(100) + 10));
    }

    public void step() {
        if (Main.clicking) {
            dots.parallelStream().forEach(dot -> {
                Vector diff = new Vector(dot.p.x - Main.x, dot.p.y - Main.y);
                double dist = diff.magnitude();
                if (dist > 25) {
                    double theta = diff.getTheta();
                    Vector grv = Vector.fromPolar(dot.mass / (dist * 10D), theta);
                    dot.a.subtract(grv);
                }
            });
        }
//        dots.parallelStream().forEach(dot -> {
//            dots.forEach(dot2 -> {
//                if (!dot.equals(dot2))
//                    dot.intermolecular(dot2);
//            });
//        });
        dots.parallelStream().forEach(dot -> dot.step());
    }

    public void draw(GraphicsContext g) {
        dots.forEach(dot -> dot.draw(g));
    }

    public double[] avgPos() {
        double[] pos = new double[2];
        double ax = 0, ay = 0;
        for (int i = 0; i < dots.size(); i++) {
            ax += dots.get(i).p.x;
            ay += dots.get(i).p.y;
        }
        ax /= (double)dots.size();
        ay /= (double)dots.size();
        pos[0] = ax;
        pos[1] = ay;
        return pos;
    }

    public double[] avgMass() {
        double[] pos = new double[2];
        double ax = 0, ay = 0, totalMass = 0;
        for (int i = 0; i < dots.size(); i++) {
            ax += dots.get(i).p.x * dots.get(i).mass;
            ay += dots.get(i).p.y * dots.get(i).mass;
            totalMass += dots.get(i).mass;
        }
        ax /= totalMass;
        ay /= totalMass;
        pos[0] = ax;
        pos[1] = ay;
        return pos;
    }
}
