package com.mojang.minecraft.gui;

import com.mojang.minecraft.Tesselator11a;
import org.lwjgl.opengl.GL11;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.mojang.minecraft.Textures;

public class Font {
    private int[] charWidths;
    private int fontTexture;
    
    public Font(final String name, final Textures textures) {
        this.charWidths = new int[256];
        this.fontTexture = 0;
        BufferedImage img;
        try {
            img = ImageIO.read(Textures.class.getResourceAsStream(name));
        }
        catch (IOException e) {
            throw new RuntimeException((Throwable)e);
        }
        final int w = img.getWidth();
        final int h = img.getHeight();
        final int[] rawPixels = new int[w * h];
        img.getRGB(0, 0, w, h, rawPixels, 0, w);
        for (int i = 0; i < 128; ++i) {
            final int xt = i % 16;
            final int yt = i / 16;
            int x = 0;
            for (boolean emptyColumn = false; x < 8 && !emptyColumn; ++x) {
                final int xPixel = xt * 8 + x;
                emptyColumn = true;
                for (int y = 0; y < 8 && emptyColumn; ++y) {
                    final int yPixel = (yt * 8 + y) * w;
                    final int pixel = rawPixels[xPixel + yPixel] & 0xFF;
                    if (pixel > 128) {
                        emptyColumn = false;
                    }
                }
            }
            if (i == 32) {
                x = 4;
            }
            this.charWidths[i] = x;
        }
        this.fontTexture = textures.loadTexture(name, 9728);
    }
    
    public void drawShadow(final String str, final int x, final int y, final int color) {
        this.draw(str, x + 1, y + 1, color, true);
        this.draw(str, x, y, color);
    }
    
    public void draw(final String str, final int x, final int y, final int color) {
        this.draw(str, x, y, color, false);
    }
    
    public void draw(final String str, final int x, final int y, int color, final boolean darken) {
        final char[] chars = str.toCharArray();
        if (darken) {
            color = (color & 0xFCFCFC) >> 2;
        }
        GL11.glEnable(3553);
        GL11.glBindTexture(3553, this.fontTexture);
        final Tesselator11a t = Tesselator11a.instance;
        t.init();
        t.color(color);
        int xo = 0;
        for (int i = 0; i < chars.length; ++i) {
            if (chars[i] == '&') {
                final int cc = "0123456789abcdef".indexOf((int)chars[i + 1]);
                final int br = (cc & 0x8) * 8;
                final int b = (cc & 0x1) * 191 + br;
                final int g = ((cc & 0x2) >> 1) * 191 + br;
                final int r = ((cc & 0x4) >> 2) * 191 + br;
                color = (r << 16 | g << 8 | b);
                i += 2;
                if (darken) {
                    color = (color & 0xFCFCFC) >> 2;
                }
                t.color(color);
            }
            final int ix = chars[i] % '\u0010' * 8;
            final int iy = chars[i] / '\u0010' * 8;
            t.vertexUV((float)(x + xo), (float)(y + 8), 0.0f, ix / 128.0f, (iy + 8) / 128.0f);
            t.vertexUV((float)(x + xo + 8), (float)(y + 8), 0.0f, (ix + 8) / 128.0f, (iy + 8) / 128.0f);
            t.vertexUV((float)(x + xo + 8), (float)y, 0.0f, (ix + 8) / 128.0f, iy / 128.0f);
            t.vertexUV((float)(x + xo), (float)y, 0.0f, ix / 128.0f, iy / 128.0f);
            xo += this.charWidths[chars[i]];
        }
        t.flush();
        GL11.glDisable(3553);
    }
    
    public int width(final String str) {
        final char[] chars = str.toCharArray();
        int len = 0;
        for (int i = 0; i < chars.length; ++i) {
            if (chars[i] == '&') {
                ++i;
            }
            else {
                len += this.charWidths[chars[i]];
            }
        }
        return len;
    }
}
