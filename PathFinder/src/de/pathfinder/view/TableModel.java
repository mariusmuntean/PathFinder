package de.pathfinder.view;

import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableDefaultModel;
import de.pathfinder.model.Cell;
import de.pathfinder.model.Map;

public class TableModel extends KTableDefaultModel {

	Map currentMap;

	public TableModel(Map newMap) {
		this.currentMap = newMap;
	}

	public Cell getStartCell() {
		return this.currentMap.getStartCell();
	}

	public Cell getGoalCell() {
		return this.currentMap.getGoalCell();
	}

	public boolean shortestPathContains(Cell c) {
		if (this.currentMap.getShortestPath() == null) {
			return false;
		}
		return this.currentMap.getShortestPath().contains(c);
	}

	@Override
	public int getFixedHeaderRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFixedSelectableRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFixedHeaderColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFixedSelectableColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isColumnResizable(int col) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRowResizable(int row) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getRowHeightMinimum() {
		// TODO Auto-generated method stub
		return 50;
	}

	@Override
	public int getInitialColumnWidth(int column) {
		// TODO Auto-generated method stub
		return 40;
	}

	@Override
	public int getInitialRowHeight(int row) {
		// TODO Auto-generated method stub
		return 50;
	}

	@Override
	public Object doGetContentAt(int col, int row) {
		// TODO Auto-generated method stub
		return this.currentMap.getCells()[row][col];
	}

	@Override
	public KTableCellEditor doGetCellEditor(int col, int row) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doSetContentAt(int col, int row, Object value) {
		// TODO Auto-generated method stub

	}

	TableCellRenderer tcr = new TableCellRenderer();

	@Override
	public KTableCellRenderer doGetCellRenderer(int col, int row) {
		// TODO Auto-generated method stub
		return tcr;
	}

	@Override
	public int doGetRowCount() {
		// TODO Auto-generated method stub
		return this.currentMap.getMapHeight();
	}

	@Override
	public int doGetColumnCount() {
		// TODO Auto-generated method stub
		return this.currentMap.getMapWidth();
	}

}
