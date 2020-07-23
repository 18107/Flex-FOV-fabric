package net.id107.flexfov.eventhandler;

import net.id107.flexfov.event.RenderEventPost;
import net.id107.flexfov.event.RenderEventPre;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.ActionResult;

public class RenderHandler implements RenderEventPre, RenderEventPost {
	
	private double fov;

	@Override
	public ActionResult renderPre(float tickDelta, long startTime, boolean tick) {
		MinecraftClient mc = MinecraftClient.getInstance();
		fov = mc.options.fov;
		mc.options.fov = Projection.getProjection().getPassFOV((float)fov);
		Projection.getProjection().renderWorld(tickDelta, startTime, tick);
		return ActionResult.PASS;
	}

	@Override
	public ActionResult renderPost(float tickDelta, long startTime, boolean tick) {
		MinecraftClient mc = MinecraftClient.getInstance();
		mc.options.fov = fov;
		Projection.getProjection().saveRenderPass();
		Projection.getProjection().runShader(tickDelta);
		return ActionResult.PASS;
	}

}
