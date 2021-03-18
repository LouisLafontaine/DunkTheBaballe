import javax.crypto.spec.PSource;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Balle{
    // Attributs
    //======================================================================
    protected double x; // pos x
    protected double y; // pos y
    protected double xCollision; // pos x de la dernière collision
    protected double yCollision; // pos y de la dernière collision
    protected double xInit ; // pos x initiale
    protected double yInit ; // pos y initiale
    protected int d; // diamètre
    protected double vx; // vitesse selon x
    protected double vy; // vitesse selon y
    protected int t;
    protected double g;
    Image characterImage;

    // Constructeur
    //======================================================================
    public Balle(int x, int y, int r, double vx, double vy, String characterImageFileName) {
        this.x = x;
        this.y = y;
        this.xInit = x;
        this.yInit = y;
        this.vx = vx;
        this.vy = vy;
        this.d = r;
        this.t = 0;
        xCollision = xInit;
        yCollision = yInit;
        g = 0.1;
        initializeCharacterImage(characterImageFileName);
    }

    // Méthodes initialisation
    private void initializeCharacterImage(String imageFileName) {
        String nameInFolder = "Ressources/Character/";
        try {
            characterImage = ImageIO.read(new File( nameInFolder + imageFileName).getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthodes
    //======================================================================
    public void drawBalle(Graphics g) {
        g.drawImage(characterImage, (int)(x)-d/2, (int)(y)-d/2,d,d,null);
    }

    public void updatePosBalle(int largeurFenetre, int hauteurFenetre, Timer timer){
        t++;
        x = xCollision+vx*t;
        y = 0.5*g*t*t + vy*t + yCollision;
        if(notInBounds(largeurFenetre,hauteurFenetre)){
            timer.stop();
            resetPosBalle();
        }
    }

    public void resetPosBalle(){
        x = xInit;
        y = yInit;
        xCollision = xInit;
        yCollision = yInit;
        t = 0;
    }

    public boolean notInBounds(int largeurFenetre, int hauteurFenetre) { // true si hors dans la fenetre
        return ((x <= 0) || (x >= largeurFenetre) || (y >= hauteurFenetre));
    }

    public int distanceBalle(int x, int y){
        double dX = x - this.x;
        double dY = y - this.y;
        return (int)(Math.sqrt(dX*dX + dY*dY));
    }

    public boolean toucheBalle(int x, int y){
        return (distanceBalle(x,y) < d/2);
    }

    public void ifClickedThrowBalle(MouseEvent e, Timer timer, int lastClickX, int lastClickY) {
        if(toucheBalle(lastClickX,lastClickY)){
            double mouseSensibility = 0.03;
            vx = (x - e.getX()) * mouseSensibility;
            vy = (y - e.getY()) * mouseSensibility;
            timer.start();
        }
    }

    // Méthodes Collisions
    public boolean hasCollided(Obstacle o) {
        boolean xOverlap = (this.x+d/2.0 > o.x) && (this.x-d/2.0 < o.x + o.largeur);
        boolean yOverlap = (this.y > o.y) && (this.y < o.y + o.hauteur);
        return (xOverlap && yOverlap);
    }

    public void solveCollision(Obstacle o) {
        double prevStep = 15;
        double xPrev = xCollision+vx*(t-prevStep);
        double yPrev = 0.5*g*t*t + vy*(t-prevStep) + yCollision;
        if (xPrev < o.x && yPrev > o.y && yPrev < o.y+o.hauteur) { // face de gauche
            double coefficientDirecteur = (y - yPrev)/(x - xPrev);
            double ordonneOrigine = y  - ( (y - yPrev)/(x - xPrev) ) * x;

            xCollision = o.x;
            yCollision = ( coefficientDirecteur * o.x + ordonneOrigine);
            vx = - vx;
            t = 1;
        }
    }
}