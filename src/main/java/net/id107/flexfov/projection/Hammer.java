package net.id107.flexfov.projection;

import net.id107.flexfov.Reader;

public class Hammer extends Projection {

	@Override
	public String getFragmentShader() {
		return Reader.read("flexfov:shaders/hammer.fs");
	}
	
	@Override
	public float[] getBackgroundColor(boolean ignored) {
		return super.getBackgroundColor(skyBackground);
	}
}
