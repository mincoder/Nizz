package com.mingames.nizz.level.block;

import nizz.mingames.nizz.level.renderers.BlockRenderer;

public interface Block{
	
	int id=0;
	BlockRenderer tile = new BlockRenderer();
	
	int x=0;
	int y=0;
	
	public int blockUpdate();
	
	public void destroyBlock();
	
	public void buildBlock();
	
	public void renderBlock();
	
}
