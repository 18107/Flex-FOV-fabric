package net.id107.flexfov.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.id107.flexfov.projection.Projection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

@Mixin(value = BillboardParticle.class, priority = 1500)
public abstract class BillboardParticleMixin extends Particle {

	protected BillboardParticleMixin(ClientWorld world, double x, double y, double z) {
		super(world, x, y, z);
	}

	@ModifyVariable(method = "buildGeometry(Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/render/Camera;F)V",
			ordinal = 0,
			at = @At(value = "INVOKE_ASSIGN", ordinal = 0,
			target = "Lnet/minecraft/client/render/Camera;getRotation()Lnet/minecraft/util/math/Quaternion;"))
	private Quaternion rotateParticle(Quaternion quaternion) {
		if (Projection.getProjection().shouldRotateParticles()) {
			MinecraftClient mc = MinecraftClient.getInstance();
			Entity camera = mc.cameraEntity;
			Vec3d cameraPos = camera.getPos().subtract(camera.prevX, camera.prevY, camera.prevZ).multiply(Projection.getTickDelta()).add(new Vec3d(camera.prevX, camera.prevY, camera.prevZ));
			Vec3d particlePos = new Vec3d(x, y, z).subtract(new Vec3d(prevPosX, prevPosY, prevPosZ)).multiply(Projection.getTickDelta()).add(new Vec3d(prevPosX, prevPosY, prevPosZ));
			Vec3d dir = cameraPos.subtract(particlePos).normalize();
			quaternion.set(0, 0, 0, 1);
			quaternion.hamiltonProduct(Vector3f.POSITIVE_Y.getRadialQuaternion((float)Math.atan2(-dir.x, -dir.z)));
			quaternion.hamiltonProduct(Vector3f.POSITIVE_X.getRadialQuaternion((float)Math.asin(dir.y)));
		}
		return quaternion;
	}
}
