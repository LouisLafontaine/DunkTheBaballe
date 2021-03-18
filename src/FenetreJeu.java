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
    public FenetreJeu(boolean playMusic){
        super("Vise le trou");

        mainPanel = new PanelJeu();
        add(mainPanel);
        if(!playMusic) mainPanel.musique.clip.stop();

        addComponentListener(this);

        setSize(600,600);
        setLocation(300, 200);
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

//        //Maintenir position de la balle
//        mainPanel.balle.xInit = r.width/5.0;
//        mainPanel.balle.yInit = r.height/2.0;
//        mainPanel.balle.resetPosBalle();
//        mainPanel.repaint();
    }
}