import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class PanelMenu extends JPanel implements ActionListener{

    JButton lvl1;
    JButton lvl2;
    JButton lvl3;
    JButton lvl4;
    JButton free;

    JTextField title;

    protected Image background;

    public PanelMenu(){

        setBackgroundImage("ImageMenu.jpg");
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Image de found
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    public void actionPerformed(ActionEvent e) {
    }

    public void setBackgroundImage(String backgroundFileName){
        String pathInFolder = "BackgroundImage/";
        try {
            background = ImageIO.read(getClass().getResourceAsStream(pathInFolder+backgroundFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
