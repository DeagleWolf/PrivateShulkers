package me.deaglewolf.privateshulkers.Functions;

import me.deaglewolf.privateshulkers.LocationConvert;
import me.deaglewolf.privateshulkers.PrivateShulkers;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Auth {
    private static FileConfiguration config = PrivateShulkers.config;
    private static FileConfiguration data = PrivateShulkers.data;
    public static Boolean CheckPermission(Player player, Block block) {
        if (!block.getType().name().toLowerCase().contains("shulker")) return true;
        else if (player.hasPermission("ps.admin") || player.hasPermission("ps.bypass")) return true;
        else if (block.getWorld().getName().equals(config.getString("worldname"))) {
            if (data.getString("data." + LocationConvert.convert(block.getLocation()) + ".owner") == null) return false;
            else if (data.getString("data." + LocationConvert.convert(block.getLocation()) + ".owner").equals(player.getName())) return true;
            else return false;
        } else {
            return true;
        }
    }
    public static void RegisterPlacedShulker(Player player, Block block) throws IOException {
        data.set("data." + LocationConvert.convert(block.getLocation()) + ".owner", player.getName());
        data.save(new File(PrivateShulkers.plugin.getDataFolder(), "data.yml"));
    }
    public static void UnRegisterShulker(Block block) throws IOException {
        PrivateShulkers.data.set("data." + LocationConvert.convert(block.getLocation()), null);
        data.save(new File(PrivateShulkers.plugin.getDataFolder(), "data.yml"));
    }
}
