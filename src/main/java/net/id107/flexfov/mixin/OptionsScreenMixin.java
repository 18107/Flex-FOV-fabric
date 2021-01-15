package net.id107.flexfov.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.id107.flexfov.gui.SettingsGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
	
	public OptionsScreenMixin(Text text) {
		super(text);
	}
	
	@Inject(method = "init()V", at = @At(value = "TAIL"))
	private void newButton(CallbackInfo callbackInfo) {
		addButton(new ButtonWidget(width / 2 - 155, height / 6 + 15, 150, 20, new LiteralText("Flex FOV Settings"), (buttonWidget) -> {
			client.openScreen(SettingsGui.getGui(this));
		}));
	}
}
