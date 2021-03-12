import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanelJeu extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    // Attributs
    private final Balle balle;
    protected Timer timer;
    Musique musique;
    protected int lastClickX; // enregistre la pos x du dernier click
    protected int lastClickY; // enregistre la pos y du dernier click
    boolean clicking;

    // Constructeur
    public PanelJeu(){
        balle = new Balle(100,250,20,0, 0);
        int fps = 60;
        timer = new Timer(1000/ fps, this);

        musique = new Musique("8bitWildBattle.wav");
        musique.clip.start();

        lastClickX = 0;
        lastClickY = 0;
        clicking = false;

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // Dessin
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        // Background
        g.setColor(Color.red);
        g.fillRect(0,0, this.getWidth(), this.getHeight());

        // Trait force
        g.setColor(Color.BLACK);
        if(clicking && balle.toucheBalle(lastClickX, lastClickY)){
            g.drawLine(lastClickX, lastClickY, balle.x, balle.y);
        }

        // Balle
        balle.drawBalle(g);
    }

    // Animation
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == timer){
            balle.updatePosBalle(this.getWidth(), this.getHeight(), timer);
            repaint();
        }
    }

    // Mouse Listener interface methods
    @Override
    public void mouseClicked(MouseEvent e) {
        setLastClickOn(e.getX(),e.getY());
        timer.stop();
        balle.resetPosBalle();
        repaint();
    }
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setLastClickOff();
        double mouseSensibility = 0.1;
        balle.vx = (balle.x - e.getX()) * mouseSensibility;
        balle.vy = (balle.y - e.getY()) * mouseSensibility;
        timer.start();
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void setLastClickOn(int x, int y) {
        lastClickX = x;
        lastClickY = y;
    }

    public void setLastClickOff() {
        clicking = false;
    }
}
