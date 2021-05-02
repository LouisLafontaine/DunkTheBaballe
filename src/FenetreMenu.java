import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

public class FenetreMenu extends JFrame implements ComponentListener {
    //Attributs
    protected PanelMenu mainPanel;

    public FenetreMenu(){

        super("Menu jeu");
        mainPanel = new PanelMenu();
        add(mainPanel);
        addComponentListener(this);

        setSize(1000,1000);
        setLocation(200, 150);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

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

    public void maintainProportion(ComponentEvent e) {
        int W = mainPanel.background.getWidth(null);
        int H = mainPanel.background.getHeight(null);
        Rectangle r = e.getComponent().getBounds();
        e.getComponent().setBounds(r.x,r.y, r.width, r.width*H/W);
        mainPanel.setBounds(0, 0, r.width, r.width*H/W);
        mainPanel.setTitle();
    }
}
