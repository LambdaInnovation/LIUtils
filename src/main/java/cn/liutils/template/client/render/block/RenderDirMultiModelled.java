/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.template.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import cn.liutils.api.render.model.ITileEntityModel;
import cn.liutils.template.block.BlockDirectionalMulti;
import cn.liutils.util.RenderUtils;

/**
 * @author WeAthFolD
 *
 */
public class RenderDirMultiModelled extends RenderTileDirMulti {
	
	protected ITileEntityModel theModel;
	protected boolean isTechne = false;

	public RenderDirMultiModelled(ITileEntityModel mdl) {
		theModel = mdl;
	}
	
	public RenderDirMultiModelled setTechne(boolean b) {
		isTechne = b;
		return this;
	}
	
	@Override
	protected void renderAtOrigin(TileEntity te) {
		int meta = ((BlockDirectionalMulti)te.getBlockType())
				.getMetadata(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord);
		ResourceLocation tex = getTexture(te);
		Block blockType = te.getBlockType();
		if(tex != null) RenderUtils.loadTexture(tex);
		GL11.glPushMatrix(); {
			if(isTechne) {
				GL11.glScalef(-0.0625F, -0.0625F, 0.0625F);
			} else {
				GL11.glScalef(scale, scale, scale);
			}
			theModel.render(te, 0F, 0F);
		} GL11.glPopMatrix();
	}

}
