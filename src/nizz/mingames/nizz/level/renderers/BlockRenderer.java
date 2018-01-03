package nizz.mingames.nizz.level.renderers;

import com.mingames.nizz.level.Level;
import com.mingames.nizz.level.Tesselator;

public class BlockRenderer implements Renderer{

	private int tex[] = new int[6];

	public void render(Tesselator t, Level level, int layer, int x, int y, int z) {

		float v0 = 0.0F;
		float v1 = v0 + 0.0624375F;
		float c1 = 1.0F;
		float c2 = 0.8F;
		float c3 = 0.6F;

		float x0 = x + 0.0F;
		float x1 = x + 1.0F;
		float y0 = y + 0.0F;
		float y1 = y + 1.0F;
		float z0 = z + 0.0F;
		float z1 = z + 1.0F;

		if (!level.isSolidTile(x, y - 1, z)) {
			float br = level.getBrightness(x, y - 1, z) * c1;
			if (((br == c1 ? 1 : 0) ^ (layer == 1 ? 1 : 0)) != 0) {
				float u0 = tex[0] / 16.0F;
				float u1 = u0 + 0.0624375F;
				
				t.color(br, br, br);
				t.tex(u0, v1);
				t.vertex(x0, y0, z1);
				t.tex(u0, v0);
				t.vertex(x0, y0, z0);
				t.tex(u1, v0);
				t.vertex(x1, y0, z0);
				t.tex(u1, v1);
				t.vertex(x1, y0, z1);
			}
		}

		if (!level.isSolidTile(x, y + 1, z)) {
			float br = level.getBrightness(x, y, z) * c1;
			if (((br == c1 ? 1 : 0) ^ (layer == 1 ? 1 : 0)) != 0) {
				float u0 = tex[1] / 16.0F;
				float u1 = u0 + 0.0624375F;
				
				t.color(br, br, br);
				t.tex(u1, v1);
				t.vertex(x1, y1, z1);
				t.tex(u1, v0);
				t.vertex(x1, y1, z0);
				t.tex(u0, v0);
				t.vertex(x0, y1, z0);
				t.tex(u0, v1);
				t.vertex(x0, y1, z1);
			}
		}

		if (!level.isSolidTile(x, y, z - 1)) {
			float br = level.getBrightness(x, y, z - 1) * c2;
			if (((br == c2 ? 1 : 0) ^ (layer == 1 ? 1 : 0)) != 0) {
				float u0 = tex[2] / 16.0F;
				float u1 = u0 + 0.0624375F;
				
				t.color(br, br, br);
				t.tex(u1, v0);
				t.vertex(x0, y1, z0);
				t.tex(u0, v0);
				t.vertex(x1, y1, z0);
				t.tex(u0, v1);
				t.vertex(x1, y0, z0);
				t.tex(u1, v1);
				t.vertex(x0, y0, z0);
			}
		}

		if (!level.isSolidTile(x, y, z + 1)) {
			float br = level.getBrightness(x, y, z + 1) * c2;
			if (((br == c2 ? 1 : 0) ^ (layer == 1 ? 1 : 0)) != 0) {
				float u0 = tex[3] / 16.0F;
				float u1 = u0 + 0.0624375F;
				
				t.color(br, br, br);
				t.tex(u0, v0);
				t.vertex(x0, y1, z1);
				t.tex(u0, v1);
				t.vertex(x0, y0, z1);
				t.tex(u1, v1);
				t.vertex(x1, y0, z1);
				t.tex(u1, v0);
				t.vertex(x1, y1, z1);
			}
		}

		if (!level.isSolidTile(x - 1, y, z)) {
			float br = level.getBrightness(x - 1, y, z) * c3;
			if (((br == c3 ? 1 : 0) ^ (layer == 1 ? 1 : 0)) != 0) {
				float u0 = tex[4] / 16.0F;
				float u1 = u0 + 0.0624375F;
				
				t.color(br, br, br);
				t.tex(u1, v0);
				t.vertex(x0, y1, z1);
				t.tex(u0, v0);
				t.vertex(x0, y1, z0);
				t.tex(u0, v1);
				t.vertex(x0, y0, z0);
				t.tex(u1, v1);
				t.vertex(x0, y0, z1);
			}
		}

		if (!level.isSolidTile(x + 1, y, z)) {
			float br = level.getBrightness(x + 1, y, z) * c3;
			if (((br == c3 ? 1 : 0) ^ (layer == 1 ? 1 : 0)) != 0) {
				float u0 = tex[5] / 16.0F;
				float u1 = u0 + 0.0624375F;
				
				t.color(br, br, br);
				t.tex(u0, v1);
				t.vertex(x1, y0, z1);
				t.tex(u1, v1);
				t.vertex(x1, y0, z0);
				t.tex(u1, v0);
				t.vertex(x1, y1, z0);
				t.tex(u0, v0);
				t.vertex(x1, y1, z1);
			}
		}
	}

	public void renderFace(Tesselator t, int x, int y, int z, int face) {
		float x0 = x + 0.0F;
		float x1 = x + 1.0F;
		float y0 = y + 0.0F;
		float y1 = y + 1.0F;
		float z0 = z + 0.0F;
		float z1 = z + 1.0F;

		if (face == 0) {
			t.vertex(x0, y0, z1);
			t.vertex(x0, y0, z0);
			t.vertex(x1, y0, z0);
			t.vertex(x1, y0, z1);
		}

		if (face == 1) {
			t.vertex(x1, y1, z1);
			t.vertex(x1, y1, z0);
			t.vertex(x0, y1, z0);
			t.vertex(x0, y1, z1);
		}

		if (face == 2) {
			t.vertex(x0, y1, z0);
			t.vertex(x1, y1, z0);
			t.vertex(x1, y0, z0);
			t.vertex(x0, y0, z0);
		}

		if (face == 3) {
			t.vertex(x0, y1, z1);
			t.vertex(x0, y0, z1);
			t.vertex(x1, y0, z1);
			t.vertex(x1, y1, z1);
		}

		if (face == 4) {
			t.vertex(x0, y1, z1);
			t.vertex(x0, y1, z0);
			t.vertex(x0, y0, z0);
			t.vertex(x0, y0, z1);
		}

		if (face == 5) {
			t.vertex(x1, y0, z1);
			t.vertex(x1, y0, z0);
			t.vertex(x1, y1, z0);
			t.vertex(x1, y1, z1);
		}
	}

	@Override
	public void setTexture(int textureId) {
		for(int i=0;i<6;i++) {
			this.tex[i]=textureId;
		}
	}

	@Override
	public void setTexture(int[] textureId) {
		for(int i=0;i<6;i++) {
			this.tex[i]=textureId[i];
		}
	}
}
