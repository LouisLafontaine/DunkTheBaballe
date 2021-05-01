import java.awt.*;

public class Panier {
    int x; // pos x
    int y; // pos y
    int d ; // rayon

    public Panier(int x, int y, int d) {
        this.x = x;
        this.y = y;
        this.d = d;
    }

    public void drawPanier(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x-d/2,y-d/2,d,d);
    }
}
