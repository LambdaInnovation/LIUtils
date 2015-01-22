package cn.liutils.core.client.render;

import java.util.LinkedList;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.render.piece.Piece;
import cn.liutils.api.render.piece.PieceCrossed;
import cn.liutils.api.render.piece.property.AssignColor;
import cn.liutils.api.render.piece.property.DisableLight;
import cn.liutils.api.render.piece.property.Transform;
import cn.liutils.core.entity.SamplePoint;
import cn.liutils.template.entity.EntityTrailFX;
import cn.liutils.util.RenderUtils;

public class RenderTrail extends Render {
	
	private static Tessellator t = Tessellator.instance;
	private Piece piece = new PieceCrossed();
	private AssignColor color;
	private Transform transform;
	private DisableLight lightSwitch;

	public RenderTrail() {
		color = new AssignColor(piece);
		transform = new Transform(piece);
		lightSwitch = new DisableLight(piece);
	}

	@Override
	public void doRender(Entity trail, double x, double y, double z, float wtf, float isthis) {
		EntityTrailFX ent = (EntityTrailFX) trail;
		LinkedList<SamplePoint> list = ent.getSamplePoints();
		
		GL11.glPushMatrix(); {
			piece.hHeight = ent.getTrailWidth() / 2;
			lightSwitch.setEnabled(!ent.getHasLight());
			
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
				transform.setOrient(sp1.x, sp1.y, sp1.z, sp2.x, sp2.y, sp2.z);
				color.setColor4f(1, 1, 1, alpha);
	
				piece.draw();
			}
		} GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}

}
