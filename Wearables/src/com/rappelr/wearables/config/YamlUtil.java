package com.rappelr.wearables.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class YamlUtil {
	
	/*
	 * YamlUtil v1.0.1
	 * by Rappelr
	 */
	
	private final JavaPlugin plugin;
	
	public void copy(@NonNull final String source, @NonNull final String destination) {
		try {
			final BufferedReader reader = new BufferedReader(new InputStreamReader(plugin.getResource(source)));
			
			val lines = new ArrayList<String>();
			
			String line = new String();
			
			while((line = reader.readLine()) != null)
				lines.add(line);
			
			reader.close();
			
			writeTo(destination, lines.toArray(new String[0]));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeTo(@NonNull final String file, @NonNull final String... lines) {
		writeTo(new File(file), lines);
	}
	
	public void writeTo(@NonNull final File file, @NonNull final String... lines) {
		try {
			if(!file.exists())
				file.createNewFile();
			
			final FileWriter writer = new FileWriter(file);
			
			for(String line : lines)
				writer.write(line + "\n");
			
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
