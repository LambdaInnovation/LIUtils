/**
 * 
 */
package cn.liutils.api.render.piece.property;

import java.util.EnumSet;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.render.piece.Piece;
import cn.liutils.util.RenderUtils;

/**
 * Color and blend properties
 * @author WeathFolD
 */
public class AssignColor extends PieceProperty {
	
	public final int color[] = new int[4];
	
	public boolean enableBlend = true;
	public int sFactor = GL11.GL_SRC_ALPHA, dFactor = GL11.GL_ONE_MINUS_SRC_ALPHA;

	public AssignColor(Piece piece) {
		this(piece, 255, 255, 255, 255);
	}
	
	public AssignColor(Piece piece, int r, int g, int b, int a) {
		super(piece);
		setColor4ub(r, g, b, a);
	}
	
	public void setColor4ub(int r, int g, int b, int a) {
		color[0] = r;
		color[1] = g;
		color[2] = b;
		color[3] = a;
	}
	
	public void setColor4f(float r, float g, float b, float a) {
		setColor4ub((int)(r * 255), (int)(g * 255), (int)(b * 255), (int)(a * 255));
	}
	
	public void setColor4d(double r, double g, double b, double a) {
		setColor4ub((int)(r * 255), (int)(g * 255), (int)(b * 255), (int)(a * 255));
	}
	
	public void setAlphaub(int a) {
		color[3] = a;
	}
	
	public void setAlphaf(float a) {
		setAlphaub((int)(a * 255));
	}
	
	public void setAlphad(double a) {
		setAlphaub((int)(a * 255));
	}

	@Override
	public EnumSet<EventType> getHandledEvents() {
		return EnumSet.of(EventType.PRE_DRAW, EventType.PRE_TESSELLATION);
	}

	@Override
	public void onEvent(EventType type) {
		if(type == EventType.PRE_DRAW) {
			if(enableBlend) {
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(sFactor, dFactor);
			}
		} else { //PRE_TESSELLATION
			RenderUtils.bindColor(color);
		}
	}

	@Override
	public String getID() {
		return "assign_color";
	}

}
