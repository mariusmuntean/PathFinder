package de.pathfinder.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

public class MapModel implements PropertyChangeListener {

	public static final String MAP = "map";

	Map currentMap;
	MapGenerator mapGen;

	// This class is observable
	PropertyChangeSupport pcs;

	public MapModel() {
		this.pcs = new PropertyChangeSupport(this);
		mapGen = new MapGenerator();

		// this.currentMap.addCellsChangeListener(this);
		// this.currentMap.addStartChangeListener(this);
		// this.currentMap.addGoalChangeListener(this);
		// this.currentMap.addPathChangeListener(this);
	}

	private void setMap(Map newMap) {
		Map oldVal = this.currentMap;
		this.currentMap = newMap;
		updateListeners();
		pcs.firePropertyChange(MAP, oldVal, this.currentMap);
	}

	public void randomizeMap() {
		Random rand = new Random();
		int width = (int) (2 + rand.nextFloat() * 100);
		int height = (int) (2 + rand.nextFloat() * 100);
		int startCellRow = (int) (height * rand.nextFloat() - 1);
		int startCellCol = (int) (width * rand.nextFloat() - 1);
		int goalCellRow = (int) (height * rand.nextFloat() - 1);
		int goalCellCol = (int) (width * rand.nextFloat() - 1);
		boolean randObstacles = rand.nextFloat() > 0.5;

		setMap(mapGen.getNewMap(width, height, startCellRow, startCellCol, goalCellRow,
				goalCellCol, randObstacles));
	}

	public void replaceMap(int width, int height, int startCellRow, int startCellCol,
			int goalCellRow, int goalCellCol, boolean randomObstacles) {

		setMap(mapGen.getNewMap(width, height, startCellRow, startCellCol, goalCellRow,
				goalCellCol, randomObstacles));
	}

	private void updateListeners() {
		this.currentMap.addCellsChangeListener(this);
		this.currentMap.addStartChangeListener(this);
		this.currentMap.addGoalChangeListener(this);
		this.currentMap.addPathChangeListener(this);

	}

	public Map getMap() {
		return this.currentMap;
	}

	public void findShortestPath() {
		LpaStar lpaStar = new LpaStar(currentMap);
		this.currentMap.setShortestPath(lpaStar.getShortestPath());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		pcs.firePropertyChange(evt);
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

}
