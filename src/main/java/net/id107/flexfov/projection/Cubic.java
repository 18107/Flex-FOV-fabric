package net.id107.flexfov.projection;

import net.id107.flexfov.Reader;

public class Cubic extends Projection {

	@Override
	public String getFragmentShader() {
		return Reader.read("flexfov:shaders/cubic.fs");
	}
	
	@Override
	public float[] getBackgroundColor() {
		if (skyBackground) {
			return new float[] {1, 0, 1};
		} else {
			return null;
		}
	}
}
