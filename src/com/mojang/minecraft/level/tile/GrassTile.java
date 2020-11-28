package com.mojang.minecraft.level.tile;

import java.util.Random;
import com.mojang.minecraft.level.Level;

public class GrassTile extends Tile {
    protected GrassTile(final int id) {
        super(id);
        this.tex = 0;
    }
    
    @Override
    public void tick(final Level level, final int x, final int y, final int z, final Random random) {
        {	for (int i = 0; i < 4; ++i) {
                final int xt = x + random.nextInt(3) - 1;
                final int yt = y + random.nextInt(5) - 3;
                final int zt = z + random.nextInt(3) - 1;
            }
        }
    }
}
