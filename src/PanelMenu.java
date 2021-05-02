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

    protected FenetreJeu fenetre1;
    protected FenetreJeu fenetre2;
    protected FenetreJeu fenetre3;
    protected FenetreJeu fenetre4;
    protected FenetreJeu fenetreFree;

    public PanelMenu(){

        setBackgroundImage("ImageMenu.jpg");

        setLayout(null);

        title = new JLabel("MENU");
        setTitle();

        lvl1 = new JButton("NIVEAU 1");
        lvl2 = new JButton("NIVEAU 2");
        lvl3 = new JButton("NIVEAU 3");
        lvl4 = new JButton("NIVEAU 4");
        free = new JButton("EDITER");

        lvl1.addActionListener(this);
        lvl2.addActionListener(this);
        lvl3.addActionListener(this);
        lvl4.addActionListener(this);
        free.addActionListener(this);

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
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Image de found
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == lvl1) {
            fenetre1 = new FenetreJeu(true, 1);
            fenetre1.setVisible(true);
        }

        if(e.getSource() == lvl2) {
            fenetre2 = new FenetreJeu(false, 2);
            fenetre2.setVisible(true);
        }

        if(e.getSource() == lvl3) {
            fenetre3 = new FenetreJeu(false, 3);
            fenetre3.setVisible(true);
        }

        if(e.getSource() == lvl4) {
            fenetre4 = new FenetreJeu(false, 4);
            fenetre4.setVisible(true);
        }

        if(e.getSource() == free) {
            fenetreFree = new FenetreJeu(false, 5);
            fenetreFree.setVisible(true);
        }
    }

    public void setBackgroundImage(String backgroundFileName){
        String pathInFolder = "BackgroundImage/";
        try {
            background = ImageIO.read(getClass().getResourceAsStream(pathInFolder+backgroundFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTitle (){
        int width = getWidth();
        int height = getHeight();

        title.setForeground(Color.WHITE);
        title.setBounds((int)(385), 175, 400,100);
        title.setFont(new Font("Arial", Font.BOLD, 80));
    }

    public void setButton (JButton lvl, int indice){
        int width = getWidth();
        int height = getHeight();

        lvl.setBounds((int)(width/4),100,80,20);
        lvl.setBackground(Color.GREEN);
        lvl.setForeground(Color.WHITE);
    }
}
