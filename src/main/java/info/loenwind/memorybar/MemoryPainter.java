package info.loenwind.memorybar;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

/**
 * Code adapted from splash screen code by mezz. Thanks to mezz for giving permission to copy it.
 *
 */
public class MemoryPainter {

  private static int getHex(String name, int def) {
    return def;
  }

  private static int fontColor = getHex("font", 0xFF000000);
  private static int barBorderColor = getHex("barBorder", 0xFFC0C0C0);
  private static int barBackgroundColor = getHex("barBackground", 0xFFFFFFFF);
  private static int memoryGoodColor = getHex("memoryGood", 0xFF78CB34);
  private static int memoryWarnColor = getHex("memoryWarn", 0xFFE6E84A);
  private static int memoryLowColor = getHex("memoryLow", 0xFFE42F2F);

  private static final int barWidth = 400;
  private static final int barHeight = 20 / 2 - 1;
  private static final float[] points = { .25f, .5f, .75f, .8f, .85f, .9f, .95f };

  private static float memoryColorPercent;
  private static long memoryColorChangeTime;

  public static void drawMemoryBar(int width) {
    int maxMemory = bytesToMb(Runtime.getRuntime().maxMemory());
    int totalMemory = bytesToMb(Runtime.getRuntime().totalMemory());
    int freeMemory = bytesToMb(Runtime.getRuntime().freeMemory());
    int usedMemory = totalMemory - freeMemory;
    float usedMemoryPercent = usedMemory / (float) maxMemory;

    FontRenderer fontRenderer = Minecraft.getMinecraft().getRenderManager().getFontRenderer();

    int actualBarWidth = MemoryPainter.barWidth < (width - 2) ? MemoryPainter.barWidth : (width - 2);
    int xOffset = (width - actualBarWidth) / 2;
    int yOffset = 4;
    int bgTransparency = 0x3FFFFFFF;

    if (Minecraft.getMinecraft().gameSettings.showDebugInfo) {
      actualBarWidth = width / 2 - 2;
      xOffset = width / 2;
      yOffset = 10;
      bgTransparency = 0xFFFFFFFF;
    }

    long time = System.currentTimeMillis();
    if (usedMemoryPercent > memoryColorPercent || (time - memoryColorChangeTime > 1000)) {
      memoryColorChangeTime = time;
      memoryColorPercent = usedMemoryPercent;
    }

    int memoryBarColor;
    if (memoryColorPercent < 0.75f) {
      memoryBarColor = memoryGoodColor;
    } else if (memoryColorPercent < 0.85f) {
      memoryBarColor = memoryWarnColor;
    } else {
      memoryBarColor = memoryLowColor;
    }

    glPushMatrix();
    glTranslatef(xOffset, yOffset, 0);
    GL11.glDisable(GL_TEXTURE_2D);

    // border
    glPushMatrix();
    setColor(barBorderColor);
    drawBorder(actualBarWidth, barHeight);

    // interior
    setColor(barBackgroundColor & bgTransparency);
    glTranslatef(1, 1, 0);
    drawBox(actualBarWidth - 2, barHeight - 2);

    // sections
    for (float f : points) {
      glPushMatrix();
      setColor(barBorderColor & 0x7FFFFFFF);
      glTranslatef((int) ((actualBarWidth - 2) * f - 2), 0, 0);
      drawBox(1, barHeight - 2);
      glPopMatrix();
    }

    // slidy part
    glPushMatrix();
    setColor(memoryLowColor);
    glTranslatef((actualBarWidth - 2) * (totalMemory) / (maxMemory) - 2, 0, 0);
    drawBox(1, barHeight - 2);
    glPopMatrix();

    // bar
    setColor(memoryBarColor);
    drawBox((actualBarWidth - 2) * (usedMemory) / (maxMemory), barHeight - 2);
    glPopMatrix();

    // progress text
    glPushMatrix();
    String progress = getMemoryString(usedMemory) + " / " + getMemoryString(maxMemory);
    final int stringWidth = fontRenderer.getStringWidth(progress);
    glTranslatef((actualBarWidth - stringWidth) / 2, 1, 0);
    setColor(fontColor);
    glEnable(GL_TEXTURE_2D);
    fontRenderer.drawString(progress, 0, 0, 0x000000);
    glPopMatrix();

    glPopMatrix();
  }

  private static String getMemoryString(int memory) {
    return StringUtils.leftPad(Integer.toString(memory), 4, ' ') + " MB";
  }

  private static int bytesToMb(long bytes) {
    return (int) (bytes / 1024L / 1024L);
  }

  private static void setColor(int color) {
    GL11.glColor4ub((byte) ((color >> 16) & 0xFF), (byte) ((color >> 8) & 0xFF), (byte) (color & 0xFF), (byte) ((color >> 24) & 0xFF));
  }

  private static void drawBox(int w, int h) {
    glBegin(GL_QUADS);
    glVertex2f(0, 0);
    glVertex2f(0, h);
    glVertex2f(w, h);
    glVertex2f(w, 0);
    glEnd();
  }

  private static void drawBorder(int w, int h) {
    glBegin(GL_QUADS);
    glVertex2f(0, 0);
    glVertex2f(0, 1);
    glVertex2f(w, 1);
    glVertex2f(w, 0);
    glVertex2f(0, h - 1);
    glVertex2f(0, h);
    glVertex2f(w, h);
    glVertex2f(w, h - 1);
    glVertex2f(0, 0);
    glVertex2f(0, h);
    glVertex2f(1, h);
    glVertex2f(1, 0);
    glVertex2f(w - 1, 0);
    glVertex2f(w - 1, h);
    glVertex2f(w, h);
    glVertex2f(w, 0);
    glEnd();
  }

}
