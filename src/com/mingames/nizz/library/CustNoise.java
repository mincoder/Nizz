package com.mingames.nizz.library;

import java.util.Random;

public class CustNoise {
	
	public static int[][] getRandomNoise(Random r, int maxliftsize, int min, int liftamount,int lifts, int width, int height, int depth) {
		int[][] map = new int[width][height];
		//Random r = new Random(seed);
		
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				map[x][y]=min;
			}
		}
		
		for(int i=0;i<lifts;i++) {
			int curx = r.nextInt(width);
			int cury = r.nextInt(height);
			int liftsize = r.nextInt(maxliftsize);
			int liftamountcur = CustMath.weightedRandom(r, liftamount, 3);
			for(int x=0;x<liftsize;x++) {
				for(int y=0;y<liftsize;y++) {
					try {
						map[x+curx][y+cury] += liftamountcur;
					} catch(ArrayIndexOutOfBoundsException e) {
						
					}
				}
			}
		}
		
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				if(map[x][y]>=depth) {
					map[x][y]=depth-1;
				}
			}
		}
		
		return map;
	}
	
}
