package com.mojang.minecraft;

import org.lwjgl.input.Keyboard;
import com.mojang.minecraft.level.Level;

public class Player extends Entity {
    public Player(final Level level) {
        super(level);
        this.heightOffset = 1.62f;
    }
    
    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        float xa = 0.0f;
        float ya = 0.0f;
        if (Keyboard.isKeyDown(19)) {
            this.resetPos();
        }
        if (Keyboard.isKeyDown(200) || Keyboard.isKeyDown(17)) {
            --ya;
        }
        if (Keyboard.isKeyDown(208) || Keyboard.isKeyDown(31)) {
            ++ya;
        }
        if (Keyboard.isKeyDown(203) || Keyboard.isKeyDown(30)) {
            --xa;
        }
        if (Keyboard.isKeyDown(205) || Keyboard.isKeyDown(32)) {
            ++xa;
        }
        if ((Keyboard.isKeyDown(57) || Keyboard.isKeyDown(219)) && this.onGround) {
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
}
