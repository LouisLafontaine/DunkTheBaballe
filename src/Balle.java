import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Balle {
    // Attributs
    //======================================================================
    protected int x; // pos x
    protected int y; // pos y
    protected int xInit ; // pos x initiale
    protected int yInit ; // pos y initiale
    protected int d; // diamètre
    protected double vx; // vitesse selon x
    protected double vy; // vitesse selon y
    protected int t;
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
        g.drawImage(characterImage, x-d/2, y-d/2,d,d,null);
    }

    public void updatePosBalle(int largeurFenetre, int hauteurFenetre, Timer timer){
        double g = 0.1;
        t++;
        x = (int) (xInit+vx*t);
        y = (int) (0.5*g*t*t + vy*t + yInit);
        if(notInBounds(largeurFenetre,hauteurFenetre)){
            timer.stop();
            resetPosBalle();
        }
    }

    public void resetPosBalle(){
        x = xInit;
        y = yInit;
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
}