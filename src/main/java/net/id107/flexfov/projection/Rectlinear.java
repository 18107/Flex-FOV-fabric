package net.id107.flexfov.projection;

import net.id107.flexfov.Reader;
import net.minecraft.client.util.math.MatrixStack;

public class Rectlinear extends Projection {

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
	public void runShader(float tickDelta) {}
	
	@Override
	public float getPassFOV(float fovIn) {
		return fovIn;
	}
}
