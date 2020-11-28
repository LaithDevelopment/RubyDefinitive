package com.mojang.minecraft.level;

import com.mojang.minecraft.HitResult;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.level.tile.Tile;
import java.util.Comparator;
import java.util.Collections;
import com.mojang.minecraft.Textures;
import org.lwjgl.opengl.GL11;
import com.mojang.minecraft.Player;
import java.util.ArrayList;
import java.util.List;

public class LevelRenderer implements LevelListener {
    public static final int MAX_REBUILDS_PER_FRAME = 8;
    public static final int CHUNK_SIZE = 16;
    private Level level;
    private Chunk[] chunks;
    private int xChunks;
    private int yChunks;
    private int zChunks;
    
    public LevelRenderer(final Level level) {
        (this.level = level).addListener(this);
        this.xChunks = level.width / 16;
        this.yChunks = level.depth / 16;
        this.zChunks = level.height / 16;
        this.chunks = new Chunk[this.xChunks * this.yChunks * this.zChunks];
        for (int x = 0; x < this.xChunks; ++x) {
            for (int y = 0; y < this.yChunks; ++y) {
                for (int z = 0; z < this.zChunks; ++z) {
                    final int x2 = x * 16;
                    final int y2 = y * 16;
                    final int z2 = z * 16;
                    int x3 = (x + 1) * 16;
                    int y3 = (y + 1) * 16;
                    int z3 = (z + 1) * 16;
                    if (x3 > level.width) {
                        x3 = level.width;
                    }
                    if (y3 > level.depth) {
                        y3 = level.depth;
                    }
                    if (z3 > level.height) {
                        z3 = level.height;
                    }
                    this.chunks[(x + y * this.xChunks) * this.zChunks + z] = new Chunk(level, x2, y2, z2, x3, y3, z3);
                }
            }
        }
    }
    
    public List<Chunk> getAllDirtyChunks() {
        ArrayList<Chunk> dirty = null;
        for (int i = 0; i < this.chunks.length; ++i) {
            final Chunk chunk = this.chunks[i];
            if (chunk.isDirty()) {
                if (dirty == null) {
                    dirty = (ArrayList<Chunk>)new ArrayList();
                }
                dirty.add(chunk);
            }
        }
        return (List<Chunk>)dirty;
    }
    
    public void render(final Player player, final int layer) {
        GL11.glEnable(3553);
        final int id = Textures.loadTexture("/terrain.png", 9728);
        GL11.glBindTexture(3553, id);
        final Frustum frustum = Frustum.getFrustum();
        for (int i = 0; i < this.chunks.length; ++i) {
            if (frustum.isVisible(this.chunks[i].aabb)) {
                this.chunks[i].render(layer);
            }
        }
        GL11.glDisable(3553);
    }
    
    public void updateDirtyChunks(final Player player) {
        final List<Chunk> dirty = this.getAllDirtyChunks();
        if (dirty == null) {
            return;
        }
        Collections.sort((List)dirty, (Comparator)new DirtyChunkSorter(player, Frustum.getFrustum()));
        for (int i = 0; i < 8 && i < dirty.size(); ++i) {
            ((Chunk)dirty.get(i)).rebuild();
        }
    }
    
    public void pick(final Player player, final Frustum frustum) {
        final Tesselator t = Tesselator.instance;
        final float r = 3.0f;
        final AABB box = player.bb.grow(r, r, r);
        final int x0 = (int)box.x0;
        final int x2 = (int)(box.x1 + 1.0f);
        final int y0 = (int)box.y0;
        final int y2 = (int)(box.y1 + 1.0f);
        final int z0 = (int)box.z0;
        final int z2 = (int)(box.z1 + 1.0f);
        GL11.glInitNames();
        GL11.glPushName(0);
        GL11.glPushName(0);
        for (int x3 = x0; x3 < x2; ++x3) {
            GL11.glLoadName(x3);
            GL11.glPushName(0);
            for (int y3 = y0; y3 < y2; ++y3) {
                GL11.glLoadName(y3);
                GL11.glPushName(0);
                for (int z3 = z0; z3 < z2; ++z3) {
                    final Tile tile = Tile.tiles[this.level.getTile(x3, y3, z3)];
                    if (tile != null && frustum.isVisible(tile.getTileAABB(x3, y3, z3))) {
                        GL11.glLoadName(z3);
                        GL11.glPushName(0);
                        for (int i = 0; i < 6; ++i) {
                            GL11.glLoadName(i);
                            t.init();
                            tile.renderFaceNoTexture(t, x3, y3, z3, i);
                            t.flush();
                        }
                        GL11.glPopName();
                    }
                }
                GL11.glPopName();
            }
            GL11.glPopName();
        }
        GL11.glPopName();
        GL11.glPopName();
    }
    
    public void renderHit(final HitResult h) {
        final Tesselator t = Tesselator.instance;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, ((float)Math.sin(System.currentTimeMillis() / 100.0) * 0.2f + 0.4f) * 0.5f);
        t.init();
        Tile.stoneBrick.renderFaceNoTexture(t, h.x, h.y, h.z, h.f);
        t.flush();
        GL11.glDisable(3042);
    }
    
    public void setDirty(int x0, int y0, int z0, int x1, int y1, int z1) {
        x0 /= 16;
        x1 /= 16;
        y0 /= 16;
        y1 /= 16;
        z0 /= 16;
        z1 /= 16;
        if (x0 < 0) {
            x0 = 0;
        }
        if (y0 < 0) {
            y0 = 0;
        }
        if (z0 < 0) {
            z0 = 0;
        }
        if (x1 >= this.xChunks) {
            x1 = this.xChunks - 1;
        }
        if (y1 >= this.yChunks) {
            y1 = this.yChunks - 1;
        }
        if (z1 >= this.zChunks) {
            z1 = this.zChunks - 1;
        }
        for (int x2 = x0; x2 <= x1; ++x2) {
            for (int y2 = y0; y2 <= y1; ++y2) {
                for (int z2 = z0; z2 <= z1; ++z2) {
                    this.chunks[(x2 + y2 * this.xChunks) * this.zChunks + z2].setDirty();
                }
            }
        }
    }
    
    public void tileChanged(final int x, final int y, final int z) {
        this.setDirty(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1);
    }
    
    public void lightColumnChanged(final int x, final int z, final int y0, final int y1) {
        this.setDirty(x - 1, y0 - 1, z - 1, x + 1, y1 + 1, z + 1);
    }
    
    public void allChanged() {
        this.setDirty(0, 0, 0, this.level.width, this.level.depth, this.level.height);
    }
}
