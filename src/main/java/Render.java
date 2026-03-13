
public class Render {
    public final int width;
    public final int height;
    public final int[] pixels;

    public Render(int w, int h) {
        this.width = w;
        this.height = h;
        this.pixels = new int[w * h];
    }

    public void drawSprite(
            int[] src, int srcW, int srcH,
            int x, int y
    ) {
        drawSpriteTo(pixels, src, srcW, srcH, x, y);
    }

    private void drawSpriteTo(
            int[] dst,
            int[] src, int srcW, int srcH,
            int dstX, int dstY
    ) {
        for (int y = 0; y < srcH; y++) {
            int dy = dstY + y;
            if (dy < 0 || dy >= height) continue;

            int srcRow = y * srcW;
            int dstRow = dy * width;

            for (int x = 0; x < srcW; x++) {
                int dx = dstX + x;
                if (dx < 0 || dx >= width) continue;

                int c = src[srcRow + x];
                if ((c >>> 24) == 0) continue;
                dst[dstRow + dx] = c;
            }
        }
    }
}
