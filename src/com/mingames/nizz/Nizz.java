package com.mingames.nizz;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.swing.JOptionPane;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.mingames.nizz.level.Chunk;
import com.mingames.nizz.level.Level;
import com.mingames.nizz.level.LevelRenderer;
import com.mingames.nizz.library.CustMath;

public class Nizz implements Runnable {
	private static final boolean FULLSCREEN_MODE = false;
	private int width;
	private int height;
	private FloatBuffer fogColor = BufferUtils.createFloatBuffer(4);
	private Timer timer = new Timer(60.0F);

	private Level level;

	private LevelRenderer levelRenderer;

	private Player player;

	public Nizz() {
	}

	public void init() throws LWJGLException, IOException {
		int col = 920330;
		float fr = 0.5F;
		float fg = 0.8F;
		float fb = 1.0F;
		fogColor.put(new float[] { (col >> 16 & 0xFF) / 255.0F,
				(col >> 8 & 0xFF) / 255.0F, (col & 0xFF) / 255.0F, 1.0F });
		fogColor.flip();

		Display.setDisplayMode(new DisplayMode(1268, 1268));

		Display.create();
		Keyboard.create();
		Mouse.create();

		width = Display.getDisplayMode().getWidth();
		height = Display.getDisplayMode().getHeight();

		GL11.glEnable(3553);
		GL11.glShadeModel(7425);
		GL11.glClearColor(fr, fg, fb, 0.0F);
		GL11.glClearDepth(1.0D);
		GL11.glEnable(2929);
		GL11.glDepthFunc(515);

		GL11.glMatrixMode(5889);
		GL11.glLoadIdentity();

		GL11.glMatrixMode(5888);

		level = new Level(512, 512, 128, levelRenderer);
		levelRenderer = new LevelRenderer(level);
		player = new Player(level);

		Mouse.setGrabbed(true);
	}

	public void destroy() {
		level.save();

		Mouse.destroy();
		Keyboard.destroy();
		Display.destroy();
	}

	public void run() {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		long lastTime = System.currentTimeMillis();
		int frames = 0;
		try {
			do {
				timer.advanceTime();
				for (int i = 0; i < timer.ticks; i++) {
					tick();
				}
				render(timer.a);
				frames++;

				while (System.currentTimeMillis() >= lastTime + 1000L) {
					System.out.println(frames + " fps, " + Chunk.updates);
					Chunk.updates = 0;

					lastTime += 1000L;
					frames = 0;
				}
				if (Keyboard.isKeyDown(1))
					break;
			} while (!Display.isCloseRequested());

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			destroy();
		}
	}

	public void tick() {
		player.tick();
	}

