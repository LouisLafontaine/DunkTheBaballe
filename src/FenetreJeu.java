import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class FenetreJeu extends JFrame{
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


        setSize(1500,1000);
        setLocation(250, 50);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}