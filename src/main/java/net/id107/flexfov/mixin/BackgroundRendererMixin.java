package net.id107.flexfov.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.id107.flexfov.projection.Projection;
import net.minecraft.client.render.BackgroundRenderer;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {
	@Shadow static float red;
	@Shadow static float green;
	@Shadow static float blue;
	
	@Inject(method = "render(Lnet/minecraft/client/render/Camera;FLnet/minecraft/client/world/ClientWorld;IF)V",
			at = @At(value = "TAIL"))
	private static void skyColor(CallbackInfo callbackInfo) {
		Projection.backgroundRed = red;
		Projection.backgroundGreen = green;
		Projection.backgroundBlue = blue;
	}
}
