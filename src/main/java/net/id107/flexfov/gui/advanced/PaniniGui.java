package net.id107.flexfov.gui.advanced;

import net.id107.flexfov.projection.Panini;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.text.LiteralText;

public class PaniniGui extends AdvancedGui {

	private static DoubleOption FOV;
	
	public PaniniGui(Screen parent) {
		super(parent);
		Projection.setProjection(new Panini());
		if (FOV == null) {
			FOV = new DoubleOption("paniniFOV", 0, 360, 1,
							(gameOptions) -> {return Projection.getProjection().getFOV();},
							(gameOptions, number) -> {Projection.fov = number;},
							(gameOptions, doubleOption) -> {return new LiteralText("FOV: " + Projection.getProjection().getFOV());});
		}
	}
	
	@Override
	protected void init() {
		super.init();
		
		addButton(FOV.createButton(client.options, width / 2 - 180, height / 6 + 36, 360));
	}
}
