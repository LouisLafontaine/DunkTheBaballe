import javax.swing.*;

public class Fenetre extends JFrame {
    // Attributs
    protected PanelJeu mainPanel;

    // Constructeur
    public Fenetre(boolean playMusic){
        super("Vise le trou");

        mainPanel = new PanelJeu();
        add(mainPanel);
        if(playMusic) mainPanel.musique.clip.start();

        setSize(700, 500);
        setLocation(300, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
