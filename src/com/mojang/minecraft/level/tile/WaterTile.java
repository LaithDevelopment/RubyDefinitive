package com.mojang.minecraft.level.tile;

import java.util.Random;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.phys.AABB;

public class WaterTile extends Tile {
    protected WaterTile(final int id) {
        super(id);
        this.tex = 2;
    }
    
    @Override
    public void tick(final Level level, final int x, final int y, final int z, final Random random) {
        final int var1 = level.getTile(x+1, y, z);
        final int var2 = level.getTile(x-1, y, z);
        final int var3 = level.getTile(x, y, z+1);
        final int var4 = level.getTile(x, y, z-1);
        final int var5 = level.getTile(x, y-1, z);
        if (var1 == 0) {
            level.setTile(x+1, y, z, 3);
        }
        if (var2 == 0) {
            level.setTile(x-1, y, z, 3);
        }
        if (var3 ==0) {
            level.setTile(x, y, z+1, 3);
        }
        if (var4 == 0) {
            level.setTile(x, y, z-1, 3);
        }
        if (var5 == 0) {
            level.setTile(x, y-1, z-1, 3);
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