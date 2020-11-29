package com.mojang.minecraft.level;

import com.mojang.minecraft.Player;
import com.mojang.minecraft.level.tile.Tile;
import org.lwjgl.opengl.GL11;
import com.mojang.minecraft.phys.AABB;

public class Chunk {
    public AABB aabb;
    public final Level level;
    public final int x0;
    public final int y0;
    public final int z0;
    public final int x1;
    public final int y1;
    public final int z1;
    public final float x;
    public final float y;
    public final float z;
    private boolean dirty;
    private int lists;
    public long dirtiedTime;
    private static Tesselator t;
    public static int updates;
    private static long totalTime;
    private static int totalUpdates;
    
    static {
        Chunk.t = Tesselator.instance;
        Chunk.updates = 0;
        Chunk.totalTime = 0L;
        Chunk.totalUpdates = 0;
    }
    
    public Chunk(final Level level, final int x0, final int y0, final int z0, final int x1, final int y1, final int z1) {
        this.dirty = true;
        this.lists = -1;
        this.dirtiedTime = 0L;
        this.level = level;
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x = (x0 + x1) / 2.0f;
        this.y = (y0 + y1) / 2.0f;
        this.z = (z0 + z1) / 2.0f;
        this.aabb = new AABB((float)x0, (float)y0, (float)z0, (float)x1, (float)y1, (float)z1);
        this.lists = GL11.glGenLists(2);
    }
    
    private void rebuild(final int layer) {
        this.dirty = false;
        ++Chunk.updates;
        final long before = System.nanoTime();
        GL11.glNewList(this.lists + layer, 4864);
        Chunk.t.init();
        int tiles = 0;
        for (int x = this.x0; x < this.x1; ++x) {
            for (int y = this.y0; y < this.y1; ++y) {
                for (int z = this.z0; z < this.z1; ++z) {
                    final int tileId = this.level.getTile(x, y, z);
                    if (tileId > 0) {
                        Tile.tiles[tileId].render(Chunk.t, this.level, layer, x, y, z);
                        ++tiles;
                    }
                }
            }
        }
        Chunk.t.flush();
        GL11.glEndList();
        final long after = System.nanoTime();
        if (tiles > 0) {
            Chunk.totalTime += after - before;
            ++Chunk.totalUpdates;
        }
    }
    
    public void rebuild() {
        this.rebuild(0);
        this.rebuild(1);
    }
    
    public void render(final int layer) {
        GL11.glCallList(this.lists + layer);
    }
    
    public void setDirty() {
        if (!this.dirty) {
            this.dirtiedTime = System.currentTimeMillis();
        }
        this.dirty = true;
    }
    
    public boolean isDirty() {
        return this.dirty;
    }
    
    public float distanceToSqr(final Player player) {
        final float xd = player.x - this.x;
        final float yd = player.y - this.y;
        final float zd = player.z - this.z;
        return xd * xd + yd * yd + zd * zd;
    }
}
