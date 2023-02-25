package co.wholesome_seal.worldportals.structure;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import co.wholesome_seal.worldportals.Plugin;

public class DataStorage {
    public static final HashMap<String, DataStorage> storages = new HashMap<>();

    public File file;
    public FileConfiguration config;

    private Plugin plugin;

    public DataStorage(Plugin plugin, String name) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), name + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to generate data file: " + name + ".yml");
                plugin.getLogger().severe(e.getMessage());
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
        storages.put(name, this);

        config.options().copyDefaults();
        save();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to generate data file: " + file.getName());
            plugin.getLogger().severe(e.getMessage());
        }
        reload();
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
