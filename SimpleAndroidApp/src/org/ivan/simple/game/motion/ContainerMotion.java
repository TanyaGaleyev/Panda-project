package org.ivan.simple.game.motion;

import org.ivan.simple.game.motion.Motion;
import org.ivan.simple.game.motion.MotionType;

public class ContainerMotion extends Motion {
	private final Motion childMotion;
	
	public ContainerMotion(MotionType type, Motion childMotion) {
		this(type, 0 , childMotion);
	}
	
	public ContainerMotion(MotionType type, int stage, Motion childMotion) {
		super(type, stage);
		this.childMotion = childMotion;
	}
	
	@Override
	public Motion getChildMotion() {
		return childMotion;
	}
}
