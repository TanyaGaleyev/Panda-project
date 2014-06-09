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
    public Motion getTpAwareChildMotion() {
        Motion ret;
        if(getType() == MotionType.TP_LR || getType() == MotionType.TP_RL)
            ret = new Motion(getChildMotion().getType().opposite(), getChildMotion().getStage());
        else
            ret = getChildMotion();
        return ret;
    }
}
