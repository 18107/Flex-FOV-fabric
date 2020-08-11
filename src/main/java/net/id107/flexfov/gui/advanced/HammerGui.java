package net.id107.flexfov.gui.advanced;

import net.id107.flexfov.ConfigManager;
import net.id107.flexfov.projection.Hammer;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;

public class HammerGui extends AdvancedGui {

	public HammerGui(Screen parent) {
		super(parent);
		Projection.setProjection(new Hammer());
	}
	
	@Override
	protected void init() {
		super.init();
		
		addButton(new ButtonWidget(width / 2 - 155, height / 6 + 84, 150, 20,
				new LiteralText("Background Color: " + (Projection.skyBackground ? "Sky" : "Black")), (buttonWidget) -> {
					Projection.skyBackground = !Projection.skyBackground;
					buttonWidget.setMessage(new LiteralText("Background Color: " + (Projection.skyBackground ? "Sky" : "Black")));
					ConfigManager.saveConfig();
				}));
	}
}
