package net.id107.flexfov.gui.advanced;

import net.id107.flexfov.ConfigManager;
import net.id107.flexfov.projection.Fisheye;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.text.LiteralText;

public class FisheyeGui extends AdvancedGui {
	
	public FisheyeGui(Screen parent) {
		super(parent);
		Projection.setProjection(new Fisheye());
	}
	
	@Override
	protected void init() {
		super.init();
		
		ButtonWidget button = new ButtonWidget(width / 2 - 190, height / 6 + 60, 76, 20,
				new LiteralText("Orthographic"), (buttonWidget) -> {
					Fisheye.fisheyeType = 0;
					client.openScreen(new FisheyeGui(parentScreen));
				});
		if (Fisheye.fisheyeType == 0) {
			button.active = false;
		}
		addButton(button);
		
		button = new ButtonWidget(width / 2 - 114, height / 6 + 60, 76, 20,
				new LiteralText("Thoby"), (buttonWidget) -> {
					Fisheye.fisheyeType = 1;
					client.openScreen(new FisheyeGui(parentScreen));
				});
		if (Fisheye.fisheyeType == 1) {
			button.active = false;
		}
		addButton(button);
		
		button = new ButtonWidget(width / 2 - 38, height / 6 + 60, 76, 20,
				new LiteralText("Equisolid"), (buttonWidget) -> {
					Fisheye.fisheyeType = 2;
					client.openScreen(new FisheyeGui(parentScreen));
				});
		if (Fisheye.fisheyeType == 2) {
			button.active = false;
		}
		addButton(button);
		
		button = new ButtonWidget(width / 2 + 38, height / 6 + 60, 76, 20,
				new LiteralText("Equidistant"), (buttonWidget) -> {
					Fisheye.fisheyeType = 3;
					client.openScreen(new FisheyeGui(parentScreen));
				});
		if (Fisheye.fisheyeType == 3) {
			button.active = false;
		}
		addButton(button);
		
		button = new ButtonWidget(width / 2 + 114, height / 6 + 60, 76, 20,
				new LiteralText("Stereographic"), (buttonWidget) -> {
					Fisheye.fisheyeType = 4;
					client.openScreen(new FisheyeGui(parentScreen));
				});
		if (Fisheye.fisheyeType == 4) {
			button.active = false;
		}
		addButton(button);
		
		int fovSliderLimit = 360;
		if (Fisheye.fisheyeType == 1) fovSliderLimit = (int)Math.ceil(fovSliderLimit*0.713); //Thoby 256.68 degrees, slider goes up to 257
		if (Fisheye.fisheyeType == 0) fovSliderLimit = 180; //Orthographic
		final int finalSliderLimit = fovSliderLimit;
		DoubleOption FOV = new DoubleOption("fisheyeFov", 0, fovSliderLimit, 1,
				(gameOptions) -> {return Math.min(finalSliderLimit, Projection.getProjection().getFovX());},
				(gameOptions, number) -> {Projection.fov = number; ConfigManager.saveConfig();},
				(gameOptions, doubleOption) -> {return new LiteralText("FOV: " + (int)Math.min(finalSliderLimit, Projection.getProjection().getFovX()));});
		addButton(FOV.createButton(client.options, width / 2 - 180, height / 6 + 132, fovSliderLimit));
		
		addButton(new ButtonWidget(width / 2 - 155, height / 6 + 84, 150, 20,
				new LiteralText("Background Color: " + (Projection.skyBackground ? "Sky" : "Black")), (buttonWidget) -> {
					Projection.skyBackground = !Projection.skyBackground;
					buttonWidget.setMessage(new LiteralText("Background Color: " + (Projection.skyBackground ? "Sky" : "Black")));
					ConfigManager.saveConfig();
				}));
		addButton(new ButtonWidget(width / 2 - 155, height / 6 + 108, 150, 20,
				new LiteralText("Full Frame: " + (Fisheye.fullFrame ? "ON" : "OFF")), (buttonWidget) -> {
					Fisheye.fullFrame = !Fisheye.fullFrame;
					buttonWidget.setMessage(new LiteralText("Full Frame: " + (Fisheye.fullFrame ? "ON" : "OFF")));
					ConfigManager.saveConfig();
				}));
	}
}
