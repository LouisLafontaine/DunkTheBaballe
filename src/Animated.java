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
    int pointerCurrentFrame;
    int numberOfFrames;
    BufferedImage spriteSheet; // image contenant les différentes frames
    BufferedImage currentFrame; // image actuelle de l'objet animé
    ArrayList<BufferedImage> frames; // tableau contenant l'ensemble des frames de l'animation
    Timer animationTimer; // Timer qui permet d'actualiser l'animation de l'objet

    /*noFrames correspond au nombres de frames vide à la fin de la spritesheet*/
    public Animated(String fileName, int x, int y, int rows, int columns, int noFrames, int fps, boolean orientation){
        this.x = x;
        this.y = y;

        animationTimer = new Timer(1000/fps,this);

        initializeBufferedImage(fileName);
        initializeFrames(rows, columns, noFrames, orientation);
        numberOfFrames = frames.size();
        currentFrame = frames.get(0);

        animationTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == animationTimer){
            setNextFrame();
        }
    }

    private void initializeBufferedImage(String fileName) {
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeFrames(int rows, int columns, int noFrames, boolean orientation){
        frames = new ArrayList<>();
        int pointeurX = 0;
        int pointeurY = 0;
        int W = spriteSheet.getWidth();
        int H = spriteSheet.getHeight();
        int oneRow = W/columns;
        int oneColumn = H/rows;
        if(orientation){
            for(int i=0; i<((rows*columns) - noFrames); i++){
                frames.add(spriteSheet.getSubimage(pointeurX, pointeurY,oneRow, oneColumn));
                pointeurX = (pointeurX + (oneRow));
                pointeurY = pointeurY + ( pointeurX/(W) ) * (oneRow);
                pointeurX %= W;
            }
        }else {
            for(int i=0; i<((rows*columns) - noFrames); i++){
                frames.add(spriteSheet.getSubimage(pointeurX, pointeurY,oneRow, oneColumn));
                pointeurY = (pointeurY + (oneRow));
                pointeurX = pointeurX + ( pointeurY/(W) ) * (oneRow);
                pointeurY %= H;
            }
        }
    }

    private void setNextFrame() {
        pointerCurrentFrame = (pointerCurrentFrame + 1) % numberOfFrames;
        currentFrame = frames.get(pointerCurrentFrame);
    }
}
