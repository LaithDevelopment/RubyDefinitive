package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.particle.Particle;
import com.mojang.minecraft.particle.ParticleEngine;
import java.util.Random;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.RubyDung;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.Tesselator;

public class Tile {
    public static final Tile[] tiles;
    public static final Tile empty;
    public static final Tile grass;
    public static final Tile rock;
    public int tex;
    public final int id;
    
    static {
        tiles = new Tile[256];
        empty = null;
        rock = new Tile(2, 1);
        grass = new Tile(1, 0);
    }
    
    protected Tile(final int id) {
        Tile.tiles[id] = this;
        this.id = id;
    }
    
    protected Tile(final int id, final int tex) {
        this(id);
        this.tex = tex;
    }
    
    public void render(final Tesselator t, final Level level, final int layer, final int x, final int y, final int z) {
        final float c1 = 1.0f;
        final float c2 = 0.8f;
        final float c3 = 0.6f;
        if (this.shouldRenderFace(level, x, y - 1, z, layer)) {
            t.color(c1, c1, c1);
            this.renderFace(t, x, y, z, 0);
        }
        if (this.shouldRenderFace(level, x, y + 1, z, layer)) {
            t.color(c1, c1, c1);
            this.renderFace(t, x, y, z, 1);
        }
        if (this.shouldRenderFace(level, x, y, z - 1, layer)) {
            t.color(c2, c2, c2);
            this.renderFace(t, x, y, z, 2);
        }
        if (this.shouldRenderFace(level, x, y, z + 1, layer)) {
            t.color(c2, c2, c2);
            this.renderFace(t, x, y, z, 3);
        }
        if (this.shouldRenderFace(level, x - 1, y, z, layer)) {
            t.color(c3, c3, c3);
            this.renderFace(t, x, y, z, 4);
        }
        if (this.shouldRenderFace(level, x + 1, y, z, layer)) {
            t.color(c3, c3, c3);
            this.renderFace(t, x, y, z, 5);
        }
    }
    
    private boolean shouldRenderFace(final Level level, final int x, final int y, final int z, final int layer) {
        return !level.isSolidTile(x, y, z) && (level.isLit(x, y, z) ^ layer == 1);
    }
    
    protected int getTexture(final int face) {
        return this.tex;
    }
    
    public void renderFace(final Tesselator t, final int x, final int y, final int z, final int face) {
        final int tex = this.getTexture(face);
        final float u0 = tex % 16 / 16.0f;
        final float u2 = u0 + 0.0624375f;
        final float v0 = tex / 16 / 16.0f;
        final float v2 = v0 + 0.0624375f;
        final float x2 = x + 0.0f;
        final float x3 = x + 1.0f;
        final float y2 = y + 0.0f;
        final float y3 = y + 1.0f;
        final float z2 = z + 0.0f;
        final float z3 = z + 1.0f;
        if (face == 0) {
            t.vertexUV(x2, y2, z3, u0, v2);
            t.vertexUV(x2, y2, z2, u0, v0);
            t.vertexUV(x3, y2, z2, u2, v0);
            t.vertexUV(x3, y2, z3, u2, v2);
        }
        if (face == 1) {
            t.vertexUV(x3, y3, z3, u2, v2);
            t.vertexUV(x3, y3, z2, u2, v0);
            t.vertexUV(x2, y3, z2, u0, v0);
            t.vertexUV(x2, y3, z3, u0, v2);
        }
        if (face == 2) {
            t.vertexUV(x2, y3, z2, u2, v0);
            t.vertexUV(x3, y3, z2, u0, v0);
            t.vertexUV(x3, y2, z2, u0, v2);
            t.vertexUV(x2, y2, z2, u2, v2);
        }
        if (face == 3) {
            t.vertexUV(x2, y3, z3, u0, v0);
            t.vertexUV(x2, y2, z3, u0, v2);
            t.vertexUV(x3, y2, z3, u2, v2);
            t.vertexUV(x3, y3, z3, u2, v0);
        }
        if (face == 4) {
            t.vertexUV(x2, y3, z3, u2, v0);
            t.vertexUV(x2, y3, z2, u0, v0);
            t.vertexUV(x2, y2, z2, u0, v2);
            t.vertexUV(x2, y2, z3, u2, v2);
        }
        if (face == 5) {
            t.vertexUV(x3, y2, z3, u0, v2);
            t.vertexUV(x3, y2, z2, u2, v2);
            t.vertexUV(x3, y3, z2, u2, v0);
            t.vertexUV(x3, y3, z3, u0, v0);
        }
    }
    
    public void renderFaceNoTexture(final Tesselator t, final int x, final int y, final int z, final int face) {
        final float x2 = x + 0.0f;
        final float x3 = x + 1.0f;
        final float y2 = y + 0.0f;
        final float y3 = y + 1.0f;
        final float z2 = z + 0.0f;
        final float z3 = z + 1.0f;
        if (face == 0) {
            t.vertex(x2, y2, z3);
            t.vertex(x2, y2, z2);
            t.vertex(x3, y2, z2);
            t.vertex(x3, y2, z3);
        }
        if (face == 1) {
            t.vertex(x3, y3, z3);
            t.vertex(x3, y3, z2);
            t.vertex(x2, y3, z2);
            t.vertex(x2, y3, z3);
        }
        if (face == 2) {
            t.vertex(x2, y3, z2);
            t.vertex(x3, y3, z2);
            t.vertex(x3, y2, z2);
            t.vertex(x2, y2, z2);
        }
        if (face == 3) {
            t.vertex(x2, y3, z3);
            t.vertex(x2, y2, z3);
            t.vertex(x3, y2, z3);
            t.vertex(x3, y3, z3);
        }
        if (face == 4) {
            t.vertex(x2, y3, z3);
            t.vertex(x2, y3, z2);
            t.vertex(x2, y2, z2);
            t.vertex(x2, y2, z3);
        }
        if (face == 5) {
            t.vertex(x3, y2, z3);
            t.vertex(x3, y2, z2);
            t.vertex(x3, y3, z2);
            t.vertex(x3, y3, z3);
        }
    }
    
    public final AABB getTileAABB(final int x, final int y, final int z) {
        return new AABB((float)x, (float)y, (float)z, (float)(x + 1), (float)(y + 1), (float)(z + 1));
    }
    
    public AABB getAABB(final int x, final int y, final int z) {
        return new AABB((float)x, (float)y, (float)z, (float)(x + 1), (float)(y + 1), (float)(z + 1));
    }
    
    public boolean blocksLight() {
        return true;
    }
    
    public boolean isSolid() {
        return true;
    }
    
    public void tick(final Level level, final int x, final int y, final int z, final Random random) {
    }
    
    public void destroy(final Level level, final int x, final int y, final int z, final ParticleEngine particleEngine) {
        for (int SD = 4, xx = 0; xx < SD; ++xx) {
            for (int yy = 0; yy < SD; ++yy) {
                for (int zz = 0; zz < SD; ++zz) {
                    final float xp = x + (xx + 0.5f) / SD;
                    final float yp = y + (yy + 0.5f) / SD;
                    final float zp = z + (zz + 0.5f) / SD;
                    if(RubyDung.OLDWORLD_ENABLED == false) {
                        particleEngine.add(new Particle(level, xp, yp, zp, xp - x - 0.5f, yp - y - 0.5f, zp - z - 0.5f, this.tex));
                    }
                }
            }
        }
    }
}
