package com.mojang.minecraft.level;

public interface LevelListener {
    void tileChanged(final int p0, final int p1, final int p2);
    
    void lightColumnChanged(final int p0, final int p1, final int p2, final int p3);
    
    void allChanged();
}
