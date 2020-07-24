package net.id107.flexfov.gui.advanced;

import net.id107.flexfov.projection.Cylinder;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.text.LiteralText;

public class CylinderGui extends AdvancedGui {

	public CylinderGui(Screen parent) {
		super(parent);
		Projection.setProjection(new Cylinder());
	}
	
	@Override
	protected void init() {
		super.init();
		
		DoubleOption FOVX = new DoubleOption("cylinderFovX", 0, 360, 1,
				(gameOptions) -> {return Projection.getProjection().getFovX();},
				(gameOptions, number) -> {Projection.fov = number;},
				(gameOptions, doubleOption) -> {return new LiteralText("Horizontal FOV: " + Math.round(Projection.getProjection().getFovX()));});
		addButton(FOVX.createButton(client.options, width / 2 - 180, height / 6 + 60, 360));
		
		DoubleOption FOVY = new DoubleOption("cylinderFovY", 0, 180, 1,
				(gameOptions) -> {return Projection.getProjection().getFovY();},
				(gameOptions, number) -> {Cylinder.fovy = number;},
				(gameOptions, doubleOption) -> {return new LiteralText("Vertical FOV: " + Math.round(Projection.getProjection().getFovY()));});
		addButton(FOVY.createButton(client.options, width / 2 - 180, height / 6 + 84, 180));
	}
}
