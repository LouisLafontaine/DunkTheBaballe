import javax.swing.*;

public class FenetreMenu extends JFrame{
    // Attributs
    protected PanelMenu mainPanel; // Panel du menu

    // Constructeur
    public FenetreMenu(){
        super("Menu jeu");
        mainPanel = new PanelMenu(this);
        add(mainPanel);

        setSize(1000,1000);
        setResizable(false);
        setLocation(500, 50);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
