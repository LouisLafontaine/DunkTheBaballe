import javax.swing.*;
import java.awt.*;

public class JPanelJeu extends JPanel {
    public JPanelJeu(){
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.red);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
    }
}
