package net.id107.flexfov.gui;

import net.id107.flexfov.ConfigManager;
import net.id107.flexfov.projection.Flex;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.text.LiteralText;

public class FlexGui extends SettingsGui {
	
	public FlexGui(Screen parent) {
		super(parent);
		Projection.setProjection(new Flex());
	}
	
	@Override
	protected void init() {
		super.init();
		
		DoubleOption FOV = new DoubleOption("flexFov", 0, 360, 1,
				(gameOptions) -> {return Projection.getProjection().getFovX();},
				(gameOptions, number) -> {Projection.fov = number; ConfigManager.saveConfig();},
				(gameOptions, doubleOption) -> {return new LiteralText("FOV: " + Projection.getProjection().getFovX());});
		addButton(FOV.createButton(client.options, width / 2 - 180, height / 6 + 36, 360));
	}
}
