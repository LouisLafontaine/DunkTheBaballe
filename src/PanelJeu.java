import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PanelJeu extends JPanel implements ActionListener, MouseListener{
    // Attributs
    private final Balle balle;
    protected Timer timer;

    // Constructeur
    public PanelJeu(){
        balle = new Balle(100,250,20,0, 0);
        int fps = 60;
        timer = new Timer(1000/ fps, this);

        addMouseListener(this);
    }

    // Dessin
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.red);
        g.fillRect(0,0, this.getWidth(), this.getHeight());
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
        timer.stop();
        balle.resetPosBalle();
        repaint();
    }
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
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
}
