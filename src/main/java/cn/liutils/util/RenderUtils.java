/**
 * 
 */
package cn.liutils.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cn.liutils.util.render.Vertex;

import com.google.common.collect.Maps;

import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

/**
 * Utilities of generic world rendering. Defined quick aliaes for binding and rending, and some generic rendering routine. 
 * (Cube, item, enchant glint, ......)
 * @author WeAthFolD
 */
@SideOnly(Side.CLIENT)
public class RenderUtils {

	public static final ResourceLocation STEVE_TEXTURE = new ResourceLocation("textures/entity/steve.png");
	private static ResourceLocation src_glint = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	
	private static Tessellator t = Tessellator.instance;
	private static int textureState = -1;
	
	//-----------------Quick aliases-----------------------------
	/**
	 * Stores the current texture state. stack depth: 1
	 */
	public static void pushTextureState() {
		if(textureState != -1) {
			System.err.println("RenderUtils:Texture State Overflow");
			return;
		}
		textureState = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
	}
	
	/**
	 * Restores the stored texture state. stack depth: 1
	 */
	public static void popTextureState() {
		if(textureState == -1) {
			System.err.println("RenderUtils:Texture State Underflow");
			return;
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureState);
		textureState = -1;
	}
	
    public static void bindColor(Vec3 cv) {
    	GL11.glColor3d(cv.xCoord, cv.yCoord, cv.zCoord);
    }
    
    public static void bindColor(int r, int g, int b, int a) {
    	GL11.glColor4ub((byte)r, (byte)g, (byte)b, (byte)a);
    }
    
    public static void bindColor(int[] arr) {
    	if(arr.length == 3)
    		bindColor(arr[0], arr[1], arr[2]);
    	else bindColor(arr[0], arr[1], arr[2], arr[3]);
    }
    
    public static void bindColor(int r, int g, int b) {
    	GL11.glColor3ub((byte)r, (byte)g, (byte)b);
    }
    
    public static void bindGray(int s) {
    	byte r = (byte) s;
    	GL11.glColor3ub(r, r, r);
    }
    
    public static void bindIdentity() {
    	GL11.glColor4f(1, 1, 1, 1);
    }
    
    public static void bindGray(int s, int alpha) {
    	byte r = (byte) s;
    	GL11.glColor4ub(r, r, r, (byte) alpha);
    }
    
    public static void bindGray(double s) {
    	GL11.glColor3d(s, s, s);
    }
    
    public static void bindGray(double s, double alpha) {
    	GL11.glColor4d(s, s, s, alpha);
    }
	
	/**
	 * Add a vertex to the tessellator with UV coords.
	 */
	public static void addVertex(Vec3 vec3, double texU, double texV) {
		t.addVertexWithUV(vec3.xCoord, vec3.yCoord, vec3.zCoord, texU, texV);
	}

	/**
	 * Add a vertex to the tessellator without UV coords.
	 * @param vec3
	 */
	public static void addVertex(Vec3 vec3) {
		t.addVertex(vec3.xCoord, vec3.yCoord, vec3.zCoord);
	}
	
	public static void loadTexture(ResourceLocation src) {
		Minecraft.getMinecraft().renderEngine.bindTexture(src);
	}
	
	/**
	 * 创建一个新的Vec3顶点。
	 */
	public static Vec3 newV3(double x, double y, double z) {
		return Vec3.createVectorHelper(x, y, z);
	}
	
	//--------------------Utility functions-----------------------
	public static void renderItemIn2d(ItemStack stackToRender, double w) {
		renderItemIn2d(stackToRender, w, null);
	}

	/**
	 * 将Item渲染成一个有厚度的薄片。（默认渲染风格）
	 */
	public static void renderItemIn2d(ItemStack stackToRender, double w, IIcon specialIcon) {
		IIcon icon = stackToRender.getIconIndex();
		if (specialIcon != null)
			icon = specialIcon;
		if (icon == null) return;
		
		Minecraft mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture(mc.renderEngine.getResourceLocation(stackToRender.getItemSpriteNumber()));
		ResourceLocation tex = mc.renderEngine.getResourceLocation(stackToRender.getItemSpriteNumber());
		
		renderItemIn2d(w, tex, tex, icon.getMinU(), icon.getMinV(), icon.getMaxU(), icon.getMaxV());
	}
	
	public static void renderItemIn2d(double w, ResourceLocation front, ResourceLocation back) {
		renderItemIn2d(w, front, back, 0, 0, 1, 1);
	}
	
