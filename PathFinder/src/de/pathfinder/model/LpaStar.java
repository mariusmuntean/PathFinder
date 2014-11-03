package de.pathfinder.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.function.Predicate;

public class LpaStar {

	Map map;
	PriorityQueue<Cell> expandedList;
	LinkedList<Cell> shortestPath;

	CellComparator cC;


	public LpaStar(Map map) {
		this.map = map;
		this.cC = new CellComparator();
	}

	private Key updateKey(Cell c) {
		Key k = c.key;
		k.k1 = Math.min(c.g, c.rhs) + (c.h = map.getHeuristic(c));
		k.k2 = Math.min(c.g, c.rhs);

		return k;
	}

	private void Initialize() {
		expandedList = new PriorityQueue<Cell>(new CellComparator());

		for (int i = 0; i < map.getCells().length; i++) {
			for (int j = 0; j < map.getCells()[0].length; j++) {
				map.getCells()[i][j].g = map.getCells()[i][j].rhs = Integer.MAX_VALUE;
			}
		}

		map.getStartCell().rhs = 0;
		map.getStartCell().key = new Key(map.getHeuristic(map.getStartCell()), 0);
		expandedList.add(map.getStartCell());

	}

	public LinkedList<Cell> getShortestPath() {

		updateShortestPath();

		return this.shortestPath;
	}

	private void updateCell(Cell c) {

		if (c != map.getStartCell()) {
			updateRhs(c);
		}

		if (expandedList.contains(c)) {
			expandedList.remove(c);
		}

		if (c.g != c.rhs) {
			updateKey(c);
			expandedList.add(c);
		}
	}

	private void updateRhs(Cell c) {
		if (c == null) {
			System.out.println("determineRhs(Cell c): argument cannot be null");
		}
		if (c == map.getGoalCell()) {
			System.out.println("Updating goal cell RHS");
		}
		long rhs = c.rhs;
		if (c.neighbors.N != null && !c.neighbors.N.isObstacle) {
			rhs = Math.min((c.neighbors.N.g + c.neighbors.N.costs.S), c.rhs);
			c.searchTree = c.rhs != rhs ? c.neighbors.N : c.searchTree;
			c.rhs = rhs;
		}

		if (c.neighbors.NE != null && !c.neighbors.NE.isObstacle) {
			rhs = Math.min((c.neighbors.NE.g + c.neighbors.NE.costs.SW), rhs);
			c.searchTree = c.rhs != rhs ? c.neighbors.NE : c.searchTree;
			c.rhs = rhs;
		}

		if (c.neighbors.E != null && !c.neighbors.E.isObstacle) {
			rhs = Math.min((c.neighbors.E.g + c.neighbors.E.costs.W), rhs);
			c.searchTree = c.rhs != rhs ? c.neighbors.E : c.searchTree;
			c.rhs = rhs;
		}

		if (c.neighbors.SE != null && !c.neighbors.SE.isObstacle) {
			rhs = Math.min((c.neighbors.SE.g + c.neighbors.SE.costs.NW), rhs);
			c.searchTree = c.rhs != rhs ? c.neighbors.SE : c.searchTree;
			c.rhs = rhs;
		}

		if (c.neighbors.S != null && !c.neighbors.S.isObstacle) {
			rhs = Math.min((c.neighbors.S.g + c.neighbors.S.costs.N), rhs);
			c.searchTree = c.rhs != rhs ? c.neighbors.S : c.searchTree;
			c.rhs = rhs;
		}

		if (c.neighbors.SW != null && !c.neighbors.SW.isObstacle) {
			rhs = Math.min((c.neighbors.SW.g + c.neighbors.SW.costs.NE), rhs);
			c.searchTree = c.rhs != rhs ? c.neighbors.SW : c.searchTree;
			c.rhs = rhs;
		}

		if (c.neighbors.W != null && !c.neighbors.W.isObstacle) {
			rhs = Math.min((c.neighbors.W.g + c.neighbors.W.costs.E), rhs);
			c.searchTree = c.rhs != rhs ? c.neighbors.W : c.searchTree;
			c.rhs = rhs;
		}

		if (c.neighbors.NW != null && !c.neighbors.NW.isObstacle) {
			rhs = Math.min((c.neighbors.NW.g + c.neighbors.NW.costs.SE), rhs);
			c.searchTree = c.rhs != rhs ? c.neighbors.NW : c.searchTree;
			c.rhs = rhs;
		}

	}

