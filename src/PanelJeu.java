import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class PanelJeu extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener{
    // Attributs
    //======================================================================
    protected  Balle balle;
    protected Son musique; // Musique de fond
    protected Panier panier; // Panier de victoire
    protected ArrayList<Obstacle> obstacles; // tableau d'obstacle
    protected Image background; // image de fond
    LinkedList<Animated> animatedItems; // Liste des animations

    protected Timer gameLoopTimer; // Timer pour indiquer le nombre de repaint par seconde
    protected Timer timeWinSound; // Timer pour relancer la musique de fond après le son de victoire

    protected int lastClickX; // enregistre la pos x du dernier click
    protected int lastClickY; // enregistre la pos y du dernier click
    protected int clickX; // enregistre la pos x actuelle du click quand dragged
    protected int clickY; // enregistre la pos x actuelle du click quand dragged
    protected int indice; // Indice du niveau sélectionné
    protected int temps; // Temps s'écoulant durant le son de victoire

    protected boolean clicking; // true si en train de clicker
    protected boolean pressingKey_Q;



    // Constructeur
    //======================================================================
    public PanelJeu(int i){

        setLayout(null);

        //Indice de niveau
        indice = i;

        // Initialisation image de fond
        setBackgroundImage("FantasyForest.png");

        // Initialisation liste des obstacles
        obstacles = new ArrayList<>();

        // Initialisation du niveau choisi
        setLvl();

        // Initialisation tableau objets animés
        animatedItems = new LinkedList<>();

        // Initialisation gameLoopTimer pour animation
        int fps = 120;
        gameLoopTimer = new Timer(1000/ fps, this);
        gameLoopTimer.start();

        //Initialisation TimeWinSound pour relancer la musique à la fin du son de victoire
        timeWinSound = new Timer(1000, this);
        temps = 0;

        // Initialisation musique de fond
        musique = new Son("Music/Pokemon.wav");
        musique.clip.loop(Clip.LOOP_CONTINUOUSLY);

        // Ajout interface
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
    }

    // Dessin
    //======================================================================
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        // Image de found
        g.drawImage(background,0,0, this.getWidth(), this.getHeight(), null);

        // Trait entre la balle et la souris au moment du lancer
        tracerSegment(g);

        // Balle
        balle.drawBalle(g);

        // Panier
        panier.drawPanier(g);

        // Obstacles
        for(Obstacle o : obstacles){
            o.drawObstacle(g);
        }

        g.setColor(Color.black);
        if(pressingKey_Q) g.fillRect(lastClickX, lastClickY, Math.abs(clickX-lastClickX), Math.abs(clickY-lastClickY));

        for(Animated animation : animatedItems){
            g.drawImage(animation.getCurrentFrame(), animation.x, animation.y, null);
        }
    }

    // Animation
    //======================================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == gameLoopTimer){
            animatedItems.removeIf(animation -> animation.maxPlayCounter == 0 );
            if(balle.moving){
                balle.updatePosBalle();
                balle.checkSolveNotInBounds(getWidth(),getHeight());
                balle.checkSolveCollisions(obstacles);
                checkSolveWin();
                for(Obstacle o : obstacles){
                    if(balle.hasCollided(o)){
                        animatedItems.add(new Animated("AnimationTest/explosion2.png",(int)balle.x-32,(int)balle.y-64,1,8,0, 16,1,true));
                        break;
                    }
                }
            }
            repaint();
        }
        if (balle.balleBloquee()){
            balle.resetPosBalle(true);
        }

        if(e.getSource() == timeWinSound){ //temps qui s'écoule après que le son de victoire commence
            temps++;
            if (temps > 5){
                temps = 0;
                timeWinSound.stop();
                musique.clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }
    }

    // MouseListener interface methods
    //======================================================================
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        setLastClickOn(e.getX(),e.getY());
        chargingAnimation();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setLastClickOff();
        if(balle.toucheBalle(lastClickX,lastClickY)) {
            balle.throwBalle(e);
            repaint();
        }
        animatedItems.removeLast();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    // MouseMotionListener interface methods
    //======================================================================
    @Override
    public void mouseDragged(MouseEvent e) {
        draggingOnBalle();
        clickX = e.getX();
        clickY = e.getY();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    // KeyListener interface methods
    //======================================================================
    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyPressed(KeyEvent e) {
        pressingSpaceBarToReset(e);
        ifSetPressingKey_Q_On(e);
        if(e.getKeyCode() == KeyEvent.VK_W && indice == 5){
            obstacles.clear();
            repaint();
        }
        if(e.getKeyCode() == KeyEvent.VK_A && indice == 5){
            balle.xInit = clickX;
            balle.yInit = clickY;
            balle.x = clickX;
            balle.y = clickY;
            balle.xCollision = clickX;
            balle.yCollision = clickY;
            repaint();
        }
        if(e.getKeyCode() == KeyEvent.VK_P && indice == 5) {
            obstacles.remove(obstacles.size()-1);
            repaint();
        }
        if(e.getKeyCode() == KeyEvent.VK_R) musique.clip.start();

        if(e.getKeyCode() == KeyEvent.VK_ENTER) animatedItems.clear();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(pressingKey_Q) addNewObstacle();
        ifSetPressingKey_Q_Off(e);
    }

    // Méthodes
    //======================================================================
    public void setBackgroundImage(String backgroundFileName){
        String pathInFolder = "BackgroundImage/";
        try {
            background = ImageIO.read(getClass().getResourceAsStream(pathInFolder+backgroundFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLastClickOn(int x, int y) {
        lastClickX = x;
        lastClickY = y;
        clicking = true;
    }

    public void setLastClickOff() {
        clicking = false;
    }

    private void pressingSpaceBarToReset(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            balle.resetPosBalle(true);
            repaint();
        }
    }

    public void draggingOnBalle() {
        if(clicking && balle.toucheBalle(lastClickX, lastClickY)){
            repaint();
        }
    }

    public void tracerSegment(Graphics g) {
        if(clicking && balle.toucheBalle(lastClickX, lastClickY)) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.green);
            g2d.drawLine(clickX, clickY, (int)balle.x, (int)balle.y);
        }
    }

    public void checkSolveWin() {
        if(balle.hasCollided(panier)){
            System.out.println("gagné");
            Son winSound = new Son("Sound/8bitWin.wav");
            musique.clip.stop();
            winSound.clip.start();
            timeWinSound.start();
            balle.resetPosBalle(false);
        }
    }

    private void addNewObstacle() {
        obstacles.add(new Obstacle(lastClickX,lastClickY,Math.abs(clickX - lastClickX),Math.abs(clickY-lastClickY)));
    }

    // Key pressed tracking
    public void ifSetPressingKey_Q_On(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_Q) pressingKey_Q = true;
    }

    public void ifSetPressingKey_Q_Off(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_Q) pressingKey_Q = false;
    }

    public void chargingAnimation() {
        if(balle.toucheBalle(lastClickX, lastClickY)) {
            animatedItems.add(new Animated("AnimationTest/flameCircle.png",(int)balle.xInit-50,(int)balle.yInit-55,7,7,8, 30,-1,true));
        }
    }

    // Méthode afin de placer les différents objets sur la fenêtre en fonction du niveau
    public void setLvl() {
        if (indice == 1) { // Niveau 1

            balle = new Balle(200, 500);

            panier = new Panier(1400, 500);

            Obstacle obstacle11 = new Obstacle(730,100, 40, 500);
            Obstacle obstacle12 = new Obstacle(350,750, 800, 40);
            Obstacle obstacle13 = new Obstacle(770,100, 200, 40);
            Obstacle obstacle14 = new Obstacle(1075,625, 300, 40);

            obstacles.add(obstacle11);
            obstacles.add(obstacle12);
            obstacles.add(obstacle13);
            obstacles.add(obstacle14);
        }

        if (indice == 2) { // Niveau 2

            balle = new Balle(200, 200);

            panier = new Panier(1400, 800);

            int x1 = 175;
            int y1 = 250;
            int h = 30;
            int l = 175;

            int x = 150;
            int y = 100;

            Obstacle obstacle21 = new Obstacle(x1+0*x,y1+0*y, l, h);
            Obstacle obstacle22 = new Obstacle(x1+1*x,y1+1*y, l, h);
            Obstacle obstacle23 = new Obstacle(x1+2*x,y1+2*y, l, h);
            Obstacle obstacle24 = new Obstacle(x1+3*x,y1+3*y, l, h);
            Obstacle obstacle25 = new Obstacle(x1+4*x,y1+4*y, l, h);
            Obstacle obstacle26 = new Obstacle(x1+5*x,y1+5*y, l, h);
            Obstacle obstacle27 = new Obstacle(x1+6*x,y1+6*y, l, h);

            Obstacle obstacle28 = new Obstacle(320,50, 30, 150);
            Obstacle obstacle29 = new Obstacle(1325,750, 30, 75);
            Obstacle obstacle210 = new Obstacle(1275,900, 150, 30);

            obstacles.add(obstacle21);
            obstacles.add(obstacle22);
            obstacles.add(obstacle23);
            obstacles.add(obstacle24);
            obstacles.add(obstacle25);
            obstacles.add(obstacle26);
            obstacles.add(obstacle27);
            obstacles.add(obstacle28);
            obstacles.add(obstacle29);
            obstacles.add(obstacle210);
        }

        if (indice == 3) { // Niveau 3

            balle = new Balle(750, 300);

            panier = new Panier(1400, 700);

            int e = 20;

            Obstacle obstacle31 = new Obstacle(625,225, 200, e);
            Obstacle obstacle32 = new Obstacle(675,225, e, 150);
            Obstacle obstacle33 = new Obstacle(675,355, 245, e);
            Obstacle obstacle34 = new Obstacle(805,225, e, 90);
            Obstacle obstacle35 = new Obstacle(900,165, e, 210);
            Obstacle obstacle36 = new Obstacle(355,50, e, 600);
            Obstacle obstacle37 = new Obstacle(355,750, 350, e);
            Obstacle obstacle38 = new Obstacle(900,550, 150, e);
            Obstacle obstacle39 = new Obstacle(1200,850, 150, e);

            obstacles.add(obstacle31);
            obstacles.add(obstacle32);
            obstacles.add(obstacle33);
            obstacles.add(obstacle34);
            obstacles.add(obstacle35);
            obstacles.add(obstacle36);
            obstacles.add(obstacle37);
            obstacles.add(obstacle38);
            obstacles.add(obstacle39);
        }

        if (indice == 4) { // Niveau 4

            balle = new Balle(250, 650);

            panier = new Panier(1400, 750);

            int e = 20;

            Obstacle obstacle41 = new Obstacle(350,550, e, 200);
            Obstacle obstacle42 = new Obstacle(850,300, 475, e);
            Obstacle obstacle43 = new Obstacle(900,250, 475, e);
            Obstacle obstacle44 = new Obstacle(1325,300, e, 500);
            Obstacle obstacle45 = new Obstacle(1375,250, e, 150);




            obstacles.add(obstacle41);
            obstacles.add(obstacle42);
            obstacles.add(obstacle43);
            obstacles.add(obstacle44);
            obstacles.add(obstacle45);
        }

        if (indice == 5) { // Niveau édition

            balle = new Balle(200, 500);

            panier = new Panier(1400, 500);

            JLabel place = new JLabel("Placer un obstacle : Q");
            JLabel remove = new JLabel("Supprimer : P");
            JLabel removeAll = new JLabel("Tout supprimer : W");

            setText(place);
            setText(remove);
            setText(removeAll);

            place.setBackground(new Color (0,230, 50));
            remove.setBackground(new Color (250,0, 0));
            removeAll.setBackground(new Color (160,0, 0));

            place.setBounds(50,900,450,50);
            remove.setBounds(975,900,200,50);
            removeAll.setBounds(1200,900,275,50);
        }

        JLabel reset = new JLabel("Réinitialiser : Espace");
        setText(reset);
        reset.setBackground(new Color (240,190, 0));
        reset.setBounds(550,900,400,50);
    }

    // Initialiser les zones de textes dans le panel de jeu
    public void setText (JLabel text){

        text.setOpaque(true);
        text.setHorizontalAlignment(JLabel.CENTER);
        text.setForeground(Color.WHITE);
        text.setFont(new Font("Arial", Font.BOLD, 25));
        add(text);
    }
}