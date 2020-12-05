package com.mojang.minecraft;

import com.mojang.minecraft.level.Tesselator;
import com.mojang.minecraft.Tesselator11a;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.level.Frustum;
import org.lwjgl.util.glu.GLU;
import com.mojang.minecraft.level.Chunk;
import java.awt.Component;
import javax.swing.JOptionPane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;
import com.mojang.minecraft.character.Zombie;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.character.Human;
import com.mojang.minecraft.character.Notch;

import java.util.ArrayList;

import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.level.LevelRenderer;
import com.mojang.minecraft.level.Level;
import java.nio.FloatBuffer;

public class RubyDung implements Runnable {
    public static final String VERSION_STRING = "rd-0.0.3";
    public static final String BUILD_STRING = "tb1";
    //game options
    public static Boolean OLDWORLD_ENABLED;
    public static Boolean CAVES_ENABLED;
    public static Boolean FLAT_TERRAIN;
    private static Boolean COORDINATES_ENABLED;
    private static Boolean FPS_ENABLED;
    private static Boolean FULLSCREEN_MODE;
    private static Boolean VSYNC_MODE;
    public static Boolean FLIGHT_ENABLED = false;
    private int width;
    private int height;
    private FloatBuffer fogColor0;
    private FloatBuffer fogColor1;
    private Timer timer;
    private Level level;
    private LevelRenderer levelRenderer;
    private Player player;
    private int paintTexture;
    private ParticleEngine particleEngine;
    private ArrayList<Zombie> zombies;
    private ArrayList<Human> humans;
    private ArrayList<Notch> notchs;
    private IntBuffer viewportBuffer;
    private IntBuffer selectBuffer;
    private HitResult hitResult;
    private Font font;
    FloatBuffer lb;
	private Textures textures;
	private String fpsString;
    
    public RubyDung() {
        this.fogColor0 = BufferUtils.createFloatBuffer(4);
        this.fogColor1 = BufferUtils.createFloatBuffer(4);
        this.timer = new Timer(20.0f);
        this.paintTexture = 1;
        this.zombies = (ArrayList<Zombie>)new ArrayList();
        this.humans = (ArrayList<Human>)new ArrayList();
        this.notchs = (ArrayList<Notch>)new ArrayList();
        this.viewportBuffer = BufferUtils.createIntBuffer(16);
        this.selectBuffer = BufferUtils.createIntBuffer(2000);
        this.hitResult = null;
        this.lb = BufferUtils.createFloatBuffer(16);
        this.fpsString = "";
    }
    
    public void init() throws LWJGLException, IOException {
        final int col0 = 16710650;
        final int col2 = 920330;
        final float fr = 0.5f;
        final float fg = 0.8f;
        final float fb = 1.0f;
        this.fogColor0.put(new float[] { (col0 >> 16 & 0xFF) / 255.0f, (col0 >> 8 & 0xFF) / 255.0f, (col0 & 0xFF) / 255.0f, 1.0f });
        this.fogColor0.flip();
        this.fogColor1.put(new float[] { (col2 >> 16 & 0xFF) / 255.0f, (col2 >> 8 & 0xFF) / 255.0f, (col2 & 0xFF) / 255.0f, 1.0f });
        this.fogColor1.flip();
        if(FULLSCREEN_MODE == true) {
            Display.setFullscreen(true);
        }else {
            Display.setDisplayMode(new DisplayMode(853, 480));
        }
        if(VSYNC_MODE == true) {
            Display.setVSyncEnabled(true);
        }
        Display.setTitle("RubyDefinitive");
        Display.create();
        Keyboard.create();
        Mouse.create();
        this.width = Display.getDisplayMode().getWidth();
        this.height = Display.getDisplayMode().getHeight();
        GL11.glEnable(3553);
        GL11.glShadeModel(7425);
        GL11.glClearColor(fr, fg, fb, 0.0f);
        GL11.glClearDepth(1.0);
        GL11.glEnable(2929);
        GL11.glDepthFunc(515);
        GL11.glEnable(3008);
        GL11.glAlphaFunc(516, 0.5f);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(5888);
        this.level = new Level(256, 256, 64); //map size
        this.levelRenderer = new LevelRenderer(this.level);
        this.player = new Player(this.level);
        this.particleEngine = new ParticleEngine(this.level);
        this.font = new Font("/default.gif", this.textures);
        Mouse.setGrabbed(true);
        //entity spawning
        if(this.OLDWORLD_ENABLED == false) {
            for (int i = 0; i < 40; ++i) {
            	//zombie entity
                final Zombie zombie = new Zombie(this.level, 128.0f, 0.0f, 128.0f);
                zombie.resetPos();
                this.zombies.add(zombie);
                //human entity
                final Human human = new Human(this.level, 128.0f, 0.0f, 128.0f);
                human.resetPos();
                this.humans.add(human);
            }
        }
    }
    
