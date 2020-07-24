package net.id107.flexfov.projection;

import net.id107.flexfov.Reader;

public class Flex extends Projection {

	@Override
	public String getFragmentShader() {
		return Reader.read("flexfov:shaders/flex.fs");
	}
	
	@Override
	public double getPassFOV(double fovIn) {
		double fov = getFOV();
		if (fov <= 90) {
			if (fov == 0) {
				fov = 0.0001f;
			}
			return fov;
		}
		return super.getPassFOV(fovIn);
	}
}
