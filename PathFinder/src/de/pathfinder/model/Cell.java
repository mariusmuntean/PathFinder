package de.pathfinder.model;

public class Cell {
	public long g, h, rhs, row, col;
	public boolean isObstacle;
	public Key key;
	public Neighbors neighbors;
	public Costs costs;
	public Cell searchTree;

	public Cell() {
		this.costs = new Costs();
		this.neighbors = new Neighbors();
		this.key = new Key();
		this.searchTree = null;
	}

	
	
	class Costs {
		public int N, NE, E, SE, S, SW, W, NW;
	}

	class Neighbors {

		public Cell N, NE, E, SE, S, SW, W, NW;
	}

}
