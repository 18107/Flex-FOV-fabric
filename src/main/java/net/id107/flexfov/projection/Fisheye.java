package net.id107.flexfov.projection;

import org.lwjgl.opengl.GL20;

import net.id107.flexfov.Reader;

public class Fisheye extends Projection {

	public static boolean fullFrame = false;
	public static int fisheyeType = 3;
	
	@Override
	public String getFragmentShader() {
		return Reader.read("flexfov:shaders/fisheye.fs");
	}
	
	@Override
	public float[] getBackgroundColor(boolean ignored) {
		return super.getBackgroundColor(skyBackground);
	}
	
	@Override
	public void loadUniforms(float tickDelta) {
		super.loadUniforms(tickDelta);
		
		int shaderProgram = getShaderProgram();
		
		int fullFrameUniform = GL20.glGetUniformLocation(shaderProgram, "fullFrame");
		GL20.glUniform1i(fullFrameUniform, fullFrame ? 1 : 0);
		int fsheyeTypeUniform = GL20.glGetUniformLocation(shaderProgram, "fisheyeType");
		GL20.glUniform1i(fsheyeTypeUniform, fisheyeType);
	}
}
