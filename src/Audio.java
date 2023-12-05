import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio {
    private Clip clip;
    private boolean soundPlaying;

    // Audio Files
    private File runningFile;
    private File jumpingFile;
    private File landingFile;
    private File backGroundMusicFile;
    private File objectCollectionFile;

    Audio(){
        soundPlaying = false;
        // Init Files
        try{
            runningFile = new File("AudioFiles/Walk.wav");
            jumpingFile = new File("AudioFiles/JumpSound.wav");
            landingFile = new File("");
            objectCollectionFile = new File("AudioFiles/CollectionSound.wav");
            backGroundMusicFile = new File("AudioFiles/BackGroundMusic.wav");
        }catch (Exception e){
            System.out.println("Error Opening Audio Files: " + e);
        }
    }

    public void runningSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (!soundPlaying) {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(runningFile);
            AudioFormat audioFormat = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                    try {
                        audioStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    soundPlaying = false;
                }
            });
            clip.start();
            soundPlaying = true;
        }
    }

    public void jumpingSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        if (!soundPlaying) {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(jumpingFile);
            AudioFormat audioFormat = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                    try {
                        audioStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    soundPlaying = false;
                }
            });
            clip.start();
            soundPlaying = true;
        }
    }

    public void landingSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (!soundPlaying) {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(landingFile);
            AudioFormat audioFormat = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                    try {
                        audioStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    soundPlaying = false;
                }
            });
            clip.start();
            soundPlaying = true;
        }
    }

    public void objectCollectionSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (!soundPlaying) {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(objectCollectionFile);
            AudioFormat audioFormat = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                    try {
                        audioStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    soundPlaying = false;
                }
            });
            clip.start();
            soundPlaying = true;
        }
    }

    public void backGroundMusic() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(backGroundMusicFile);
        AudioFormat audioFormat = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
        clip = (Clip) AudioSystem.getLine(info);
        clip.open(audioStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY); // Set the clip to loop continuously
        clip.start();
    }

}
