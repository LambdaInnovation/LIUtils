/**
 * 
 */
package cn.liutils.core.client.render;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;

/**
 * @author WeathFolD
 *
 */
public class RenderPlayer2 extends RenderPlayer {

	/**
	 * 
	 */
	public RenderPlayer2() {
		// TODO Auto-generated constructor stub
	}
	
    @Override
	public void doRender(AbstractClientPlayer par1AbstractClientPlayer, double par2, double par4, double par6, float par8, float par9)
    {
    	System.out.println("Using my version of renderPlayer");
    	super.doRender(par1AbstractClientPlayer, par2, par4, par6, par8, par9);
    }

}
