import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Balle {
    // Attributs
    //======================================================================
    protected double xInit ; // pos x initiale
    protected double yInit ; // pos y initiale
    protected double x; // pos x
    protected double y; // pos y
    protected double xPrev; // pos x une étape plus tot
    protected double yPrev; // pos y une étape plus tot
    protected double xCollision; // pos x de la dernière collision
    protected double yCollision; // pos y de la dernière collision
    protected int d; // diamètre
    protected double vx; // vitesse selon x
    protected double vy; // vitesse selon y
    protected int t; // variable temps pour calcul de la trajectoire
    protected final double g; // constante gravité
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

    public void updatePosBalle(){
        xPrev =  x;
        yPrev = y;
        t++;
        x = xCollision+vx*t;
        y = 0.5*g*t*t + vy*t + yCollision;
    }

    public void resetPosBalle(Timer timer){
        timer.stop();
        x = xInit;
        y = yInit;
        xPrev =  xInit;
        yPrev = yInit;
        xCollision = xInit;
        yCollision = yInit;
        t = 0;

        // Son de reset
        Son resetSound = new Son("Sound/8bitBlipBlip.wav");
        resetSound.clip.start();
    }

    public boolean notInBounds(int largeurFenetre, int hauteurFenetre) {
        return ((x <= 0) || (x >= largeurFenetre) || (y< - hauteurFenetre/2.0) || (y >= hauteurFenetre));
    }

    public int distanceBalle(int x, int y){
        double dX = x - this.x;
        double dY = y - this.y;
        return (int)(Math.sqrt(dX*dX + dY*dY));
    }

    public boolean toucheBalle(int x, int y){
        return (distanceBalle(x,y) < d/2);
    }

    public void throwBalle(MouseEvent e, Timer timer) {
        double mouseSensibility = 0.03;
        vx = (x - e.getX()) * mouseSensibility;
        vy = (y - e.getY()) * mouseSensibility;
        timer.start();
    }

    // Méthodes Collisions
    public boolean hasCollided(Obstacle o) {
        boolean xOverlap = (this.x >= o.x) && (this.x <= o.x + o.largeur);
        boolean yOverlap = (this.y >= o.y) && (this.y <= o.y + o.hauteur);
        return (xOverlap && yOverlap);
    }

    public void solveCollision(Obstacle o) {

        // Son produit à l'impact
        Son impactSound = new Son("Sound/8bitImpactGround.wav");
        impactSound.clip.start();

        // Coefficient d'amortissement
        double amortissement = 0.9;

        /*
         * Afin de Résoudre une collision on a besoin de calculer le point où celle-ci a eu lieu.
         * Pour cela on calcule l'intersection entre l'approximation affine de la trajectoire de la boule et le bord de
         * l'obstacle touché. Cependant on ne peut pas calculer cette approximation affine, si (x - xPrev) = 0, car
         * alors on aurait une division par 0 dans le calcul du coefficient directeur.
         * D'où une séparation des cas où (x - xPrev != 0) et (x - xPrev = 0)
         */
        t = 0; // On recommence la trajectoire à partir du point de collision
        if(x - xPrev != 0){
            // Approximation affine de la trajectoire
            double coefficientDirecteur = (y - yPrev)/(x - xPrev);
            double ordonneOrigine = y  - ( (y - yPrev)/(x - xPrev) ) * x;

            // face de gauche
            if (xPrev < o.x && yPrev > o.y && yPrev < o.y+o.hauteur) {
                xCollision = o.x;
                yCollision = ( coefficientDirecteur * o.x + ordonneOrigine);
                vx = -amortissement * vx;
                vy = amortissement * vy;
            }
            // face de droite
            else if (xPrev > o.x + o.largeur && yPrev > o.y && yPrev < o.y+o.hauteur) {
                xCollision = o.x + o.largeur;
                yCollision = ( coefficientDirecteur * (o.x+ o.largeur) + ordonneOrigine);
                vx = -amortissement * vx;
                vy = amortissement * vy;

            }
            // face du haut
            else if (yPrev < o.y && xPrev > o.x && xPrev < o.x+o.largeur) {
                xCollision = (o.y - ordonneOrigine) / coefficientDirecteur;
                yCollision = o.y-1; // -1 car sinon la balle traverse l'obstacle
                vx = amortissement * vx;
                vy = -amortissement * Math.abs(vy);
                /* Du fait de la gravité, la balle peut toucher une surface horizontale même si vy est dirigé vers le
                * haut (négative). Alors, si on inversait vy à l'impact, celle-ci deviendrait positive, ce qui
                * permettrait à la balle de traverser l'obstacle. D'où la nécessité de - Math.abs() pour s'assurer que
                * vy pointe vers le haut */
            }
            // face du bas
            else if (yPrev > o.y + o.hauteur && xPrev > o.x && xPrev < o.x+o.largeur) {
                xCollision = (o.y + o.hauteur - ordonneOrigine) / coefficientDirecteur;
                yCollision = o.y + o.hauteur;
                vx = amortissement * vx;
                vy = -amortissement * vy;
            }
        }
        else { // (x - xPrev = 0)
            // face du haut
            if(yPrev > o.y && xPrev > o.x && xPrev < o.x+o.largeur){
                yCollision = o.y + o.hauteur;
                vy = -amortissement * vy;
            }
            // Face du bas
            else{
                yCollision = o.y -1 ;
                vy = -amortissement * Math.abs(vy); // meme raison que ci-dessus pour face du haut lorsque x-xPrev != 0
            }
        }
    }
}