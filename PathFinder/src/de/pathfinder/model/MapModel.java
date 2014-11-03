package de.pathfinder.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class MapModel implements PropertyChangeListener {

	public static final String MAP = "map";

	Map currentMap;

	// This class is observable
	PropertyChangeSupport pcs;

	public MapModel() {
		this.pcs = new PropertyChangeSupport(this);

		// this.currentMap.addCellsChangeListener(this);
		// this.currentMap.addStartChangeListener(this);
		// this.currentMap.addGoalChangeListener(this);
		// this.currentMap.addPathChangeListener(this);
	}

	public void setMap(Map newMap) {
		Map oldVal = this.currentMap;
		this.currentMap = newMap;
		updateListeners();
		pcs.firePropertyChange(MAP, oldVal, this.currentMap);
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
