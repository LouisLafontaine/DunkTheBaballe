import javax.swing.*;

public class FenetreMenu extends JFrame{
    // Attributs
    protected PanelMenu mainPanel; // Panel du menu

    // Constructeur
    public FenetreMenu(){
        super("Menu jeu");
        mainPanel = new PanelMenu(this);
        add(mainPanel);

        // taille de la fenêtre identique à celle de l'image
        setSize(2*mainPanel.background.getCurrentFrame().getWidth(),2*mainPanel.background.getCurrentFrame().getHeight());

        setResizable(false);
        setLocation(350, 150);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
