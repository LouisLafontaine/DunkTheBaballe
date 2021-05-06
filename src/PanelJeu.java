import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
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
    protected boolean modePlacer;
    protected boolean modePlacerB;
    protected boolean modePlacerP;

    protected JButton retry;
    protected JButton place;
    protected JButton remove;
    protected JButton removeAll;
    protected JButton save;
    protected JButton placeB;
    protected JButton placeP;
    protected JButton resetAll;



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
        animatedItems.add(new Animated("Animation/flameCircle.png", panier.x-50, panier.y-55, 7,7,3, 30,true));

        // Initialisation gameLoopTimer pour animation
        int fps = 120;
        gameLoopTimer = new Timer(1000/ fps, this);

        //Initialisation TimeWinSound pour relancer la musique à la fin du son de victoire
        timeWinSound = new Timer(1000, this);
        temps = 0;

        // Initialisation musique de fond
        musique = new Son("Music/Pokemon.wav");
        musique.clip.loop(Clip.LOOP_CONTINUOUSLY);

        modePlacer = false;
        modePlacerB = false;
        modePlacerP = false;

        // Ajout interface
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);

        gameLoopTimer.start();
    }

    public void save(String filename){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("Ressources/Saves/"+filename);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            bufferedWriter.write("b"+(int)balle.x+","+(int)balle.y);
            bufferedWriter.newLine();

            bufferedWriter.write("p"+panier.x+","+panier.y);
            bufferedWriter.newLine();

            for (Obstacle o : obstacles) {
                bufferedWriter.write("o"+o.x+","+o.y+","+ o.largeur+","+o.hauteur);
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSave(String filename){
        try {
            obstacles.clear();
            InputStream txtStream = getClass().getResourceAsStream("Saves/"+filename);
            InputStreamReader streamReader = new InputStreamReader(txtStream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("b")) {
                    int balleX = Integer.parseInt(line.substring(1, line.indexOf(",")));
                    line = line.substring(line.indexOf(",")+1);
                    int balleY = Integer.parseInt(line);
                    balle = new Balle(balleX, balleY);
                }
                if (line.startsWith("p")) {
                    int panierX = Integer.parseInt(line.substring(1, line.indexOf(",")));
                    line = line.substring(line.indexOf(",")+1);
                    int panierY = Integer.parseInt(line);
                    panier = new Panier(panierX, panierY);
                }
                if (line.startsWith("o")) {
                    int x = Integer.parseInt(line.substring(1, line.indexOf(",")));
                    line = line.substring(line.indexOf(",")+1);
                    int y = Integer.parseInt(line.substring(0, line.indexOf(",")));
                    line = line.substring(line.indexOf(",")+1);
                    int largeur = Integer.parseInt(line.substring(0, line.indexOf(",")));
                    line = line.substring(line.indexOf(",")+1);
                    int hauteur = Integer.parseInt(line);
                    obstacles.add(new Obstacle(x,y,largeur,hauteur));
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        if(clicking && modePlacer) drawRectangleFromMouse(g);

        g.setColor(Color.black);

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
                for(Obstacle o : obstacles){
                    if(balle.hasCollided(o)){
                        balle.solveCollision(o);
                        animatedItems.add(new Animated("Animation/explosion2.png",(int)balle.x-32,(int)balle.y-64,1,8,0, 16,1,true));
                        break;
                    }
                }
                checkSolveWin();
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
                if(isDisplayable()) musique.clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }

        //Boutons

        if (e.getSource() == retry) {
            balle.resetPosBalle(true);
            repaint();
        }

        if (e.getSource() == remove) {
            if (obstacles.size() > 0) obstacles.remove(obstacles.size()-1);
            repaint();
        }

        if (e.getSource() == removeAll) {
            obstacles.clear();
            repaint();
        }

        if (e.getSource() == resetAll) {
            loadSave("editeur.txt");
        }

        if (e.getSource() == place) {
            modePlacer = !modePlacer;
            modePlacerB = false;
            modePlacerP = false;
            resetBouton();

            if(modePlacer) place.setBackground(new Color (18, 239, 55));
        }

        if (e.getSource() == placeB) {
            modePlacerB = !modePlacerB;
            modePlacer = false;
            modePlacerP = false;
            resetBouton();

            if(modePlacerB) placeB.setBackground(new Color (162, 109, 224));
        }

        if (e.getSource() == placeP) {
            modePlacerP = !modePlacerP;
            modePlacerB = false;
            modePlacer = false;
            resetBouton();

            if(modePlacerP) placeP.setBackground(new Color (245, 210, 38));
        }

        if (e.getSource() == save) {
            save("editeur.txt");
            resetBouton();

            save.setBackground(new Color (0, 210, 38));
            save.setText("Sauvegardé !");
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
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setLastClickOff();
        if(balle.toucheBalle(lastClickX,lastClickY)  && !modePlacer && !modePlacerP && !modePlacerB) {
            balle.throwBalle(e);
            repaint();
        }

        if(modePlacer) addNewObstacleFromMouse();

        if(modePlacerB) {
            balle.xInit = clickX;
            balle.yInit = clickY;
            balle.x = clickX;
            balle.y = clickY;
            balle.xCollision = clickX;
            balle.yCollision = clickY;
            repaint();
        }

        if(modePlacerP) {
            panier.x = clickX;
            panier.y = clickY;
            repaint();
        }

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

        if(modePlacerB){
            balle.xInit = clickX;
            balle.yInit = clickY;
            balle.x = clickX;
            balle.y = clickY;
            balle.xCollision = clickX;
            balle.yCollision = clickY;
        }

        if(modePlacerP){
            panier.x = clickX;
            panier.y = clickY;
        }
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
        if(e.getKeyCode() == KeyEvent.VK_W ){
            obstacles.clear();
            repaint();
        }
        if(e.getKeyCode() == KeyEvent.VK_A ){
            balle.xInit = clickX;
            balle.yInit = clickY;
            balle.x = clickX;
            balle.y = clickY;
            balle.xCollision = clickX;
            balle.yCollision = clickY;
            repaint();
        }
        if(e.getKeyCode() == KeyEvent.VK_P ) {
            if (obstacles.size() > 0) obstacles.remove(obstacles.size()-1);
            repaint();
        }
        if(e.getKeyCode() == KeyEvent.VK_R) musique.clip.start();

        if(e.getKeyCode() == KeyEvent.VK_ENTER) animatedItems.clear();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(pressingKey_Q) addNewObstacleFromMouse();
        ifSetPressingKey_Q_Off(e);
    }

    // Méthodes
    //======================================================================
    public void setBackgroundImage(String backgroundFileName){
        String pathInFolder = "Background/";
        try {
            background = ImageIO.read(getClass().getResourceAsStream(pathInFolder+backgroundFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLastClickOn(int x, int y) {
        clickX = x;
        clickY = y;
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
        if(clicking && balle.toucheBalle(lastClickX, lastClickY)  && !modePlacer && !modePlacerP && !modePlacerB) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(5));
            g2d.setColor(Color.red);
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

    public void drawRectangleFromMouse(Graphics g) {
        g.setColor(new Color(250,250,0));
        if(clickX>lastClickX && clickY>lastClickY){
            g.fillRect(lastClickX, lastClickY, multipleOfThick(clickX-lastClickX), multipleOfThick(clickY-lastClickY));
        }else if(clickX<lastClickX && clickY>lastClickY) {
            g.fillRect(multipleOfThick(clickX), lastClickY, multipleOfThick(lastClickX-multipleOfThick(clickX)), multipleOfThick(clickY-lastClickY));
        }else if(clickX>lastClickX && clickY<lastClickY) {
            g.fillRect(lastClickX, multipleOfThick(clickY), multipleOfThick(clickX-lastClickX), multipleOfThick(lastClickY-multipleOfThick(clickY)));
        }else if(clickX<lastClickX && clickY<lastClickY)
            g.fillRect(multipleOfThick(clickX), multipleOfThick(clickY), multipleOfThick(lastClickX-multipleOfThick(clickX)), multipleOfThick(lastClickY-multipleOfThick(clickY)));
    }

    private void addNewObstacleFromMouse() {
        if(clickX>lastClickX && clickY>lastClickY){
            obstacles.add(new Obstacle(lastClickX, lastClickY, multipleOfThick(clickX-lastClickX), multipleOfThick(clickY-lastClickY)));
        }else if(clickX<lastClickX && clickY>lastClickY) {
            obstacles.add(new Obstacle(multipleOfThick(clickX), lastClickY, multipleOfThick(lastClickX-multipleOfThick(clickX)), multipleOfThick(clickY-lastClickY)));
        }else if(clickX>lastClickX && clickY<lastClickY) {
            obstacles.add(new Obstacle(lastClickX, multipleOfThick(clickY), multipleOfThick(clickX-lastClickX), multipleOfThick(lastClickY-multipleOfThick(clickY))));
        }else if(clickX<lastClickX && clickY<lastClickY)
            obstacles.add(new Obstacle(multipleOfThick(clickX), multipleOfThick(clickY), multipleOfThick(lastClickX-multipleOfThick(clickX)), multipleOfThick(lastClickY-multipleOfThick(clickY))));
    }

    public int multipleOfThick(int number){
        int thickMin = 30; // Pour que l'obstacle que l'on place ai une épaisseur standard
        return ((number/thickMin)*thickMin);
    }

    // Key pressed tracking
    public void ifSetPressingKey_Q_On(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_Q) pressingKey_Q = true;
    }

    public void ifSetPressingKey_Q_Off(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_Q) pressingKey_Q = false;
    }

    // Méthode afin de placer les différents objets sur la fenêtre en fonction du niveau
    public void setLvl() {

        retry = new JButton("Réessayer");
        setText(retry);
        retry.setBounds(450,20,300,50);
        retry.setBackground(new Color (240,190, 0));

        place = null;
        remove = null;
        removeAll = null;
        save = null;
        placeB = null;
        placeP = null;
        resetAll = null;

        if (indice >= 1 && indice <= 4) loadSave("niveau"+indice+".txt");

        if (indice == 5) { // Niveau édition

            loadSave("editeur.txt");

            place = new JButton("Placer un obstacle");
            remove = new JButton("Supprimer");
            removeAll = new JButton("Tout supprimer");
            save = new JButton("Sauvegarder");
            placeB = new JButton("Placer la balle");
            placeP = new JButton("Placer le panier");
            resetAll = new JButton("Reset");

            setText(place);
            setText(remove);
            setText(removeAll);
            setText(save);
            setText(placeB);
            setText(placeP);
            setText(resetAll);

            resetBouton();

            place.setBounds(20,20,300,50);
            remove.setBounds(660,20,200,50);
            removeAll.setBounds(880,20,300,50);
            save.setBounds(880,90,300,50);
            placeB.setBounds(20,90,300,50);
            placeP.setBounds(340,90,300,50);
            resetAll.setBounds(660,90,200,50);
            retry.setBounds(340,20,300,50);
        }
    }

    // Initialiser les zones de textes dans le panel de jeu
    public void setText (JButton text){

        text.setOpaque(true);
        text.setBorderPainted(false);
        text.setHorizontalAlignment(JLabel.CENTER);
        text.setForeground(Color.WHITE);
        text.setFont(new Font("Arial", Font.BOLD, 25));
        text.addActionListener(this);
        add(text);
    }

    public void resetBouton(){
        place.setBackground(new Color (31, 109, 10));
        remove.setBackground(new Color (250,0, 0));
        removeAll.setBackground(new Color (160,0, 0));
        save.setBackground(new Color (0,20, 120));
        save.setText("Sauvegarder");
        placeB.setBackground(new Color (93, 22, 102, 255));
        placeP.setBackground(new Color (236, 106, 6));
        resetAll.setBackground(new Color (5,0, 0));
    }

}