	public static void renderItemIn2d(double w, ResourceLocation front, ResourceLocation back, double u1, double v1, double u2, double v2) {
		Vec3 a1 = newV3(0, 0, w), a2 = newV3(1, 0, w), a3 = newV3(1, 1, w), a4 = newV3(
				0, 1, w), a5 = newV3(0, 0, -w), a6 = newV3(1, 0, -w), a7 = newV3(
				1, 1, -w), a8 = newV3(0, 1, -w);

		Minecraft mc = Minecraft.getMinecraft();

		GL11.glPushMatrix();
		
		RenderUtils.loadTexture(back);
		t.startDrawingQuads();
		t.setNormal(0.0F, 0.0F, 1.0F);
		addVertex(a1, u2, v2);
		addVertex(a2, u1, v2);
		addVertex(a3, u1, v1);
		addVertex(a4, u2, v1);
		t.draw();

		RenderUtils.loadTexture(front);
		t.startDrawingQuads();
		t.setNormal(0.0F, 0.0F, -1.0F);
		addVertex(a8, u2, v1);
		addVertex(a7, u1, v1);
		addVertex(a6, u1, v2);
		addVertex(a5, u2, v2);
		t.draw();

		int var7;
		float var8;
		double var9;
		float var10;
		
		/*
		 * Gets the width/16 of the currently bound texture, used to fix the
		 * side rendering issues on textures != 16
		 */
		int tileSize = 32;
		float tx = 1.0f / (32 * tileSize);
		float tz = 1.0f / tileSize;

		t.startDrawingQuads();
		t.setNormal(-1.0F, 0.0F, 0.0F);
		for (var7 = 0; var7 < tileSize; ++var7) {
			var8 = (float) var7 / tileSize;
			var9 = u2 - (u2 - u1) * var8 - tx;
			var10 = 1.0F * var8;
			t.addVertexWithUV(var10, 0.0D, -w, var9, v2);
			t.addVertexWithUV(var10, 0.0D, w, var9, v2);
			t.addVertexWithUV(var10, 1.0D, w, var9, v1);
			t.addVertexWithUV(var10, 1.0D, -w, var9, v1);

			t.addVertexWithUV(var10, 1.0D, w, var9, v1);
			t.addVertexWithUV(var10, 0.0D, w, var9, v2);
			t.addVertexWithUV(var10, 0.0D, -w, var9, v2);
			t.addVertexWithUV(var10, 1.0D, -w, var9, v1);
		}
		t.draw();

		GL11.glPopMatrix();
	}
	
	public static void renderOverlay_Equip(ResourceLocation src) {
		GL11.glDepthFunc(GL11.GL_EQUAL);
    	GL11.glDisable(GL11.GL_LIGHTING);
    	loadTexture(src);
    	GL11.glEnable(GL11.GL_BLEND);
    	GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
    	float f7 = 0.76F;
    	//GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
    	GL11.glMatrixMode(GL11.GL_TEXTURE);
    	GL11.glPushMatrix();
        float f8 = 0.125F;
        GL11.glScalef(f8, f8, f8);
        float f9 = Minecraft.getSystemTime() % 3000L / 3000.0F * 8.0F;
        GL11.glTranslatef(f9, 0.0F, 0.0F);
        GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
        ItemRenderer.renderItemIn2D(t, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(f8, f8, f8);
        f9 = Minecraft.getSystemTime() % 4873L / 4873.0F * 8.0F;
        GL11.glTranslatef(-f9, 0.0F, 0.0F);
        GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
        ItemRenderer.renderItemIn2D(t, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
	}
	
	public static void renderEnchantGlint_Equip() {
		GL11.glColor3f(0.301F, 0.78F, 1.0F);
		renderOverlay_Equip(src_glint);
	}
	
	public static void renderOverlay_Inv(ResourceLocation src) {
		GL11.glDepthFunc(GL11.GL_EQUAL);
    	GL11.glDisable(GL11.GL_LIGHTING);
    	loadTexture(src);
    	GL11.glEnable(GL11.GL_BLEND);
    	GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
    	float f7 = 0.76F;
    	//GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
    	GL11.glMatrixMode(GL11.GL_TEXTURE);
    	GL11.glPushMatrix();
        float f8 = 0.125F;
        GL11.glScalef(f8, f8, f8);
        float f9 = Minecraft.getSystemTime() % 3000L / 3000.0F * 8.0F;
        GL11.glTranslatef(f9, 0.0F, 0.0F);
        GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
        t.startDrawingQuads();
        t.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0);
		t.addVertexWithUV(0.0, 16.0, 0.0, 0.0, 1.0);
		t.addVertexWithUV(16.0, 16.0, 0.0, 1.0, 1.0);
		t.addVertexWithUV(16.0, 0.0, 0.0, 1.0, 0.0);
        t.draw();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(f8, f8, f8);
        f9 = Minecraft.getSystemTime() % 4873L / 4873.0F * 8.0F;
        GL11.glTranslatef(-f9, 0.0F, 0.0F);
        GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
        t.startDrawingQuads();
        t.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0);
		t.addVertexWithUV(0.0, 16.0, 0.0, 0.0, 1.0);
		t.addVertexWithUV(16.0, 16.0, 0.0, 1.0, 1.0);
		t.addVertexWithUV(16.0, 0.0, 0.0, 1.0, 0.0);
        t.draw();
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
	}
	
