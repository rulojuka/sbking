package br.com.sbk.sbking.core;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Board implements Serializable {

	private Hand northHand;
	private Hand eastHand;
	private Hand southHand;
	private Hand westHand;
	private Direction dealer;

	public Board(Hand northHand, Hand eastHand, Hand southHand, Hand westHand, Direction dealer) {
		this.northHand = northHand;
		this.eastHand = eastHand;
		this.southHand = southHand;
		this.westHand = westHand;
		this.sortAllHands();
		this.dealer = dealer;
	}

	public Direction getDealer() {
		return dealer;
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
		for (Direction direction : Direction.values()) {
			this.getHandOf(direction).sort();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eastHand == null) ? 0 : eastHand.hashCode());
		result = prime * result + ((dealer == null) ? 0 : dealer.hashCode());
		result = prime * result + ((northHand == null) ? 0 : northHand.hashCode());
		result = prime * result + ((southHand == null) ? 0 : southHand.hashCode());
		result = prime * result + ((westHand == null) ? 0 : westHand.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		if (eastHand == null) {
			if (other.eastHand != null)
				return false;
		} else if (!eastHand.equals(other.eastHand))
			return false;
		if (dealer != other.dealer)
			return false;
		if (northHand == null) {
			if (other.northHand != null)
				return false;
		} else if (!northHand.equals(other.northHand))
			return false;
		if (southHand == null) {
			if (other.southHand != null)
				return false;
		} else if (!southHand.equals(other.southHand))
			return false;
		if (westHand == null) {
			if (other.westHand != null)
				return false;
		} else if (!westHand.equals(other.westHand))
			return false;
		return true;
	}
	
	

}
