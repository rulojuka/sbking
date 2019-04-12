package br.com.sbk.sbking.core;

import java.io.Serializable;

import br.com.sbk.sbking.core.rulesets.interfaces.Scoreable;

@SuppressWarnings("serial")
public class Score implements Serializable {

	private int northSouthPoints = 0;
	private int eastWestPoints = 0;
	private Scoreable scoreable;

	public Score(Scoreable scoreable) {
		this.scoreable = scoreable;
	}

	public int getNorthSouthPoints() {
		return northSouthPoints;
	}

	public int getEastWestPoints() {
		return eastWestPoints;
	}

	public void addTrickToDirection(Trick trick, Direction winner) {
		if (winner.isNorthSouth()) {
			addNorthSouth(trick);
		} else {
			addEastWest(trick);
		}
	}

	private void addNorthSouth(Trick trick) {
		northSouthPoints += this.scoreable.getPoints(trick);
	}

	private void addEastWest(Trick trick) {
		eastWestPoints += this.scoreable.getPoints(trick);
	}

	public int getAllPoints() {
		return this.eastWestPoints + this.northSouthPoints;
	}
	
	public int getFinalPunctuation() {
		int points = (getNorthSouthPoints() - getEastWestPoints()) * scoreable.getScoreMultiplier();
		if (scoreable.isNegative()) {
			points *= -1;
		}
		return points;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + eastWestPoints;
		result = prime * result + northSouthPoints;
		result = prime * result + ((scoreable == null) ? 0 : scoreable.hashCode());
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
		Score other = (Score) obj;
		if (eastWestPoints != other.eastWestPoints)
			return false;
		if (northSouthPoints != other.northSouthPoints)
			return false;
		if (scoreable == null) {
			if (other.scoreable != null)
				return false;
		} else if (!(scoreable.getClass() == other.scoreable.getClass()))
			return false;
		return true;
	}

}
