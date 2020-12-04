package com.mojang.minecraft.level.tile;

import java.util.Random;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.Tesselator;
import com.mojang.minecraft.phys.AABB;

public class CalmLiquidTile extends LiquidTile {
    protected CalmLiquidTile(final int id, final int tex) {
        super(id, tex);
        this.tileId = id - 1;
        this.calmTileId = id;
    }
    
    @Override
    public void tick(final Level level, final int x, final int y, final int z, final Random random) {
        if (level.getTile(x - 1, y, z) == 0) {
            level.setTile(x-1, y, z, this.calmTileId);
        }
        if (level.getTile(x + 1, y, z) == 0) {
            level.setTile(x+1, y, z, this.calmTileId);
        }
        if (level.getTile(x, y, z - 1) == 0) {
            level.setTile(x, y, z-1, this.calmTileId);
        }
        if (level.getTile(x, y, z + 1) == 0) {
            level.setTile(x, y, z+1, this.calmTileId);
        }
        if (level.getTile(x, y - 1, z) == 0) {
            level.setTile(x, y - 1, z, this.tileId);
        }
        if (level.getTile(x, y + 1, z) != 0) {
            level.setTile(x, y, z, this.tileId);
        }
        if(tileId == 3 && level.getTile(x, y+1, z) == 5 || tileId == 3 && level.getTile(x, y+1, z) == 6) {
        	level.setTile(x, y, z, 5);
        }
        if(tileId == 5 && level.getTile(x, y+1, z) == 3 || tileId == 5 && level.getTile(x, y+1, z) == 4) {
        	level.setTile(x, y, z, 3);
        }
        if(this.calmTileId == 4) {
            if (level.getTile(x - 1, y, z) == 6) {
                level.setTile(x-1, y, z, 2);
            }
            if (level.getTile(x + 1, y, z) == 6) {
                level.setTile(x+1, y, z, 2);
            }
            if (level.getTile(x, y, z - 1) == 6) {
                level.setTile(x, y, z-1, 2);
            }
            if (level.getTile(x, y, z + 1) == 6) {
                level.setTile(x, y, z+1, 2);
            }
        }
        if(this.tileId == 3) {
            if (level.getTile(x - 1, y, z) == 5) {
                level.setTile(x-1, y, z, 2);
            }
            if (level.getTile(x + 1, y, z) == 5) {
                level.setTile(x+1, y, z, 2);
            }
            if (level.getTile(x, y, z - 1) == 5) {
                level.setTile(x, y, z-1, 2);
            }
            if (level.getTile(x, y, z + 1) == 5) {
                level.setTile(x, y, z+1, 2);
            }
        }
    }
    
    @Override
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
        if (face == 0) { //bottom
            t.vertexUV(x2, y2, z3, u0, v2);
            t.vertexUV(x2, y2, z2, u0, v0);
            t.vertexUV(x3, y2, z2, u2, v0);
            t.vertexUV(x3, y2, z3, u2, v2);
        }
        if (face == 1) { //top
            t.vertexUV(x3, y3-0.1F, z3, u2, v2);
            t.vertexUV(x3, y3-0.1F, z2, u2, v0);
            t.vertexUV(x2, y3-0.1F, z2, u0, v0);
            t.vertexUV(x2, y3-0.1F, z3, u0, v2);
        }
        if (face == 2) {
            t.vertexUV(x2, y3-0.1F, z2, u2, v0);
            t.vertexUV(x3, y3-0.1F, z2, u0, v0);
            t.vertexUV(x3, y2-0.1F, z2, u0, v2);
            t.vertexUV(x2, y2-0.1F, z2, u2, v2);
        }
        if (face == 3) {
            t.vertexUV(x2, y3-0.1F, z3, u0, v0);
            t.vertexUV(x2, y2-0.1F, z3, u0, v2);
            t.vertexUV(x3, y2-0.1F, z3, u2, v2);
            t.vertexUV(x3, y3-0.1F, z3, u2, v0);
        }
        if (face == 4) {
            t.vertexUV(x2, y3-0.1F, z3, u2, v0);
            t.vertexUV(x2, y3-0.1F, z2, u0, v0);
            t.vertexUV(x2, y2-0.1F, z2, u0, v2);
            t.vertexUV(x2, y2-0.1F, z3, u2, v2);
        }
        if (face == 5) {
            t.vertexUV(x3, y2-0.1F, z3, u0, v2);
            t.vertexUV(x3, y2-0.1F, z2, u2, v2);
            t.vertexUV(x3, y3-0.1F, z2, u2, v0);
            t.vertexUV(x3, y3-0.1F, z3, u0, v0);
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