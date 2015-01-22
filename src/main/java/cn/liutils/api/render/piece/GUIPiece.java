/**
 * 
 */
package cn.liutils.api.render.piece;

import net.minecraft.util.ResourceLocation;
import cn.liutils.api.render.piece.property.AssignColor;
import cn.liutils.api.render.piece.property.AssignTexture;
import cn.liutils.api.render.piece.property.DisableTexture;
import cn.liutils.api.render.piece.property.Transform2D;

/**
 * @author WeathFolD
 *
 */
public class GUIPiece extends Piece2D {
	
	private Transform2D transform;
	private AssignColor color = new AssignColor(this);

	public GUIPiece() {}

	public GUIPiece(double x, double y, double w, double h, double u0, double v0, double u1,
			double v1) {
		super(w, h, u0, v0, u1, v1);
		transform = new Transform2D(this, x, y);
	}

	public GUIPiece(double x, double y, double w, double h) {
		super(w, h);
		transform = new Transform2D(this, x , y);
	}
	
	public void setPos(double x, double y) {
		transform.tx = x;
		transform.ty = y;
	}
	 
	public Transform2D getTransform() {
		return transform;
	}
	
	public void setColor4ub(int r, int g, int b, int a) {
		color.setColor4ub(r, g, b, a);
	}
	
	public void setColor4f(float r, float g, float b, float a) {
		color.setColor4f(r, g, b, a);
	}
	
	public void setColor4d(double r, double g, double b, double a) {
		color.setColor4d(r, g, b, a);
	}
	
	public void setAlphaub(int alpha) {
		color.setAlphaub(alpha);
	}
	
	public void setAlphaf(float alpha) {
		color.setAlphaf(alpha);
	}
	
	public void setAlphad(double alpha) {
		color.setAlphad(alpha);
	}
	
	public static class Textured extends GUIPiece {
		AssignTexture assign = new AssignTexture(this);
		
		public Textured(ResourceLocation tex) {
			assign.setTexture(tex);
		}

		public Textured(ResourceLocation tex, 
				double x, double y, double w, double h, double u0, double v0, double u1, double v1) {
			super(x, y, w, h, u0, v0, u1, v1);
			assign.setTexture(tex);
		}

		public Textured(ResourceLocation tex, double x, double y, double w, double h) {
			super(x, y, w, h);
			assign.setTexture(tex);
		}
	}
	
	public static class Colored extends GUIPiece {
		{
			new DisableTexture(this);
		}
		
		public Colored() {
		}

		public Colored(double x, double y, double w, double h, double u0, double v0, double u1, double v1) {
			super(x, y, w, h, u0, v0, u1, v1);
		}

		public Colored(ResourceLocation tex, double x, double y, double w, double h) {
			super(x, y, w, h);
		}
		
	}

}
