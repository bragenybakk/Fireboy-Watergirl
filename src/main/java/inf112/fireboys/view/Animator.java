package inf112.fireboys.view;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Håndterer animasjon av sprites over tid.
 * Holder styr på hvilken frame som skal vises basert på tid.
 */
public class Animator {
    private List<BufferedImage> frames;
    private int currentFrame;
    private long lastFrameTime;
    private long frameDelay; // Millisekunder mellom hver frame
    private boolean looping;
    private boolean playing;
    /**
     * Oppretter en ny Animator
     * 
     * @param frames
     *            Liste med frames i animasjonen
     * @param frameDelay
     *            Millisekunder mellom hver frame
     * @param looping
     *            Om animasjonen skal loope
     */
    public Animator(List<BufferedImage> frames, long frameDelay, boolean looping) {
        this.frames = frames;
        this.frameDelay = frameDelay;
        this.looping = looping;
        this.currentFrame = 0;
        this.lastFrameTime = System.currentTimeMillis();
        this.playing = true;
    }

    /**
     * Oppdaterer animasjonen - skal kalles hvert frame
     */
    public void update() {
        if (!playing || frames == null || frames.isEmpty()) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime >= frameDelay) {
            currentFrame++;
            if (currentFrame >= frames.size()) {
                if (looping) {
                    currentFrame = 0;
                } else {
                    currentFrame = frames.size() - 1;
                    playing = false;
                }
            }
            lastFrameTime = currentTime;
        }
    }

    /**
     * @return Nåværende frame som skal tegnes
     */
    public BufferedImage getCurrentFrame() {
        if (frames == null || frames.isEmpty()) {
            return null;
        }
        return frames.get(currentFrame);
    }

    /**
     * Starter animasjonen fra begynnelsen
     */
    public void start() {
        currentFrame = 0;
        lastFrameTime = System.currentTimeMillis();
        playing = true;
    }

    /**
     * Pauser animasjonen
     */
    public void pause() {
        playing = false;
    }

    /**
     * Fortsetter animasjonen
     */
    public void resume() {
        playing = true;
        lastFrameTime = System.currentTimeMillis();
    }

    /**
     * Stopper og tilbakestiller animasjonen
     */
    public void stop() {
        playing = false;
        currentFrame = 0;
    }

    /**
     * Bytter til nye frames (for å bytte animasjon)
     * 
     * @param newFrames
     *            Nye frames
     */
    public void setFrames(List<BufferedImage> newFrames) {
        this.frames = newFrames;
        this.currentFrame = 0;
        this.lastFrameTime = System.currentTimeMillis();
    }

    /**
     * @param delay
     *            Ny frame-delay i millisekunder
     */
    public void setFrameDelay(long delay) {
        this.frameDelay = delay;
    }

    /**
     * @param looping
     *            Om animasjonen skal loope
     */
    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    /**
     * @return Om animasjonen spiller
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * @return Om animasjonen er ferdig (kun relevant for ikke-loopende)
     */
    public boolean isFinished() {
        return !looping && currentFrame >= frames.size() - 1 && !playing;
    }

    /**
     * @return Antall frames i animasjonen
     */
    public int getFrameCount() {
        return frames != null ? frames.size() : 0;
    }

    /**
     * @return Nåværende frame-indeks
     */
    public int getCurrentFrameIndex() {
        return currentFrame;
    }
}
