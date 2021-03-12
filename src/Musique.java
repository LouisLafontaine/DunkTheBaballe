import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Musique {
    // Attributs
    //======================================================================
    Clip clip;
    AudioInputStream audioInputStream;
    final String pathInFile = "Ressources/Musique/";

    // Constructeur
    //======================================================================
    public Musique(String fileName){
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(pathInFolder + fileName).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
