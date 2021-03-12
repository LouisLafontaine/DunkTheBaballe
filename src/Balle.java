import javax.swing.*;
import java.awt.*;

public class Balle {
    // Attributs
    //======================================================================
    protected int x; // pos x
    protected int y; // pos y
    protected int xInit ; // pos x initiale
    protected int yInit ; // pos y initiale
    protected int r; // rayon
    protected double vx; // vitesse selon x
    protected double vy; // vitesse selon y
    protected Color maCouleur; // couleur

    // Constructeur
    //======================================================================
    public Balle(int x, int y, int r, double vx, double vy) {
        this.x = x;
        this.y = y;
        this.xInit = x;
        this.yInit = y;
        this.vx = vx;
        this.vy = vy;
        this.r = r;
        maCouleur = Color.yellow;
    }

    // Méthodes
    //======================================================================
    public void drawBalle(Graphics g) {
        g.setColor(maCouleur);
        g.fillOval(x-r/2, y-r/2, r,r);

    }

    public void updatePosBalle(int largeurFenetre, int hauteurFenetre, Timer timer){
        x = (int) (x+vx);
        y = (int) (y+vy);
        if(notInBounds(largeurFenetre,hauteurFenetre)){
            timer.stop();
            resetPosBalle();
        }
    }

    public void resetPosBalle(){
        x = xInit;
        y = yInit;
    }

    public boolean notInBounds(int largeurFenetre, int hauteurFenetre) { // true si hors dans la fenetre
        return ((x <= 0) || (x >= largeurFenetre) || (y <= 0) || (y >= hauteurFenetre));
    }

    public int distanceBalle(int x, int y){
        return(Math.abs(x-this.x) + Math.abs(y-this.y));
    }

    public boolean toucheBalle(int x, int y){
        return (distanceBalle(x,y) < r);
    }
}