	public static void renderSimpleOverlay_Inv(ResourceLocation src) {
		//GL11.glDepthFunc(GL11.GL_EQUAL);
		GL11.glDisable(GL11.GL_LIGHTING);
		RenderUtils.loadTexture(src);
		 t.startDrawingQuads();
	        t.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0);
			t.addVertexWithUV(0.0, 16.0, 0.0, 0.0, 1.0);
			t.addVertexWithUV(16.0, 16.0, 0.0, 1.0, 1.0);
			t.addVertexWithUV(16.0, 0.0, 0.0, 1.0, 0.0);
	       t.draw();
	       GL11.glEnable(GL11.GL_LIGHTING);
	     GL11.glMatrixMode(GL11.GL_MODELVIEW);
	     GL11.glDepthFunc(GL11.GL_LEQUAL);
	}
	
	public static void renderEnchantGlint_Inv() {
		GL11.glColor3f(0.301F, 0.78F, 1.0F);
		renderOverlay_Inv(src_glint);
	}
	
	/**
	 * 直接在物品栏渲染物品icon。确认你已经绑定好贴图。
	 * @param item
	 */
	public static void renderItemInventory(ItemStack item) {
		IIcon icon = item.getIconIndex();
		renderItemInventory(icon);
	}
	
	/**
	 * 直接在物品栏渲染物品icon。确认你已经绑定好贴图。
	 * @param item
	 */
	public static void renderItemInventory(IIcon icon) {
		if(icon != null) {
			t.startDrawingQuads();
			t.addVertexWithUV(0.0, 0.0, 0.0, icon.getMinU(), icon.getMinV());
			t.addVertexWithUV(0.0, 16.0, 0.0, icon.getMinU(), icon.getMaxV());
			t.addVertexWithUV(16.0, 16.0, 0.0, icon.getMaxU(), icon.getMaxV());
			t.addVertexWithUV(16.0, 0.0, 0.0, icon.getMaxU(), icon.getMinV());
			t.draw();
		}
	}
    
    /**
     * see drawCube(w, l, h, texture).
     */
    public static void drawCube(double w, double l, double h) {
    	drawCube(w, l, h, false);
    }
    
    
   
    /**
     * Draw a cube with xwidth=w zwidth=l height=h at (0, 0, 0) with no texture.
     * <br/>Often used for debugging? ^^
     * @param w xwidth
     * @param l zwidth
     * @param h height
     */
    public static void drawCube(double w, double l, double h, boolean texture) {
    	final Vec3 vs[] = {
    	    null, //placeholder
    	    newV3(0, 0, 0),
    	    newV3(w, 0, 0),
    	    newV3(w, 0, l),
    	    newV3(0, 0, l),
    	    newV3(0, h, 0),
    	    newV3(w, h, 0),
    	    newV3(w, h, l),
    	    newV3(0, h, l),
    	};
    	final int uvs[][] = {
    		{0, 0},
    		{1, 0}, 
    		{1, 1},
    		{0, 1}
    	};
    	final int arr[][] = {
    		{1, 2, 3, 4},
    		{5, 6, 2, 1},
    		{7, 3, 2, 6},
    		{7, 8, 4, 3},
    		{8, 7, 6, 5},
    		{5, 1, 4, 8}
    	};
    	final int normals[][] = {
    		{0, -1, 0},
    		{0, 0, -1},
    		{1, 0, 0},
    		{0, 0, 1},
    		{0, 1, 0},
    		{-1, 0, 0}
    	};
    	if(!texture)
    		GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glPushMatrix(); {
    		for(int i = 0; i < arr.length; ++i) {
    			t.startDrawingQuads();
    			t.setBrightness(15728880);
    			t.setNormal(normals[i][0], normals[i][1], normals[i][2]);
    			int[] va = arr[i];
    			for(int j = 0; j < 4; ++j) {
    				Vec3 v = vs[va[j]];
    				t.addVertexWithUV(v.xCoord, v.yCoord, v.zCoord, uvs[j][0], uvs[j][1]);
    			}
    			t.draw();
    		}
    	} GL11.glPopMatrix();
    	GL11.glEnable(GL11.GL_TEXTURE_2D);
    	GL11.glDisable(GL11.GL_BLEND);
    }
    
    //Implementations
    private static Vertex vert(double x, double y, double z, double u, double v) {
    	return new Vertex(x, y, z, u, v);
    }

}
