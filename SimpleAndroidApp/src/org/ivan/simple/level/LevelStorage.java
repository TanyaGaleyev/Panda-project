package org.ivan.simple.level;
public class LevelStorage {
	public int[][][] getLevel(int num) {
		switch (num)
		{
		case 1:
	    return level1();
		case 2:
		return level1();

		
		default:
		return level1();

		}
	}
	public int[][] getPrizesMap(int num){
		switch (num)
		{
		case 1:
	    return prizeMap1();
		case 2:
		return prizeMap1();

		
		default:
		return prizeMap1();

		}
		
		
	}
private int[][][] level1() {
	int[][][] lev={
			{{1,0,0,0},{0,1,0,3},{0,6,0,2},{0,0,0,2},{0,0,0,2},{0,0,0,2},{0,0,0,2},{0,0,0,7},{0,0,0,8},{0,6,0,0}},
			{{1,0,1,0},{1,3,1,1},{0,0,0,1},{0,0,0,1},{0,0,0,1},{1,0,0,1},{0,0,0,1},{0,7,1,1},{0,8,0,0},{0,0,0,0}},
			{{0,0,0,5},{0,0,0,1},{0,0,0,1},{0,0,0,1},{1,0,0,1},{0,0,0,1},{0,0,0,1},{1,0,0,1},{0,0,0,1},{0,0,0,0}},
			{{0,5,0,4},{0,0,0,0},{0,0,0,4},{0,0,0,4},{0,0,0,4},{0,0,0,4},{0,0,0,8},{0,0,0,4},{0,0,0,8},{0,0,0,0}},
			{{0,4,0,1},{0,4,0,1},{0,4,0,1},{0,4,0,1},{0,4,0,1},{0,4,0,1},{0,8,0,1},{0,4,0,1},{0,8,0,1},{0,0,0,1}}
	//0-none
	//1-simple
	//2- reduce
	//3-angle right
	//4-angle left
	//5- trampoline
	//6-magnet
	//7-trowout right
	//8-trowout left		
	
	};
   return lev;
	
}
private int[][] prizeMap1(){
	int[][] map={
			{0,0,0,1,1,1,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,1,1,1,1,1,1,1,0}		
			
	};
return map;	
	
}
	

}
