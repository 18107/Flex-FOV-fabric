package net.id107.flexfov.gui.advanced;

import net.id107.flexfov.gui.SettingsGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;

public class AdvancedGui extends SettingsGui {

	private static int currentGui = 5;
	
	public AdvancedGui(Screen parent) {
		super(parent);
	}
	
	public static AdvancedGui getGui(Screen parent) {
		switch(currentGui) {
		case 0:
		default:
			return new CubicGui(parent);
		case 1:
			return new HammerGui(parent);
		case 2:
			return new PaniniGui(parent);
		case 3:
			return new CylinderGui(parent);
		case 4:
			return new FisheyeGui(parent);
		case 5:
			return new EquirectangularGui(parent);
		}
	}
	
	@Override
	protected void init() {
		super.init();
		
		ButtonWidget button = new ButtonWidget(width / 2 - 180, height / 6 + 12, 100, 20,
				new LiteralText("Cubic"), (buttonWidget) -> {
					currentGui = 0;
					client.openScreen(new CubicGui(parentScreen));
		});
		if (this instanceof CubicGui) {
			button.active = false;
		}
		addButton(button);
		
		button = new ButtonWidget(width / 2 - 50, height / 6 + 12, 100, 20,
				new LiteralText("Hammer"), (buttonWidget) -> {
					currentGui = 1;
					client.openScreen(new HammerGui(parentScreen));
				});
		if (this instanceof HammerGui) {
			button.active = false;
		}
		addButton(button);
		
		button = new ButtonWidget(width / 2 + 80, height / 6 + 12, 100, 20,
				new LiteralText("Panini"), (buttonWidget) -> {
					currentGui = 2;
					client.openScreen(new PaniniGui(parentScreen));
				});
		if (this instanceof PaniniGui) {
			button.active = false;
		}
		addButton(button);
		
		button = new ButtonWidget(width / 2 - 180, height / 6 + 36, 100, 20,
				new LiteralText("Cylinder"), (buttonWidget) -> {
					currentGui = 3;
					client.openScreen(new CylinderGui(parentScreen));
				});
		if (this instanceof CylinderGui) {
			button.active = false;
		}
		addButton(button);
		
		button = new ButtonWidget(width / 2 - 50, height / 6 + 36, 100, 20,
				new LiteralText("Fisheye"), (buttonWidget) -> {
					currentGui = 4;
					client.openScreen(new FisheyeGui(parentScreen));
				});
		if (this instanceof FisheyeGui) {
			button.active = false;
		}
		addButton(button);
		
		button = new ButtonWidget(width / 2 + 80, height / 6 + 36, 100, 20,
				new LiteralText("Equirectangular"), (buttonWidget) -> {
					currentGui = 5;
					client.openScreen(new EquirectangularGui(parentScreen));
				});
		if (this instanceof EquirectangularGui) {
			button.active = false;
		}
		addButton(button);
	}
}
