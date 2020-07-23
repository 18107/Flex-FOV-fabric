package net.id107.flexfov.projection;

import net.id107.flexfov.Reader;

public class Panini extends Projection {

	@Override
	public String getFragmentShader() {
		return Reader.read("flexfov:shaders/panini.fs");
	}
}
