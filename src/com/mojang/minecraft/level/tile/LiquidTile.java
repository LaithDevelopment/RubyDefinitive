package com.mojang.minecraft.level.tile;

import java.util.Random;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.Tesselator;
import com.mojang.minecraft.phys.AABB;

public class LiquidTile extends Tile {
    protected int tileId;
    protected int calmTileId;
    protected LiquidTile(final int id, final int tex) {
        super(id, tex);
        this.tileId = id;
        this.calmTileId = id + 1;
    }
    
    @Override
    public void tick(final Level level, final int x, final int y, final int z, final Random random) {
        if (level.getTile(x - 1, y, z) == 0 && level.getTile(x - 1, y + 1, z) != 0) {
            level.setTile(x-1, y, z, this.tileId);
        }
        if (level.getTile(x + 1, y, z) == 0 && level.getTile(x + 1, y + 1, z) != 0) {
            level.setTile(x+1, y, z, this.tileId);
        }
        if (level.getTile(x, y, z - 1) == 0 && level.getTile(x, y + 1, z - 1) != 0) {
            level.setTile(x, y, z-1, this.tileId);
        }
        if (level.getTile(x, y, z + 1) == 0 && level.getTile(x, y + 1, z + 1) != 0) {
            level.setTile(x, y, z+1, this.tileId);
        }
        if (level.getTile(x, y - 1, z) == 0) {
            level.setTile(x, y-1, z, this.tileId);
        }
        if (level.getTile(x, y + 1, z) == 0) {
            level.setTile(x, y, z, this.calmTileId);
        }
        if(tileId == 3 && level.getTile(x, y+1, z) == 5 || tileId == 3 && level.getTile(x, y+1, z) == 6) {
        	level.setTile(x, y, z, 5);
        }
        if(tileId == 5 && level.getTile(x, y+1, z) == 3 || tileId == 5 && level.getTile(x, y+1, z) == 4) {
        	level.setTile(x, y, z, 3);
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