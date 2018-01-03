package nizz.mingames.nizz.level.renderers;

import com.mingames.nizz.level.Level;
import com.mingames.nizz.level.Tesselator;

public interface Renderer {

	int texure = 0;
	
	public void setTexture(int textureId);
	
	public void setTexture(int[] textureId);
	
	public void render(Tesselator t, Level level, int layer, int x, int y, int z);

	public void renderFace(Tesselator t, int x, int y, int z, int face);

}
