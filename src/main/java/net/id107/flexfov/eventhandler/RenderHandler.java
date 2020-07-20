package net.id107.flexfov.eventhandler;

import net.id107.flexfov.event.RenderEventPost;
import net.id107.flexfov.event.RenderEventPre;
import net.minecraft.util.ActionResult;

public class RenderHandler implements RenderEventPre, RenderEventPost {

	@Override
	public ActionResult renderPre(float tickDelta, long startTime, boolean tick) {
		System.out.println("renderPre");
		return ActionResult.PASS;
	}

	@Override
	public ActionResult renderPost(float tickDelta, long startTime, boolean tick) {
		System.out.println("renderPost");
		return ActionResult.PASS;
	}

}
