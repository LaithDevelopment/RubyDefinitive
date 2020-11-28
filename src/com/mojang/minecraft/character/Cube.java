package com.mojang.minecraft.character;

import org.lwjgl.opengl.GL11;

public class Cube {
    private Vertex[] vertices;
    private Polygon[] polygons;
    private int xTexOffs;
    private int yTexOffs;
    public float x;
    public float y;
    public float z;
    public float xRot;
    public float yRot;
    public float zRot;
    private boolean compiled;
    private int list;
    
    public Cube(final int xTexOffs, final int yTexOffs) {
        this.compiled = false;
        this.list = 0;
        this.xTexOffs = xTexOffs;
        this.yTexOffs = yTexOffs;
    }
    
    public void setTexOffs(final int xTexOffs, final int yTexOffs) {
        this.xTexOffs = xTexOffs;
        this.yTexOffs = yTexOffs;
    }
    
    public void addBox(final float x0, final float y0, final float z0, final int w, final int h, final int d) {
        this.vertices = new Vertex[8];
        this.polygons = new Polygon[6];
        final float x = x0 + w;
        final float y = y0 + h;
        final float z = z0 + d;
        final Vertex u0 = new Vertex(x0, y0, z0, 0.0f, 0.0f);
        final Vertex u2 = new Vertex(x, y0, z0, 0.0f, 8.0f);
        final Vertex u3 = new Vertex(x, y, z0, 8.0f, 8.0f);
        final Vertex u4 = new Vertex(x0, y, z0, 8.0f, 0.0f);
        final Vertex l0 = new Vertex(x0, y0, z, 0.0f, 0.0f);
        final Vertex l2 = new Vertex(x, y0, z, 0.0f, 8.0f);
        final Vertex l3 = new Vertex(x, y, z, 8.0f, 8.0f);
        final Vertex l4 = new Vertex(x0, y, z, 8.0f, 0.0f);
        this.vertices[0] = u0;
        this.vertices[1] = u2;
        this.vertices[2] = u3;
        this.vertices[3] = u4;
        this.vertices[4] = l0;
        this.vertices[5] = l2;
        this.vertices[6] = l3;
        this.vertices[7] = l4;
        this.polygons[0] = new Polygon(new Vertex[] { l2, u2, u3, l3 }, this.xTexOffs + d + w, this.yTexOffs + d, this.xTexOffs + d + w + d, this.yTexOffs + d + h);
        this.polygons[1] = new Polygon(new Vertex[] { u0, l0, l4, u4 }, this.xTexOffs + 0, this.yTexOffs + d, this.xTexOffs + d, this.yTexOffs + d + h);
        this.polygons[2] = new Polygon(new Vertex[] { l2, l0, u0, u2 }, this.xTexOffs + d, this.yTexOffs + 0, this.xTexOffs + d + w, this.yTexOffs + d);
        this.polygons[3] = new Polygon(new Vertex[] { u3, u4, l4, l3 }, this.xTexOffs + d + w, this.yTexOffs + 0, this.xTexOffs + d + w + w, this.yTexOffs + d);
        this.polygons[4] = new Polygon(new Vertex[] { u2, u0, u4, u3 }, this.xTexOffs + d, this.yTexOffs + d, this.xTexOffs + d + w, this.yTexOffs + d + h);
        this.polygons[5] = new Polygon(new Vertex[] { l0, l2, l3, l4 }, this.xTexOffs + d + w + d, this.yTexOffs + d, this.xTexOffs + d + w + d + w, this.yTexOffs + d + h);
    }
    
    public void setPos(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void render() {
        if (!this.compiled) {
            this.compile();
        }
        final float c = 57.29578f;
        GL11.glPushMatrix();
        GL11.glTranslatef(this.x, this.y, this.z);
        GL11.glRotatef(this.zRot * c, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(this.yRot * c, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(this.xRot * c, 1.0f, 0.0f, 0.0f);
        GL11.glCallList(this.list);
        GL11.glPopMatrix();
    }
    
    private void compile() {
        GL11.glNewList(this.list = GL11.glGenLists(1), 4864);
        GL11.glBegin(7);
        for (int i = 0; i < this.polygons.length; ++i) {
            this.polygons[i].render();
        }
        GL11.glEnd();
        GL11.glEndList();
        this.compiled = true;
    }
}
