package org.ivan.simple.game;

public class ContainerMotion extends Motion {
	private final Motion childMotion;
	
	public ContainerMotion(MotionType type, Motion childMotion) {
		super(type);
		this.childMotion = childMotion; 
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
