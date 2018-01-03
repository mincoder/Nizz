package com.mingames.nizz;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Textures {
	private static HashMap<String, Integer> idMap = new HashMap();

	private static int lastId = -9999999;

	public Textures() {
	}

	public static int loadTexture(String resourceName, int mode) {
		try {
			if (idMap.containsKey(resourceName)) {
				return ((Integer) idMap.get(resourceName)).intValue();
			}

			IntBuffer ib = BufferUtils.createIntBuffer(1);

			GL11.glGenTextures(ib);
			int id = ib.get(0);

			bind(id);

			GL11.glTexParameteri(3553, 10241, mode);
			GL11.glTexParameteri(3553, 10240, mode);

			BufferedImage img = ImageIO.read(new FileInputStream(resourceName));
			int w = img.getWidth();
			int h = img.getHeight();

			ByteBuffer pixels = BufferUtils.createByteBuffer(w * h * 4);
			int[] rawPixels = new int[w * h];
			img.getRGB(0, 0, w, h, rawPixels, 0, w);
			for (int i = 0; i < rawPixels.length; i++) {
				int a = rawPixels[i] >> 24 & 0xFF;
				int r = rawPixels[i] >> 16 & 0xFF;
				int g = rawPixels[i] >> 8 & 0xFF;
				int b = rawPixels[i] & 0xFF;

				rawPixels[i] = (a << 24 | b << 16 | g << 8 | r);
				/*Color pColor = new Color(rawPixels[i], true);

				int alpha = 100;
				int red = pColor.getRed();
				int green = pColor.getGreen();
				int blue = pColor.getBlue();

				rawPixels[i] = alpha<<24 | blue<<16 | green<<8 | red;
				rawPixels[i] = rawPixels[i] >> 24;
				rawPixels[i] = alpha<<24 | blue<<16 | green<<8 | red;*/
			}
			pixels.asIntBuffer().put(rawPixels);
			GLU.gluBuild2DMipmaps(3553, 6408, w, h, 6408, 5121, pixels);
			return id;
		} catch (IOException e) {
			throw new RuntimeException("!!");
			//return 1;
		}
	}

	public static void bind(int id) {
		if (id != lastId) {
			GL11.glBindTexture(3553, id);
			lastId = id;
		}
	}
	
}
