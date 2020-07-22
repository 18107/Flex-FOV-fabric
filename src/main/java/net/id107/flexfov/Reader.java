package net.id107.flexfov;

import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class Reader {

	public static String read(String resourceIn) {
		ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
		Resource resource = null;
		try {
			resource = resourceManager.getResource(new Identifier(resourceIn));
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		
		InputStream is = resource.getInputStream();
		if (is == null) {
			System.out.println("Shader not found");
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		int i;
		
		try {
			i = is.read();
			while (i != -1) {
				sb.append((char) i);
				i = is.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		
		return sb.toString();
	}
}
