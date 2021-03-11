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
        balle = new Balle(100,250,20,5);

        int fps = 60;
        timer = new Timer(1000/ fps, this);

        addMouseListener(this);
    }

    // Méthodes
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.red);
        g.fillRect(0,0, this.getWidth(), this.getHeight());
        balle.drawBalle(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == timer){
            balle.updatePosBalle(this.getWidth(), this.getHeight());
            repaint();
        }
    }

    // Mouse Listener interface method
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        timer.start();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        timer.stop();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
