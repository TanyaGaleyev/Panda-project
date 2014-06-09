package org.ivan.simple.game.motion;

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

    @Override
    public MotionType getTpAwareChildMotionType() {
        MotionType ret;
        if(getType() == MotionType.TP_LR || getType() == MotionType.TP_RL)
            ret = getChildMotion().getType().opposite();
        else
            ret = getChildMotion().getType();
        return ret;
    }
}
