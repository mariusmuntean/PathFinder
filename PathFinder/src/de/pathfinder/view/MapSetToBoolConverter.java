package de.pathfinder.view;

import org.eclipse.core.databinding.conversion.IConverter;

import de.pathfinder.model.MapModel;

public class MapSetToBoolConverter implements IConverter {

	@Override
	public Object getFromType() {
		// TODO Auto-generated method stub
		return MapModel.class;
	}

	@Override
	public Object getToType() {
		// TODO Auto-generated method stub
		return Boolean.class;
	}

	@Override
	public Object convert(Object fromObject) {

		return fromObject != null;
	}

}
