/**
 * 
 */
package cn.liutils.api.draw.tess;

import java.util.EnumSet;

import cn.liutils.api.draw.DrawHandler;
import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.DrawObject.EventType;

/**
 * @author WeathFolD
 *
 */
public abstract class TessBase extends DrawHandler {

	private String id;
	
	public TessBase(String id) {
		this.id = id;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	@Override
	public String getID() {
		return id;
	}

}
