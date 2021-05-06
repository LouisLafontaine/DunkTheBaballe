import java.awt.*;

public class Obstacle {
    int x; // position x du coin supérieur gauche du rectangle
    int y; // position y du coin supérieur gauche du rectangle
    int largeur;
    int hauteur;

    public Obstacle(int x, int y, int largeur, int hauteur) {
        this.x = x;
        this.y = y;
        this.largeur = largeur;
        this.hauteur = hauteur;
    }

    public void drawObstacle(Graphics g) {
        Color brown = new Color(70,10,0);
        g.setColor(brown);
        g.fillRect(x,y,largeur,hauteur);
    }
}
