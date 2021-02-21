package com.rappelr.wearables.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class StoreFile {

	private static final String STO = "=";

	/*
	 * StoreFile v1.1
	 * by Rappelr
	 */

	private final HashMap<String, String> store;

	private final File file;

	private final JavaPlugin plugin;

	private int taskId;

	private StoreFile(JavaPlugin plugin, File file, boolean load) {
		this.plugin = plugin;
		this.file = file;
		
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				plugin.getLogger().warning("store file could not be created, " + file.getAbsolutePath());
				e.printStackTrace();
			}

		taskId = -1;
		store = new HashMap<String, String>();

		if(load)
			load();
	}

	public void scheduleSave(long inteval) {
		if(taskId != -1)
			Bukkit.getScheduler().cancelTask(taskId);
		
		if(inteval > 0)
			taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> save(true), 1l, inteval);
		else
			taskId = -1;
	}

	public static StoreFile of(JavaPlugin plugin, String file, boolean load) {
		return file == null ? null : of(plugin, new File(file), load);
	}

	public static StoreFile of(JavaPlugin plugin, File file, boolean load) {
		return plugin == null || file == null ? null : new StoreFile(plugin, file, load);
	}

	public void load() {
		load(false);
	}

	public void load(final boolean async) {
		store.clear();

		if(!file.exists())
			return;

		if(async)
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> read());
		else
			read();
	}

	public void save() {
		save(false);
	}

	public void save(final boolean async) {
		if(async)
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> write());
		else
			write();
	}

	public int size() {
		return store.size();
	}

	public HashMap<String, String> getstore() {
		return store;
	}

	public Set<String> getKeys() {
		return store.keySet();
	}

	public Collection<String> getValues() {
		return store.values();
	}

	public boolean contains(String... keys) {
		for(String key : keys)
			if(!contains(key))
				return false;
		return true;
	}

	public boolean contains(String key) {
		return store.containsKey(key);
	}

	public String get(String key) {
		return store.get(key);
	}

	public int getInt(String key) {
		return Integer.parseInt(get(key));
	}

	public float getFloat(String key) {
		return Float.parseFloat(get(key));
	}

	public double getDouble(String key) {
		return Double.parseDouble(get(key));
	}

	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(get(key));
	}
	
	public void clear() {
		store.clear();
	}

	public String remove(String key) {
		return store.remove(key);
	}

	public String set(String key, String value) {
		return store.put(key, value);
	}

	public String set(String key, int value) {
		return store.put(key, "" + value);
	}

	public String set(String key, float value) {
		return store.put(key, "" + value);
	}

	public String set(String key, double value) {
		return store.put(key, "" + value);
	}

	public String set(String key, boolean value) {
		return store.put(key, "" + value);
	}

	private void write() {
		try {
			final FileWriter writer = new FileWriter(file);

			for(final Map.Entry<String, String> entry : store.entrySet()) {
				if(entry.getKey() == null || entry.getValue() == null)
					continue;

				writer.write(entry.getKey() + STO + entry.getValue());
			}

			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void read() {
		try {
			final BufferedReader reader = new BufferedReader(new FileReader(file));

			String line = "";

			while((line = reader.readLine()) != null) {

				try {
					if(line.isEmpty())
						continue;

					final int index = line.indexOf(STO);
					final int post = index + STO.length();

					if(index == -1 || index == 0 || post == line.length())
						continue;

					final String key = line.substring(0, line.indexOf(STO));
					final String value = line.substring(post);

					store.put(key, value);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
