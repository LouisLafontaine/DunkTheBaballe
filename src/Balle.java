import java.awt.*;

public class Balle {
    // Attributs
    protected int x; // pos x
    protected int y; // pos y
    protected int xInit ; // pos x initiale
    protected int yInit ; // pos y initiale
    protected int r; // rayon
    protected double v; // vitesse
    protected Color maCouleur; // couleur

    // Constructeur
    public Balle(int x, int y, int r, double v) {
        this.x = x;
        this.y = y;
        this.xInit = x;
        this.yInit = y;
        this.r = r;
        this.v = v;
        maCouleur = new Color(100, 200,200);
    }

    // Méthodes
    public void drawBalle(Graphics g) {
        g.setColor(maCouleur);
        g.fillOval(x, y, r,r);
    }

    public void updatePosBalle(int largeurFenetre, int hauteurFenetre){
        x = (int) (x+v);
//        y = (int) (y+v);
        if(notInBounds(largeurFenetre,hauteurFenetre)){
            resetPosBalle();
        }
    }

    public void resetPosBalle(){
        x = xInit;
        y = yInit;
    }

    public boolean notInBounds(int largeurFenetre, int hauteurFenetre) { // true si hors dans la fenetre
        return (this.x > largeurFenetre || this.y > hauteurFenetre);
    }
}
