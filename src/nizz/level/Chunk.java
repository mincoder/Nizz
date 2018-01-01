package nizz.level;

import org.lwjgl.opengl.GL11;

public class Chunk {
	public nizz.physics.AABB aabb;
	public final Level level;
	public final int x0;
	public final int y0;
	public final int z0;
	public final int x1;
	public final int y1;
	public final int z1;
	private boolean dirty = true;
	private int lists = -1;
	private static int texture = nizz.core.Textures.loadTexture("/terrain.png",
			9728);

	private static Tesselator t = new Tesselator();

	public static int rebuiltThisFrame = 0;
	public static int updates = 0;

	public Chunk(Level level, int x0, int y0, int z0, int x1, int y1, int z1) {
		this.level = level;
		this.x0 = x0;
		this.y0 = y0;
		this.z0 = z0;
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;

		aabb = new nizz.physics.AABB(x0, y0, z0, x1, y1, z1);
		lists = GL11.glGenLists(2);
	}

	private void rebuild(int layer) {
		if (rebuiltThisFrame == 2)
			return;
		dirty = false;

		updates += 1;

		rebuiltThisFrame += 1;

		GL11.glNewList(lists + layer, 4864);
		GL11.glEnable(3553);
		GL11.glBindTexture(3553, texture);
		t.init();
		int tiles = 0;
		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++)
				for (int z = z0; z < z1; z++) {
					if (level.isTile(x, y, z)) {

						int tex = y == level.depth * 2 / 3 ? 0 : 1;
						tiles++;
						if (tex == 0) {
							Tile.rock.render(t, level, layer, x, y, z);
						} else
							Tile.grass.render(t, level, layer, x, y, z);
					}
				}
		}
		t.flush();
		GL11.glDisable(3553);
		GL11.glEndList();
	}

	public void render(int layer) {
		if (dirty) {
			rebuild(0);
			rebuild(1);
		}

		GL11.glCallList(lists + layer);
	}

	public void setDirty() {
		dirty = true;
	}
}
