import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class FenetreJeu extends JFrame implements ComponentListener {
    // Attributs
    //======================================================================
    protected PanelJeu mainPanel;

    // Constructeur
    //======================================================================
    public FenetreJeu(boolean playMusic, int indice){
        super("Vise le trou");

        mainPanel = new PanelJeu(indice);
        mainPanel.setFocusable(true); // Sinon la KeyListener interface ne marche pas
        add(mainPanel);
        if(!playMusic) mainPanel.musique.clip.stop();

        addComponentListener(this);

        setSize(2000,1000);
        setLocation(0, 0);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // ComponentListener interface methods
    //======================================================================
    @Override
    public void componentResized(ComponentEvent e) {
        maintainProportion(e);
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

    // Méthodes
    //======================================================================
    public void maintainProportion(ComponentEvent e) {
        int W = mainPanel.background.getWidth(null);
        int H = mainPanel.background.getHeight(null);
        Rectangle r = e.getComponent().getBounds();
        e.getComponent().setBounds(r.x, r.y, r.width, r.width*H/W);
    }
}