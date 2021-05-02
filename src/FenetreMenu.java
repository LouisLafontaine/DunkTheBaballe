import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class FenetreMenu extends JFrame implements ComponentListener {
    //Attributs
    protected PanelMenu mainPanel;

    public FenetreMenu(){

        super("Menu jeu");
        mainPanel = new PanelMenu();
        add(mainPanel);
        addComponentListener(this);

        setSize(1000,1000);
        setLocation(500, 50);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void componentResized(ComponentEvent e) {
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
