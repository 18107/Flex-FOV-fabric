package net.id107.flexfov;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.util.Window;

public class BufferManager {

	private static Framebuffer framebuffer;
	public static int[] framebufferTextures = new int[6];
	
	private static float minX;
	private static float maxX;
	private static float minY;
	private static float maxY;
	private static float fov;
	
	private static int displayWidth;
	private static int displayHeight;
	
	public static void setupFrame() {
		Window window = MinecraftClient.getInstance().getWindow();
		
		if (window.getWidth() != displayWidth || window.getHeight() != displayHeight) {
			deleteFramebuffer();
			createFramebuffer();
		}
	}
	
	public static void createFramebuffer() {
		if (framebuffer != null) return;
		
		Window window = MinecraftClient.getInstance().getWindow();
		int width = Math.min(window.getWidth(), window.getHeight());
		framebuffer = new Framebuffer(width, width, false, false);
		
		for (int i = 0; i < framebufferTextures.length; i++) {
			framebufferTextures[i] = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebufferTextures[i]);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, width,
					0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		float aspectRatio = (float)window.getWidth()/(float)window.getHeight();
		
		if (aspectRatio >= 1) {
			minY = 0;
			maxY = 1;
			
			minX = 0.5f - 0.5f/aspectRatio;
			maxX = 0.5f + 0.5f/aspectRatio;
			
			fov = 90f;
		} else {
			minX = 0;
			maxX = 1;
			
			minY = 0.5f - 0.5f*aspectRatio;
			maxY = 0.5f + 0.5f*aspectRatio;
			
			fov = (float) Math.toDegrees(2*Math.atan(Math.tan(Math.toRadians(90f/2))/aspectRatio));
		}
		
		displayWidth = window.getWidth();
		displayHeight = window.getHeight();
	}
	
	public static void deleteFramebuffer() {
		if (framebuffer == null) return;
		
		for (int i = 0; i < framebufferTextures.length; i++) {
			GL11.glDeleteTextures(framebufferTextures[i]);
			framebufferTextures[i] = -1;
		}
		framebuffer.delete();
		framebuffer = null;
	}
	
	public static Framebuffer getFramebuffer() {
		return framebuffer;
	}

	public static float getMinX() {
		return minX;
	}

	public static float getMaxX() {
		return maxX;
	}

	public static float getMinY() {
		return minY;
	}

	public static float getMaxY() {
		return maxY;
	}
	
	public static float getFOV() {
		return fov;
	}
}
