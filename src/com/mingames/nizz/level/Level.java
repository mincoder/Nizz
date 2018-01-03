package com.mingames.nizz.level;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Random;

import com.mingames.nizz.library.CustMath;
import com.mingames.nizz.library.CustNoise;
import com.mingames.nizz.physics.AABB;

public class Level {
	public final int width;
	public final int height;
	public final int depth;
	private byte[] blocks;
	public static int[][][] blockid;
	private int[] lightDepths;
	private ArrayList<LevelListener> levelListeners = new ArrayList();
	int[][] map;
	Random r;

	public Level(int w, int h, int d, LevelRenderer levelRenderer) {
		r = new Random(new Random().nextLong());
		width = w;
		height = h;
		depth = d;
		blocks = new byte[w * h * d];

		generateTerrain(w, h, d);

		lightDepths = new int[w * h];

		calcLightDepths(0, 0, w, h);

		levelRenderer = new LevelRenderer(this);

		generateDetail(w, h, d);

		// load();
	}

	public void generateDetail(int w, int h, int d) {
		
		// Generate mountains
		for(int j=0;j<r.nextInt(8);j++) {
		int a = r.nextInt(w-25);
		int b = r.nextInt(h-25);
		int width = CustMath.weightedRandom(r, w-a, 4);
		int height = CustMath.randInt(width/2, (int)(width/2*1.5), r);
		int[][] newmap = CustNoise.getRandomNoise(r, 30, 32, 10, (w+h)/2*4, w, h, d);
		for (int x = a; x < width+a; x++) {
			for (int z = b; z < height+b; z++) {
				map[x][z]=newmap[x][z];
				for (int y = 0; y < d; y++) {
					setTile(x,y,z,0,0);
				}
			}
		}
		for (int x = a; x < width+a; x++) {
			for (int z = b; z < height+b; z++) {
				for (int y = 0; y < newmap[x][z]; y++) {
					if (y == newmap[x][z] - 1) {
						setTile(x,y,z,1,0);

					} else {
						setTile(x,y,z,1,1);
					}
				}
			}
		}
		}
		
		//Put water
		for (int x = 0; x < w; x++) {
			for (int z = 0; z < h; z++) {
				for (int y = 0; y < 35; y++) {
					if (!isSolidTile(x, y, z)) {
						setTile(x,y,z,1,4);
					}
				}
			}
		}
		
		// Generate Structures
		for (int x = 0; x < w; x++) {
			for (int z = 0; z < h; z++) {
				for (int y = 0; y < map[x][z]; y++) {
					if (y == map[x][z] - 1&&map[x][z]>35) {
						
						// Tree generation
						if (r.nextInt(300) == 0) {
							int max = CustMath.randInt(5, 8, r);
							for (int j = 0; j < max; j++) {
								setTile(x, y + j, z, 1, 2);
							}

							// Foliage
							setTile(x, y + max, z, 1, 3);

							setTile(x - 1, y + max, z, 1, 3);
							setTile(x + 1, y + max, z, 1, 3);
							setTile(x, y + max, z + 1, 1, 3);
							setTile(x, y + max, z - 1, 1, 3);

							setTile(x - 1, y + max - 1, z, 1, 3);
							setTile(x + 1, y + max - 1, z, 1, 3);
							setTile(x, y + max - 1, z + 1, 1, 3);
							setTile(x, y + max - 1, z - 1, 1, 3);
							setTile(x - 1, y + max - 1, z-1, 1, 3);
							setTile(x + 1, y + max - 1, z+1, 1, 3);
							setTile(x-1, y + max - 1, z + 1, 1, 3);
							setTile(x+1, y + max - 1, z - 1, 1, 3);
							
							setTile(x - 1, y + max - 2, z, 1, 3);
							setTile(x + 1, y + max - 2, z, 1, 3);
							setTile(x, y + max - 2, z + 1, 1, 3);
							setTile(x, y + max - 2, z - 1, 1, 3);
							setTile(x - 1, y + max - 2, z-1, 1, 3);
							setTile(x + 1, y + max - 2, z+1, 1, 3);
							setTile(x-1, y + max - 2, z + 1, 1, 3);
							setTile(x+1, y + max - 2, z - 1, 1, 3);
							
							if(r.nextBoolean()) {
								setTile(x - 1, y + max, z, 0, 3);
							}
							if(r.nextBoolean()) {
								setTile(x + 1, y + max - 1, z+1, 1, 3);
							}
							if(r.nextBoolean()) {
								setTile(x-1, y + max - 2, z + 1, 0, 3);
							}
							if(r.nextBoolean()) {
								setTile(x+1, y + max - 1, z - 1, 0, 3);
							}
							
						//Generate boulders
						} else if (r.nextInt(400) == 0) {
							setTile(x, y + 1, z, 1, 1);
						} else if (r.nextInt(1) == 0) {
							for (int a = x; a < r.nextInt(20+x); a++) {
								for (int b = z; b < r.nextInt(20)+z; b++) {
									for (int c = y; c < r.nextInt(20)+y; c++) {
										setTile(a,b,c,0,0);
									}
								}
							}
						}
						
					}
				}
			}
		}
	}

	public void generateTerrain(int w, int h, int d) {

		blockid = new int[w][d][h];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < d; y++) {
				for (int z = 0; z < h; z++) {
					blockid[x][y][z] = 0;
				}
			}
		}

		// Generate basic terrain
		map = CustNoise.getRandomNoise(r, 30, 32, 2, (w+h)/2*6, w, h, d);
		for (int x = 0; x < w; x++) {
			for (int z = 0; z < h; z++) {
				for (int y = 0; y < map[x][z]; y++) {
					int i = (y * height + z) * width + x;
					blocks[i] = ((byte) (y <= d * 2 / 3 ? 1 : 0));
					if (y == map[x][z] - 1) {
						blockid[x][y][z] = 0;

					} else {
						blockid[x][y][z] = 1;
					}
				}
			}
		}

	}

	public void load() {
		try {
			DataInputStream dis = new DataInputStream(
					new java.util.zip.GZIPInputStream(new java.io.FileInputStream(new java.io.File("level.dat"))));
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
					new java.util.zip.GZIPOutputStream(new java.io.FileOutputStream(new java.io.File("level.dat"))));
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
						((LevelListener) levelListeners.get(i)).lightColumnChanged(x, z, yl0, yl1);
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
		if ((x < 0) || (y < 0) || (z < 0) || (x >= width) || (y >= depth) || (z >= height))
			return false;
		return blocks[((y * height + z) * width + x)] == 1;
	}

	public boolean isSolidTile(int x, int y, int z) {
		return isTile(x, y, z);
	}

	public boolean isLightBlocker(int x, int y, int z) {
		if(isSolidTile(x, y, z)) {
			if(blockid[x][y][z]==3) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
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
			for (int z = z0; z < z1; z++) {
				for (int y = y0; y < new Random().nextInt(depth); y++) {
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
		if ((x < 0) || (y < 0) || (z < 0) || (x >= width) || (y >= depth) || (z >= height))
			return light;
		if (y < lightDepths[(x + z * width)])
			return dark;
		return light;
	}

	public void setTile(int x, int y, int z, int type, int id) {
		if ((x < 0) || (y < 0) || (z < 0) || (x >= width) || (y >= depth) || (z >= height))
			return;
		blocks[((y * height + z) * width + x)] = ((byte) type);
		blockid[x][y][z] = id;
		calcLightDepths(x, z, 1, 1);
		for (int i = 0; i < levelListeners.size(); i++) {
			((LevelListener) levelListeners.get(i)).tileChanged(x, y, z);
		}
	}
}
