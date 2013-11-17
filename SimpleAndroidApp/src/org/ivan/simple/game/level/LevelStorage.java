package org.ivan.simple.game.level;
public class LevelStorage {
	public int[][][] getLevels(int setid) {
		switch(setid) {
            case 1: return new int[][][]
                    {{ {1, 25, 50} , {2, 10, 20} , {3, 25, 50} , {4, 25, 50} },
                     { {5, 25, 60} , {2, 25, 50} , {3, 25, 50} , {3, 25, 50} }};
		case 2: return new int[][][]
				{{ {3, 25, 50} , {2, 10, 20} , {1, 25, 50} , {1, 25, 50} },
				 { {4, 25, 60} , {2, 25, 50} , {3, 25, 50} , {3, 25, 50} }};
		case 3:	return new int[][][]
				{{ {1, 25, 50} , {1, 25, 50} , {1, 25, 50} , {1, 25, 50} },
				 { {2, 25, 50} , {2, 25, 50} }};
		case 4: return new int[][][]
				{{ {4, 25, 50} , {4, 25, 50} },
				 { {1, 25, 50} , {2, 25, 50} , {3, 25, 50} , {4, 25, 50} }};
		default:return new int[][][]
				{{ {3, 25, 50} , {2, 25, 50} , {1, 25, 50} , {1, 25, 50} },
				 { {4, 25, 50} , {2, 25, 50} , {3, 25, 50} , {3, 25, 50} }};
		}
	}
	
	public int[][][][] getLevel(int num) {
		switch (num)
		{
		case 1: return level1();
		case 2: return level2();
		case 3: return level3();
		case 4: return level4();
        case 5: return level5();
		default:return level1();
		}
	}
	public int[][] getPrizesMap(int num){
		switch (num)
		{
		case 1: return prizeMap1();
		case 2: return prizeMap2();
		case 3: return prizeMap3();
		case 4: return prizeMap4();
        case 5: return prizeMap5();
		default:return prizeMap1();
		}
	}
	
	public int[] getWinCell(int num) {
		switch (num)
		{
		case 1: return winCell1();
		case 2: return winCell2();
		case 3: return winCell3();
		case 4: return winCell4();
        case 5: return winCell5();
		default:return winCell1();
		}
	}
	
	private int[][][][] level1() {
		int[][][][] lev= {
                {  { {1}, {10}, {10}, {0} },  { {0}, {1}, {0}, {10} },  { {0}, {1}, {0}, {0} },  { {0}, {1}, {0}, {0} },  { {0}, {1}, {0}, {0} },  { {0}, {1}, {0}, {0} },  { {0}, {1}, {0}, {0} },  { {0}, {1}, {0}, {10} },  { {0}, {1}, {0}, {0} },  { {0}, {1}, {1}, {0} }  },
                {  { {1}, {0}, {0}, {0} },  { {0}, {0}, {10}, {0} },  { {0}, {0}, {0}, {10} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {10} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {10} },  { {0}, {0}, {1}, {0} }  },
                {  { {1}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {10}, {0} },  { {0}, {0}, {0}, {10} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {1}, {0} }  },
                {  { {1}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {10}, {0} },  { {0}, {0}, {0}, {10} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {1}, {0} }  },
                {  { {1}, {0}, {0}, {1} },  { {0}, {0}, {0}, {1} },  { {0}, {0}, {0}, {1} },  { {0}, {0}, {0}, {1} },  { {0}, {0}, {0}, {1} },  { {0}, {0}, {0}, {1} },  { {0}, {0}, {0}, {1} },  { {0}, {0}, {0}, {1} },  { {0}, {0}, {0}, {1} },  { {0}, {0}, {1}, {1} }  }
				 //0-none
		//1-simple
		//2- reduce
		//3-angle right
		//4-angle left
		//5- trampoline
		//6-magnet
		//7-trowout right
		//8-trowout left	
		//9-spring
		//10-spike
		//11-teleport_l_v
		//12-teleport_r_v
		//13-slick
		//14-slope
		//15-one way right (down)
		//16-one way left (up)
		//17-switch platform
		//18-unlock
		//19-string
		//20-limit_way
		//21-brick
		//22-glue
		//23-teleport horizontal
		//24-transparent
		//25-way up and down
		//26-invisible
		//27-cloud
		};
	   return lev;
		
	}
	
