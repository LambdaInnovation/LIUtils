/**
 * 
 */
package cn.liutils.api.debug;

import net.minecraft.util.Vec3;
import net.minecraftforge.client.IItemRenderer;
import cn.liutils.api.client.render.RenderModelItem;
import cn.liutils.api.debug.KeyMoving.EnumKey;

/**
 * @author WeAthFolD
 * 
 */
public class Debug_ProcessorModel<T extends RenderModelItem> implements
		Debug_MovingProcessor<T> {

	// 0: stdOffset XYZ
	// 1: stdRotation XYZ
	// 2: scale
	// 3: equipOffset XYZ
	// 4: invOffset XY && invScale
	@Override
	public boolean isProcessAvailable(IItemRenderer render, int mode) {
		return render instanceof RenderModelItem;
	}

	@Override
	public String doProcess(T render, EnumKey key, int mode,
			int ticks) {
		float amtToAdd = ticks * ticks * 0.002F;
		String s = "";
		switch (mode) {
		case 0:
			s = onSetOffset(render, key, amtToAdd);
			break;
		case 1:
			s = onSetRotation(render, key, amtToAdd);
			break;
		case 2:
			s = onSetScale(render, key, amtToAdd);
			break;
		case 3:
			s = onSetEquipOffset(render, key, amtToAdd);
			break;
		case 4:
			s = onSetInvScale(render, key, amtToAdd);
			break;
		case 5:
			s = "[INV ROTATION : " + stdSet(render.invRotation, key, amtToAdd) + "]";
			break;
		case 6:
			s = "[EQUIP ROTATION : " + stdSet(render.equipRotation, key, amtToAdd) + "]";
			break;
		}
		if(s != "")
			System.out.println(s);
		return s;
	}
	
	private String stdSet(Vec3 vec, EnumKey key, float factor) {
		switch (key) {
		case UP:
			vec.yCoord += factor;
			break;
		case DOWN:
			vec.yCoord -= factor;
			break;
		case LEFT:
			vec.xCoord -= factor;
			break;
		case RIGHT:
			vec.xCoord += factor;
			break;
		case FORWARD:
			vec.zCoord += factor;
			break;
		case BACK:
			vec.zCoord -= factor;
			break;
		}
		return vec.toString();
	}

	private String onSetOffset(RenderModelItem render, EnumKey key, float factor) {
		return "[OFFSET : " + stdSet(render.stdOffset, key, factor) + "]";
	}

	private String onSetEquipOffset(RenderModelItem render, EnumKey key,
			float factor) {
		return "[EQUIP OFFSET : " + stdSet(render.equipOffset, key, factor) + "]";
	}

	private String onSetRotation(RenderModelItem render, EnumKey key,
			float factor) {
		return "[ROTATION: " + stdSet(render.stdRotation, key, factor) + ") ]";
	}

	private String onSetScale(RenderModelItem render, EnumKey key, float factor) {
		switch (key) {
		case UP:
		case FORWARD:
			render.scale += factor;
			break;
		case DOWN:
		case BACK:
			render.scale -= factor;
			break;
		default:
			break;
		}
		return "[SCALE: " + render.scale + "]";
	}

	private String onSetInvScale(RenderModelItem render, EnumKey key,
			float factor) {
		switch (key) {
		case UP:
			render.invOffset.y += factor;
			break;
		case DOWN:
			render.invOffset.y -= factor;
			break;
		case LEFT:
			render.invOffset.x -= factor;
			break;
		case RIGHT:
			render.invOffset.x += factor;
			break;
		case FORWARD:
			render.invScale += factor;
			break;
		case BACK:
			render.invScale -= factor;
			break;
		}
		return "[INV OFFSET : " +  render.invOffset +", SCALE : " + render.invScale + "]";
	}

	@Override
	public String getDescription(int mode) {
		switch(mode) {
		case 0:
			return "Std Offset XYZ";
		case 1:
			return "Std Rotation XYZ";
		case 2:
			return "Std Scale";
		case 3:
			return "Equipped Offset XYZ";
		case 4:
			return "Inventory Offset XY & invScale";
		case 5:
			return "Inventory Rotation";
		case 6:
			return "Equip Rotation";
		default:
			return null;
		}
	}
}
