package com.mingames.nizz.library;

import java.util.Random;

public class CustMath {
	
	public static int seed = 2;
	private static int secretseed = seed;
	private static int resets = 0;
	private static int uncounted = 0;
	
	public static int weightedRandom(int max, int weight) {
		int number = max;
		for(int i=0;i<weight;i++) {
			int cur = new Random().nextInt(max);
			if(cur<number) {
				number=cur;
			}
		}
		return number+1;
	}
	
	public static int weightedRandom(Random r,int max, int weight) {
		int number = max;
		for(int i=0;i<weight;i++) {
			int cur = r.nextInt(max);
			if(cur<number) {
				number=cur;
			}
		}
		return number+1;
	}
	
	public static int randInt(int min, int max, Random rand) {
		
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	public static int randInt(int min, int max) {
		
		Random rand = new Random();
		
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
}