	private int[][][][] level2() {
		int[][][][] lev={
                {  { {1}, {10}, {0}, {0} },  { {0}, {1}, {0}, {2} },  { {0}, {1}, {0}, {2} },  { {0}, {1}, {0}, {2} },  { {0}, {1}, {0}, {2} },  { {0}, {1}, {0}, {0} },  { {0}, {1}, {0}, {0} },  { {0}, {1}, {1}, {0} },  { {0}, {1}, {0}, {0} },  { {0}, {1}, {1}, {0} }  },
                {  { {1}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {2} },  { {0}, {0}, {0}, {2} },  { {0}, {0}, {0}, {2} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {1}, {0} }  },
                {  { {1}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {2} },  { {0}, {0}, {0}, {2} },  { {0}, {0}, {0}, {2} },  { {0}, {0}, {0}, {2} },  { {0}, {0}, {0}, {2} },  { {0}, {0}, {1}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {1}, {0} }  },
                {  { {1}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {1}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {1}, {0} }  },
                {  { {1}, {0}, {0}, {1} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {1}, {2} },  { {0}, {0}, {0}, {2} },  { {0}, {0}, {1}, {2} }  }
				};
	   return lev;
	}
	
	private int[][][][] level3() {
		int[][][][] lev={
                {  { {1}, {10}, {0}, {0} },  { {0}, {10}, {0}, {0} },  { {0}, {10}, {0}, {0} },  { {0}, {10}, {0}, {0} },  { {0}, {10}, {0}, {0} },  { {0}, {10}, {0}, {0} },  { {0}, {10}, {0}, {0} },  { {0}, {10}, {0}, {0} },  { {0}, {10}, {0}, {0} },  { {0}, {10}, {1}, {0} }  },
                {  { {1}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {1}, {0} }  },
                {  { {1}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {1}, {0} }  },
                {  { {1}, {0}, {0}, {1} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {2} },  { {0}, {0}, {0}, {2} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {1}, {1} }  },
                {  { {1}, {0}, {0}, {13} },  { {0}, {0}, {0}, {13} },  { {0}, {0}, {0}, {13} },  { {0}, {0}, {0}, {13} },  { {0}, {0}, {0}, {13} },  { {0}, {0}, {0}, {13} },  { {0}, {0}, {0}, {13} },  { {0}, {0}, {0}, {13} },  { {0}, {0}, {0}, {13} },  { {0}, {0}, {1}, {13} }  }
        };
	   return lev;
	}
	
