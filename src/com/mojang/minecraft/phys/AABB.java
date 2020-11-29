package com.mojang.minecraft.phys;

public class AABB {
    private float epsilon;
    public float x0;
    public float y0;
    public float z0;
    public float x1;
    public float y1;
    public float z1;
    
    public AABB(final float x0, final float y0, final float z0, final float x1, final float y1, final float z1) {
        this.epsilon = 0.0f;
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
    }
    
    public AABB expand(final float xa, final float ya, final float za) {
        float _x0 = this.x0;
        float _y0 = this.y0;
        float _z0 = this.z0;
        float _x2 = this.x1;
        float _y2 = this.y1;
        float _z2 = this.z1;
        if (xa < 0.0f) {
            _x0 += xa;
        }
        if (xa > 0.0f) {
            _x2 += xa;
        }
        if (ya < 0.0f) {
            _y0 += ya;
        }
        if (ya > 0.0f) {
            _y2 += ya;
        }
        if (za < 0.0f) {
            _z0 += za;
        }
        if (za > 0.0f) {
            _z2 += za;
        }
        return new AABB(_x0, _y0, _z0, _x2, _y2, _z2);
    }
    
    public AABB grow(final float xa, final float ya, final float za) {
        final float _x0 = this.x0 - xa;
        final float _y0 = this.y0 - ya;
        final float _z0 = this.z0 - za;
        final float _x2 = this.x1 + xa;
        final float _y2 = this.y1 + ya;
        final float _z2 = this.z1 + za;
        return new AABB(_x0, _y0, _z0, _x2, _y2, _z2);
    }
    
    public float clipXCollide(final AABB c, float xa) {
        if (c.y1 <= this.y0 || c.y0 >= this.y1) {
            return xa;
        }
        if (c.z1 <= this.z0 || c.z0 >= this.z1) {
            return xa;
        }
        if (xa > 0.0f && c.x1 <= this.x0) {
            final float max = this.x0 - c.x1 - this.epsilon;
            if (max < xa) {
                xa = max;
            }
        }
        if (xa < 0.0f && c.x0 >= this.x1) {
            final float max = this.x1 - c.x0 + this.epsilon;
            if (max > xa) {
                xa = max;
            }
        }
        return xa;
    }
    
    public float clipYCollide(final AABB c, float ya) {
        if (c.x1 <= this.x0 || c.x0 >= this.x1) {
            return ya;
        }
        if (c.z1 <= this.z0 || c.z0 >= this.z1) {
            return ya;
        }
        if (ya > 0.0f && c.y1 <= this.y0) {
            final float max = this.y0 - c.y1 - this.epsilon;
            if (max < ya) {
                ya = max;
            }
        }
        if (ya < 0.0f && c.y0 >= this.y1) {
            final float max = this.y1 - c.y0 + this.epsilon;
            if (max > ya) {
                ya = max;
            }
        }
        return ya;
    }
    
    public float clipZCollide(final AABB c, float za) {
        if (c.x1 <= this.x0 || c.x0 >= this.x1) {
            return za;
        }
        if (c.y1 <= this.y0 || c.y0 >= this.y1) {
            return za;
        }
        if (za > 0.0f && c.z1 <= this.z0) {
            final float max = this.z0 - c.z1 - this.epsilon;
            if (max < za) {
                za = max;
            }
        }
        if (za < 0.0f && c.z0 >= this.z1) {
            final float max = this.z1 - c.z0 + this.epsilon;
            if (max > za) {
                za = max;
            }
        }
        return za;
    }
    
    public boolean intersects(final AABB c) {
        return c.x1 > this.x0 && c.x0 < this.x1 && c.y1 > this.y0 && c.y0 < this.y1 && c.z1 > this.z0 && c.z0 < this.z1;
    }
    
    public void move(final float xa, final float ya, final float za) {
        this.x0 += xa;
        this.y0 += ya;
        this.z0 += za;
        this.x1 += xa;
        this.y1 += ya;
        this.z1 += za;
    }
}
