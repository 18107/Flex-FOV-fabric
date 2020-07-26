package net.id107.flexfov.gui;

import net.id107.flexfov.gui.advanced.AdvancedGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public abstract class SettingsGui extends Screen {

	protected final Screen parentScreen;
	
	private static int currentGui = 1;
	
	public SettingsGui(Screen parent) {
		super(new LiteralText("Flex FOV Settings"));
		parentScreen = parent;
	}
	
	public static SettingsGui getGui(Screen parent) {
		switch (currentGui) {
		case 0:
		default:
			return new RectlinearGui(parent);
		case 1:
			return new FlexGui(parent);
		case 2:
			return AdvancedGui.getGui(parent);
		}
	}
	
	@Override
	protected void init() {
		ButtonWidget button = new ButtonWidget(width / 2 - 190, height / 6 - 12, 120, 20,
				new LiteralText("Default"), (buttonWidget) -> {
					currentGui = 0;
					client.openScreen(new RectlinearGui(parentScreen));
		});
		if (this instanceof RectlinearGui) {
			button.active = false;
		}
		addButton(button);
		
		button = new ButtonWidget(width / 2 - 60, height / 6 - 12, 120, 20,
				new LiteralText("Flex"), (buttonWidget) -> {
					currentGui = 1;
					client.openScreen(new FlexGui(parentScreen));
				});
		if (this instanceof FlexGui) {
			button.active = false;
		}
		addButton(button);
		
		button = new ButtonWidget(width / 2 + 70, height / 6 - 12, 120, 20,
				new LiteralText("Advanced"), (buttonWidget) -> {
					currentGui = 2;
					client.openScreen(AdvancedGui.getGui(parentScreen));
				});
		if (this instanceof AdvancedGui) {
			button.active = false;
		}
		addButton(button);
		
		addButton(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 168, 200, 20, ScreenTexts.DONE, (buttonWidget) -> {
			client.openScreen(parentScreen);
		}));
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		this.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 16777215);
		super.render(matrices, mouseX, mouseY, delta);
	}
}
