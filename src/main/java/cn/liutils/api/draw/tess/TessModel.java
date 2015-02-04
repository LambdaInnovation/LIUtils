/**
 * 
 */
package cn.liutils.api.draw.tess;

import java.util.EnumSet;

import net.minecraftforge.client.model.IModelCustom;
import cn.liutils.api.draw.DrawHandler;
import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.DrawObject.EventType;

/**
 * @author WeathFolD
 *
 */
public class TessModel extends DrawHandler {

	IModelCustom model;

	public TessModel(IModelCustom _model) {
		model = _model;
	}

	@Override
	public EnumSet<EventType> getEvents() {
		return EnumSet.of(EventType.DO_TESS);
	}

	@Override
	public String getID() {
		return "model";
	}

	@Override
	public void onEvent(EventType event, DrawObject obj) {
		model.renderAll();
	}

}
