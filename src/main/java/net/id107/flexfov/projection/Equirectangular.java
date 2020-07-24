package net.id107.flexfov.projection;

import org.lwjgl.opengl.GL20;

import net.id107.flexfov.Reader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

public class Equirectangular extends Projection {

	public static boolean drawCircle = false;
	public static boolean stabilizePitch = false;
	public static boolean stabilizeYaw = false;
	
	@Override
	public String getFragmentShader() {
		return Reader.read("flexfov:shaders/equirectangular.fs");
	}
	
	@Override
	public void loadUniforms(float tickDelta) {
		super.loadUniforms(tickDelta);
		
		MinecraftClient mc = MinecraftClient.getInstance();
		int shaderProgram = getShaderProgram();
		
		int circleUniform = GL20.glGetUniformLocation(shaderProgram, "drawCircle");
		GL20.glUniform1i(circleUniform, drawCircle ? 1 : 0);
		
		Entity entity = MinecraftClient.getInstance().getCameraEntity();
		float pitch = 0;
		float yaw = 0;
		if (stabilizePitch) {
			pitch = entity.prevPitch + (entity.pitch - entity.prevPitch) * tickDelta;
		}
		if (stabilizeYaw) {
			yaw = entity.prevYaw + (entity.yaw - entity.prevYaw) * tickDelta;
		}
		if (mc.options.perspective == 2) {
			pitch = -pitch;
		}
		
		int angleUniform = GL20.glGetUniformLocation(shaderProgram, "rotation");
		GL20.glUniform2f(angleUniform, yaw, pitch);
	}
}
