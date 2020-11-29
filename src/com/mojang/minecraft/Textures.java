package com.mojang.minecraft;

import java.nio.ByteBuffer;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.io.IOException;
import org.lwjgl.util.glu.GLU;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import java.util.HashMap;

public class Textures {
    private static HashMap<String, Integer> idMap;
    
    static {
        Textures.idMap = (HashMap<String, Integer>)new HashMap();
    }
    
    public static int loadTexture(final String resourceName, final int mode) {
        try {
            if (Textures.idMap.containsKey(resourceName)) {
                return (int)Textures.idMap.get(resourceName);
            }
            final IntBuffer ib = BufferUtils.createIntBuffer(1);
            ib.clear();
            GL11.glGenTextures(ib);
            final int id = ib.get(0);
            Textures.idMap.put(resourceName, id);
            System.out.println(String.valueOf(resourceName) + " -> " + id);
            GL11.glBindTexture(3553, id);
            GL11.glTexParameteri(3553, 10241, mode);
            GL11.glTexParameteri(3553, 10240, mode);
            final BufferedImage img = ImageIO.read(Textures.class.getResourceAsStream(resourceName));
            final int w = img.getWidth();
            final int h = img.getHeight();
            final ByteBuffer pixels = BufferUtils.createByteBuffer(w * h * 4);
            final int[] rawPixels = new int[w * h];
            img.getRGB(0, 0, w, h, rawPixels, 0, w);
            for (int i = 0; i < rawPixels.length; ++i) {
                final int a = rawPixels[i] >> 24 & 0xFF;
                final int r = rawPixels[i] >> 16 & 0xFF;
                final int g = rawPixels[i] >> 8 & 0xFF;
                final int b = rawPixels[i] & 0xFF;
                rawPixels[i] = (a << 24 | b << 16 | g << 8 | r);
            }
            pixels.asIntBuffer().put(rawPixels);
            GLU.gluBuild2DMipmaps(3553, 6408, w, h, 6408, 5121, pixels);
            return id;
        }
        catch (IOException e) {
            throw new RuntimeException("!!");
        }
    }
}
