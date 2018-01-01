package com.mojang.rubydung.level;

import com.mojang.rubydung.phys.AABB;
import java.io.DataInputStream;
import java.util.ArrayList;

public class Level {
	public final int width;
	public final int height;
	public final int depth;
	private byte[] blocks;
	private int[] lightDepths;
	private ArrayList<LevelListener> levelListeners = new ArrayList();

	public Level(int w, int h, int d) {
		width = w;
		height = h;
		depth = d;
		blocks = new byte[w * h * d];

		lightDepths = new int[w * h];

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < d; y++) {
				for (int z = 0; z < h; z++) {
					int i = (y * height + z) * width + x;
					blocks[i] = ((byte) (y <= d * 2 / 3 ? 1 : 0));
				}
			}
		}
		calcLightDepths(0, 0, w, h);

		load();
	}

	public void load() {
		try {
			DataInputStream dis = new DataInputStream(
					new java.util.zip.GZIPInputStream(
							new java.io.FileInputStream(new java.io.File(
									"level.dat"))));
			dis.readFully(blocks);
			calcLightDepths(0, 0, width, height);
			for (int i = 0; i < levelListeners.size(); i++)
				((LevelListener) levelListeners.get(i)).allChanged();
			dis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			java.io.DataOutputStream dos = new java.io.DataOutputStream(
					new java.util.zip.GZIPOutputStream(
							new java.io.FileOutputStream(new java.io.File(
									"level.dat"))));
			dos.write(blocks);
			dos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void calcLightDepths(int x0, int y0, int x1, int y1) {
		for (int x = x0; x < x0 + x1; x++) {
			for (int z = y0; z < y0 + y1; z++) {
				int oldDepth = lightDepths[(x + z * width)];
				int y = depth - 1;
				while ((y > 0) && (!isLightBlocker(x, y, z)))
					y--;
				lightDepths[(x + z * width)] = y;

				if (oldDepth != y) {
					int yl0 = oldDepth < y ? oldDepth : y;
					int yl1 = oldDepth > y ? oldDepth : y;
					for (int i = 0; i < levelListeners.size(); i++)
						((LevelListener) levelListeners.get(i))
								.lightColumnChanged(x, z, yl0, yl1);
				}
			}
		}
	}

	public void addListener(LevelListener levelListener) {
		levelListeners.add(levelListener);
	}

	public void removeListener(LevelListener levelListener) {
		levelListeners.remove(levelListener);
	}

	public boolean isTile(int x, int y, int z) {
		if ((x < 0) || (y < 0) || (z < 0) || (x >= width) || (y >= depth)
				|| (z >= height))
			return false;
		return blocks[((y * height + z) * width + x)] == 1;
	}

	public boolean isSolidTile(int x, int y, int z) {
		return isTile(x, y, z);
	}

	public boolean isLightBlocker(int x, int y, int z) {
		return isSolidTile(x, y, z);
	}

	public ArrayList<AABB> getCubes(AABB aABB) {
		ArrayList<AABB> aABBs = new ArrayList();
		int x0 = (int) aABB.x0;
		int x1 = (int) (aABB.x1 + 1.0F);
		int y0 = (int) aABB.y0;
		int y1 = (int) (aABB.y1 + 1.0F);
		int z0 = (int) aABB.z0;
		int z1 = (int) (aABB.z1 + 1.0F);

		if (x0 < 0)
			x0 = 0;
		if (y0 < 0)
			y0 = 0;
		if (z0 < 0)
			z0 = 0;
		if (x1 > width)
			x1 = width;
		if (y1 > depth)
			y1 = depth;
		if (z1 > height) {
			z1 = height;
		}
		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				for (int z = z0; z < z1; z++) {
					if (isSolidTile(x, y, z)) {
						aABBs.add(new AABB(x, y, z, x + 1, y + 1, z + 1));
					}
				}
			}
		}
		return aABBs;
	}

	public float getBrightness(int x, int y, int z) {
		float dark = 0.8F;
		float light = 1.0F;
		if ((x < 0) || (y < 0) || (z < 0) || (x >= width) || (y >= depth)
				|| (z >= height))
			return light;
		if (y < lightDepths[(x + z * width)])
			return dark;
		return light;
	}

	public void setTile(int x, int y, int z, int type) {
		if ((x < 0) || (y < 0) || (z < 0) || (x >= width) || (y >= depth)
				|| (z >= height))
			return;
		blocks[((y * height + z) * width + x)] = ((byte) type);
		calcLightDepths(x, z, 1, 1);
		for (int i = 0; i < levelListeners.size(); i++) {
			((LevelListener) levelListeners.get(i)).tileChanged(x, y, z);
		}
	}
}