	private void moveCameraToPlayer(float a) {
		GL11.glTranslatef(0.0F, 0.0F, -0.3F);
		GL11.glRotatef(player.xRot, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(player.yRot, 0.0F, 1.0F, 0.0F);

		float x = player.xo + (player.x - player.xo) * a;
		float y = player.yo + (player.y - player.yo) * a;
		float z = player.zo + (player.z - player.zo) * a;
		GL11.glTranslatef(-x, -y, -z);
	}

	private void setupCamera(float a) {
		GL11.glMatrixMode(5889);
		GL11.glLoadIdentity();
		GLU.gluPerspective(70.0F, width / height, 0.05F, 1000.0F);
		GL11.glMatrixMode(5888);
		GL11.glLoadIdentity();
		moveCameraToPlayer(a);
	}

	private IntBuffer viewportBuffer = BufferUtils.createIntBuffer(16);

	private void setupPickCamera(float a, int x, int y) {
		GL11.glMatrixMode(5889);
		GL11.glLoadIdentity();
		viewportBuffer.clear();
		GL11.glGetInteger(2978, viewportBuffer);
		viewportBuffer.flip();
		viewportBuffer.limit(16);
		GLU.gluPickMatrix(x, y, 5.0F, 5.0F, viewportBuffer);
		GLU.gluPerspective(70.0F, width / height, 0.05F, 1000.0F);
		GL11.glMatrixMode(5888);
		GL11.glLoadIdentity();
		moveCameraToPlayer(a);
	}

	private IntBuffer selectBuffer = BufferUtils.createIntBuffer(2000);
	private HitResult hitResult = null;

	private void pick(float a) {
		selectBuffer.clear();
		GL11.glSelectBuffer(selectBuffer);
		GL11.glRenderMode(7170);
		setupPickCamera(a, width / 2, height / 2);
		levelRenderer.pick(player);
		int hits = GL11.glRenderMode(7168);
		selectBuffer.flip();
		selectBuffer.limit(selectBuffer.capacity());

		long closest = 0L;
		int[] names = new int[10];
		int hitNameCount = 0;
		for (int i = 0; i < hits; i++) {
			int nameCount = selectBuffer.get();
			long minZ = selectBuffer.get();
			selectBuffer.get();

			long dist = minZ;

			if ((dist < closest) || (i == 0)) {
				closest = dist;
				hitNameCount = nameCount;
				for (int j = 0; j < nameCount; j++) {
					names[j] = selectBuffer.get();
				}
			} else {
				for (int j = 0; j < nameCount; j++) {
					selectBuffer.get();
				}
			}
		}
		if (hitNameCount > 0) {
			hitResult = new HitResult(names[0], names[1], names[2], names[3],
					names[4]);
		} else {
			hitResult = null;
		}
	}

	public void render(float a) {
		float xo = Mouse.getDX();
		float yo = Mouse.getDY();
		player.turn(xo, yo);
		pick(a);

		while (Mouse.next()) {
			//Destroy block
			if ((Mouse.getEventButton() == 1) && (Mouse.getEventButtonState())) {
				System.out.println("Mouse event 1");
				if (hitResult != null) {
					level.setTile(hitResult.x, hitResult.y, hitResult.z, 0, 1);
				}
			}
			//Create block
			if ((Mouse.getEventButton() == 0) && (Mouse.getEventButtonState())) {
				System.out.println("Mouse event 0");
				if (hitResult != null) {
					int x = hitResult.x;
					int y = hitResult.y;
					int z = hitResult.z;

					if (hitResult.f == 0)
						y--;
					if (hitResult.f == 1)
						y++;
					if (hitResult.f == 2)
						z--;
					if (hitResult.f == 3)
						z++;
					if (hitResult.f == 4)
						x--;
					if (hitResult.f == 5) {
						x++;
					}
					level.setTile(x, y, z, 1, 1);
				}
			}
		}

		while (Keyboard.next()) {
			if ((Keyboard.getEventKey() == 28) && (Keyboard.getEventKeyState())) {
				level.save();
			}
		}

		GL11.glClear(16640);
		setupCamera(a);

		GL11.glEnable(2884);
		GL11.glEnable(2912);
		GL11.glFogi(2917, 2048);
		GL11.glFogf(2914, 0.2F);
		GL11.glFog(2918, fogColor);

		GL11.glDisable(2912);
		levelRenderer.render(player, 0);
		GL11.glEnable(2912);
		levelRenderer.render(player, 1);
		GL11.glDisable(3553);

		if (hitResult != null) {
			levelRenderer.renderHit(hitResult);
		}
		GL11.glDisable(2912);
		
		Text.drawString("X: " + player.x + " Y: " + player.y + " Z: " + player.z, 0, 0, 100, 200);

		Display.update();
	}

	public static void checkError() {
		int e = GL11.glGetError();
		if (e != 0) {
			throw new IllegalStateException(GLU.gluErrorString(e));
		}
	}

	public static void main(String[] args) throws LWJGLException {
		new Thread(new Nizz()).start();
		/*int istrue = 0;
		for(int i=0;i<50;i++) {
			System.out.println(CustMath.nextBoolean());
			if(CustMath.nextBoolean()) istrue++;
		}
		System.out.println(istrue + " " + (50-istrue));*/
	}
}
