package net.id107.flexfov;

import net.fabricmc.api.ClientModInitializer;

public class FlexFOV implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ConfigManager.loadConfig();
		System.out.println("FlexFOV loaded");
	}
}
