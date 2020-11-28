package com.mojang.minecraft.level;

import com.mojang.minecraft.Player;
import java.util.Comparator;

public class DirtyChunkSorter implements Comparator<Chunk> {
    private Player player;
    private Frustum frustum;
    private long now;
    
    public DirtyChunkSorter(final Player player, final Frustum frustum) {
        this.now = System.currentTimeMillis();
        this.player = player;
        this.frustum = frustum;
    }
    
    public int compare(final Chunk c0, final Chunk c1) {
        final boolean i0 = this.frustum.isVisible(c0.aabb);
        final boolean i2 = this.frustum.isVisible(c1.aabb);
        if (i0 && !i2) {
            return -1;
        }
        if (i2 && !i0) {
            return 1;
        }
        final int t0 = (int)((this.now - c0.dirtiedTime) / 2000L);
        final int t2 = (int)((this.now - c1.dirtiedTime) / 2000L);
        if (t0 < t2) {
            return -1;
        }
        if (t0 > t2) {
            return 1;
        }
        return (c0.distanceToSqr(this.player) < c1.distanceToSqr(this.player)) ? -1 : 1;
    }
}
