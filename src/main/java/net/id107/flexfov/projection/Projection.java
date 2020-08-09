package net.id107.flexfov.projection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import com.mojang.blaze3d.platform.FramebufferInfo;
import com.mojang.blaze3d.platform.GlStateManager;

import net.id107.flexfov.BufferManager;
import net.id107.flexfov.Reader;
import net.id107.flexfov.ShaderManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;

public abstract class Projection {

	private static Projection currentProjection = new Flex();
	private static ShaderManager shader = new ShaderManager();
	
	public static float backgroundRed;
	public static float backgroundGreen;
	public static float backgroundBlue;
	
	public static double fov = 140f;
	public static int antialiasing = 16;
	public static boolean skyBackground = true;
	
	protected int renderPass;
	
	private static boolean hudHidden;
	
	private static float tickDelta;
	
	public static Projection getProjection() {
		return currentProjection;
	}
	
	public static void setProjection(Projection projection) {
		currentProjection = projection;
		shader.deleteShaderProgram();
		shader.createShaderProgram(projection);
	}
	
	public String getVertexShader() {
		return Reader.read("flexfov:shaders/quad.vs");
	}
	
	public abstract String getFragmentShader();
	
	public void renderWorld(float tickDelta, long startTime, boolean tick) {
		Projection.tickDelta = tickDelta;
		MinecraftClient mc = MinecraftClient.getInstance();
		int displayWidth = mc.getWindow().getWidth();
		int displayHeight = mc.getWindow().getHeight();
		hudHidden = mc.options.hudHidden;
		
		if (Math.max(getFovX(), getFovY()) > 90) {
			for (renderPass = 1; renderPass < 5; renderPass++) {
				GL11.glViewport(0, 0, displayWidth, displayHeight);
				mc.worldRenderer.scheduleTerrainUpdate();
				mc.gameRenderer.renderWorld(tickDelta, startTime, new MatrixStack());
				saveRenderPass();
			}
			if (Math.max(getFovX(), getFovY()) > 250) {
				renderPass = 5;
				GL11.glViewport(0, 0, displayWidth, displayHeight);
				mc.worldRenderer.scheduleTerrainUpdate();
				mc.gameRenderer.renderWorld(tickDelta, startTime, new MatrixStack());
				saveRenderPass();
			}
		}
		renderPass = 0;
		GL11.glViewport(0, 0, displayWidth, displayHeight);
		mc.worldRenderer.scheduleTerrainUpdate();
		
		mc.options.hudHidden = hudHidden;
	}
	
	public void rotateCamera(MatrixStack matrixStack) {
		Matrix4f matrix;
		switch (renderPass) {
		case 0:
			break;
		case 1:
			matrix = new Matrix4f(new Quaternion(0, 0.707106781f, 0, 0.707106781f)); //look right
			matrixStack.peek().getModel().multiply(matrix);
			break;
		case 2:
			matrix = new Matrix4f(new Quaternion(0, -0.707106781f, 0, 0.707106781f)); //look left
			matrixStack.peek().getModel().multiply(matrix);
			break;
		case 3:
			matrix = new Matrix4f(new Quaternion(0.707106781f, 0, 0, 0.707106781f)); //look down
			matrixStack.peek().getModel().multiply(matrix);
			break;
		case 4:
			matrix = new Matrix4f(new Quaternion(-0.707106781f, 0, 0, 0.707106781f)); //look up
			matrixStack.peek().getModel().multiply(matrix);
			break;
		case 5:
			matrix = new Matrix4f(new Quaternion(0, -1, 0, 0)); //look back
			matrixStack.peek().getModel().multiply(matrix);
			break;
		}
	}
	