	private int[][][][] level4() {
		int[][][][] lev={
                {  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {2} },  { {0}, {0}, {10}, {15} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {2} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} }  },
                {  { {0}, {0}, {10}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {10}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {10}, {2} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {2} },  { {0}, {0}, {0}, {4} }  },
                {  { {0}, {0}, {10}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {10}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {10} },  { {0}, {0}, {10}, {2} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {10}, {2} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} }  },
                {  { {0}, {0}, {10}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {10}, {0} },  { {0}, {0}, {0}, {10} },  { {0}, {0}, {0}, {16} },  { {0}, {0}, {10}, {0} },  { {0}, {0}, {10}, {2} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} }  },
                {  { {0}, {0}, {10}, {1} },  { {0}, {0}, {0}, {2} },  { {0}, {0}, {0}, {2} },  { {0}, {0}, {0}, {2} },  { {0}, {0}, {0}, {2} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {7} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {3} },  { {0}, {0}, {0}, {0} }  }
				};
	   return lev;
	}
    private int[][][][] level5() {
        int[][][][] lev={
                {  { {10}, {10}, {22}, {0} },  { {0}, {10}, {0}, {0} },  { {0}, {10}, {22}, {0} },  { {0}, {10}, {0}, {0} },  { {0}, {10}, {22}, {0} },  { {0}, {10}, {0}, {0} },  { {0}, {10}, {22}, {0} },  { {0}, {10}, {0}, {0} },  { {0}, {10}, {22}, {0} },  { {0}, {10}, {10}, {0} }  },
                {  { {10}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {10}, {0} }  },
                {  { {10}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {10}, {0} }  },
                {  { {10}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {0}, {0} },  { {0}, {0}, {10}, {0} }  },
                {  { {10}, {0}, {0}, {23,0} },  { {0}, {0}, {0}, {23,1} },  { {0}, {0}, {0}, {23,2} },  { {0}, {0}, {0}, {23,3} },  { {0}, {0}, {0}, {23,4} },  { {0}, {0}, {0}, {23,5} },  { {0}, {0}, {0}, {23,6} },  { {0}, {0}, {0}, {23,7} },  { {0}, {0}, {0}, {23,8  } },  { {0}, {0}, {10}, {23,9} }  }
        };
        return lev;
    }

	private int[][] prizeMap1(){
		int[][] map={
                {1,0,0,0,0,0,0,0,0,0 },
                {1,1,0,0,0,0,0,1,0,0 },
                {1,1,1,0,0,0,1,1,1,0 },
                {1,0,1,1,0,0,1,1,1,0 },
                {1,0,0,1,1,1,1,1,1,0 }
        };
		return map;	
	}
	
	private int[][] prizeMap2(){
		int[][] map={
                {1,1,1,1,1,1,0,0,1,1 },
                {1,0,1,1,1,1,0,1,1,1 },
                {1,0,1,1,1,1,1,1,1,1 },
                {1,0,0,0,0,0,0,0,1,1 },
                {1,0,0,0,0,0,0,1,1,1 }
		           };
		return map;	
	}
	
	private int[][] prizeMap3(){
		int[][] map={
                {0,0,0,0,0,0,0,0,0,0 },
                {0,0,0,0,0,0,0,0,0,0 },
                {0,0,0,0,0,0,0,0,0,0 },
                {0,0,0,0,0,0,0,0,0,0 },
                {1,1,1,1,1,1,1,1,1,1 }
        };
		return map;	
	}
	
	private int[][] prizeMap4(){
		int[][] map={
                {1,1,1,0,0,1,1,1,1,0 },
                {1,1,0,0,0,1,1,1,1,1 },
                {1,1,0,1,1,1,1,1,0,0 },
                {1,1,0,0,1,0,1,0,0,0 },
                {1,1,0,1,1,0,0,0,0,0 }};
		return map;	
	}
    private int[][] prizeMap5(){
        int[][] map={
                {1,1,1,1,1,1,1,1,1,1 },
                {1,1,1,1,1,1,1,1,1,1 },
                {1,1,1,1,1,1,1,1,1,1 },
                {1,0,1,0,1,0,1,1,0,1 },
                {0,0,0,0,0,0,0,0,0,0 }};
        return map;
    }
	
	private int[] winCell1() {
		return new int[]{4,8};
	}
	
	private int[] winCell2() {
		return new int[]{4,9};
	}
	
	private int[] winCell3() {
		return new int[]{0,9};
	}
	
	private int[] winCell4() {
		return new int[]{4,9};
	}
    private int[] winCell5() {
        return new int[]{4,9};
    }
	
	public int[][] getRouteArray(int lev) {
		switch (lev) {
		case 1:
			return new int[][] {
					{0,3},{0,0},{5,0},{5,5}
			};
		case 2:
			return new int[][] {
					{0,0},{5,0},{5,5}
			};
            case 3:
                return new int[][] {
                        {9,4},{0,4}
                };
            case 5:
                return new int[][] {
                        {9,2},{0,2},{0,1},{9,1},{9,2}
                };




		default:
			return new int[][] {
					{0,2},{0,0},{2,0},{2,2},{0,2}
			};
		}
	}
	

}
