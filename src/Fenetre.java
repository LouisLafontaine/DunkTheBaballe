import javax.swing.*;

public class Fenetre extends JFrame {
    // Attributs
    protected PanelJeu mainPanel;

    // Constructeur
    public Fenetre(){
        super("Vise le trou");

        mainPanel = new PanelJeu();
        add(mainPanel);

        setSize(700, 500);
        setLocation(300, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
