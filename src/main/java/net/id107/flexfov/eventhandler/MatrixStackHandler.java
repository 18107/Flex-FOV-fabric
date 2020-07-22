package net.id107.flexfov.eventhandler;

import net.id107.flexfov.event.MatrixStackEvent;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.util.math.MatrixStack;

public class MatrixStackHandler implements MatrixStackEvent {

	@Override
	public void changeMatrixStack(MatrixStack matrixStack) {
		Projection.getProjection().rotateCamera(matrixStack);
	}
}
