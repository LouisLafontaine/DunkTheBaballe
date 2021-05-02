import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

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
    protected Son musique;

    public PanelMenu(){

        setBackgroundImage("ImageMenu.jpg");

        // Initialisation musique de fond
        musique = new Son("Music/8bitMoskau.wav");
        musique.clip.loop(Clip.LOOP_CONTINUOUSLY);

        setLayout(null);

        title = new JLabel("MENU");
        setTitle();

        lvl1 = new JButton("NIVEAU 1");
        lvl2 = new JButton("NIVEAU 2");
        lvl3 = new JButton("NIVEAU 3");
        lvl4 = new JButton("NIVEAU 4");
        free = new JButton("EDITER");


        setButton(lvl1,0,0);
        setButton(lvl2,0,150);
        setButton(lvl3,550,0);
        setButton(lvl4,550,150);
        setButton(free,275,300);

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Image de found
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == lvl1) {
            musique.clip.stop();
            fenetre1 = new FenetreJeu(true, 1); //Création niveau 1
            fenetre1.setVisible(true);
        }

        if(e.getSource() == lvl2) {
            musique.clip.stop();
            fenetre2 = new FenetreJeu(true, 2); //Création niveau 2
            fenetre2.setVisible(true);
        }

        if(e.getSource() == lvl3) {
            musique.clip.stop();
            fenetre3 = new FenetreJeu(true, 3); //Création niveau 3
            fenetre3.setVisible(true);
        }

        if(e.getSource() == lvl4) {
            musique.clip.stop();
            fenetre4 = new FenetreJeu(true, 4); //Création niveau 4
            fenetre4.setVisible(true);
        }

        if(e.getSource() == free) {
            musique.clip.stop();
            fenetreFree = new FenetreJeu(true, 5); //Création niveau édition
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

    public void setTitle (){ //Initialiser le texte du titre

        title.setForeground(Color.WHITE);
        title.setBounds((int)(385), 175, 400,100);
        title.setFont(new Font("Arial", Font.BOLD, 80));
    }

    public void setButton (JButton button, int indiceX, int indiceY) { //Initialiser les boutons

        button.addActionListener(this);
        button.setBackground(new Color (170,0, 100));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 35));
        button.setSize(250, 75);
        button.setLocation(100+indiceX,400+indiceY);
        add(button);
    }
}
