package de.pathfinder.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;

import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableModel;
import de.pathfinder.model.Cell;

public class TableCellRenderer implements KTableCellRenderer {

	@Override
	public int getOptimalWidth(GC gc, int col, int row, Object content, boolean fixed,
			KTableModel model) {
		// TODO Auto-generated method stub
		return 40;
	}

	@Override
	public void drawCell(GC gc, Rectangle rect, int col, int row, Object content, boolean focus,
			boolean header, boolean clicked, KTableModel model) {
		Color color = getColor(model, content);
		gc.setBackground(color);
		gc.fillRectangle(rect);

		Cell currentCell = (Cell) content;
		String g = currentCell.g == Integer.MAX_VALUE ? "" : Long.toString(currentCell.g);
		String rhs = currentCell.rhs == Integer.MAX_VALUE ? "" : Long.toString(currentCell.rhs);
		String h = Long.toString(currentCell.h);



		gc.drawText(String.format("G:%s\nRHS:%s\nH:%s", g, rhs, h), rect.x, rect.y);

		// Transform tr = new Transform(gc.getDevice());
		// // gc.getTransform(tr);
		// // tr.scale(0.6f, 0.6f);
		// gc.setTransform(tr);
		// tr.dispose();

		color.dispose();

	}

	private Color getColor(KTableModel model, Object content) {
		TableModel tm = (TableModel) model;
		Cell cell = (Cell) content;
		if (tm.getStartCell() == cell) {
			return new Color(Display.getCurrent(), new RGB(121, 2, 2));
		}
		if (tm.getGoalCell() == cell) {
			return new Color(Display.getCurrent(), new RGB(2, 121, 2));
		}

		if (tm.shortestPathContains(cell)) {
			return new Color(Display.getCurrent(), new RGB(2, 2, 221));
		}

		if (cell.g != Integer.MAX_VALUE) {
			return new Color(Display.getCurrent(), new RGB(2, 2, 97));
		}

		if (cell.isObstacle) {
			return new Color(Display.getCurrent(), new RGB(121, 2, 121));
		}

		return new Color(Display.getCurrent(), new RGB(121, 141, 141));
	}

}
