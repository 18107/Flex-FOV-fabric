package net.id107.flexfov.eventhandler;

import net.id107.flexfov.event.MatrixStackEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;

public class MatrixStackHandler implements MatrixStackEvent {
		
	private int dir = 0;

	@Override
	public void changeMatrixStack(MatrixStack matrixStack) {
		Matrix4f matrix;
		switch (dir) {
		case 0:
			//dir = 1;
			break;
		case 1:
			dir = 2;
			matrix = new Matrix4f(new Quaternion(0, 0.707106781f, 0, 0.707106781f)); //look right
			matrixStack.peek().getModel().multiply(matrix);
			break;
		case 2:
			dir = 3;
			matrix = new Matrix4f(new Quaternion(0, -0.707106781f, 0, 0.707106781f)); //look left
			matrixStack.peek().getModel().multiply(matrix);
			break;
		case 3:
			dir = 4;
			matrix = new Matrix4f(new Quaternion(0, -1, 0, 0)); //look back
			matrixStack.peek().getModel().multiply(matrix);
			break;
		case 4:
			dir = 5;
			matrix = new Matrix4f(new Quaternion(0.707106781f, 0, 0, 0.707106781f)); //look down
			matrixStack.peek().getModel().multiply(matrix);
			break;
		case 5:
			dir = 0;
			matrix = new Matrix4f(new Quaternion(-0.707106781f, 0, 0, 0.707106781f)); //look up
			matrixStack.peek().getModel().multiply(matrix);
			break;
		}
	}
}
