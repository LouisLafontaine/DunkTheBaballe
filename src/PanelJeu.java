import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PanelJeu extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener{
    // Attributs
    //======================================================================
    protected final Balle balle;
    protected Timer timer;
    protected Son musique;
    protected int lastClickX; // enregistre la pos x du dernier click
    protected int lastClickY; // enregistre la pos y du dernier click
    protected int clickX; // enregistre la pos x actuelle du click si la souris est en train de clicker sur la balle
    protected int clickY; // enregistre la pos x actuelle du click si la souris est en train de clicker sur la balle
    protected boolean clicking; // true si en train de clicker
    protected Image background; // image de fond
    protected ArrayList<Obstacle> obstacles; // tableau d'obstacle

    // Constructeur
    //======================================================================
    public PanelJeu(){
        // Initialisation de la balle
        balle = new Balle(300,250,25,0, 0, "fireBall.png");

        // Initialisation des obstacles
        Obstacle obstacle1 = new Obstacle(100,50, 300, 20);
        Obstacle obstacle2 = new Obstacle(380,90, 20, 200);
        Obstacle obstacle3 = new Obstacle(100,90, 20, 200);
        Obstacle obstacle4 = new Obstacle(100,310, 300, 20);
        Obstacle obstacle5 = new Obstacle(200,150, 80, 80);

        // Ajout des obstacles au tableau d'obstacles
        obstacles = new ArrayList<>();
        obstacles.add(obstacle1);
        obstacles.add(obstacle2);
        obstacles.add(obstacle3);
        obstacles.add(obstacle4);
        obstacles.add(obstacle5);

        // Initialisation timer pour animation
        int fps = 120;
        timer = new Timer(1000/ fps, this);

        // Initialisation musique de fond
        musique = new Son("Music/8bitWildBattle.wav");
        musique.clip.loop(Clip.LOOP_CONTINUOUSLY);

        // Initialisation image de fond
        setBackgroundImage("FantasyForest.png");

        // Ajout interface
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
    }

    // Dessin
    //======================================================================
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        // Image de found
        g.drawImage(background,0,0, this.getWidth(), this.getHeight(), null);

        // Trait entre la balle et la souris au moment du lancer
        tracerSegment(g);

        // Balle
        balle.drawBalle(g);

        // Obstacles
        for(Obstacle o : obstacles){
            o.drawObstacle(g);
        }
    }

    // Animation
    //======================================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == timer){
            balle.updatePosBalle();
            if(balle.notInBounds(getWidth(),getHeight())){
                balle.resetPosBalle(timer);
            }
            for (Obstacle o : obstacles){
                if (balle.hasCollided(o)) {
                    balle.solveCollision(o);
                    break;
                }
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
        if(balle.toucheBalle(lastClickX,lastClickY)) {
            balle.throwBalle(e, timer);
            repaint();
        }
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

    // KeyListener interface methods
    //======================================================================
    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            balle.resetPosBalle(timer);
            repaint();
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {

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
        if(clicking && balle.toucheBalle(lastClickX, lastClickY)){
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