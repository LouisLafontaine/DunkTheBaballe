import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Animated implements ActionListener {
    int x;
    int y;
    int pointerX;
    int pointerY;
    int W;
    int H;
    int oneRow;
    int oneColumn;
    boolean orientation;
    int maxPlayCounter;
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
        oneRow = W/columns;
        oneColumn = H/rows;
        this.orientation  = orientation;

        animationTimer = new Timer(1000/fps,this);

        this.maxPlayCounter = (maxPlayCounter * rows * columns) - noFrames;

        animationTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == animationTimer){
            if(orientation){
                pointerX = (pointerX + (oneRow));
                pointerY = pointerY + ( pointerX/(W) ) * (oneRow);
                pointerX %= W;
                pointerY %= H;
            }else {
                pointerY = (pointerY + (oneRow));
                pointerX = pointerX + ( pointerY/(W) ) * (oneRow);
                pointerY %= H;
                pointerX %= W;
            }
            maxPlayCounter --;
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
        return spriteSheet.getSubimage(pointerX, pointerY,oneRow, oneColumn);
    }
}
