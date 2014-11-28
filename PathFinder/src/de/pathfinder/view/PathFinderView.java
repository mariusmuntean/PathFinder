package de.pathfinder.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.SWTX;
import de.pathfinder.model.Map;
import de.pathfinder.model.MapGenerator;
import de.pathfinder.model.MapModel;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.GridData;

public class PathFinderView implements PropertyChangeListener {
	private MapModel mapModel;

	public MapModel getMapModel() {
		return mapModel;
	}

	public void setMapModel(MapModel mapModel) {
		this.mapModel = mapModel;
	}

	private Shell viewingFrame;

	KTable tblMap;

	private Composite c1;
	private MenuItem mntmFile;
	private ToolBar toolBar;

	MenuItem miNewMap;
	MenuItem miNewRandMap;
	ToolItem tltmDropdownItem;
	NewMapDialog newMapDialog;

	public PathFinderView(MapModel model) {
		this.mapModel = model;
		this.mapModel.addPropertyChangeListener(this);

		initUi();
	}

	private void initUi() {
		this.viewingFrame = new Shell(new Display());
		viewingFrame.setMinimumSize(new Point(600, 39));
		this.viewingFrame.setLayout(new GridLayout(2, false));
		GridDataFactory.fillDefaults().grab(true, true).applyTo(viewingFrame);

		toolBar = new ToolBar(viewingFrame, SWT.FLAT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		tltmDropdownItem = new ToolItem(toolBar, SWT.DROP_DOWN);
		tltmDropdownItem.setText("New ...");
		Menu newMapMenu = new Menu(this.viewingFrame);
		SelectionAdapter selAdaptNewMap = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.detail == SWT.ARROW) {
					newMapMenu.setLocation(tltmDropdownItem.getParent().toDisplay(
							tltmDropdownItem.getBounds().x,
							tltmDropdownItem.getBounds().y + tltmDropdownItem.getBounds().height));
					newMapMenu.setVisible(true);
				} else {
					if (tltmDropdownItem.getData() != null) {
						SelectionAdapter selAdapt = (SelectionAdapter) tltmDropdownItem.getData();
						selAdapt.widgetSelected(e);
					}
				}
			}
		};
		tltmDropdownItem.addSelectionListener(selAdaptNewMap);

		miNewMap = new MenuItem(newMapMenu, SWT.NONE);
		miNewMap.setText("New Map");
		SelectionAdapter selAdapt = getNewMapSelectionAdapter(miNewMap, false);
		miNewMap.addSelectionListener(selAdapt);
		
		miNewRandMap = new MenuItem(newMapMenu, SWT.NONE);
		miNewRandMap.setText("New Random Map");
		miNewRandMap.addSelectionListener(getNewMapSelectionAdapter(miNewRandMap, true));

		// Make the "New Map" default
		tltmDropdownItem.setData(selAdapt);
		tltmDropdownItem.setText(miNewMap.getText());

		ToolItem tltmSeprator = new ToolItem(toolBar, SWT.SEPARATOR);
		tltmSeprator.setText("Seprator");

		ToolItem tiPlaceStart = new ToolItem(toolBar, SWT.NONE);
		tiPlaceStart.setText("Place Start");

		ToolItem tiPlaceGoal = new ToolItem(toolBar, SWT.NONE);
		tiPlaceGoal.setText("Place Goal");

		ToolItem tiPlaceObstacle = new ToolItem(toolBar, SWT.NONE);
		tiPlaceObstacle.setText("Place Obstacle");

		ToolItem toolItem = new ToolItem(toolBar, SWT.SEPARATOR);

		ToolItem tltmSolve = new ToolItem(toolBar, SWT.NONE);
		tltmSolve.setText("Solve");
		tltmSolve.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				mapModel.findShortestPath();
			}
		});

		new Label(viewingFrame, SWT.NONE);

		c1 = new Composite(viewingFrame, SWT.NONE);
		GridLayoutFactory.swtDefaults().applyTo(c1);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(c1);

		tblMap = new KTable(c1, SWT.FLAT | SWTX.AUTO_SCROLL);
		tblMap.setVisible(false);

		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(tblMap);
		// tblMap.setVisible(true);

		Menu menu = new Menu(viewingFrame, SWT.BAR);
		viewingFrame.setMenuBar(menu);

		mntmFile = new MenuItem(menu, SWT.NONE);
		mntmFile.setText("File");

		MenuItem mntmMap = new MenuItem(menu, SWT.NONE);
		mntmMap.setText("Map");

		MenuItem mntmHelp = new MenuItem(menu, SWT.NONE);
		mntmHelp.setText("Help");
		new Label(viewingFrame, SWT.NONE);


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

	private SelectionAdapter getNewMapSelectionAdapter(MenuItem mi, Boolean makeRandomMap) {
		SelectionAdapter selAdapt = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (makeRandomMap) {
					mapModel.randomizeMap();
				} else {
					// Display dialog for interactive map creation
					newMapDialog = new NewMapDialog(viewingFrame);
					handleDialogResult(newMapDialog.open()); // Returns 0 for OK and 1 for
																// Cancel/Close

				}
				tltmDropdownItem.setData(this);
				tltmDropdownItem.setText(mi.getText());
			}
		};
		return selAdapt;
	}

	protected void handleDialogResult(int open) {
		if (open != Window.OK) {
			return;
		}



	}

}
