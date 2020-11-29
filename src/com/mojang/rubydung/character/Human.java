package com.mojang.minecraft.character;

import com.mojang.minecraft.Textures;
import org.lwjgl.opengl.GL11;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.Entity;

public class Human extends Entity {
    public float rot;
    public float timeOffs;
    public float speed;
    public float rotA;
    private static ZombieModel zombieModel;
    
    static {
        Human.zombieModel = new ZombieModel();
    }
    
    public Human(final Level level, final float x, final float y, final float z) {
        super(level);
        this.rotA = (float)(Math.random() + 1.0) * 0.01f;
        this.setPos(x, y, z);
        this.timeOffs = (float)Math.random() * 1239813.0f;
        this.rot = (float)(Math.random() * 3.141592653589793 * 2.0);
        this.speed = 1.0f;
    }
    
    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        float xa = 0.0f;
        float ya = 0.0f;
        if (this.y < -100.0f) {
            this.remove();
        }
        this.rot += this.rotA;
        this.rotA *= (float)0.99;
        this.rotA += (float)((Math.random() - Math.random()) * Math.random() * Math.random() * 0.07999999821186066);
        xa = (float)Math.sin((double)this.rot);
        ya = (float)Math.cos((double)this.rot);
        if (this.onGround && Math.random() < 0.08) {
            this.yd = 0.5f;
        }
        this.moveRelative(xa, ya, this.onGround ? 0.1f : 0.02f);
        this.yd -= (float)0.08;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.91f;
        this.yd *= 0.98f;
        this.zd *= 0.91f;
        if (this.onGround) {
            this.xd *= 0.7f;
            this.zd *= 0.7f;
        }
    }
    
    public void render(final float a) {
        GL11.glEnable(3553);
        GL11.glBindTexture(3553, Textures.loadTexture("/char.png", 9728));
        GL11.glPushMatrix();
        final double time = System.nanoTime() / 1.0E9 * 10.0 * this.speed + this.timeOffs;
        final float size = 0.058333334f;
        final float yy = (float)(-Math.abs(Math.sin(time * 0.6662)) * 5.0 - 23.0);
        GL11.glTranslatef(this.xo + (this.x - this.xo) * a, this.yo + (this.y - this.yo) * a, this.zo + (this.z - this.zo) * a);
        GL11.glScalef(1.0f, -1.0f, 1.0f);
        GL11.glScalef(size, size, size);
        GL11.glTranslatef(0.0f, yy, 0.0f);
        final float c = 57.29578f;
        GL11.glRotatef(this.rot * c + 180.0f, 0.0f, 1.0f, 0.0f);
        Human.zombieModel.render((float)time);
        GL11.glPopMatrix();
        GL11.glDisable(3553);
    }
}