	public void saveRenderPass() {
		Framebuffer defaultFramebuffer = MinecraftClient.getInstance().getFramebuffer();
		Framebuffer targetFramebuffer = BufferManager.getFramebuffer();
		if (targetFramebuffer == null) { //FIXME
			BufferManager.createFramebuffer();
			targetFramebuffer = BufferManager.getFramebuffer();
			shader.createShaderProgram(getProjection());
		}
		
		GlStateManager.bindFramebuffer(FramebufferInfo.FRAME_BUFFER, targetFramebuffer.fbo);
		GL11.glViewport(0, 0, targetFramebuffer.textureWidth, targetFramebuffer.textureHeight);
		GlStateManager.framebufferTexture2D(FramebufferInfo.FRAME_BUFFER, FramebufferInfo.COLOR_ATTACHMENT,
				GL11.GL_TEXTURE_2D, BufferManager.framebufferTextures[renderPass], 0);
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glOrtho(-1, 1, -1, 1, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, defaultFramebuffer.colorAttachment);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2f(BufferManager.getMinX(), BufferManager.getMinY());
			GL11.glVertex2f(-1, -1);
			GL11.glTexCoord2f(BufferManager.getMaxX(), BufferManager.getMinY());
			GL11.glVertex2f(1, -1);
			GL11.glTexCoord2f(BufferManager.getMaxX(), BufferManager.getMaxY());
			GL11.glVertex2f(1, 1);
			GL11.glTexCoord2f(BufferManager.getMinX(), BufferManager.getMaxY());
			GL11.glVertex2f(-1, 1);
		}
		GL11.glEnd();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		
		GlStateManager.bindFramebuffer(FramebufferInfo.FRAME_BUFFER, defaultFramebuffer.fbo);
	}
	
	public void loadUniforms(float tickDelta) {
		int shaderProgram = shader.getShaderProgram();
		int displayWidth = MinecraftClient.getInstance().getWindow().getWidth();
		int displayHeight = MinecraftClient.getInstance().getWindow().getHeight();
		GL20.glUseProgram(shaderProgram);
		
		int aaUniform = GL20.glGetUniformLocation(shaderProgram, "antialiasing");
		GL20.glUniform1i(aaUniform, getAntialiasing());
		int pixelOffestUniform;
		if (getAntialiasing() == 16) {
			float left = (-1f+0.25f)/displayWidth;
			float top = (-1f+0.25f)/displayHeight;
			float right = 0.5f/displayWidth;
			float bottom = 0.5f/displayHeight;
			for (int y = 0; y < 4; y++) {
				for (int x = 0; x < 4; x++) {
					pixelOffestUniform = GL20.glGetUniformLocation(shaderProgram, "pixelOffset[" + (y*4+x) + "]");
					GL20.glUniform2f(pixelOffestUniform, left + right*x, top + bottom*y);
				}
			}
		} else if (getAntialiasing() == 4) {
			pixelOffestUniform = GL20.glGetUniformLocation(shaderProgram, "pixelOffset[0]");
			GL20.glUniform2f(pixelOffestUniform, -0.5f/displayWidth, -0.5f/displayHeight);
			pixelOffestUniform = GL20.glGetUniformLocation(shaderProgram, "pixelOffset[1]");
			GL20.glUniform2f(pixelOffestUniform, 0.5f/displayWidth, -0.5f/displayHeight);
			pixelOffestUniform = GL20.glGetUniformLocation(shaderProgram, "pixelOffset[2]");
			GL20.glUniform2f(pixelOffestUniform, -0.5f/displayWidth, 0.5f/displayHeight);
			pixelOffestUniform = GL20.glGetUniformLocation(shaderProgram, "pixelOffset[3]");
			GL20.glUniform2f(pixelOffestUniform, 0.5f/displayWidth, 0.5f/displayHeight);
		} else { //if (getAntialiasing() == 1)
			pixelOffestUniform = GL20.glGetUniformLocation(shaderProgram, "pixelOffset[0]");
			GL20.glUniform2f(pixelOffestUniform, 0, 0);
		}
		
		int texUniform = GL20.glGetUniformLocation(shaderProgram, "texFront");
		GL20.glUniform1i(texUniform, 0);
		texUniform = GL20.glGetUniformLocation(shaderProgram, "texBack");
		GL20.glUniform1i(texUniform, 5);
		texUniform = GL20.glGetUniformLocation(shaderProgram, "texLeft");
		GL20.glUniform1i(texUniform, 2);
		texUniform = GL20.glGetUniformLocation(shaderProgram, "texRight");
		GL20.glUniform1i(texUniform, 1);
		texUniform = GL20.glGetUniformLocation(shaderProgram, "texTop");
		GL20.glUniform1i(texUniform, 4);
		texUniform = GL20.glGetUniformLocation(shaderProgram, "texBottom");
		GL20.glUniform1i(texUniform, 3);
		
		int fovxUniform = GL20.glGetUniformLocation(shaderProgram, "fovx");
		GL20.glUniform1f(fovxUniform, (float) getFovX());
		int fovyUniform = GL20.glGetUniformLocation(shaderProgram, "fovy");
		GL20.glUniform1f(fovyUniform, (float) getFovY());
		
		int backgroundUniform = GL20.glGetUniformLocation(shaderProgram, "backgroundColor");
		float backgroundColor[] = getBackgroundColor(false);
		if (backgroundColor != null) {
			GL20.glUniform4f(backgroundUniform, backgroundColor[0], backgroundColor[1], backgroundColor[2], 1);
		} else {
			GL20.glUniform4f(backgroundUniform, 0, 0, 0, 1);
		}
	}
	
	public void runShader(float tickDelta) {
		int displayWidth = MinecraftClient.getInstance().getWindow().getWidth();
		int displayHeight = MinecraftClient.getInstance().getWindow().getHeight();
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		int lightmap = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glOrtho(-1, 1, -1, 1, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		
		for (int i = 0; i < BufferManager.framebufferTextures.length; i++) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0+i);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, BufferManager.framebufferTextures[i]);
		}
		GL11.glViewport(0, 0, displayWidth, displayHeight);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(-1, -1);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f(1, -1);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2f(1, 1);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2f(-1, 1);
		}
		GL11.glEnd();
		for (int i = BufferManager.framebufferTextures.length-1; i >= 0; i--) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0+i);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		}
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, lightmap);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		
		GL20.glUseProgram(0);
	}
	
	protected int getShaderProgram() {
		return shader.getShaderProgram();
	}
	
	public static float getTickDelta() {
		return tickDelta;
	}
	
	public int getAntialiasing() {
		return antialiasing;
	}
	
	public float[] getBackgroundColor(boolean sky) {
		if (sky) {
			return new float[] {backgroundRed, backgroundGreen, backgroundBlue};
		} else {
			return null;
		}
	}
	
	public boolean shouldRotateParticles() {
		return true;
	}
	
	public boolean shouldOverrideFOV() {
		return true;
	}
	
	public double getPassFOV(double fovIn) {
		return BufferManager.getFOV();
	}
	
	public double getFovX() {
		return fov;
	}
	
	public double getFovY() {
		double displayWidth = MinecraftClient.getInstance().getWindow().getWidth();
		double displayHeight = MinecraftClient.getInstance().getWindow().getHeight();
		return getFovX()*displayHeight/displayWidth;
	}
}
