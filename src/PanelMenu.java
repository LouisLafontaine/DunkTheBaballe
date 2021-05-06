import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

public class PanelMenu extends JPanel implements ActionListener{
    // Attributs
    //======================================================================
    final FenetreMenu fenetreMenu;
    final JLabel title;
    final JButton[] buttons;
    protected Son musique;
    Timer animationTimer;
    Animated background;

    // Constructeur
    //======================================================================
    public PanelMenu(FenetreMenu fenetreMenu){
        this.fenetreMenu = fenetreMenu;

        // Animated Background
        background = new Animated("Background/JapanCity.png",0,0,25,5,30, true);
        animationTimer = new Timer(1000/30, this);

        // Initialisation musique de fond
        musique = new Son("Music/8bitMoskau.wav");
        musique.clip.loop(Clip.LOOP_CONTINUOUSLY);

        setLayout(null);

        //Initialisation titre
        title = new JLabel("MENU");
        setTitle();

        //Initialisation des boutons
        buttons = new JButton[5];
        for(int i = 0; i <= 4; i++) {
            buttons[i] = new JButton("NIVEAU " + (i+1));
        }
        buttons[4] = new JButton("EDITEUR");

        setButton(buttons[0],0,0);
        setButton(buttons[1],0,150);
        setButton(buttons[2],550,0);
        setButton(buttons[3],550,150);
        setButton(buttons[4],275,300);

        animationTimer.start();
    }

    // Méthodes Initialisation
    //======================================================================
    public void setTitle (){
        title.setForeground(Color.white);
        title.setBounds(530, 120, 400,110);
        Font font = importFont("Font/PixelFont.ttf", 100);
        title.setFont(font);
        add(title);
    }

    public void setButton (JButton button, int indiceX, int indiceY) { //Initialiser les boutons

        Font font = importFont("Font/PixelFont.ttf", 40);
        button.addActionListener(this);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setBackground(new Color (170,0, 100));
        button.setForeground(Color.WHITE);
        button.setFont(font);
        button.setSize(250, 70);
        button.setLocation(275+indiceX,250+indiceY);
        add(button);
    }

    private Font importFont(String filename, int size) {
        Font maFont;
        try {
            InputStream fontstream = getClass().getResourceAsStream(filename);
            maFont  = Font.createFont(Font.TRUETYPE_FONT, fontstream);
            return maFont.deriveFont(Font.BOLD,(float)size);
        } catch (Exception e) {
            System.out.println("problème Police PixelFont");
            e.printStackTrace();
            return new Font("Arial", Font.BOLD, size);
        }
    }

    // Dessin
    //======================================================================
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Image de found
        g.drawImage(background.getCurrentFrame(), 0, 0, this.getWidth(), this.getHeight(), null);
    }

    // Interaction
    //======================================================================
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < buttons.length; i++) {
            if (e.getSource() == buttons[i]) {
                musique.clip.stop();
                new FenetreJeu(fenetreMenu, true, i+1);
                fenetreMenu.setVisible(false);
            }
        }
        repaint();
    }

    // Méthodes
    //======================================================================

}
