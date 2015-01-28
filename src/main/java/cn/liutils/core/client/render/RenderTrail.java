package cn.liutils.core.client.render;

import java.util.LinkedList;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.prop.AssignColor;
import cn.liutils.api.draw.prop.DisableLight;
import cn.liutils.api.draw.tess.CrossedSquare;
import cn.liutils.core.entity.SamplePoint;
import cn.liutils.template.entity.EntityTrailFX;
import cn.liutils.util.GenericUtils;
import cn.liutils.util.RenderUtils;

public class RenderTrail extends Render {
	
	private static Tessellator t = Tessellator.instance;
	private DrawObject drawer = new DrawObject();
	private AssignColor color;
	private DisableLight lightSwitch;
	private CrossedSquare square;

	public RenderTrail() {
		color = new AssignColor();
		lightSwitch = new DisableLight();
		square = new CrossedSquare();
		drawer.addHandlers(color, square, lightSwitch);
	}

	@Override
	public void doRender(Entity trail, double x, double y, double z, float wtf, float isthis) {
		EntityTrailFX ent = (EntityTrailFX) trail;
		LinkedList<SamplePoint> list = ent.getSamplePoints();
		
		GL11.glPushMatrix(); {
			square.height = ent.getTrailWidth();
			square.ty = -square.height / 2;
			lightSwitch.enabled = !ent.getHasLight();
			
			GL11.glTranslated(x, y, z);
			for (int i = 0; i != list.size() - 1; ++i) {
				SamplePoint sp1 = list.get(i), sp2 = list.get(i + 1);
				if (i == 0 && ent.doesRenderEnd()) {
					bindTexture(ent.getTexEnd());
				} else {
					bindTexture(ent.getTexNormal());
				}
				
				float dt = ent.ticksExisted - sp1.tick;
				float alpha = 1.0F;
				if (dt > ent.getDecayTime()) { //calculate blendness
					alpha = 1.0F - (dt - ent.getDecayTime()) / ent.getDecayTime();
				}
				
				//Set up the piece
				double dist = GenericUtils.distance(sp1.x, sp1.y, sp1.z, sp2.x, sp2.y, sp2.z);
				square.width = dist;
				double yaw = Math.atan2(sp2.x - sp1.x, sp2.z - sp1.z) * 180 / Math.PI;
				double pitch = Math.atan2(sp2.y - sp1.y, GenericUtils.planeDistance(sp2.x - sp1.x, sp2.z - sp1.z)) * 180 / Math.PI;
				
				GL11.glTranslated(sp1.x, sp1.y, sp1.z);
				GL11.glRotated(yaw, 0, 1, 0);
				GL11.glRotated(pitch, 1, 0, 0);
				
				color.set(1, 1, 1, alpha);
	
				drawer.draw();
			}
		} GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}

}
