import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class PanelJeu extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    // Attributs
    //======================================================================
    private final Balle balle;
    protected Timer timer;
    Musique musique;
    protected int lastClickX; // enregistre la pos x du dernier click
    protected int lastClickY; // enregistre la pos y du dernier click
    protected int clickX; // enregistre la pos x actuelle du click
    protected int clickY; // enregistre la pos x actuelle du click
    boolean clicking; // true si en train de clicker
    Image background;

    // Constructeur
    //======================================================================
    public PanelJeu(){
        balle = new Balle(100,250,30,0, 0);
        int fps = 60;
        timer = new Timer(1000/ fps, this);

        musique = new Musique("8bitWildBattle.wav");
        musique.clip.start();

        setBackgroundImage("FantasyForest.png");

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // Dessin
    //======================================================================
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        // Background
        g.setColor(Color.red);
        g.fillRect(0,0, this.getWidth(), this.getHeight());

        // Background
        g.drawImage(background,0,0, this.getWidth(), this.getHeight(), null);

        // Trait force
        tracerSegment(g);

        // Balle
        balle.drawBalle(g);
        }

    // Animation
    //======================================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == timer){
            balle.updatePosBalle(this.getWidth(), this.getHeight(), timer);
            repaint();
        }
    }

    // Mouse Listener interface methods
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
        ifClikedThrowBalle(e);
    }

    // Méthodes
    //======================================================================
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void setBackgroundImage(String backgroundFileName){
        String pathInFolder = "Ressources/BackgroundImage/";
        try {
            background = ImageIO.read(new File(pathInFolder + backgroundFileName).getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        draggingOnBalle(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

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
            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(Color.green);
            g2d.drawLine(clickX, clickY, balle.x, balle.y);
        }
    }

    public void ifClikedThrowBalle(MouseEvent e) {
        if(balle.toucheBalle(lastClickX,lastClickY)){
            double mouseSensibility = 0.1;
            balle.vx = (balle.x - e.getX()) * mouseSensibility;
            balle.vy = (balle.y - e.getY()) * mouseSensibility;
            timer.start();
            repaint();
        }
    }
}
