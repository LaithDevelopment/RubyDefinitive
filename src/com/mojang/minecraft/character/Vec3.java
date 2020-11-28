package com.mojang.minecraft.character;

public class Vec3 {
    public float x;
    public float y;
    public float z;
    
    public Vec3(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vec3 interpolateTo(final Vec3 t, final float p) {
        final float xt = this.x + (t.x - this.x) * p;
        final float yt = this.y + (t.y - this.y) * p;
        final float zt = this.z + (t.z - this.z) * p;
        return new Vec3(xt, yt, zt);
    }
    
    public void set(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
