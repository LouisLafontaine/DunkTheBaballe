import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class PanelJeu extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    // Attributs
    //======================================================================
    protected final Balle balle;
    protected Timer timer;
    Son musique;
    protected int lastClickX; // enregistre la pos x du dernier click
    protected int lastClickY; // enregistre la pos y du dernier click
    protected int clickX; // enregistre la pos x actuelle du click
    protected int clickY; // enregistre la pos x actuelle du click
    boolean clicking; // true si en train de clicker
    Image background;
    Obstacle obstacle;
    Obstacle obstacle2;
    Obstacle obstacle3;
    Obstacle obstacle4;

    // Constructeur
    //======================================================================
    public PanelJeu(){
        balle = new Balle(300,250,25,0, 0, "fireBall.png");
        obstacle = new Obstacle(100,50, 300, 20);
        obstacle2 = new Obstacle(400,50, 20, 300);
        obstacle3 = new Obstacle(100,350, 320, 20);
        obstacle4 = new Obstacle(100,50, 20, 300);
        int fps = 120;
        timer = new Timer(1000/ fps, this);

        musique = new Son("Musique_pokemon.wav");
        musique.clip.loop(Clip.LOOP_CONTINUOUSLY);

        setBackgroundImage("FantasyForest.png");

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // Dessin
    //======================================================================
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        // Background Image
        g.drawImage(background,0,0, this.getWidth(), this.getHeight(), null);

        // Trait force
        tracerSegment(g);

        // Balle
        balle.drawBalle(g);

        // Obstacle
        obstacle.drawObstacle(g);
        obstacle2.drawObstacle(g);
        obstacle3.drawObstacle(g);
        obstacle4.drawObstacle(g);
        }

    // Animation
    //======================================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == timer){
            balle.updatePosBalle(this.getWidth(), this.getHeight(), timer);
            if (balle.hasCollided(obstacle)){
                balle.solveCollision(obstacle);
            }else if (balle.hasCollided(obstacle2)){
                balle.solveCollision(obstacle2);
            }else if (balle.hasCollided(obstacle3)){
                System.out.println("hasCollided");
                balle.solveCollision(obstacle3);
            }else if (balle.hasCollided(obstacle4)){
                System.out.println("hasCollided");
                balle.solveCollision(obstacle4);
            }
            repaint();
        }
    }

    // MouseListener interface methods
    //======================================================================
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        setLastClickOn(e.getX(),e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setLastClickOff();
        balle.ifClickedThrowBalle(e, timer,lastClickX, lastClickY);
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    // MouseMotionListener interface methods
    //======================================================================
    @Override
    public void mouseDragged(MouseEvent e) {
        draggingOnBalle(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    // Méthodes
    //======================================================================
    public void setBackgroundImage(String backgroundFileName){
        String pathInFolder = "Ressources/BackgroundImage/";
        try {
            background = ImageIO.read(new File(pathInFolder + backgroundFileName).getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLastClickOn(int x, int y) {
        lastClickX = x;
        lastClickY = y;
        clicking = true;
    }

    public void setLastClickOff() {
        clicking = false;
    }

    public void draggingOnBalle(MouseEvent e) {
        if(balle.toucheBalle(lastClickX, lastClickY) && clicking){
            clickX = e.getX();
            clickY = e.getY();
            repaint();
        }
    }

    public void tracerSegment(Graphics g) {
        if(clicking && balle.toucheBalle(lastClickX, lastClickY)) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.green);
            g2d.drawLine(clickX, clickY, (int)balle.x, (int)balle.y);
        }
    }
}