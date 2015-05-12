package cn.liutils.render.mesh;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import cn.liutils.render.material.Material;

public class Mesh {

	private double[][] vertices;
	
	private double[][] uvs;
	
	private float[][] normals;
	
	private int[] triangles;
	
	public Mesh() {}
	
	public Mesh setUVs(double[][] uvs) {
		if(vertices == null || vertices.length != uvs.length) {
			throw new IllegalStateException("UVs size must be equal to vert size");
		}
		this.uvs = uvs;
		return this;
	}
	
	public Mesh setVertices(double[][] vertices) {
		this.vertices = vertices;
		if(uvs.length != vertices.length) uvs = null;
		if(normals.length != vertices.length) normals = null;
		//Dont check triangle.
		return this;
	}
	
	public Mesh setTriangles(int[] triangles) {
		this.triangles = triangles;
		return this;
	}
	
	public Mesh setNormals(float[][] normals) {
		if(vertices == null || vertices.length != uvs.length) {
			throw new IllegalStateException("Normals size must be equal to vert size");
		}
		this.normals = normals;
		return this;
	}
	
	/**
	 * Decompose to triangles.
	 */
	public Mesh setQuads(int[] quads) {
		if(quads.length % 4 != 0) {
			System.err.println("You should specify quads by a list of length of multiply of 4.");
		}
		int[] result = new int[(quads.length / 4) * 6];
		int j = 0;
		for(int i = 0; i + 3 < quads.length; i += 4, j += 6) {
			result[j] 	  = quads[i];
			result[j + 1] = quads[i + 1];
			result[j + 2] = quads[i + 2];
			
			result[j + 3] = quads[i];
			result[j + 4] = quads[i + 2];
			result[j + 5] = quads[i + 3];
		}
		setTriangles(result);
		return this;
	}
	
	public void draw(Material mat) {
		mat.onRenderStage(RenderStage.START);
		GL11.glPushMatrix();
		
		mat.onRenderStage(RenderStage.BEFORE_TESSELLATE);
		Tessellator t = Tessellator.instance;
		t.startDrawing(GL11.GL_TRIANGLES);
		mat.onRenderStage(RenderStage.START_TESSELLATE);
		
		if(uvs != null) {
			for(int i : triangles) {
				double[] vert = vertices[i];
				double[] uv = uvs[i];
				if(normals != null) {
					t.setNormal(normals[i][0], normals[i][1], normals[i][2]);
				}
				t.addVertexWithUV(vert[0], vert[1], vert[2], uv[0], uv[1]);
				
			}
		} else {
			for(int i : triangles) {
				double[] vert = vertices[i];
				if(normals != null) {
					t.setNormal(normals[i][0], normals[i][1], normals[i][2]);
				}
				t.addVertex(vert[0], vert[1], vert[2]);
			}
		}
		t.draw();
		
		GL11.glPopMatrix();
		mat.onRenderStage(RenderStage.END);
	}
	
}
