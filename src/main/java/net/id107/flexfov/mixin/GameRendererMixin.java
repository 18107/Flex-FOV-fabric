package net.id107.flexfov.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.id107.flexfov.projection.Projection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow final MinecraftClient client;
	@Shadow boolean renderingPanorama;
	private boolean renderingPanoramaTemp;
	private double fovTemp;
	
	public GameRendererMixin() {
		client = null;
	}
	
	@Inject(method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
	private void panoramaFov(CallbackInfoReturnable<Double> callbackInfo) {
		callbackInfo.setReturnValue((double)Projection.getProjection().getPassFOV(90));
	}
	
	@Inject(method = "render(FJZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V", ordinal = 0))
	private void renderPre(float tickDelta, long startTime, boolean tick, CallbackInfo callbackInfo) {
		renderingPanoramaTemp = renderingPanorama;
		renderingPanorama = Projection.getProjection().shouldOverrideFOV();
		fovTemp = client.options.fov;
		client.options.fov = Projection.getProjection().getPassFOV(fovTemp);
		Projection.getProjection().renderWorld(tickDelta, startTime, tick);
	}
	
	@Inject(method = "render(FJZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;isIntegratedServerRunning()Z", ordinal = 0))
	private void renderPost(float tickDelta, long startTime, boolean tick, CallbackInfo callbackInfo) {
		renderingPanorama = renderingPanoramaTemp;
		client.options.fov = fovTemp;
		Projection.getProjection().saveRenderPass();
		Projection.getProjection().loadUniforms(tickDelta);
		Projection.getProjection().runShader(tickDelta);
	}
	
	@ModifyVariable(method = "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V",
			ordinal = 1,
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;bobViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V", ordinal = 0))
	private MatrixStack updateCamera(MatrixStack matrixStack) {
		Projection.getProjection().rotateCamera(matrixStack);
		return matrixStack;
	}
	
	@Redirect(method = "render(FJZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;render(Lnet/minecraft/client/util/math/MatrixStack;F)V"))
	private void renderHud(InGameHud inGameHud, MatrixStack matrixStack, float tickDelta) {
		if (!Projection.getProjection().getResizeGui()) {
			inGameHud.render(matrixStack, tickDelta);
		}
	}
	
	@Redirect(method = "render(FJZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V"))
	private void renderCurrentScreen(Screen currentScreen, MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
		if (!Projection.getProjection().getResizeGui()) {
			currentScreen.render(matrixStack, mouseX, mouseY, delta);
		}
	}
}
