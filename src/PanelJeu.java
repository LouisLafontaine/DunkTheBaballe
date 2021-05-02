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
    protected final Balle balle;
    protected Timer gameLoopTimer;
    protected Son musique;
    protected int lastClickX; // enregistre la pos x du dernier click
    protected int lastClickY; // enregistre la pos y du dernier click
    protected int clickX; // enregistre la pos x actuelle du click quand dragged
    protected int clickY; // enregistre la pos x actuelle du click quand dragged
    protected boolean clicking; // true si en train de clicker
    protected Image background; // image de fond
    protected ArrayList<Obstacle> obstacles; // tableau d'obstacle
    protected Panier panier;
    protected boolean pressingKey_Q;
    protected int resolution;
    LinkedList<Animated> animatedItems;

    // Constructeur
    //======================================================================
    public PanelJeu(int indice){

        setLayout(null);

        // Initialisation image de fond
        setBackgroundImage("FantasyForest.png");

        //Taille panel

        // Initialisation de la balle
        balle = new Balle(300,750,25,0, 0, "Character/fireBall.png");

        // Initialisation zone d'arrivée
        panier = new Panier(1800, 500, 60);

        // Initialisation des obstacles
        obstacles = new ArrayList<>();
        setLvl(indice);

        // Initialisation tableau objets animés
        animatedItems = new LinkedList<>();

        // Initialisation gameLoopTimer pour animation
        int fps = 120;
        gameLoopTimer = new Timer(1000/ fps, this);
        gameLoopTimer.start();


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
        if(e.getKeyCode() == KeyEvent.VK_W){
            obstacles.clear();
            repaint();
        }
        if(e.getKeyCode() == KeyEvent.VK_A){
            balle.xInit = clickX;
            balle.yInit = clickY;
            balle.x = clickX;
            balle.y = clickY;
            balle.xCollision = clickX;
            balle.yCollision = clickY;
            repaint();
        }
        if(e.getKeyCode() == KeyEvent.VK_P) {
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
            balle.resetPosBalle(false);
            Son winSound = new Son("Sound/8bitWin.wav");
            musique.clip.stop();
            winSound.clip.start();
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

    public void setLvl(int indice) {
        if (indice == 1) {
            Obstacle obstacle11 = new Obstacle(100,50, 300, 20);
            Obstacle obstacle12 = new Obstacle(380,90, 20, 200);
            Obstacle obstacle13 = new Obstacle(100,90, 20, 200);
            Obstacle obstacle14 = new Obstacle(100,310, 300, 20);
            Obstacle obstacle15 = new Obstacle(200,150, 80, 80);

            obstacles.add(obstacle11);
            obstacles.add(obstacle12);
            obstacles.add(obstacle13);
            obstacles.add(obstacle14);
            obstacles.add(obstacle15);
        }

        if (indice == 2) {
            Obstacle obstacle21 = new Obstacle(100,50, 300, 20);
            Obstacle obstacle22 = new Obstacle(380,90, 20, 200);
            Obstacle obstacle23 = new Obstacle(100,90, 20, 200);
            Obstacle obstacle24 = new Obstacle(100,310, 300, 20);

            obstacles.add(obstacle21);
            obstacles.add(obstacle22);
            obstacles.add(obstacle23);
            obstacles.add(obstacle24);
        }

        if (indice == 3) {
            Obstacle obstacle31 = new Obstacle(100,50, 300, 20);
            Obstacle obstacle32 = new Obstacle(380,90, 20, 200);
            Obstacle obstacle33 = new Obstacle(100,90, 20, 200);

            obstacles.add(obstacle31);
            obstacles.add(obstacle32);
            obstacles.add(obstacle33);
        }

        if (indice == 4) {
            Obstacle obstacle41 = new Obstacle(100,50, 300, 20);
            Obstacle obstacle42 = new Obstacle(380,90, 20, 200);

            obstacles.add(obstacle41);
            obstacles.add(obstacle42);
        }

        if (indice == 5) {
            JLabel text1 = new JLabel("Placer un obstacle : q");
            JLabel text2 = new JLabel("Supprimer un obstacle : p");
            JLabel text3 = new JLabel("Supprimer tous les obstacles : w");

            text1.setBounds(20,500,200,50);
            text2.setBounds(220,500,200,50);
            text3.setBounds(440,500,200,50);

            add(text1);
            add(text2);
            add(text3);
        }

        JLabel text4 = new JLabel("Réinitialiser : espace");
        text4.setBounds(20,500,200,50);
        add(text4);
    }
}