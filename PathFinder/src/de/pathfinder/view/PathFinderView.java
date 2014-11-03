package de.pathfinder.view;

import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import de.kupzog.examples.PaletteExampleModel;
import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableDefaultModel;
import de.kupzog.ktable.SWTX;
import de.pathfinder.model.Map;
import de.pathfinder.model.MapGenerator;
import de.pathfinder.model.MapModel;

public class PathFinderView implements PropertyChangeListener {

	private MapModel mapModel;

	private Shell viewingFrame;

	KTable tblMap;

	private Composite c1;

	public PathFinderView(MapModel model) {
		this.mapModel = model;
		this.mapModel.addPropertyChangeListener(this);

		initUi();
	}

	private void initUi() {
		this.viewingFrame = new Shell(new Display());
		this.viewingFrame.setLayout(new GridLayout(2, false));
		GridDataFactory.fillDefaults().grab(true, true).applyTo(viewingFrame);

		c1 = new Composite(viewingFrame, SWT.NONE);
		GridLayoutFactory.swtDefaults().applyTo(c1);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(c1);

		tblMap = new KTable(c1, SWT.FLAT | SWTX.AUTO_SCROLL);
		tblMap.setVisible(false);

		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(tblMap);
		// tblMap.setVisible(true);

		Composite c2 = new Composite(viewingFrame, SWT.NONE);
		GridLayoutFactory.swtDefaults().applyTo(c2);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(false, true).applyTo(c2);

		Button btnTest = new Button(c2, SWT.NONE);
		btnTest.setText("New Map");
		btnTest.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				mapModel.setMap(new MapGenerator().getNewMap(25, 25, 0, 0, 18, 17, true));
				// mapModel.setMap(new MapGenerator().getRandomMap());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		Button btnSolve = new Button(c2, SWT.NONE);
		btnSolve.setText("Solve");
		btnSolve.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				mapModel.findShortestPath();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void show() {
		this.viewingFrame.open();
		this.viewingFrame.setVisible(true);

		while (!viewingFrame.isDisposed()) {
			if (!viewingFrame.getDisplay().readAndDispatch()) {
				viewingFrame.getDisplay().sleep();
			}
		}
		// viewingFrame.getDisplay().dispose();
	}

	public Display getDisplay() {
		return this.viewingFrame.getDisplay();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(MapModel.MAP)) {
			// this.tblMap.setModel(new TableModel((Map) evt.getNewValue()));
			this.tblMap.setModel(new TableModel((Map) evt.getNewValue()));
			this.tblMap.setVisible(true);
			// c1.getParent().layout();
			// c1.layout(true);
			// c1.redraw();
			return;
		}

		if (evt.getPropertyName().equals(Map.SHORTEST_PATH)) {
			this.tblMap.redraw();
		}

	}

}
