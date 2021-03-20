import java.io.File;
import java.io.IOException;
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
    final String pathInFile = "Ressources/";

    // Constructeur
    //======================================================================
    public Son(String fileName){
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(pathInFile + fileName).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

}