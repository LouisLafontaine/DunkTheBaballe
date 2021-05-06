import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Animated implements ActionListener {
    int x;
    int y;
    int pointerX;
    int pointerY;
    int W;
    int H;
    int oneRow;
    int oneColumn;
    boolean orientation; // true si horizontale, false si vertical
    int maxPlayCounter;
    int noFrames;
    final boolean PLAY_NO_STOP;
    BufferedImage spriteSheet; // image contenant les différentes frames
    Timer animationTimer; // Timer qui permet d'actualiser l'animation de l'objet

    /*noFrames correspond au nombres de frames vide à la fin de la spritesheet*/
    public Animated(String fileName, int x, int y, int rows, int columns, int noFrames, int fps, int maxPlayCounter, boolean orientation){

        initializeSpritesheet(fileName);

        this.x = x;
        this.y = y;
        pointerX = 0;
        pointerY = 0;
        W = spriteSheet.getWidth();
        H = spriteSheet.getHeight();
        oneRow = H/rows;
        oneColumn = W/columns;
        this.orientation  = orientation;
        this.PLAY_NO_STOP = false;

        animationTimer = new Timer(1000/fps,this);

        this.maxPlayCounter = (maxPlayCounter * rows * columns) - noFrames;
        this.noFrames = noFrames;

        animationTimer.start();
    }
    public Animated(String fileName, int x, int y, int rows, int columns, int noFrames, int fps, boolean orientation){

        initializeSpritesheet(fileName);

        this.x = x;
        this.y = y;
        pointerX = 0;
        pointerY = 0;
        W = spriteSheet.getWidth();
        H = spriteSheet.getHeight();
        oneRow = H/rows;
        oneColumn = W/columns;
        this.orientation  = orientation;
        this.PLAY_NO_STOP = true;

        animationTimer = new Timer(1000/fps,this);

        this.maxPlayCounter = 1;
        this.noFrames = noFrames;

        animationTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == animationTimer){
            if(orientation){
                pointerX = (pointerX + (oneColumn));
                pointerY = pointerY + ( pointerX/(W) ) * (oneRow);
                pointerX %= W - (pointerY/H)*noFrames*oneColumn;
                pointerY %= H;
            }else {
                pointerY = (pointerY + (oneRow));
                pointerX = pointerX + ( pointerY/(W) ) * (oneColumn);
                pointerY %= H;
                pointerX %= W - (pointerX/W)*noFrames*oneRow;
            }
            if(!PLAY_NO_STOP) maxPlayCounter --;
        }
    }

    private void initializeSpritesheet(String fileName) {
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getCurrentFrame(){
        if(maxPlayCounter <0) return null;
        return spriteSheet.getSubimage(pointerX, pointerY,oneColumn, oneRow);
    }
}
