package org.ivan.simple.game;

/**
 * This enumeration represents type of motion that hero can do.
 * Motion type is based on current state, user control, position in level grid
 * For example:
 * Jump left/right
 * Slide left/right
 * Jump up
 * Fall
 * Fly left/right
 * Attach to wall
 * @author Ivan
 *
 */
public enum MotionType {
	JUMP {

		public int getYSpeed() {
			if(stage == 0) return 0;
			return -1;
		}
		
		public void continueMotion() {
			stage = 1;
			super.continueMotion();
		}
		
		public boolean isUncontrolable() {
			return stage == 0;
		}
		
	},
//	PRE_JUMP ,
	BEAT_ROOF,
	FALL {
		public int getYSpeed() {
			return 1;
		}
	},
	FALL_BLANSH {
		public int getYSpeed() {
			return 1;
		}
	},
	JUMP_LEFT {
		public int getXSpeed() {
			return -1;
		}
	},
	JUMP_RIGHT {
		public int getXSpeed() {
			return 1;
		}
	},
//	STEP_LEFT {
//		public int getXSpeed() {
//			return -1;
//		}
//	},
//	STEP_RIGHT {
//		public int getXSpeed() {
//			return 1;
//		}
//	},
	STAY,
//	STEP_LEFT_WALL,
//	STEP_RIGHT_WALL,
	JUMP_RIGHT_WALL,
	JUMP_LEFT_WALL,
	MAGNET {
		
		public void continueMotion() {
			stage = 1;
			super.continueMotion();
		}
		
		public void finishMotion() {
			stage = 2;
		}
		
		@Override
		public boolean isFinishing() {
			return stage == 2;
		}
	},
	PRE_MAGNET,
	THROW_LEFT {
		public int getXSpeed() {
			return -1;
		}
		
		public void continueMotion() {
			stage = (stage + 1) % 2;
		}
	},
	THROW_RIGHT {
		public int getXSpeed() {
			return 1;
		}
		
		public void continueMotion() {
			stage = (stage + 1) % 2;
		}
	},
	FLY_RIGHT {
		public int getXSpeed() {
			if(stage == 0) return 0;
			return 1;
		}
		
		public void continueMotion() {
			stage = 1;
			super.continueMotion();
		}
		
		public void finishMotion() {
			stage = 2;
		}
		
		@Override
		public boolean isFinishing() {
			return stage == 2;
		}
		
		@Override
		public boolean isUncontrolable() {
			return stage == 0;
		}
	},
	FLY_LEFT {
		public int getXSpeed() {
			if(stage == 0) return 0;
			return -1;
		}
		
		public void continueMotion() {
			stage = 1;
			super.continueMotion();
		}
		
		public void finishMotion() {
			stage = 2;
		}
		
		@Override
		public boolean isFinishing() {
			return stage == 2;
		}
		
		@Override
		public boolean isUncontrolable() {
			return stage == 0;
		}
	},
	NONE;
	
	
	/**
	 * Try to group sets of motion animations to single motion
	 * (for example group JUMP and PRE_JUMP to single jump motion) 
	 */
	int stage = 0;
	private int stages = 1;
	
	public void startMotion() {
		stage = 0;
	}
	
	public void continueMotion() {
		if(isFinishing()) stage = 0;
	}
	
	public void finishMotion() {
		stage = 0;
	}
	
	public int getStage() {
		return stage;
	}
	
	public boolean isFinishing() {
		return false;
	}
	
	public boolean isStarting() {
		return false;
	}
	
	public int getXSpeed() {
		return 0;
	}
	
	public int getYSpeed() {
		return 0;
	}
	
	public boolean isUncontrolable() {
		return false;
	}
	
}
