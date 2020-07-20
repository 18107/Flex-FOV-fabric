package net.id107.flexfov.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface RenderEventPre {

	Event<RenderEventPre> EVENT = EventFactory.createArrayBacked(RenderEventPre.class,
			(listenters) -> (tickDelta, startTime, tick) -> {
				for (RenderEventPre listener : listenters) {
					ActionResult result = listener.renderPre(tickDelta, startTime, tick);
					if (result != ActionResult.PASS) {
						return result;
					}
				}
				return ActionResult.PASS;
			});
	
	ActionResult renderPre(float tickDelta, long startTime, boolean tick);
}
