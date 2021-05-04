import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PanelMenu extends JPanel implements ActionListener{

    JButton lvl1;
    JButton lvl2;
    JButton lvl3;
    JButton lvl4;
    JButton free;

    JLabel title;

    protected FenetreJeu fenetre1;
    protected FenetreJeu fenetre2;
    protected FenetreJeu fenetre3;
    protected FenetreJeu fenetre4;
    protected FenetreJeu fenetreFree;

    protected Son musique;
    protected Image background;

    public PanelMenu(){

        setBackgroundImage("ImageMenu.jpg");

        // Initialisation musique de fond
        musique = new Son("Music/8bitMoskau.wav");
        musique.clip.loop(Clip.LOOP_CONTINUOUSLY);

        setLayout(null);

        //Initialisation titre
        title = new JLabel("MENU");
        setTitle();

        //Initialisation des boutons
        lvl1 = new JButton("NIVEAU 1");
        lvl2 = new JButton("NIVEAU 2");
        lvl3 = new JButton("NIVEAU 3");
        lvl4 = new JButton("NIVEAU 4");
        free = new JButton("EDITER");


        lvl1.setOpaque(true);
        lvl2.setOpaque(true);
        lvl3.setOpaque(true);
        lvl4.setOpaque(true);
        free.setOpaque(true);

        lvl1.setBorderPainted(false);
        lvl2.setBorderPainted(false);
        lvl3.setBorderPainted(false);
        lvl4.setBorderPainted(false);
        free.setBorderPainted(false);


        setButton(lvl1,0,0);
        setButton(lvl2,0,150);
        setButton(lvl3,550,0);
        setButton(lvl4,550,150);
        setButton(free,275,300);

    }

    // Dessiner image du fond
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Image de found
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == lvl1) {
            //Création niveau 1
            musique.clip.stop();
            fenetre1 = new FenetreJeu(true, 1);
            fenetre1.setVisible(true);
        }

        if(e.getSource() == lvl2) {
            //Création niveau 2
            musique.clip.stop();
            fenetre2 = new FenetreJeu(true, 2);
            fenetre2.setVisible(true);
        }

        if(e.getSource() == lvl3) {
            //Création niveau 3
            musique.clip.stop();
            fenetre3 = new FenetreJeu(true, 3);
            fenetre3.setVisible(true);
        }

        if(e.getSource() == lvl4) {
            //Création niveau 4
            musique.clip.stop();
            fenetre4 = new FenetreJeu(true, 4);
            fenetre4.setVisible(true);
        }

        if(e.getSource() == free) {
            //Création niveau édition
            musique.clip.stop();
            fenetreFree = new FenetreJeu(true, 5);
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

    //Méthodes d'initialisation du titre et des boutons

    public void setTitle (){

        title.setForeground(Color.WHITE);
        title.setBounds(385, 175, 400,100);
        title.setFont(new Font("Arial", Font.BOLD, 80));
        add(title);
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
