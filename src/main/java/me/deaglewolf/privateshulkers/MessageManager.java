package me.deaglewolf.privateshulkers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MessageManager {
    public static void SendMessage(Player player, String messagepath) {
        FileConfiguration lang = PrivateShulkers.lang;
        String message;
        if (lang.getBoolean("messages." + messagepath + ".disabled")) return;
        else if (lang.getBoolean("messages." + messagepath + ".send-with-prefix")) message = PrivateShulkers.prefix() + lang.getString("messages." + messagepath + ".text");
        else message = lang.getString("messages." + messagepath + ".text");
        if (lang.getBoolean("messages." + messagepath + ".send-with-actionbar")) player.sendActionBar(PrivateShulkers.Color(message));
        else player.sendMessage(PrivateShulkers.Color(message));
    }
}
