package net.id107.flexfov.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.util.math.MatrixStack;

public interface MatrixStackEvent {

	Event<MatrixStackEvent> EVENT = EventFactory.createArrayBacked(MatrixStackEvent.class,
			(listeners) -> (matrixStack) -> {
				for (MatrixStackEvent listener : listeners) {
					listener.changeMatrixStack(matrixStack);
				}
			});
	
	void changeMatrixStack(MatrixStack matrixStack);
}
