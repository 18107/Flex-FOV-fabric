package net.id107.flexfov.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.id107.flexfov.event.MatrixStackEvent;
import net.id107.flexfov.event.RenderEventPost;
import net.id107.flexfov.event.RenderEventPre;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow boolean renderingPanorama;
	private boolean renderingPanoramaTemp;
	
	@Inject(method = "render(FJZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V", ordinal = 0))
	private void renderPre(float tickDelta, long startTime, boolean tick, CallbackInfo callbackInfo) {
		renderingPanoramaTemp = renderingPanorama;
		renderingPanorama = true;
		RenderEventPre.EVENT.invoker().renderPre(tickDelta, startTime, tick);
	}
	
	@Inject(method = "render(FJZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;isIntegratedServerRunning()Z", ordinal = 0))
	private void renderPost(float tickDelta, long startTime, boolean tick, CallbackInfo callbackInfo) {
		renderingPanorama = renderingPanoramaTemp;
		RenderEventPost.EVENT.invoker().renderPost(tickDelta, startTime, tick);
	}
	
	@ModifyVariable(method = "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V",
			ordinal = 1,
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;bobViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V", ordinal = 0))
	private MatrixStack updateCamera(MatrixStack matrixStack) {
		MatrixStackEvent.EVENT.invoker().changeMatrixStack(matrixStack);
		return matrixStack;
	}
}
