package de.pathfinder.model;

import java.util.Random;

public class MapGenerator {

	public MapGenerator() {
	}

	public Map getRandomMap() {
		Random rand = new Random();
		int width = 2 + (int) (rand.nextFloat() * 200);
		int height = 2 + (int) (rand.nextFloat() * 200);

		int startRow = (int) (rand.nextFloat() * height - 1);
		int startCol = (int) (rand.nextFloat() * width) - 1;

		int goalRow = (int) (rand.nextFloat() * height - 1);
		int goalCol = (int) (rand.nextFloat() * width - 1);

		return generateMap(width, height, startRow, startCol, goalRow, goalCol, true);
	}

	public Map getNewMap(int width, int height, int startCellRow, int startCellCol,
			int goalCellRow, int goalCellCol, boolean randomObstacles) {

		return generateMap(width, height, startCellRow, startCellCol, goalCellRow, goalCellCol,
				randomObstacles);
	}

	private Map generateMap(int width, int height, int startCellRow, int startCellCol,
			int goalCellRow, int goalCellCol, boolean randomObstacles) {
		Random rand = null;
		if (randomObstacles) {
			rand = new Random();
		}

		Cell[][] cells = new Cell[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Cell c = new Cell();
				c.row = i;
				c.col = j;

				long xOffset = Math.abs(c.col - goalCellRow);
				long yOffset = Math.abs(c.row - goalCellCol);
				c.h = Math.max(xOffset, yOffset);

				if (randomObstacles && (i != startCellRow || j != startCellCol)
						&& (i != goalCellRow || j != goalCellCol)) {
					c.isObstacle = rand.nextFloat() < 0.65f ? false : true;
				} else {
					c.isObstacle = false;
				}
				cells[i][j] = c;
			}
		}

		// Set neighbors and costs for inner cells
		for (int i = 1; i <= height - 2; i++) {
			for (int j = 1; j <= width - 2; j++) {
				Cell c = cells[i][j];
				c.costs.N = c.costs.NE = c.costs.E = c.costs.SE = c.costs.S = c.costs.SW = c.costs.W = c.costs.NW = 1;
				c.neighbors.N = cells[i - 1][j];
				c.neighbors.NE = cells[i - 1][j + 1];
				c.neighbors.E = cells[i][j + 1];
				c.neighbors.SE = cells[i + 1][j + 1];
				c.neighbors.S = cells[i + 1][j];
				c.neighbors.SW = cells[i + 1][j - 1];
				c.neighbors.W = cells[i][j - 1];
				c.neighbors.NW = cells[i - 1][j - 1];
			}
		}
		// Set neighbors for corner cells
		Cell c = cells[0][0];
		c.costs.N = c.costs.NE = c.costs.SW = c.costs.W = c.costs.NW = Integer.MAX_VALUE;
		c.costs.E = c.costs.SE = c.costs.S = 1;
		c.neighbors.N = null;
		c.neighbors.NE = null;
		c.neighbors.E = cells[0][1];
		c.neighbors.SE = cells[1][1];
		c.neighbors.S = cells[1][0];
		c.neighbors.SW = null;
		c.neighbors.W = null;
		c.neighbors.NW = null;

		c = cells[0][width - 1];
		c.costs.N = c.costs.NE = c.costs.NW = c.costs.E = c.costs.SE = Integer.MAX_VALUE;
		c.costs.SW = c.costs.W = c.costs.S = 1;
		c.neighbors.N = null;
		c.neighbors.NE = null;
		c.neighbors.E = null;
		c.neighbors.SE = null;
		c.neighbors.S = cells[1][width - 1];
		c.neighbors.SW = cells[1][width - 2];
		c.neighbors.W = cells[0][width - 2];
		c.neighbors.NW = null;

		c = cells[height - 1][width - 1];
		c.costs.N = c.costs.NW = c.costs.W = 1;
		c.costs.NE = c.costs.E = c.costs.SE = c.costs.SW = c.costs.S = Integer.MAX_VALUE;
		c.neighbors.N = cells[height - 2][width - 1];
		c.neighbors.NE = null;
		c.neighbors.E = null;
		c.neighbors.SE = null;
		c.neighbors.S = null;
		c.neighbors.SW = null;
		c.neighbors.W = cells[height - 1][width - 2];
		c.neighbors.NW = cells[height - 2][width - 2];

		c = cells[height - 1][0];
		c.costs.N = c.costs.NE = c.costs.E = 1;
		c.costs.NW = c.costs.W = c.costs.SE = c.costs.SW = c.costs.S = Integer.MAX_VALUE;
		c.neighbors.N = cells[height - 2][0];
		c.neighbors.NE = cells[height - 2][1];
		c.neighbors.E = cells[height - 1][1];
		c.neighbors.SE = null;
		c.neighbors.S = null;
		c.neighbors.SW = null;
		c.neighbors.W = null;
		c.neighbors.NW = null;

		// Set neighbors for boundary cells, except corners
		Cell c2;
		for (int j = 1; j < width - 2; j++) {
			c = cells[0][j];
			c.costs.NW = c.costs.N = c.costs.NE = Integer.MAX_VALUE;
			c.costs.W = c.costs.SW = c.costs.S = c.costs.SE = c.costs.E = 1;
			c.neighbors.NE = c.neighbors.N = c.neighbors.NW = null;
			c.neighbors.E = cells[0][j + 1];
			c.neighbors.SE = cells[1][j + 1];
			c.neighbors.S = cells[1][j];
			c.neighbors.SW = cells[1][j - 1];
			c.neighbors.W = cells[0][j - 1];

			c2 = cells[height - 1][j];
			c2.costs.W = c2.costs.NW = c2.costs.N = c2.costs.NE = c2.costs.E = 1;
			c2.costs.SE = c2.costs.S = c2.costs.SW = Integer.MAX_VALUE;
			c2.neighbors.SW = c2.neighbors.S = c2.neighbors.SE = null;
			c2.neighbors.W = cells[height - 1][j - 1];
			c2.neighbors.NW = cells[height - 2][j - 1];
			c2.neighbors.N = cells[height - 2][j];
			c2.neighbors.NE = cells[height - 2][j + 1];
			c2.neighbors.E = cells[height - 1][j + 1];
		}

		for (int i = 1; i < height - 2; i++) {
			c = cells[i][0];
			c.costs.NW = c.costs.W = c.costs.SW = Integer.MAX_VALUE;
			c.costs.N = c.costs.NE = c.costs.E = c.costs.SE = c.costs.S = 1;
			c.neighbors.NW = c.neighbors.W = c.neighbors.SW = null;
			c.neighbors.N = cells[i - 1][0];
			c.neighbors.NE = cells[i - 1][1];
			c.neighbors.E = cells[i][1];
			c.neighbors.SE = cells[i + 1][1];
			c.neighbors.S = cells[i + 1][0];

			c2 = cells[i][width - 1];
			c2.costs.NE = c2.costs.E = c2.costs.SE = Integer.MAX_VALUE;
			c2.costs.N = c2.costs.NW = c2.costs.W = c2.costs.SW = c2.costs.S = 1;
			c2.neighbors.NE = c2.neighbors.E = c2.neighbors.SE = null;
			c2.neighbors.N = cells[i - 1][width - 1];
			c2.neighbors.NW = cells[i - 1][width - 2];
			c2.neighbors.W = cells[i][width - 2];
			c2.neighbors.SW = cells[i + 1][width - 2];
			c2.neighbors.S = cells[i + 1][width - 1];
		}

		Map map = new Map(cells, startCellRow, startCellCol, goalCellRow, goalCellCol);
		return map;
	}

}
