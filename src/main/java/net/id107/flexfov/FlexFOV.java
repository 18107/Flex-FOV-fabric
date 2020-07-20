package net.id107.flexfov;

import net.fabricmc.api.ClientModInitializer;
import net.id107.flexfov.event.MatrixStackEvent;
import net.id107.flexfov.event.RenderEventPost;
import net.id107.flexfov.event.RenderEventPre;
import net.id107.flexfov.eventhandler.MatrixStackHandler;
import net.id107.flexfov.eventhandler.RenderHandler;

public class FlexFOV implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		// TODO remove
		System.out.println("FlexFOV loaded");
		
		MatrixStackEvent.EVENT.register(new MatrixStackHandler());
		RenderHandler renderHandler = new RenderHandler();
		RenderEventPre.EVENT.register(renderHandler);
		RenderEventPost.EVENT.register(renderHandler);
	}
}
