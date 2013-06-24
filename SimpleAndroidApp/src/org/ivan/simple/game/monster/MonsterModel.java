package org.ivan.simple.game.monster;

import org.ivan.simple.game.monster.strategies.MonsterStrategy;

public class MonsterModel {
	
	private MonsterStrategy strategy;
	
	private MonsterDirection currDirection = MonsterDirection.IDLE;
	
	private int row;
	private int col;
	private int prevRow;
	private int prevCol;
	private int monsterId;
	
	public MonsterModel(int row, int col, MonsterStrategy strategy, int monsterId) {
		this.strategy = strategy;
		this.row = row;
		this.col = col;
		prevRow = row;
		prevCol = col;
		//currDirection = strategy.nextDirection(false);
		
		this.monsterId = monsterId;
	}
	
	public MonsterDirection getDirection() {
		return currDirection;
	}
	
	public void setDirection(MonsterDirection direction) {
		currDirection = direction;
	}
	
	public void moveInDirection() {
		prevRow = row;
		prevCol = col;
		switch(currDirection) {
		case UP:
			row--;
			break;
		case LEFT:
			col--;
			break;
		case DOWN:
			row++;
			break;
		case RIGHT:
			col++;
			break;
		default:
			break;
		}
	}
	
	public MonsterStrategy getStrategy() {
		return strategy;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getPrevRow() {
		return prevRow;
	}

	public int getPrevCol() {
		return prevCol;
	}

	public int getMonsterId() {
		return monsterId;
	}
	
	
}
