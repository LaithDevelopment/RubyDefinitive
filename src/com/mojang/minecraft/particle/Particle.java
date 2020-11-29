package com.mojang.minecraft.particle;

import com.mojang.minecraft.level.Tesselator;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.Entity;

public class Particle extends Entity {
    private float xd;
    private float yd;
    private float zd;
    public int tex;
    private float uo;
    private float vo;
    private int age;
    private int lifetime;
    private float size;
    
    public Particle(final Level level, final float x, final float y, final float z, final float xa, final float ya, final float za, final int tex) {
        super(level);
        this.age = 0;
        this.lifetime = 0;
        this.tex = tex;
        this.setSize(0.2f, 0.2f);
        this.heightOffset = this.bbHeight / 2.0f;
        this.setPos(x, y, z);
        this.xd = xa + (float)(Math.random() * 2.0 - 1.0) * 0.4f;
        this.yd = ya + (float)(Math.random() * 2.0 - 1.0) * 0.4f;
        this.zd = za + (float)(Math.random() * 2.0 - 1.0) * 0.4f;
        final float speed = (float)(Math.random() + Math.random() + 1.0) * 0.15f;
        final float dd = (float)Math.sqrt((double)(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd));
        this.xd = this.xd / dd * speed * 0.4f;
        this.yd = this.yd / dd * speed * 0.4f + 0.1f;
        this.zd = this.zd / dd * speed * 0.4f;
        this.uo = (float)Math.random() * 3.0f;
        this.vo = (float)Math.random() * 3.0f;
        this.size = (float)(Math.random() * 0.5 + 0.5);
        this.lifetime = (int)(4.0 / (Math.random() * 0.9 + 0.1));
        this.age = 0;
    }
    
    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
        this.yd -= (float)0.04;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.98f;
        this.yd *= 0.98f;
        this.zd *= 0.98f;
        if (this.onGround) {
            this.xd *= 0.7f;
            this.zd *= 0.7f;
        }
    }
    
    public void render(final Tesselator t, final float a, final float xa, final float ya, final float za, final float xa2, final float za2) {
        final float u0 = (this.tex % 16 + this.uo / 4.0f) / 16.0f;
        final float u2 = u0 + 0.015609375f;
        final float v0 = (this.tex / 16 + this.vo / 4.0f) / 16.0f;
        final float v2 = v0 + 0.015609375f;
        final float r = 0.1f * this.size;
        final float x = this.xo + (this.x - this.xo) * a;
        final float y = this.yo + (this.y - this.yo) * a;
        final float z = this.zo + (this.z - this.zo) * a;
        t.vertexUV(x - xa * r - xa2 * r, y - ya * r, z - za * r - za2 * r, u0, v2);
        t.vertexUV(x - xa * r + xa2 * r, y + ya * r, z - za * r + za2 * r, u0, v0);
        t.vertexUV(x + xa * r + xa2 * r, y + ya * r, z + za * r + za2 * r, u2, v0);
        t.vertexUV(x + xa * r - xa2 * r, y - ya * r, z + za * r - za2 * r, u2, v2);
    }
}
