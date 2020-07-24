package net.id107.flexfov.gui;

import net.id107.flexfov.projection.Projection;
import net.id107.flexfov.projection.Rectlinear;
import net.minecraft.client.gui.screen.Screen;

public class RectlinearGui extends SettingsGui {

	public RectlinearGui(Screen parent) {
		super(parent);
		Projection.setProjection(new Rectlinear());
	}
}
