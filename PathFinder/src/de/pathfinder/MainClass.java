package de.pathfinder;

import de.pathfinder.controller.PathFinderController;
import de.pathfinder.model.MapGenerator;
import de.pathfinder.model.MapModel;
import de.pathfinder.view.PathFinderView;

public class MainClass {

	public static void main(String[] args) {
		MapModel mapModel = new MapModel();
		PathFinderView finderView = new PathFinderView(mapModel);
		PathFinderController controller = new PathFinderController(mapModel, finderView);

		finderView.show();
		// mapModel.setMap(new MapGenerator().getNewMap(20, 20, 0, 0, 18, 17, true));

	}

}
