import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

public class Game {
    public static void main(String[] args){
        final int INTERNAL_W = 1920;
        final int INTERNAL_H = 1080;

        if (!GLFW.glfwInit()) throw new IllegalStateException("GLFW init failed");

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);

        long window = GLFW.glfwCreateWindow(1920, 1080, "Bester Editor Number 1", 0, 0);
        if (window == 0) throw new RuntimeException("Window creation failed");

        Render renderer = new Render(INTERNAL_W, INTERNAL_H);
        Journey journey = new Journey(window);

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        // Set viewport to the actual framebuffer size (important for HiDPI) Keine Ahnung was das bedeutet
        int[] fbW = new int[1];
        int[] fbH = new int[1];
        GLFW.glfwGetFramebufferSize(window, fbW, fbH);
        GL11.glViewport(0, 0, fbW[0], fbH[0]);

        int frameTex = createTexture(INTERNAL_W, INTERNAL_H);
        IntBuffer scratch = MemoryUtil.memAllocInt(INTERNAL_W * INTERNAL_H);

        final double TARGET_FPS = 60.0;
        final double FRAME_TIME = 1.0 / TARGET_FPS;
        double lastTime = GLFW.glfwGetTime();

        while (!GLFW.glfwWindowShouldClose(window)) {
            double now = GLFW.glfwGetTime();
            double delta = now - lastTime;

            if (delta >= FRAME_TIME) {
                lastTime = now;

                //Game Logic
                journey.render(renderer);

                uploadTexture(frameTex, INTERNAL_W, INTERNAL_H, renderer.getPixels(), scratch);

                drawFullscreenTexturedQuad(frameTex);

                GLFW.glfwSwapBuffers(window);
                GLFW.glfwPollEvents();
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignored) {
                }
            }
        }

        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();

        MemoryUtil.memFree(scratch);
    }
    static int createTexture(int w, int h) {
        int tex = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);

        // Pixel-art: no blur
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        // Clamp edges (nice for scaling)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        // Allocate storage (no data yet)
        GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8,
                w, h, 0,
                GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV,
                (java.nio.ByteBuffer) null
        );

        return tex;
    }

    static void uploadTexture(int tex, int w, int h, int[] argbPixels, IntBuffer scratch) {
        scratch.clear();
        scratch.put(argbPixels);
        scratch.flip();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
        GL11.glTexSubImage2D(
                GL11.GL_TEXTURE_2D, 0, 0, 0, w, h,
                GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV,
                scratch
        );
    }

    static void drawFullscreenTexturedQuad(int tex) {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);

        // Use normalized device coords [-1..1] so it always fills the screen
        GL11.glBegin(GL11.GL_QUADS);

        // Note: these texcoords flip Y so the image isn't upside down.
        GL11.glTexCoord2f(0f, 1f);
        GL11.glVertex2f(-1f, -1f);
        GL11.glTexCoord2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);
        GL11.glTexCoord2f(1f, 0f);
        GL11.glVertex2f(1f, 1f);
        GL11.glTexCoord2f(0f, 0f);
        GL11.glVertex2f(-1f, 1f);

        GL11.glEnd();
    }
}
