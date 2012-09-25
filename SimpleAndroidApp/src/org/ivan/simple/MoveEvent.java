package org.ivan.simple;

public class MoveEvent {
	MoveType type = MoveType.IDLE;
	int move_times = 0;

	public MoveEvent(MoveType type, int move_times) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.move_times = move_times;
	}
}
