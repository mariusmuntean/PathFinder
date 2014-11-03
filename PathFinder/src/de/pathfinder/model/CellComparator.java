package de.pathfinder.model;

import java.util.Comparator;

public class CellComparator implements Comparator<Cell> {

	@Override
	public int compare(Cell arg0, Cell arg1) {

		if (arg0 == null || arg1 == null) {
			System.out.println("Arguments cannot be null");
		}

		long dif = arg0.key.k1 - arg1.key.k1;
		if (dif != 0) {
			return (int) dif;
		} else {
			return (int) (arg0.key.k2 - arg1.key.k2);
		}

		// if (arg0.k1 < arg1.k1) {
		// return -1;
		// } else {
		//
		// if (arg0.k1 == arg1.k1) {
		//
		// if (arg0.k2 < arg1.k2) {
		// return -1;
		// } else {
		// if (arg0.k2 == arg1.k2) {
		// return 0;
		// } else {
		// return 1;
		// }
		// }
		//
		// } else {
		// return 1;
		// }
		// }
	}

}
