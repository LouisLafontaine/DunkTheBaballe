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
        y = 0.5*g*t*t +vy*t + yCollision;
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
        boolean xOverlap = (this.x > o.x) && (this.x < o.x + o.largeur);
        boolean yOverlap = (this.y > o.y) && (this.y < o.y + o.hauteur);
        return (xOverlap && yOverlap);
    }

    public void solveCollision(Obstacle o) {
        double prevStep = 1;
        double xPrev = xCollision+vx*(t-prevStep);
        double yPrev = 0.5*g*t*t +vy*(t-prevStep) + yCollision;

        double coefficientDirecteur = (y - yPrev)/(x - xPrev);
        double ordonneOrigine = y  - ( (y - yPrev)/(x - xPrev) ) * x;


        if (xPrev < o.x && yPrev > o.y && yPrev < o.y+o.hauteur) { // face de gauche
            xCollision = o.x;
            yCollision = ( coefficientDirecteur * o.x + ordonneOrigine);
            vx = -4.0/5.0*vx;
            System.out.println("gauche"); // TEST
            t = 1;
        }
        else if (xPrev > o.x + o.largeur && yPrev > o.y && yPrev < o.y+o.hauteur) { // face de droite
            xCollision = o.x + o.largeur;
            yCollision = ( coefficientDirecteur * (o.x+ o.largeur) + ordonneOrigine);
            vx = -4.0/5.0* vx;
            System.out.println("droite"); // TEST
            t = 1;

        } else if(Math.abs(vy) > 3){
            if (yPrev < o.y && xPrev > o.x && xPrev < o.x+o.largeur) { // face du haut
                xCollision = (o.y - ordonneOrigine) / coefficientDirecteur;
                yCollision = o.y;
                System.out.println("dessus"); // TEST
                vy = -4.0/5.0* vy;
                t = 1;
            }
            else if (yPrev > o.y + o.hauteur && xPrev > o.x && xPrev < o.x+o.largeur) { // face du bas
                xCollision = (o.y + o.hauteur - ordonneOrigine) / coefficientDirecteur;
                yCollision = o.y + o.hauteur;
                System.out.println("dessous"); // TEST
                t = 1;
                vy = -4.0/5.0* vy;
            }
        } else{
            y = o.y;
            xCollision = x;
            yCollision = o.y;
            t=0;
        }

        // TEST
        System.out.println(vy);
        System.out.println(y);
        System.out.println(yPrev);
        System.out.println(o.y);
        System.out.println(yPrev < o.y);
        System.out.println(xPrev > o.x);
        System.out.println(xPrev < o.x+o.largeur);
    }
}