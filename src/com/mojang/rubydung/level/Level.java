package com.mojang.minecraft.level;

import com.mojang.minecraft.phys.AABB;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.FileOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.io.FileInputStream;
import java.io.File;
import com.mojang.minecraft.level.tile.Tile;
import java.util.Random;
import java.util.ArrayList;

public class Level {
    private static final int TILE_UPDATE_INTERVAL = 400;
    public final int width;
    public final int height;
    public final int depth;
    private byte[] blocks;
    private int[] lightDepths;
    private ArrayList<LevelListener> levelListeners;
    private Random random;
    int unprocessed;
    
    public Level(final int w, final int h, final int d) {
        this.levelListeners = (ArrayList<LevelListener>)new ArrayList();
        this.random = new Random();
        this.unprocessed = 0;
        this.width = w;
        this.height = h;
        this.depth = d;
        this.blocks = new byte[w * h * d];
        this.lightDepths = new int[w * h];
        final boolean mapLoaded = this.load();
        if (!mapLoaded) {
            this.generateMap();
        }
        this.calcLightDepths(0, 0, w, h);
    }
    
    private void generateMap() {
        final int w = this.width;
        final int h = this.height;
        final int d = this.depth;
        final int[] heightmap1 = new PerlinNoiseFilter(0).read(w, h);
        final int[] heightmap2 = new PerlinNoiseFilter(0).read(w, h);
        final int[] cf = new PerlinNoiseFilter(1).read(w, h);
        final int[] rockMap = new PerlinNoiseFilter(1).read(w, h);
        for (int x = 0; x < w; ++x) {
            for (int y = 0; y < d; ++y) {
                for (int z = 0; z < h; ++z) {
                    final int dh1 = heightmap1[x + z * this.width];
                    int dh2 = heightmap2[x + z * this.width];
                    final int cfh = cf[x + z * this.width];
                    if (cfh < 128) {
                        dh2 = dh1;
                    }
                    int dh3 = dh1;
                    if (dh2 > dh3) {
                        dh3 = dh2;
                    }
                    else {
                        dh2 = dh1;
                    }
                    dh3 = dh3 / 8 + d / 3;
                    int rh = rockMap[x + z * this.width] / 8 + d / 3;
                    if (rh > dh3 - 2) {
                        rh = dh3 - 2;
                    }
                    final int i = (y * this.height + z) * this.width + x;
                    int id = 0;
                    if (y == dh3) {
                        id = Tile.grass.id;
                    }
                    if (y < dh3) {
                        id = Tile.stoneBrick.id;
                    }
                    if (y <= rh) {
                        id = Tile.stoneBrick.id;
                    }
                    this.blocks[i] = (byte)id;
                }
            }
        }
    }
    
    public boolean load() {
        try {
            final DataInputStream dis = new DataInputStream((InputStream)new GZIPInputStream((InputStream)new FileInputStream(new File("level.dat"))));
            dis.readFully(this.blocks);
            this.calcLightDepths(0, 0, this.width, this.height);
            for (int i = 0; i < this.levelListeners.size(); ++i) {
                ((LevelListener)this.levelListeners.get(i)).allChanged();
            }
            dis.close();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void save() {
        try {
            final DataOutputStream dos = new DataOutputStream((OutputStream)new GZIPOutputStream((OutputStream)new FileOutputStream(new File("level.dat"))));
            dos.write(this.blocks);
            dos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void calcLightDepths(final int x0, final int y0, final int x1, final int y1) {
        for (int x2 = x0; x2 < x0 + x1; ++x2) {
            for (int z = y0; z < y0 + y1; ++z) {
                final int oldDepth = this.lightDepths[x2 + z * this.width];
                int y2;
                for (y2 = this.depth - 1; y2 > 0 && !this.isLightBlocker(x2, y2, z); --y2) {}
                if (oldDepth != (this.lightDepths[x2 + z * this.width] = y2)) {
                    final int yl0 = (oldDepth < y2) ? oldDepth : y2;
                    final int yl2 = (oldDepth > y2) ? oldDepth : y2;
                    for (int i = 0; i < this.levelListeners.size(); ++i) {
                        ((LevelListener)this.levelListeners.get(i)).lightColumnChanged(x2, z, yl0, yl2);
                    }
                }
            }
        }
    }
    
    public void addListener(final LevelListener levelListener) {
        this.levelListeners.add(levelListener);
    }
    
    public void removeListener(final LevelListener levelListener) {
        this.levelListeners.remove(levelListener);
    }
    
    public boolean isLightBlocker(final int x, final int y, final int z) {
        final Tile tile = Tile.tiles[this.getTile(x, y, z)];
        return tile != null && tile.blocksLight();
    }
    
    public ArrayList<AABB> getCubes(final AABB aABB) {
        final ArrayList<AABB> aABBs = (ArrayList<AABB>)new ArrayList();
        int x0 = (int)aABB.x0;
        int x2 = (int)(aABB.x1 + 1.0f);
        int y0 = (int)aABB.y0;
        int y2 = (int)(aABB.y1 + 1.0f);
        int z0 = (int)aABB.z0;
        int z2 = (int)(aABB.z1 + 1.0f);
        if (x0 < 0) {
            x0 = 0;
        }
        if (y0 < 0) {
            y0 = 0;
        }
        if (z0 < 0) {
            z0 = 0;
        }
        if (x2 > this.width) {
            x2 = this.width;
        }
        if (y2 > this.depth) {
            y2 = this.depth;
        }
        if (z2 > this.height) {
            z2 = this.height;
        }
        for (int x3 = x0; x3 < x2; ++x3) {
            for (int y3 = y0; y3 < y2; ++y3) {
                for (int z3 = z0; z3 < z2; ++z3) {
                    final Tile tile = Tile.tiles[this.getTile(x3, y3, z3)];
                    if (tile != null) {
                        final AABB aabb = tile.getAABB(x3, y3, z3);
                        if (aabb != null) {
                            aABBs.add(aabb);
                        }
                    }
                }
            }
        }
        return aABBs;
    }
    
    public boolean setTile(final int x, final int y, final int z, final int type) {
        if (x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.depth || z >= this.height) {
            return false;
        }
        if (type == this.blocks[(y * this.height + z) * this.width + x]) {
            return false;
        }
        this.blocks[(y * this.height + z) * this.width + x] = (byte)type;
        this.calcLightDepths(x, z, 1, 1);
        for (int i = 0; i < this.levelListeners.size(); ++i) {
            ((LevelListener)this.levelListeners.get(i)).tileChanged(x, y, z);
        }
        return true;
    }
    
    public boolean isLit(final int x, final int y, final int z) {
        return x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.depth || z >= this.height || y >= this.lightDepths[x + z * this.width];
    }
    
    public int getTile(final int x, final int y, final int z) {
        if (x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.depth || z >= this.height) {
            return 0;
        }
        return this.blocks[(y * this.height + z) * this.width + x];
    }
    
    public boolean isSolidTile(final int x, final int y, final int z) {
        final Tile tile = Tile.tiles[this.getTile(x, y, z)];
        return tile != null && tile.isSolid();
    }
    
    public void tick() {
        this.unprocessed += this.width * this.height * this.depth;
        final int ticks = this.unprocessed / 400;
        this.unprocessed -= ticks * 400;
        for (int i = 0; i < ticks; ++i) {
            final int x = this.random.nextInt(this.width);
            final int y = this.random.nextInt(this.depth);
            final int z = this.random.nextInt(this.height);
            final Tile tile = Tile.tiles[this.getTile(x, y, z)];
            if (tile != null) {
                tile.tick(this, x, y, z, this.random);
            }
        }
    }
}