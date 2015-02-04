/**
 * 
 */
package cn.weaponry.api.client.render;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.draw.DrawObject;
import cn.liutils.util.RenderUtils;
import cn.weaponry.api.info.InfManager;
import cn.weaponry.api.info.InfWeapon;

/**
 * @author WeathFolD
 */
public class WeaponRenderSimple extends WeaponRenderBase {
	
	public DrawObject drawer;

	public WeaponRenderSimple() {}
	
	public WeaponRenderSimple setDrawer(DrawObject _drawer) {
		drawer = _drawer;
		return this;
	}

	@Override
	public void renderEquipped(EntityLivingBase holder, ItemStack stack) {
		GL11.glPushMatrix();
		if(holder instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) holder;
			InfWeapon inf = InfManager.getInfo(player);
			if(stack == inf.getCurStack())
				inf.postRenderEvent("_all");
		}
		drawAtOrigin(stack);
		GL11.glPopMatrix();
	}

	@Override
	public void renderFirstPerson(EntityPlayer holder, ItemStack stack) {
		renderEquipped(holder, stack);
	}

	@Override
	public void renderEntityItem(ItemStack stack) {
		drawAtOrigin(stack);
	}
	
	private void drawAtOrigin(ItemStack stack) {
		post(EventType.PRE_DRAW);
		post(EventType.PRE_TRANSFORM);
		post(EventType.PRE_TESS);
		if(isModel()) {
			drawer.draw();
		} else {
			RenderUtils.renderItemIn2d(stack, 0.0625);
		}
		post(EventType.POST_TESS);
		post(EventType.POST_DRAW);
	}
	
	private boolean isModel() {
		return drawer != null;
	}

}
