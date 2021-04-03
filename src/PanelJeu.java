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
    LinkedList<Animated> animatedItems;

    // Constructeur
    //======================================================================
    public PanelJeu(){
        // Initialisation de la balle
        balle = new Balle(300,250,25,0, 0, "Character/fireBall.png");

        // Initialisation zone d'arrivée
        panier = new Panier(700, 300, 60);

        // Initialisation des obstacles
        Obstacle obstacle1 = new Obstacle(100,50, 300, 20);
        Obstacle obstacle2 = new Obstacle(380,90, 20, 200);
        Obstacle obstacle3 = new Obstacle(100,90, 20, 200);
        Obstacle obstacle4 = new Obstacle(100,310, 300, 20);
        Obstacle obstacle5 = new Obstacle(200,150, 80, 80);

        // Ajout des obstacles au tableau d'obstacles
        obstacles = new ArrayList<>();
        obstacles.add(obstacle1);
        obstacles.add(obstacle2);
        obstacles.add(obstacle3);
        obstacles.add(obstacle4);
        obstacles.add(obstacle5);

        // Initialisation tableau objets animés
        animatedItems = new LinkedList<>();

        // Initialisation gameLoopTimer pour animation
        int fps = 120;
        gameLoopTimer = new Timer(1000/ fps, this);
        gameLoopTimer.start();


        // Initialisation musique de fond
        musique = new Son("Music/8bitMegaBattle.wav");
        musique.clip.loop(Clip.LOOP_CONTINUOUSLY);

        // Initialisation image de fond
        setBackgroundImage("FantasyForest.png");

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
            balle.x = balle.xInit;
            balle.y = balle.yInit;
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
}