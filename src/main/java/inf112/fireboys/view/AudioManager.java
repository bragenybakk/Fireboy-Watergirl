package inf112.fireboys.view;

import javazoom.jl.player.Player;
import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Spiller av bakgrunnsmusikk fra resources-mappen.
 * Looper musikken automatisk, og kjører i en daemon-tråd.
 */
public class AudioManager {
    private Thread musicThread;
    private volatile boolean playing = false;
    private volatile boolean soundEnabled = true;
    private String currentMusicPath = null;
    public boolean isMusicEnabled() {
        return playing;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }

    public void playMusic(String resourcePath) {
        currentMusicPath = resourcePath;
        playing = true;
        musicThread = new Thread(() -> {
            while (playing) {
                try (InputStream is = getClass().getResourceAsStream(resourcePath);
                        BufferedInputStream bis = new BufferedInputStream(is)) {
                    Player player = new Player(bis);
                    player.play();
                } catch (Exception e) {
                    System.err.println("Feil ved avspilling av musikk: " + e.getMessage());
                    break;
                }
            }
        }, "music-thread");
        musicThread.setDaemon(true);
        musicThread.start();
    }

    public void restartMusic() {
        if (currentMusicPath != null) {
            stop();
            playMusic(currentMusicPath);
        }
    }

    public void playSound(String resourcePath) {
        if (!soundEnabled)
            return;
        new Thread(() -> {
            try (InputStream is = getClass().getResourceAsStream(resourcePath);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    AudioInputStream ais = AudioSystem.getAudioInputStream(bis)) {
                AudioFormat baseFormat = ais.getFormat();
                AudioFormat decodedFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        baseFormat.getSampleRate(),
                        16,
                        baseFormat.getChannels(),
                        baseFormat.getChannels() * 2,
                        baseFormat.getSampleRate(),
                        false);
                try (AudioInputStream decoded = AudioSystem.getAudioInputStream(decodedFormat, ais);
                        SourceDataLine line = AudioSystem.getSourceDataLine(decodedFormat)) {
                    line.open(decodedFormat);
                    line.start();
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = decoded.read(buffer)) != -1) {
                        line.write(buffer, 0, bytesRead);
                    }
                    line.drain();
                    line.stop();
                }
            } catch (Exception e) {
                System.err.println("Feil ved avspilling av lyd: " + e.getMessage());
            }
        }, "sound-effect-thread").start();
    }

    public void stop() {
        playing = false;
        if (musicThread != null) {
            musicThread.interrupt();
        }
    }
}
