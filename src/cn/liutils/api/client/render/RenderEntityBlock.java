/**
 * Code by Lambda Innovation, 2013.
 */
package cn.liutils.api.client.render;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.entity.EntityBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * @author WeAthFolD
 *
 */
public class RenderEntityBlock extends Render {

	RenderBlocks render = new RenderBlocks();
	
	/**
	 * 
	 */
	public RenderEntityBlock() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see net.minecraft.client.renderer.entity.Render#doRender(net.minecraft.entity.Entity, double, double, double, float, float)
	 */
	@Override
	public void doRender(Entity entity, double d0, double d1, double d2,
			float f, float f1) {
		EntityBlock ent = (EntityBlock) entity;
		Block block = Block.blocksList[ent.blockID];
		
		if(block != null) {
			GL11.glPushMatrix();
			
			GL11.glTranslated(d0, d1, d2);
			//GL11.glRotatef(f, 0.0F, 1.0F, 0.0F);
			//GL11.glRotatef(f1, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(0.5F - ent.blockX, 0.5F - ent.blockY, 0.5F - ent.blockZ); //to the center
			render.blockAccess = ent.worldObj;
			render.renderBlockByRenderType(block, ent.blockX, ent.blockY, ent.blockZ);
			
			GL11.glPopMatrix();
		}
		
	}

	/* (non-Javadoc)
	 * @see net.minecraft.client.renderer.entity.Render#getEntityTexture(net.minecraft.entity.Entity)
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}

}
