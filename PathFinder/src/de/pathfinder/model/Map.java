package de.pathfinder.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

public class Map {

	public static final String START_CELL = "start";
	public static final String GOAL_CELL = "goal";
	public static final String CELLS = "cells";
	public static final String SHORTEST_PATH = "path";

	Cell[][] cells;
	Cell startCell, goalCell;
	LinkedList<Cell> shortestPath;

	// This class is observable
	PropertyChangeSupport pcs;

	public Map(Cell[][] cells, int startRow, int startCol, int goalRow, int goalCol) {
		this.cells = cells;
		this.startCell = this.cells[startRow][startCol];
		this.goalCell = this.cells[goalRow][goalCol];
		this.pcs = new PropertyChangeSupport(this);
	}

	public void setStartCell(int startRow, int startCol) {
		if (startCell.row != startRow || startCell.col != startCol) {
			Cell oldVal = this.cells[(int) startCell.row][(int) startCell.col];
			this.startCell = this.cells[startRow][startCol];
			this.pcs.firePropertyChange(START_CELL, oldVal, this.startCell);
		}
	}

	public Cell getStartCell() {
		return this.startCell;
	}

	public void setGoalCell(int goalRow, int goalCol) {
		if (goalCell.row != goalRow || goalCell.col != goalCol) {
			Cell oldVal = this.cells[(int) goalCell.row][(int) goalCell.col];
			this.goalCell = this.cells[goalRow][goalCol];
			pcs.firePropertyChange(GOAL_CELL, oldVal, this.goalCell);
		}
	}

	public Cell getGoalCell() {
		return this.goalCell;
	}

	public void setShortestPath(LinkedList<Cell> newShortestPath) {
		LinkedList<Cell> oldVal = this.shortestPath;
		this.shortestPath = newShortestPath;
		pcs.firePropertyChange(SHORTEST_PATH, oldVal, this.shortestPath);
	}

	public LinkedList<Cell> getShortestPath() {
		return this.shortestPath;
	}

	public void replaceCells(Cell[][] newCells) {
		Cell[][] oldVal = this.cells;
		this.cells = newCells;
		pcs.firePropertyChange(CELLS, oldVal, this.cells);
	}

	public Cell[][] getCells() {
		return this.cells;
	}

	public long getHeuristic(Cell c) {
		long xOffset = Math.abs(c.col - goalCell.col);
		long yOffset = Math.abs(c.row - goalCell.row);

		return Math.max(xOffset, yOffset);
	}

	public int getMapWidth() {
		return this.cells[0].length;
	}

	public int getMapHeight() {
		return this.cells.length;
	}

	public void addCellsChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(CELLS, pcl);
	}

	public void addStartChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(START_CELL, pcl);
	}

	public void addGoalChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(GOAL_CELL, pcl);
	}

	public void addPathChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(SHORTEST_PATH, pcl);
	}

}
