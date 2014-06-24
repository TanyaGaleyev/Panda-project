package org.ivan.simple.game.monster;

import android.graphics.Canvas;

import org.ivan.simple.game.hero.Sprite;

public class Monster {

    public static final float MONSTER_SPEED_SCALE = 16;

	private Sprite sprite;
	
	/* strictly speaking float coordinates may lead to some precision bugs
	 * may be we need some kind of "at least" uniform integral steps iterator */
	private float xCoordinate = 0;
    private float yCoordinate = 0;
    public final int gridStep;
    public final float speed;
	
	private final MonsterModel model;
	
	Monster(MonsterModel model, Sprite sprite, int gridStep) {
		this.model = model;
		this.sprite = sprite;
        this.gridStep = gridStep;
        // TODO this speed needs to be more generic should be dividable to gridStep without remainder
        this.speed = gridStep / MONSTER_SPEED_SCALE;
	}
	
	public void onDraw(Canvas canvas, boolean update) {
		if(model == null) return;
		sprite.onDraw(canvas, getXCoordinate(), getYCoordinate(), update);
	}
	
	public void moveInCurrentDirection() {
		if(model == null) return;
		switch(model.getDirection()) {
		case UP:
			yCoordinate -= speed;
			break;
		case LEFT:
			xCoordinate -= speed;
			break;
		case DOWN:
			yCoordinate += speed;
			break;
		case RIGHT:
			xCoordinate += speed;
			break;
		case IDLE:
		default:
			break;
		}
	}

    public int getXCoordinate() {
        return Math.round(xCoordinate);
    }

    public void setXCoordinate(float xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getYCoordinate() {
        return Math.round(yCoordinate);
    }

    public void setYCoordinate(float yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
	
}
