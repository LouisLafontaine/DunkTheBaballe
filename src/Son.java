import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Son {
    // Attributs
    //======================================================================
    Clip clip;
    AudioInputStream audioInputStream;

    // Constructeur
    //======================================================================
    public Son(String fileName){
        try {
            InputStream audioSrc = getClass().getResourceAsStream(fileName);
            BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc);
            audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

}