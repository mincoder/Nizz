package nizz.level;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Tesselator {
	private static final int MAX_VERTICES = 100000;
	private FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(300000);
	private FloatBuffer texCoordBuffer = BufferUtils.createFloatBuffer(200000);
	private FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(300000);
	private int vertices = 0;
	private float u;
	private float v;
	private float r;
	private float g;
	private float b;
	private boolean hasColor = false;
	private boolean hasTexture = false;

	public Tesselator() {
	}

	public void flush() {
		vertexBuffer.flip();
		texCoordBuffer.flip();
		colorBuffer.flip();

		GL11.glVertexPointer(3, 0, vertexBuffer);
		if (hasTexture)
			GL11.glTexCoordPointer(2, 0, texCoordBuffer);
		if (hasColor) {
			GL11.glColorPointer(3, 0, colorBuffer);
		}
		GL11.glEnableClientState(32884);
		if (hasTexture)
			GL11.glEnableClientState(32888);
		if (hasColor) {
			GL11.glEnableClientState(32886);
		}

		GL11.glDrawArrays(7, 0, vertices);

		GL11.glDisableClientState(32884);
		if (hasTexture)
			GL11.glDisableClientState(32888);
		if (hasColor) {
			GL11.glDisableClientState(32886);
		}
		clear();
	}

	private void clear() {
		vertices = 0;

		vertexBuffer.clear();
		texCoordBuffer.clear();
		colorBuffer.clear();
	}

	public void init() {
		clear();
		hasColor = false;
		hasTexture = false;
	}

	public void tex(float u, float v) {
		hasTexture = true;
		this.u = u;
		this.v = v;
	}

	public void color(float r, float g, float b) {
		hasColor = true;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public void vertex(float x, float y, float z) {
		vertexBuffer.put(vertices * 3 + 0, x).put(vertices * 3 + 1, y)
				.put(vertices * 3 + 2, z);
		if (hasTexture)
			texCoordBuffer.put(vertices * 2 + 0, u).put(vertices * 2 + 1, v);
		if (hasColor)
			colorBuffer.put(vertices * 3 + 0, r).put(vertices * 3 + 1, g)
					.put(vertices * 3 + 2, b);
		vertices += 1;
		if (vertices == 100000) {
			flush();
		}
	}
}
