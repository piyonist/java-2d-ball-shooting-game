package oyun;

import java.awt.*;
import java.util.Random;

public class Top {
    public double x;
    public double y;
    public double diameter = 20;
    public double hiz;
    double minHiz = 1;
    public int startTime;
    public Color color;
    public boolean asagi = true;

    public Top(){
        Random random = new Random();
        x = random.nextDouble() * (Core.WIDTH - diameter);
        hiz = random.nextDouble() * 1 + minHiz;
        startTime = random.nextInt(1000);

        float r,g,b;
        r = random.nextFloat();
        g = random.nextFloat();
        b = random.nextFloat();

        color = new Color(r, g, b);
    }
}
