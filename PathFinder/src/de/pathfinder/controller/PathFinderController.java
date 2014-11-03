package de.pathfinder.controller;

import de.pathfinder.model.MapModel;
import de.pathfinder.view.PathFinderView;

public class PathFinderController {

	private MapModel mapModel;
	private PathFinderView mapView;

	public PathFinderController(MapModel mapModel, PathFinderView mapView) {
		this.mapModel = mapModel;
		this.mapView = mapView;
	}

}
