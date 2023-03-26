import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Created by douwz on 1/30/2017.
 */
public class Dot {

    private Random rnd = new Random();
    public static final double LBOUND = 0, RBOUND = Main.w, UBOUND = 0, DBOUND = Main.h;
    public double vx, vy, mass;
    public Vector p, pp, a, orgA;
    private final double GRAVITY = 0.4, DRAG = 0.025, WDRAG = 0.08, BOUNCE = rnd.nextDouble() / 4D + 0.15;
    private Color c;

    public Dot(double ax, double ay) {
        p = new Vector(ax, ay);
//        pp = new Vector(p.x + (rnd.nextDouble()*10D-5), p.y + (rnd.nextDouble()*10D-5));
        pp = new Vector(p);
//        a = new Vector(0, GRAVITY - pressure());
        a = new Vector(0, GRAVITY);
        orgA = new Vector(a);
        mass = rnd.nextInt(1000) + 1000;
    }

    public void step() {
        double slope, yint, xint;
        vx = ((p.x - pp.x) + a.x);
        vy = ((p.y - pp.y) + a.y);
        vx -= DRAG * vx;
        vy -= DRAG * vy;
        if ((p.x + vx <= LBOUND || p.x + vx >= RBOUND) || (p.y + vy <= UBOUND || p.y + vy >= DBOUND)) {
            if (p.x + vx <= LBOUND || p.x + vx >= RBOUND) {
                slope = vy / vx;
                yint = p.y - slope * p.x;
                if (p.x + vx <= LBOUND)
                    p.x = LBOUND;
                else if (p.x + vx >= RBOUND)
                    p.x = RBOUND - 1;
                p.y = slope * p.x + yint;
                pp.x = p.x + vx * BOUNCE;
                pp.y = p.y - vy * BOUNCE;
            }
            if (p.y + vy <= UBOUND || p.y + vy >= DBOUND) {
                slope = vx / vy;
                xint = p.x - slope * p.y;
                if (p.y + vy <= UBOUND)
                    p.y = UBOUND;
                else if (p.y + vy >= DBOUND)
                    p.y = DBOUND - 1;
                p.x = slope * p.y + xint;
                pp.x = p.x - vx * BOUNCE;
                pp.y = p.y + vy * BOUNCE;
            }
        } else {
            pp.set(p);
            p.x += vx;
            p.y += vy;
            if (getMagVel() < 0.00000001)
                pp.set(p);
        }
//        a = new Vector(0, GRAVITY - pressure());
        a = new Vector(0, GRAVITY);
    }

    public void intermolecular(Dot d) {
        Vector diff = new Vector(p.x - d.p.x, p.y - d.p.y);
        double dist = diff.magnitude();
        double theta = diff.getTheta();
        if (dist < 3) {
            Vector grv = Vector.fromPolar(mass * d.mass / 10000000D, theta);
            a.add(grv);
            d.a.subtract(grv);
        }
    }

//    public double pressure() {
//        if (p.y > Main.h / 2D)
//            return Math.pow(p.y, 4) / 1E11D;
//        else return 0;
//    }

    private double getMagVel() {
        return Math.sqrt(Math.pow(p.x - pp.x, 2) + Math.pow(p.y - pp.y, 2));
    }

    public void draw(GraphicsContext g) {
        c = Color.hsb(200 + getMagVel() * 4D, 0.9, 0.9);
        g.setStroke(c);
//        g.strokeOval(p.x - 10, p.y - 10, 20, 20);
        g.setLineWidth(3);
        double drawPX = pp.x, drawPY = pp.y;
        if (drawPX < LBOUND)
            drawPX = LBOUND;
        if (drawPX > RBOUND)
            drawPX = RBOUND;
        if (drawPY < UBOUND)
            drawPY = UBOUND;
        if (drawPY > DBOUND)
            drawPY = DBOUND;
        g.strokeLine(drawPX, drawPY, p.x, p.y);
    }
}