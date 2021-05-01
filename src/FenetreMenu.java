import javax.swing.*;
import java.awt.*;
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
        setLocation(200, 150);
        setVisible(true);
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
        e.getComponent().setBounds(r.x, r.y, r.width, r.width*H/W);
        System.out.println(mainPanel.getHeight()+" "+mainPanel.getWidth());

        int x = e.getComponent().getWidth();
        int y = e.getComponent().getHeight();
        double t = x*x + y*y;
        int taille = (int)(30.0+t/100000);
        System.out.println(taille);

        mainPanel.title.setBounds((int)(x/2-50), (int)(y/20), 200,100);
        mainPanel.title.setFont(new Font("Arial", Font.BOLD, taille));
    }
}
