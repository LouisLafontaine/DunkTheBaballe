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

    JLabel title;

    protected Image background;

    public PanelMenu(){

        setBackgroundImage("DarkForest.jpg");

        title = new JLabel("MENU");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setBounds(100,20,100,100);
        title.setForeground(Color.WHITE);

        lvl1 = new JButton("NIVEAU 1");
        lvl2 = new JButton("NIVEAU 2");
        lvl3 = new JButton("NIVEAU 3");
        lvl4 = new JButton("NIVEAU 4");
        free = new JButton("EDITER");

        lvl1.setBounds(20,100,80,20);
        lvl2.setBounds(20,120,80,20);
        lvl3.setBounds(20,130,80,20);
        lvl4.setBounds(20,140,80,20);
        free.setBounds(20,150,80,20);

        lvl1.setBackground(Color.GREEN);
        lvl2.setBackground(Color.GREEN);
        lvl3.setBackground(Color.GREEN);
        lvl4.setBackground(Color.GREEN);
        free.setBackground(Color.GREEN);

        lvl1.setForeground(Color.WHITE);
        lvl2.setForeground(Color.WHITE);
        lvl3.setForeground(Color.WHITE);
        lvl4.setForeground(Color.WHITE);
        free.setForeground(Color.WHITE);

        add(title);
        add(lvl1);
        add(lvl2);
        add(lvl3);
        add(lvl4);
        add(free);

        System.out.println(getHeight()+" "+getWidth());
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
