import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Animated implements ActionListener {
    Timer animationTimer; // gameLoopTimer qui permet d'actualiser l'animation de l'objet
    BufferedImage spriteSheet; // image contenant les différentes frames
    BufferedImage currentFrame; // image actuelle de l'objet animé
    int x;
    int y;
    int posX;
    int posY;

    public Animated(String fileName, int posX, int posY){
        int fps = 10;
        animationTimer = new Timer(1000/fps,this);
        initializeBufferedImage(fileName);
        x = 0;
        y = 0;
        this.posX = posX;
        this.posY = posY;
        currentFrame = spriteSheet.getSubimage(x,y,100,100);
        animationTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == animationTimer){
            currentFrame = spriteSheet.getSubimage(x,y,100,100);
            y = (y+100)%700;
            if(y == 600) x = (x+100)%700;
        }
    }

    private void initializeBufferedImage(String fileName) {
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
