/**
 * 
 */
package cn.weaponry.api.client.render;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.draw.DrawObject.EventType;
import cn.liutils.api.render.model.Model;
import cn.liutils.util.RenderUtils;
import cn.weaponry.api.info.InfManager;
import cn.weaponry.api.info.InfWeapon;

/**
 * @author WeathFolD
 *
 */
public class WeaponRenderParted extends WeaponRenderBase {
	
	Model model;
	ResourceLocation tex;

	public WeaponRenderParted(Model _model, ResourceLocation _tex) {
		model = _model;
		tex = _tex;
	}

	@Override
	public void renderEquipped(EntityLivingBase holder, ItemStack stack) {
		if(holder instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) holder;
			InfWeapon inf = InfManager.getInfo(player);
			if(inf.getCurStack() == stack) {
				renderFull(inf);
			} else {
				renderSimple(stack);
			}
		} else {
			renderSimple(stack);
		}
	}

	@Override
	public void renderFirstPerson(EntityPlayer holder, ItemStack stack) {
		renderEquipped(holder, stack);
	}
	
	private void renderFull(InfWeapon inf) {
		post(EventType.PRE_DRAW);
		post(EventType.PRE_TRANSFORM);
		post(EventType.PRE_TESS);
		
		inf.postRenderEvent("_all");
		for(String part : model.getParts()) {
			GL11.glPushMatrix();
			
			inf.postRenderEvent(part);
			model.draw(part);
			GL11.glPopMatrix();
			
			
		}
		GL11.glPushMatrix();
		inf.postRenderEvent("_misc");
		GL11.glPopMatrix();
		
		post(EventType.POST_TESS);
		post(EventType.POST_DRAW);
	}
	
	private void renderSimple(ItemStack stack) {
		post(EventType.PRE_DRAW);
		post(EventType.PRE_TRANSFORM);
		post(EventType.PRE_TESS);
		
		GL11.glPushMatrix();
		
		RenderUtils.loadTexture(tex);
		model.drawAll();
		
		GL11.glPopMatrix();
		
		post(EventType.POST_TESS);
		post(EventType.POST_DRAW);
	}
	
	@Override
	public void renderEntityItem(ItemStack stack) {
		renderSimple(stack);
	}

}