	private void computeShortestPath() {
		Cell goalCell = map.getGoalCell();
		updateKey(goalCell);
		// Cell peekedCell = expandedList.peek() == null ? new Cell() {
		// {
		// key = new Key(Integer.MAX_VALUE, Integer.MAX_VALUE);
		// }
		// } : expandedList.peek();
		while (expandedList.peek() != null && cC.compare(expandedList.peek(), goalCell) < 0
				|| goalCell.rhs != goalCell.g) {
			Cell topCell = expandedList.poll();
			if (topCell.g > topCell.rhs) {
				topCell.g = topCell.rhs;
				for (Cell c : getCellSuccessors(topCell)) {
					updateCell(c);
				}
			} else {
				topCell.g = Integer.MAX_VALUE;
				ArrayList<Cell> successors = getCellSuccessors(topCell);
				successors.add(topCell);
				for (Cell c : successors) {
					updateCell(c);
				}
			}
		}

		Cell c = goalCell;
		LinkedList<Cell> path = new LinkedList<Cell>();
		while (c != null) {
			path.add(c);
			System.out.println(String.format("row:%02d col:%02d", c.row, c.col));
			c = c.searchTree;
		}
		if (!path.isEmpty() && path.getLast() == map.getStartCell()) {
			this.shortestPath = path;
		}
	}

	private ArrayList<Cell> getCellSuccessors(Cell topCell) {
		ArrayList<Cell> successors = new ArrayList<Cell>() {
			{
				add(topCell.neighbors.N);
				add(topCell.neighbors.NE);
				add(topCell.neighbors.E);
				add(topCell.neighbors.SE);
				add(topCell.neighbors.S);
				add(topCell.neighbors.SW);
				add(topCell.neighbors.W);
				add(topCell.neighbors.NW);
			}
		};

		successors.removeIf(new Predicate<Cell>() {

			@Override
			public boolean test(Cell t) {
				return t == null || t.isObstacle;
			}
		});

		return successors;
	}

	private void updateShortestPath() {
		Initialize();
		computeShortestPath();
		// printMap();
		// printPath();
	}

	private void printPath() {
		Cell c = map.getGoalCell();
		while (c != null) {
			System.out.println(String.format("row:%02d col:%02d", c.row, c.col));
			c = c.searchTree;
		}

	}

	private void printMap() {
		System.out.print("     ");
		for (int i = 0; i < map.getMapWidth(); i++) {
			System.out.print(String.format(" %02d   ", i));
		}
		System.out.println();
		for (int i = 0; i < map.getMapHeight(); i++) {
			for (int j = 0; j < map.getMapWidth(); j++) {

				if (j == 0)
					System.out.print(String.format("%02d", i));

				System.out.print(" ");
				if (map.cells[i][j] == map.getStartCell()) {
					System.out.print("**S**");
					continue;
				}
				if (map.cells[i][j] == map.getGoalCell()) {
					System.out.print("**G**");
					continue;
				}

				if (map.cells[i][j].isObstacle) {
					System.out.print("|||||");
					continue;
				}
				// if (map.cells[i][j].searchTree == null) {
				String rhsSymbol = map.cells[i][j].rhs == Integer.MAX_VALUE ? "M" : Long
						.toString(map.cells[i][j].rhs);
				if (this.shortestPath != null && this.shortestPath.contains(map.cells[i][j])) {
					System.err.print(String.format("..%2s.", rhsSymbol));
				} else {
					System.out.print(String.format("..%2s.", rhsSymbol));
				}
				// }
			}
			System.out.println();
		}

	}

}
