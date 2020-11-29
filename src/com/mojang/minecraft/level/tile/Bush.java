package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.level.Tesselator;
import java.util.Random;
import com.mojang.minecraft.level.Level;

public class Bush extends Tile {
    protected Bush(final int id) {
        super(id);
        this.tex = 15;
    }
    
    @Override
    public void tick(final Level level, final int x, final int y, final int z, final Random random) {
        final int below = level.getTile(x, y - 1, z);
        //if (!level.isLit(x, y, z) || (below != Tile.dirt.id && below != Tile.grass.id)) {
        //    level.setTile(x, y, z, 0);
        //}
    }
    
    @Override
    public void render(final Tesselator t, final Level level, final int layer, final int x, final int y, final int z) {
        if (level.isLit(x, y, z) ^ layer != 1) {
            return;
        }
        final int tex = this.getTexture(15);
        final float u0 = tex % 16 / 16.0f;
        final float u2 = u0 + 0.0624375f;
        final float v0 = tex / 16 / 16.0f;
        final float v2 = v0 + 0.0624375f;
        final int rots = 2;
        t.color(1.0f, 1.0f, 1.0f);
        for (int r = 0; r < rots; ++r) {
            final float xa = (float)(Math.sin(r * 3.141592653589793 / rots + 0.7853981633974483) * 0.5);
            final float za = (float)(Math.cos(r * 3.141592653589793 / rots + 0.7853981633974483) * 0.5);
            final float x2 = x + 0.5f - xa;
            final float x3 = x + 0.5f + xa;
            final float y2 = y + 0.0f;
            final float y3 = y + 1.0f;
            final float z2 = z + 0.5f - za;
            final float z3 = z + 0.5f + za;
            t.vertexUV(x2, y3, z2, u2, v0);
            t.vertexUV(x3, y3, z3, u0, v0);
            t.vertexUV(x3, y2, z3, u0, v2);
            t.vertexUV(x2, y2, z2, u2, v2);
            t.vertexUV(x3, y3, z3, u0, v0);
            t.vertexUV(x2, y3, z2, u2, v0);
            t.vertexUV(x2, y2, z2, u2, v2);
            t.vertexUV(x3, y2, z3, u0, v2);
        }
    }
    
    @Override
    public AABB getAABB(final int x, final int y, final int z) {
        return null;
    }
    
    @Override
    public boolean blocksLight() {
        return false;
    }
    
    @Override
    public boolean isSolid() {
        return false;
    }
}
