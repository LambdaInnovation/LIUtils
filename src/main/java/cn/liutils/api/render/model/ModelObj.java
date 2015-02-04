package cn.liutils.api.render.model;

import net.minecraftforge.client.model.IModelCustom;

public class ModelObj extends Model {
	
	IModelCustom model;

	public ModelObj(IModelCustom _model) {
		model = _model;
	}

	public ModelObj(IModelCustom _model, Object[][] data) {
		super(data);
		model = _model;
	}

	@Override
	public void draw(String part) {
		model.renderPart(part);
	}

	@Override
	public void drawAll() {
		model.renderAll();
	}

}
