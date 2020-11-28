package com.mojang.minecraft.level;

import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;

public class Tesselator {
    private static final int MAX_MEMORY_USE = 4194304;
    private static final int MAX_FLOATS = 524288;
    private FloatBuffer buffer;
    private float[] array;
    private int vertices;
    private float u;
    private float v;
    private float r;
    private float g;
    private float b;
    private boolean hasColor;
    private boolean hasTexture;
    private int len;
    private int p;
    public static Tesselator instance;
    
    static {
        Tesselator.instance = new Tesselator();
    }
    
    private Tesselator() {
        this.buffer = BufferUtils.createFloatBuffer(524288);
        this.array = new float[524288];
        this.vertices = 0;
        this.hasColor = false;
        this.hasTexture = false;
        this.len = 3;
        this.p = 0;
    }
    
    public void flush() {
        this.buffer.clear();
        this.buffer.put(this.array, 0, this.p);
        this.buffer.flip();
        if (this.hasTexture && this.hasColor) {
            GL11.glInterleavedArrays(10794, 0, this.buffer);
        }
        else if (this.hasTexture) {
            GL11.glInterleavedArrays(10791, 0, this.buffer);
        }
        else if (this.hasColor) {
            GL11.glInterleavedArrays(10788, 0, this.buffer);
        }
        else {
            GL11.glInterleavedArrays(10785, 0, this.buffer);
        }
        GL11.glEnableClientState(32884);
        if (this.hasTexture) {
            GL11.glEnableClientState(32888);
        }
        if (this.hasColor) {
            GL11.glEnableClientState(32886);
        }
        GL11.glDrawArrays(7, 0, this.vertices);
        GL11.glDisableClientState(32884);
        if (this.hasTexture) {
            GL11.glDisableClientState(32888);
        }
        if (this.hasColor) {
            GL11.glDisableClientState(32886);
        }
        this.clear();
    }
    
    private void clear() {
        this.vertices = 0;
        this.buffer.clear();
        this.p = 0;
    }
    
    public void init() {
        this.clear();
        this.hasColor = false;
        this.hasTexture = false;
    }
    
    public void tex(final float u, final float v) {
        if (!this.hasTexture) {
            this.len += 2;
        }
        this.hasTexture = true;
        this.u = u;
        this.v = v;
    }
    
    public void color(final float r, final float g, final float b) {
        if (!this.hasColor) {
            this.len += 3;
        }
        this.hasColor = true;
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public void vertexUV(final float x, final float y, final float z, final float u, final float v) {
        this.tex(u, v);
        this.vertex(x, y, z);
    }
    
    public void vertex(final float x, final float y, final float z) {
        if (this.hasTexture) {
            this.array[this.p++] = this.u;
            this.array[this.p++] = this.v;
        }
        if (this.hasColor) {
            this.array[this.p++] = this.r;
            this.array[this.p++] = this.g;
            this.array[this.p++] = this.b;
        }
        this.array[this.p++] = x;
        this.array[this.p++] = y;
        this.array[this.p++] = z;
        ++this.vertices;
        if (this.vertices % 4 == 0 && this.p >= 524288 - this.len * 4) {
            this.flush();
        }
    }
}
