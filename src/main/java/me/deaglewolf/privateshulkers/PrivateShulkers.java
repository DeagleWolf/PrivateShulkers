package me.deaglewolf.privateshulkers;

import me.deaglewolf.privateshulkers.Commands.PrivateShulkersCommand;
import me.deaglewolf.privateshulkers.EventListeners.PlayerEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public final class PrivateShulkers extends JavaPlugin {
    public static PrivateShulkers plugin;
    public static FileConfiguration config;
    public static FileConfiguration lang;
    public static FileConfiguration data;
    @Override
    public void onEnable() {
        plugin = this;
		PluginManager pm = Bukkit.getPluginManager();
        File langFile = new File(getDataFolder(), "lang.yml");
        File configFile = new File(getDataFolder(), "config.yml");
        File dataFile = new File(getDataFolder(), "data.yml");
        try {
            createFiles(langFile, configFile, dataFile);
            loadConfigs();
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().severe("The error occurred while creating the config file.");
			pm.disablePlugin(plugin);
            throw new RuntimeException(e);
        }
		pm.registerEvents(new PlayerEvents(), this);
		getCommand("privateshulker").setExecutor(new PrivateShulkersCommand());
    }
    public static void loadConfigs() throws IOException, InvalidConfigurationException {
        File configf = new File(plugin.getDataFolder(), "config.yml");
        File langf = new File(plugin.getDataFolder(), "lang.yml");
        File dataf = new File(plugin.getDataFolder(), "data.yml");
        config = YamlConfiguration.loadConfiguration(configf);
        lang = YamlConfiguration.loadConfiguration(langf);
        data = YamlConfiguration.loadConfiguration(dataf);
    }
    private void createFiles(File langFile, File configFile, File dataFile) {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        if (!langFile.exists()) {
            saveResource("lang.yml", false);
        }
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
        if (!dataFile.exists()) {
            saveResource("data.yml", false);
        }
    }
    public static String Color(String s) {
        return String.format(ChatColor.translateAlternateColorCodes('&', s));
    }
    public static String prefix() {
        return Color(lang.getString("prefix"));
    }
}
