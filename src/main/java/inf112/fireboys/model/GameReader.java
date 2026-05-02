package inf112.fireboys.model;

import inf112.fireboys.coordinateSystem.Board;

import java.io.InputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

/**
 * Parses level files from the classpath and builds a Board with all game
 * entities.
 */
public class GameReader {
    /**
     * Reads a level file from the classpath and returns a Board.
     * The fileName should be just the filename, e.g. "level1.txt".
     */
    public static Board loadLevel(String fileName) throws IOException {
        InputStream is = GameReader.class.getClassLoader().getResourceAsStream(fileName);
        if (is == null) {
            throw new IOException("Level file not found on classpath: " + fileName);
        }
        EntityFactory factory = new EntityFactory();
        double width = 0;
        double height = 0;
        try (Scanner sc = new Scanner(is).useLocale(Locale.US)) {
            while (sc.hasNext()) {
                String token = sc.next().toUpperCase();
                if (token.equals("BOARD")) {
                    width = sc.nextDouble();
                    height = sc.nextDouble();
                } else if (!factory.parse(token, sc)) {
                    if (sc.hasNextLine())
                        sc.nextLine();
                }
            }
        }
        return factory.build(width, height);
    }
}