    private void checkGlError(final String string) {
        final int errorCode = GL11.glGetError();
        if (errorCode != 0) {
            final String errorString = GLU.gluErrorString(errorCode);
            System.out.println("########## GL ERROR ##########");
            System.out.println(new StringBuilder("@ ").append(string).toString());
            System.out.println(String.valueOf(errorCode) + ": " + errorString);
            System.exit(0);
        }
    }
    
    public void destroy() {
        this.level.save();
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }
    
    public void run() {
        try {
            this.init();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog((Component)null, e.toString(), "Failed to start RubyDung", 0);
            System.exit(0);
        }
        long lastTime = System.currentTimeMillis();
        int frames = 0;
        try {
            while (!Keyboard.isKeyDown(1)) {
                if (Display.isCloseRequested()) {
                    break;
                }
                this.timer.advanceTime();
                for (int i = 0; i < this.timer.ticks; ++i) {
                    this.tick();
                }
                this.checkGlError("Pre render");
                this.render(this.timer.a);
                this.checkGlError("Post render");
                ++frames;
                while (System.currentTimeMillis() >= lastTime + 1000L) {
                    System.out.println(String.valueOf(frames) + " fps, " + Chunk.updates);
                    this.fpsString = String.valueOf(frames);
                    Chunk.updates = 0;
                    lastTime += 1000L;
                    frames = 0;
                }
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            return;
        }
        finally {
            this.destroy();
        }
        this.destroy();
    }
    
    public void tick() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == 28) {
                    this.level.save();
                }
                if (Keyboard.getEventKey() == 2) {
                    this.paintTexture = 1;
                }
                if (Keyboard.getEventKey() == 3) {
                    this.paintTexture = 2;
                }
                if (Keyboard.getEventKey() == 4) {
                    this.paintTexture = 3;
                }
                if (Keyboard.getEventKey() == 5) {
                    this.paintTexture = 5;
                }
                if (Keyboard.getEventKey() == 34) {
                    this.humans.add(new Human(this.level, this.player.x, this.player.y, this.player.z));
                }
                if (Keyboard.getEventKey() == 35) {
                    this.zombies.add(new Zombie(this.level, this.player.x, this.player.y, this.player.z));
                }
                if (Keyboard.getEventKey() == 33) {
                	RubyDung.FLIGHT_ENABLED ^= true;
                }
            }
        }
        this.level.tick();
        this.particleEngine.tick();
        //zombie entity
        for (int i = 0; i < this.zombies.size(); ++i) {
            ((Zombie)this.zombies.get(i)).tick();
            if (((Zombie)this.zombies.get(i)).removed) {
                this.zombies.remove(i--);
            }
        }
        //human entity
        for (int i = 0; i < this.humans.size(); ++i) {
            ((Human)this.humans.get(i)).tick();
            if (((Human)this.humans.get(i)).removed) {
                this.humans.remove(i--);
            }
        }
        //notch entity
        for (int i = 0; i < this.notchs.size(); ++i) {
            ((Notch)this.notchs.get(i)).tick();
            if (((Notch)this.notchs.get(i)).removed) {
                this.notchs.remove(i--);
            }
        }
        this.player.tick();
    }
    
    private void moveCameraToPlayer(final float a) {
        GL11.glTranslatef(0.0f, 0.0f, -0.3f);
        GL11.glRotatef(this.player.xRot, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(this.player.yRot, 0.0f, 1.0f, 0.0f);
        final float x = this.player.xo + (this.player.x - this.player.xo) * a;
        final float y = this.player.yo + (this.player.y - this.player.yo) * a;
        final float z = this.player.zo + (this.player.z - this.player.zo) * a;
        GL11.glTranslatef(-x, -y, -z);
    }
    
    private void setupCamera(final float a) {
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GLU.gluPerspective(70.0f, this.width / (float)this.height, 0.05f, 1000.0f);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        this.moveCameraToPlayer(a);
    }
    
    private void setupPickCamera(final float a, final int x, final int y) {
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        this.viewportBuffer.clear();
        GL11.glGetInteger(2978, this.viewportBuffer);
        this.viewportBuffer.flip();
        this.viewportBuffer.limit(16);
        GLU.gluPickMatrix((float)x, (float)y, 5.0f, 5.0f, this.viewportBuffer);
        GLU.gluPerspective(70.0f, this.width / (float)this.height, 0.05f, 1000.0f);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        this.moveCameraToPlayer(a);
    }
    
    private void pick(final float a) {
        this.selectBuffer.clear();
        GL11.glSelectBuffer(this.selectBuffer);
        GL11.glRenderMode(7170);
        this.setupPickCamera(a, this.width / 2, this.height / 2);
        this.levelRenderer.pick(this.player, Frustum.getFrustum());
        final int hits = GL11.glRenderMode(7168);
        this.selectBuffer.flip();
        this.selectBuffer.limit(this.selectBuffer.capacity());
        long closest = 0L;
        final int[] names = new int[10];
        int hitNameCount = 0;
        for (int i = 0; i < hits; ++i) {
            final int nameCount = this.selectBuffer.get();
            final long minZ = this.selectBuffer.get();
            this.selectBuffer.get();
            final long dist = minZ;
            if (dist < closest || i == 0) {
                closest = dist;
                hitNameCount = nameCount;
                for (int j = 0; j < nameCount; ++j) {
                    names[j] = this.selectBuffer.get();
                }
            }
            else {
                for (int j = 0; j < nameCount; ++j) {
                    this.selectBuffer.get();
                }
            }
        }
        if (hitNameCount > 0) {
            this.hitResult = new HitResult(names[0], names[1], names[2], names[3], names[4]);
        }
        else {
            this.hitResult = null;
        }
    }
    
    public void render(final float a) {
        final float xo = (float)Mouse.getDX();
        final float yo = (float)Mouse.getDY();
        this.player.turn(xo, yo);
        this.pick(a);
        while (Mouse.next()) {
            if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState() && this.hitResult != null) {
                final Tile oldTile = Tile.tiles[this.level.getTile(this.hitResult.x, this.hitResult.y, this.hitResult.z)];
                final boolean changed = this.level.setTile(this.hitResult.x, this.hitResult.y, this.hitResult.z, 0);
                if (oldTile != null && changed) {
                    oldTile.destroy(this.level, this.hitResult.x, this.hitResult.y, this.hitResult.z, this.particleEngine);
                }
            }
            if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.hitResult != null) {
                int x = this.hitResult.x;
                int y = this.hitResult.y;
                int z = this.hitResult.z;
                if (this.hitResult.f == 0) {
                    --y;
                }
                if (this.hitResult.f == 1) {
                    ++y;
                }
                if (this.hitResult.f == 2) {
                    --z;
                }
                if (this.hitResult.f == 3) {
                    ++z;
                }
                if (this.hitResult.f == 4) {
                    --x;
                }
                if (this.hitResult.f == 5) {
                    ++x;
                }
                if(OLDWORLD_ENABLED == true && y == 32) {
                    this.level.setTile(x, y, z, 1);
                }
                else if(OLDWORLD_ENABLED == true) {
                this.level.setTile(x, y, z, 2);
                }
                else {
                    this.level.setTile(x, y, z, this.paintTexture);
                }
            }
        }
        GL11.glClear(16640);
        this.setupCamera(a);
        GL11.glEnable(2884);
        final Frustum frustum = Frustum.getFrustum();
        this.levelRenderer.updateDirtyChunks(this.player);
        this.setupFog(0);
        GL11.glEnable(2912);
        this.levelRenderer.render(this.player, 0);
        //zombie entity
        for (int i = 0; i < this.zombies.size(); ++i) {
            final Zombie zombie = (Zombie)this.zombies.get(i);
            if (zombie.isLit() && frustum.isVisible(zombie.bb)) {
                ((Zombie)this.zombies.get(i)).render(a);
            }
        }
        //human entity
        for (int i = 0; i < this.humans.size(); ++i) {
            final Human human = (Human)this.humans.get(i);
            if (human.isLit() && frustum.isVisible(human.bb)) {
                ((Human)this.humans.get(i)).render(a);
            }
        }
        //notch entity
        for (int i = 0; i < this.notchs.size(); ++i) {
            final Notch notch = (Notch)this.notchs.get(i);
            if (notch.isLit() && frustum.isVisible(notch.bb)) {
                ((Notch)this.notchs.get(i)).render(a);
            }
        }
        this.particleEngine.render(this.player, a, 0);
        this.setupFog(1);
        this.levelRenderer.render(this.player, 1);
        //zombie entity
        for (int i = 0; i < this.zombies.size(); ++i) {
            final Zombie zombie = (Zombie)this.zombies.get(i);
            if (!zombie.isLit() && frustum.isVisible(zombie.bb)) {
                ((Zombie)this.zombies.get(i)).render(a);
            }
        }
        //human entity
        for (int i = 0; i < this.humans.size(); ++i) {
            final Human human = (Human)this.humans.get(i);
            if (!human.isLit() && frustum.isVisible(human.bb)) {
                ((Human)this.humans.get(i)).render(a);
            }
        }
        //notch entity
        for (int i = 0; i < this.notchs.size(); ++i) {
            final Notch notch = (Notch)this.notchs.get(i);
            if (!notch.isLit() && frustum.isVisible(notch.bb)) {
                ((Human)this.humans.get(i)).render(a);
            }
        }
        this.particleEngine.render(this.player, a, 1);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glDisable(2912);
        this.checkGlError("Rendered rest");
        if (this.hitResult != null) {
            GL11.glDisable(3008);
            this.levelRenderer.renderHit(this.hitResult);
            GL11.glEnable(3008);
        }
        this.checkGlError("Rendered hit");
        this.drawGui(a);
        this.checkGlError("Rendered gui");
        Display.update();
    }
    
    private void drawGui(final float a) {
        final int screenWidth = this.width * 240 / this.height;
        final int screenHeight = this.height * 240 / this.height;
        GL11.glClear(256); //map size
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, (double)screenWidth, (double)screenHeight, 0.0, 100.0, 300.0);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -200.0f);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(screenWidth - 16), 16.0f, 0.0f);
        final Tesselator t = Tesselator.instance;
        GL11.glScalef(16.0f, 16.0f, 16.0f);
        GL11.glRotatef(30.0f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(-1.5f, 0.5f, -0.5f);
        GL11.glScalef(-1.0f, -1.0f, 1.0f);
        final int id = Textures.loadTexture("/terrain.png", 9728);
        GL11.glBindTexture(3553, id);
        
        if(OLDWORLD_ENABLED == false) {
            GL11.glEnable(3553);
            t.init();
            Tile.tiles[this.paintTexture].render(t, this.level, 0, -2, 0, 0);
            t.flush();
            GL11.glDisable(3553);
        }
        
        GL11.glPopMatrix();
        this.checkGlError("GUI: Draw selected");
        this.font.drawShadow(VERSION_STRING+" "+BUILD_STRING, 2, 2, 16777215);
        
        if(FLIGHT_ENABLED == true) {
            this.font.drawShadow("Flight ON", 2, 32, 16777215);
        }
        
        if(FPS_ENABLED == true) {
            this.font.drawShadow("Fps: "+this.fpsString, 2, 12, 16777215);
            this.font.drawShadow("Selected Block Id: "+this.paintTexture, 2, 22, 16777215);
        }
        
        if (COORDINATES_ENABLED == true) {
            float y = this.player.y + 0.38F;
            this.font.drawShadow("x: "+this.player.x, 2, 42, 16777215);
            this.font.drawShadow("y: "+y, 2, 52, 16777215);
            this.font.drawShadow("z: "+this.player.z, 2, 62, 16777215);
        }
        
        this.checkGlError("GUI: Draw text");
        final int wc = screenWidth / 2;
        final int hc = screenHeight / 2;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        t.init();
        t.vertex((float)(wc + 1), (float)(hc - 4), 0.0f);
        t.vertex((float)(wc - 0), (float)(hc - 4), 0.0f);
        t.vertex((float)(wc - 0), (float)(hc + 5), 0.0f);
        t.vertex((float)(wc + 1), (float)(hc + 5), 0.0f);
        t.vertex((float)(wc + 5), (float)(hc - 0), 0.0f);
        t.vertex((float)(wc - 4), (float)(hc - 0), 0.0f);
        t.vertex((float)(wc - 4), (float)(hc + 1), 0.0f);
        t.vertex((float)(wc + 5), (float)(hc + 1), 0.0f);
        t.flush();
    }
    
    private void setupFog(final int i) {
        if (i == 0) {
            GL11.glFogi(2917, 2048);
            GL11.glFogf(2914, 0.001f);
            GL11.glFog(2918, this.fogColor0);
            GL11.glDisable(2896);
        }
        else if (i == 1) {
            GL11.glFogi(2917, 2048);
            GL11.glFogf(2914, 0.06f);
            GL11.glFog(2918, this.fogColor1);
            GL11.glEnable(2896);
            GL11.glEnable(2903);
            final float br = 0.6f;
            GL11.glLightModel(2899, this.getBuffer(br, br, br, 1.0f));
        }
    }
    
    private FloatBuffer getBuffer(final float a, final float b, final float c, final float d) {
        this.lb.clear();
        this.lb.put(a).put(b).put(c).put(d);
        this.lb.flip();
        return this.lb;
    }
    
    public static void checkError() {
        final int e = GL11.glGetError();
        if (e != 0) {
            throw new IllegalStateException(GLU.gluErrorString(e));
        }
    }
    
	private static void buildOptions() throws IOException {
        FileWriter myWriter = new FileWriter("options-rubydefinitive.txt");
        myWriter.write("Fullscreen:false\nCoordinatesGui:false\nShowFPS:true\nOldWorld:false\nFlatWorld:false\nGenerateCaves:true\nVsync:false");
        myWriter.close();
	}
    
    public static void main(final String[] args) throws LWJGLException {
            	
        try {
            File myObj = new File("options-rubydefinitive.txt");
            if (myObj.createNewFile()) {
              System.out.println("Created " + myObj.getName());
              buildOptions();
            }
          } catch (IOException e) {
        	  System.out.println("An error occurred.");
        	  e.printStackTrace();
          }
        
        String text = "";
        int lineNumber;
        try {
          FileReader readfile = new FileReader("options-rubydefinitive.txt");
          BufferedReader readbuffer = new BufferedReader(readfile);
          for (lineNumber = 1; lineNumber < 10; lineNumber++) {
        	  
        	  if (lineNumber == 1) {
        		  text = readbuffer.readLine();
        		  if(text.contains("Fullscreen:false") == false && text.contains("Fullscreen:true") == false) {
        			  buildOptions();
        		  }
        		  text = text.replace("Fullscreen:", "");
        		  boolean fullscreenOption = Boolean.parseBoolean(text);
        		  FULLSCREEN_MODE = fullscreenOption;
        	  }
            
        	  else if (lineNumber == 2) {
        		  text = readbuffer.readLine();
        		  if(text.contains("CoordinatesGui:false") == false && text.contains("CoordinatesGui:true") == false) {
        			  buildOptions();
        		  }
        		  text = text.replace("CoordinatesGui:", "");
        		  boolean coordinatesGuiOption = Boolean.parseBoolean(text);
        		  COORDINATES_ENABLED = coordinatesGuiOption;
        	  }
        	  
        	  else if (lineNumber == 3) {
        		  text = readbuffer.readLine();
        		  if(text.contains("ShowFPS:false") == false && text.contains("ShowFPS:true") == false) {
        			  buildOptions();
        		  }
        		  text = text.replace("ShowFPS:", "");
        		  boolean FPSGuiOption = Boolean.parseBoolean(text);
        		  FPS_ENABLED = FPSGuiOption;
        	  }
            
        	  else if (lineNumber == 4) {
        		  text = readbuffer.readLine();
        		  if(text.contains("OldWorld:false") == false && text.contains("OldWorld:true") == false) {
        			  buildOptions();
        		  }
        		  text = text.replace("OldWorld:", "");
        		  boolean oldWorldOption = Boolean.parseBoolean(text);
        		  OLDWORLD_ENABLED = oldWorldOption;
        	  }
            
        	  else if (lineNumber == 5) {
        		  text = readbuffer.readLine();
        		  if(text.contains("FlatWorld:false") == false && text.contains("FlatWorld:true") == false) {
        			  buildOptions();
        		  }
        		  text = text.replace("FlatWorld:", "");
        		  boolean flatWorldOption = Boolean.parseBoolean(text);
        		  FLAT_TERRAIN = flatWorldOption;
        	  }
            
        	  else if (lineNumber == 6) {
        		  text = readbuffer.readLine();
        		  if(text.contains("GenerateCaves:false") == false && text.contains("GenerateCaves:true") == false) {
        			  buildOptions();
        		  }
        		  text = text.replace("GenerateCaves:", "");
        		  boolean generateCavesOption = Boolean.parseBoolean(text);
        		  CAVES_ENABLED = generateCavesOption;
        	  }
        	  else if (lineNumber == 7) {
        		  text = readbuffer.readLine();
        		  if(text.contains("Vsync:false") == false && text.contains("Vsync:true") == false) {
        			  buildOptions();
        		  }
        		  text = text.replace("Vsync:", "");
        		  boolean vsyncOption = Boolean.parseBoolean(text);
        		  VSYNC_MODE = vsyncOption;
        	  }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
        //start game
        new Thread((Runnable)new RubyDung()).start();
      }
}
