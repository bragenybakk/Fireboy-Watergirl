package inf112.fireboys.view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * Håndterer lasting og oppdeling av spritesheets.
 * Støtter ikke-uniform grid ved å bruke bounding boxes.
 */
public class SpriteSheet {
    private BufferedImage spriteSheet;
    private Map<String, List<BufferedImage>> animations;
    public SpriteSheet(String path) {
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream(path));
            animations = new HashMap<>();
            loadAllAnimations();
        } catch (IOException e) {
            System.err.println("Kunne ikke laste spritesheet: " + path);
            e.printStackTrace();
        }
    }

    /**
     * Laster alle animasjoner fra spritesheetet
     */
    private void loadAllAnimations() {
        // Watergirl animasjoner
        loadWatergirlAnimations();
        // Fireboy animasjoner
        loadFireboyAnimations();
        // Objekter
        loadObjectSprites();
    }

    /**
     * Watergirl animasjoner basert på analysen
     */
    private void loadWatergirlAnimations() {
        // Rad 1: Gå/løpe (y≈211, 8 frames, ~59×65 px)
        animations.put("watergirl_run", extractSpritesFromRow(0, 211, 8, 59, 65));
        // Rad 2: Hopp/sideveis (y≈171, 3 frames, ~80×60 px)
        animations.put("watergirl_jump", extractSpritesFromRow(0, 171, 3, 80, 60));
        // Rad 3: Hopp variant (y≈229, 5 frames)
        animations.put("watergirl_jump2", extractSpritesFromRow(0, 229, 5, 80, 60));
        // Rad 4: Enkelt-frame (y≈294, 1 frame)
        animations.put("watergirl_single", extractSpritesFromRow(0, 294, 1, 60, 65));
        // Rad 5-8: Idle/ansikt (y≈486-708, ~5-10 per rad, ~60×65 px)
        animations.put("watergirl_idle", extractSpritesFromRow(0, 486, 10, 60, 65));
        // Rad 9-10: Idle/stå med kropp (y≈1200-1270)
        animations.put("watergirl_stand", extractSpritesFromRow(0, 1200, 6, 60, 65));
        // Rad 11: Liten frame/diamant (y≈1351)
        animations.put("watergirl_small", extractSpritesFromRow(0, 1351, 1, 36, 30));
        // Rad 12: Krympe/dø (y≈1754, 9 frames, størrelse varierer)
        animations.put("watergirl_die1", extractSpritesFromRow(0, 1754, 9, 50, 50));
        // Rad 13: Krympe/dø del 2 (y≈1810, 10 frames)
        animations.put("watergirl_die2", extractSpritesFromRow(0, 1810, 10, 40, 40));
    }

    /**
     * Fireboy animasjoner basert på analysen
     */
    private void loadFireboyAnimations() {
        // Rad 1: Enkelt stor frame (y≈291, 1 frame, 52×89)
        animations.put("fireboy_big", extractSpritesFromRow(400, 291, 1, 52, 89));
        // Rad 2: Liten frame (y≈437, 1 frame)
        animations.put("fireboy_small", extractSpritesFromRow(400, 437, 1, 40, 40));
        // Rad 3: Stå/idle med kropp (y≈804, 5 frames)
        animations.put("fireboy_stand", extractSpritesFromRow(400, 804, 5, 46, 61));
        // Rad 4: Idle/ansikt (y≈870, 10 frames)
        animations.put("fireboy_idle", extractSpritesFromRow(400, 870, 10, 46, 61));
        // Rad 5: Stå/idle (y≈968, 5 frames)
        animations.put("fireboy_idle2", extractSpritesFromRow(400, 968, 5, 46, 61));
        // Rad 6-7: Liggende/hopp (y≈1033-1138, brede frames ~60-71×43)
        animations.put("fireboy_jump", extractSpritesFromRow(400, 1033, 9, 71, 43));
        // Rad 8: Blandet (y≈1196, 2 frames)
        animations.put("fireboy_mixed", extractSpritesFromRow(400, 1196, 2, 50, 50));
        // Rad 9: Stå/gå (y≈1301, 6 frames)
        animations.put("fireboy_run", extractSpritesFromRow(400, 1301, 6, 46, 61));
        // Rad 10: Liten frame (y≈1353, 1 frame)
        animations.put("fireboy_tiny", extractSpritesFromRow(400, 1353, 1, 36, 30));
        // Rad 11: Stor frame (y≈1536, 1 frame, 48×85)
        animations.put("fireboy_large", extractSpritesFromRow(400, 1536, 1, 48, 85));
        // Rad 12: Krympe/dø (y≈1616, 13 frames)
        animations.put("fireboy_die1", extractSpritesFromRow(400, 1616, 13, 46, 46));
        // Rad 13: Siste krymping (y≈1705, 2 frames)
        animations.put("fireboy_die2", extractSpritesFromRow(400, 1705, 2, 33, 33));
    }

    /**
     * Objekter (diamanter, steiner etc.) fra bunnen av bildet
     */
    private void loadObjectSprites() {
        // Diamant (x=5, y=1385, ~65×65 px)
        animations.put("diamond", extractSpritesFromRow(1000, 1380, 1, 65, 65));
        // Steiner/plattformer
        animations.put("stones", extractSpritesFromRow(0, 1400, 5, 50, 50));
    }

    /**
     * Ekstraherer sprites fra en rad i spritesheetet
     * 
     * @param startX
     *            Start X-posisjon
     * @param y
     *            Y-posisjon for raden
     * @param count
     *            Antall sprites å hente
     * @param width
     *            Bredde på hver sprite
     * @param height
     *            Høyde på hver sprite
     * @return Liste med BufferedImage sprites
     */
    private List<BufferedImage> extractSpritesFromRow(int startX, int y, int count, int width, int height) {
        List<BufferedImage> sprites = new ArrayList<>();
        if (spriteSheet == null) {
            return sprites;
        }
        int x = startX;
        for (int i = 0; i < count; i++) {
            // Sjekk at vi ikke går utenfor bildet
            if (x + width <= spriteSheet.getWidth() && y + height <= spriteSheet.getHeight()) {
                BufferedImage sprite = spriteSheet.getSubimage(x, y, width, height);
                sprites.add(sprite);
            }
            x += width + 2; // Litt mellomrom mellom sprites
        }
        return sprites;
    }

    /**
     * Ekstraherer en enkelt sprite fra en spesifikk posisjon
     * 
     * @param x
     *            X-posisjon
     * @param y
     *            Y-posisjon
     * @param width
     *            Bredde
     * @param height
     *            Høyde
     * @return BufferedImage sprite
     */
    public BufferedImage getSprite(int x, int y, int width, int height) {
        if (spriteSheet == null) {
            return null;
        }
        if (x + width <= spriteSheet.getWidth() && y + height <= spriteSheet.getHeight()) {
            return spriteSheet.getSubimage(x, y, width, height);
        }
        return null;
    }

    /**
     * Henter en animasjon basert på navn
     * 
     * @param name
     *            Navnet på animasjonen (f.eks. "watergirl_run")
     * @return Liste med sprites for animasjonen
     */
    public List<BufferedImage> getAnimation(String name) {
        return animations.getOrDefault(name, new ArrayList<>());
    }

    /**
     * Henter en spesifikk frame fra en animasjon
     * 
     * @param name
     *            Animasjonsnavn
     * @param frameIndex
     *            Frame-indeks
     * @return BufferedImage for framen, eller null
     */
    public BufferedImage getFrame(String name, int frameIndex) {
        List<BufferedImage> anim = animations.get(name);
        if (anim != null && frameIndex >= 0 && frameIndex < anim.size()) {
            return anim.get(frameIndex);
        }
        return null;
    }

    /**
     * @return Hele spritesheetet
     */
    public BufferedImage getFullSheet() {
        return spriteSheet;
    }

    /**
     * @return Liste over alle tilgjengelige animasjonsnavn
     */
    public List<String> getAnimationNames() {
        return new ArrayList<>(animations.keySet());
    }
}
