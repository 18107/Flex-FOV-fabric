package net.id107.flexfov;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.fabricmc.loader.api.FabricLoader;
import net.id107.flexfov.gui.SettingsGui;
import net.id107.flexfov.gui.advanced.AdvancedGui;
import net.id107.flexfov.projection.Cylinder;
import net.id107.flexfov.projection.Equirectangular;
import net.id107.flexfov.projection.Fisheye;
import net.id107.flexfov.projection.Projection;

public class ConfigManager {

	private static final Path filePath = Paths.get(FabricLoader.getInstance().getConfigDirectory().getPath() + "/FlexFOVconfig.bin");
	
	public static void loadConfig() {
		if (Files.exists(filePath)) {
			byte[] bytes;
			try {
				bytes = Files.readAllBytes(filePath);
				if (bytes.length < 4) return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			SettingsGui.currentGui = bytes[0];
			AdvancedGui.currentGui = bytes[1];
			Fisheye.fisheyeType = bytes[2];
			Projection.skyBackground = bytes[3] != 0;
			Projection.fov = ((bytes[4]&0xFF)<<8) | (bytes[5]&0xFF);
			Cylinder.fovy = ((bytes[6]&0xFF)<<8) | (bytes[7]&0xFF);
			Fisheye.fullFrame = bytes[8] != 0;
			Equirectangular.drawCircle = bytes[9] != 0;
			Equirectangular.stabilizePitch = bytes[10] != 0;
			Equirectangular.stabilizeYaw = bytes[11] != 0;
		}
	}
	
	public static void saveConfig() {
		byte[] bytes = new byte[] {
				(byte)SettingsGui.currentGui,
				(byte)AdvancedGui.currentGui,
				(byte)Fisheye.fisheyeType,
				Projection.skyBackground ? (byte)1 : (byte)0,
				(byte)((Math.round(Projection.fov)>>8)&0xFF),
				(byte)(Math.round(Projection.fov)&0xFF),
				(byte)((Math.round(Cylinder.fovy)>>8)&0xFF),
				(byte)(Math.round(Cylinder.fovy)&0xFF),
				Fisheye.fullFrame ? (byte)1 : (byte)0,
				Equirectangular.drawCircle ? (byte)1 : (byte)0,
				Equirectangular.stabilizePitch ? (byte)1 : (byte)0,
				Equirectangular.stabilizeYaw ? (byte)1 : (byte)0
		};
		try {
			Files.write(filePath, bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
