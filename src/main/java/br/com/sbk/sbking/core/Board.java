package br.com.sbk.sbking.core;

public class Board {

	private Hand northHand;
	private Hand eastHand;
	private Hand southHand;
	private Hand westHand;
	private Direction leader;

	public Board(Hand northHand, Hand eastHand, Hand southHand, Hand westHand, Direction leader) {
		this.northHand = northHand;
		this.eastHand = eastHand;
		this.southHand = southHand;
		this.westHand = westHand;
		this.sortAllHands();
		this.leader = leader;
	}

	public Direction getLeader() {
		return leader;
	}
	
	public Hand getHandOf(Direction direction) {
		if (direction.isNorth()) {
			return this.northHand;
		}
		if (direction.isEast()) {
			return this.eastHand;
		}
		if (direction.isSouth()) {
			return this.southHand;
		}
		if (direction.isWest()) {
			return this.westHand;
		}
		throw new RuntimeException("Invalid direction");
	}
	
	private void sortAllHands() {
		for(Direction direction : Direction.values()) {
			this.getHandOf(direction).sort();
		}
	}

}
