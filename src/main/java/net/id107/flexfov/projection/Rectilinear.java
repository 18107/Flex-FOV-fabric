package net.id107.flexfov.projection;

import net.id107.flexfov.Reader;
import net.minecraft.client.util.math.MatrixStack;

public class Rectilinear extends Projection {

	@Override
	public String getFragmentShader() {
		return Reader.read("flexfov:shaders/error.fs");
	}
	
	@Override
	public void renderWorld(float tickDelta, long startTime, boolean tick) {}
	
	@Override
	public void rotateCamera(MatrixStack matrixStack) {}
	
	@Override
	public void saveRenderPass() {}
	
	@Override
	public void loadUniforms(float tickDelta) {}
	
	@Override
	public void runShader(float tickDelta) {}
	
	@Override
	public boolean shouldRotateParticles() {
		return false;
	}
	
	@Override
	public boolean shouldOverrideFOV() {
		return false;
	}
	
	@Override
	public double getPassFOV(double fovIn) {
		return fovIn;
	}
}
