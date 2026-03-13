import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class Sprite {
    final int width;
    final int height;
    final int[] pixels;

    Sprite(int w, int h, int[] p) {
        this.width = w;
        this.height = h;
        this.pixels = p;
    }

    static Sprite loadSprite(String path) {
        try {
            BufferedImage img = ImageIO.read(Game.class.getResourceAsStream(path));
            int w = img.getWidth(), h = img.getHeight();
            int[] px = new int[w * h];
            img.getRGB(0, 0, w, h, px, 0, w);
            return new Sprite(w, h, px);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + path, e);
        }
    }
}
