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
    private volatile Player currentPlayer = null;

    /**
     * Returns true if music is currently playing.
     *
     * @return true if music is playing
     */
    public boolean isMusicEnabled() {
        return playing;
    }

    /**
     * Returns true if sound effects are enabled.
     *
     * @return true if sound effects are enabled
     */
    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    /**
     * Enables or disables sound effects.
     *
     * @param enabled
     *            true to enable, false to disable
     */
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }

    /**
     * Starts looping background music from the given resource path.
     *
     * @param resourcePath
     *            the classpath resource path to the music file
     */
    public void playMusic(String resourcePath) {
        currentMusicPath = resourcePath;
        playing = true;
        musicThread = new Thread(() -> {
            while (playing) {
                try (InputStream is = getClass().getResourceAsStream(resourcePath);
                        BufferedInputStream bis = new BufferedInputStream(is)) {
                    Player player = new Player(bis);
                    currentPlayer = player;
                    if (!playing) {
                        player.close();
                        break;
                    }
                    player.play();
                    currentPlayer = null;
                } catch (Exception e) {
                    if (playing) {
                        System.err.println("Feil ved avspilling av musikk: " + e.getMessage());
                    }
                    break;
                }
            }
        }, "music-thread");
        musicThread.setDaemon(true);
        musicThread.start();
    }

    /**
     * Stops and restarts the current background music track from the beginning.
     */
    public void restartMusic() {
        if (currentMusicPath != null) {
            stop();
            playMusic(currentMusicPath);
        }
    }

    /**
     * Plays a one-shot sound effect on a background thread.
     *
     * @param resourcePath
     *            the classpath resource path to the sound file (OGG format)
     */
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

    /**
     * Stops background music and interrupts the music thread.
     */
    public void stop() {
        playing = false;
        Player p = currentPlayer;
        if (p != null) {
            p.close();
            currentPlayer = null;
        }
        if (musicThread != null) {
            musicThread.interrupt();
        }
    }
}
