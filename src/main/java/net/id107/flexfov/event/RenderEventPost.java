package net.id107.flexfov.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface RenderEventPost {

	Event<RenderEventPost> EVENT = EventFactory.createArrayBacked(RenderEventPost.class,
			(listenters) -> (tickDelta, startTime, tick) -> {
				for (RenderEventPost listener : listenters) {
					ActionResult result = listener.renderPost(tickDelta, startTime, tick);
					if (result != ActionResult.PASS) {
						return result;
					}
				}
				return ActionResult.PASS;
			});
	
	ActionResult renderPost(float tickDelta, long startTime, boolean tick);
}
