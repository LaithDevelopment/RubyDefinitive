package com.mojang.minecraft;

import java.util.List;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.level.Level;

public class Entity {
    protected Level level;
    public float xo;
    public float yo;
    public float zo;
    public float x;
    public float y;
    public float z;
    public float xd;
    public float yd;
    public float zd;
    public float yRot;
    public float xRot;
    public AABB bb;
    public boolean onGround;
    public boolean removed;
    protected float heightOffset;
    protected float bbWidth;
    protected float bbHeight;
    
    public Entity(final Level level) {
        this.onGround = false;
        this.removed = false;
        this.heightOffset = 0.0f;
        this.bbWidth = 0.6f;
        this.bbHeight = 1.8f;
        this.level = level;
        this.resetPos();
    }
    
    protected void resetPos() {
        final float x = (float)Math.random() * this.level.width;
        final float y = (float)(this.level.depth + 10);
        final float z = (float)Math.random() * this.level.height;
        this.setPos(x, y, z);
    }
    
    public void remove() {
        this.removed = true;
    }
    
    protected void setSize(final float w, final float h) {
        this.bbWidth = w;
        this.bbHeight = h;
    }
    
    protected void setPos(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        final float w = this.bbWidth / 2.0f;
        final float h = this.bbHeight / 2.0f;
        this.bb = new AABB(x - w, y - h, z - w, x + w, y + h, z + w);
    }
    
    public void turn(final float xo, final float yo) {
        this.yRot += (float)(xo * 0.15);
        this.xRot -= (float)(yo * 0.15);
        if (this.xRot < -90.0f) {
            this.xRot = -90.0f;
        }
        if (this.xRot > 90.0f) {
            this.xRot = 90.0f;
        }
    }
    
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
    }
    
    public void move(float xa, float ya, float za) {
        final float xaOrg = xa;
        final float yaOrg = ya;
        final float zaOrg = za;
        final List<AABB> aABBs = (List<AABB>)this.level.getCubes(this.bb.expand(xa, ya, za));
        for (int i = 0; i < aABBs.size(); ++i) {
            ya = ((AABB)aABBs.get(i)).clipYCollide(this.bb, ya);
        }
        this.bb.move(0.0f, ya, 0.0f);
        for (int i = 0; i < aABBs.size(); ++i) {
            xa = ((AABB)aABBs.get(i)).clipXCollide(this.bb, xa);
        }
        this.bb.move(xa, 0.0f, 0.0f);
        for (int i = 0; i < aABBs.size(); ++i) {
            za = ((AABB)aABBs.get(i)).clipZCollide(this.bb, za);
        }
        this.bb.move(0.0f, 0.0f, za);
        this.onGround = (yaOrg != ya && yaOrg < 0.0f);
        if (xaOrg != xa) {
            this.xd = 0.0f;
        }
        if (yaOrg != ya) {
            this.yd = 0.0f;
        }
        if (zaOrg != za) {
            this.zd = 0.0f;
        }
        this.x = (this.bb.x0 + this.bb.x1) / 2.0f;
        this.y = this.bb.y0 + this.heightOffset;
        this.z = (this.bb.z0 + this.bb.z1) / 2.0f;
    }
    
    public void moveRelative(float xa, float za, final float speed) {
        float dist = xa * xa + za * za;
        if (dist < 0.01f) {
            return;
        }
        dist = speed / (float)Math.sqrt((double)dist);
        xa *= dist;
        za *= dist;
        final float sin = (float)Math.sin(this.yRot * 3.141592653589793 / 180.0);
        final float cos = (float)Math.cos(this.yRot * 3.141592653589793 / 180.0);
        this.xd += xa * cos - za * sin;
        this.zd += za * cos + xa * sin;
    }
    
    public boolean isLit() {
        final int xTile = (int)this.x;
        final int yTile = (int)this.y;
        final int zTile = (int)this.z;
        return this.level.isLit(xTile, yTile, zTile);
    }
}
