package oyun;

import java.awt.*;

public class Mermi {
    public double x;
    public double y;
    public double diameter = 10;
    public double aci;
    private int V = 20;
    public double Vx;
    public double Vy;
    public Color color;

    public Mermi(double _aci){
        aci = _aci;
        x = Core.WIDTH/2 - diameter/2 + 2;
        y = Core.HEIGHT - diameter/2;
        Vx = V*Math.cos(Math.toRadians(90 - aci));
        Vy = V*Math.sin(Math.toRadians(90 - aci));
        color = new Color(232, 234, 237);
    }
}
