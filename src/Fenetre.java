import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class Fenetre extends JFrame implements ComponentListener {
    // Attributs
    //======================================================================
    protected PanelJeu mainPanel;

    // Constructeur
    //======================================================================
    public Fenetre(boolean playMusic){
        super("Vise le trou");

        mainPanel = new PanelJeu();
        add(mainPanel);
        if(playMusic) mainPanel.musique.clip.start();

        addComponentListener(this);

        setSize(500,500);
        setLocation(300, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // ComponentListener interface methods
    //======================================================================
    @Override
    public void componentResized(ComponentEvent e) {
        int W = mainPanel.img.getWidth(null);
        int H = mainPanel.img.getHeight(null);
        Rectangle b = e.getComponent().getBounds();
        e.getComponent().setBounds(b.x, b.y, b.width, b.width*H/W);
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
