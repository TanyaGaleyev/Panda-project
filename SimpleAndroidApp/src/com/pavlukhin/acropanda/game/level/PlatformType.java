package com.pavlukhin.acropanda.game.level;

public enum PlatformType {
	SIMPLE,
	//v=vertical =)
	SIMPLE_V,
	
	REDUCE,
	ANGLE_RIGHT,
	ANGLE_LEFT,
	TRAMPOLINE,
	ELECTRO,
	THROW_OUT_LEFT,
	THROW_OUT_RIGHT,
	SPRING,
	SPIKE,
	SPIKE_V,
	WIN,
	TELEPORT_L_V,
	TELEPORT_R_V,
	SLICK,
	SLOPE,
	ONE_WAY_LEFT,
	ONE_WAY_RIGHT,
	ONE_WAY_DOWN,
	ONE_WAY_UP,
	SWITCH,
	UNLOCK,
	UNLOCK_H,
	STRING,
	LIMIT,
	BRICK,
	BRICK_V,
	GLUE,
	GLUE_V,
	TELEPORT,
	INVISIBLE,
	TRANSPARENT,
	TRANSPARENT_V,
	WAY_UP_DOWN,
	CLOUD,
    SPIKE_UP,
	
	NONE;

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
	//15-one way right
	//16-one way left
    public static PlatformType getTypeById(int typeid, boolean horizontal) {
        switch(typeid) {
            case 1: return horizontal ? SIMPLE : SIMPLE_V;
            case 2: return REDUCE;
            case 3: return ANGLE_RIGHT;
            case 4: return ANGLE_LEFT;
            case 5: return TRAMPOLINE;
            case 6: return ELECTRO;
            case 7: return THROW_OUT_RIGHT;
            case 8: return THROW_OUT_LEFT;
            case 9: return SPRING;
            case 10: return horizontal? SPIKE : SPIKE_V;
            case 11: return TELEPORT_L_V;
            case 12: return TELEPORT_R_V;
            case 13: return SLICK;
            case 14: return SLOPE;
            case 15: return horizontal ? ONE_WAY_DOWN : ONE_WAY_RIGHT;
            case 16: return horizontal ? ONE_WAY_UP : ONE_WAY_LEFT;
            case 17: return SWITCH;
            case 18: return horizontal ? UNLOCK_H : UNLOCK;
            case 19: return STRING;
            case 20: return LIMIT;
            case 21: return horizontal ? BRICK : BRICK_V;
            case 22: return horizontal ? GLUE : GLUE_V;
            case 23: return TELEPORT;
            case 24: return horizontal ? TRANSPARENT : TRANSPARENT_V;
            case 25: return WAY_UP_DOWN;
            case 26: return INVISIBLE;
            case 27: return CLOUD;
            case 28: return SPIKE_UP;
            default: return NONE;
        }
    }
	
}

