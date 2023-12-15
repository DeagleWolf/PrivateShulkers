package me.deaglewolf.privateshulkers.Commands;

import me.deaglewolf.privateshulkers.MessageManager;
import me.deaglewolf.privateshulkers.PrivateShulkers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrivateShulkersCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length < 1 || !args[0].equals("reload")) {
                PrivateShulkers.plugin.getLogger().info("Unknown command! The console can only use the /ps reload command.");
                return false;
            } else {
                try {
                    PrivateShulkers.loadConfigs();
                } catch (IOException | InvalidConfigurationException e) {
                    throw new RuntimeException(e);
                }
                PrivateShulkers.plugin.getLogger().info("Config reloaded.");
            }
            return true;
        }
        if (sender.hasPermission("ps.admin")) {
            if (args.length < 1) {
                MessageManager.SendMessage((Player) sender, "unknowncommand");
                return false;
            }
            switch (args[0]) {
                case "reload": {
                    try {
                        PrivateShulkers.loadConfigs();
                    } catch (IOException | InvalidConfigurationException e) {
                        sender.sendMessage("The error occurred while creating the config file.");
                        throw new RuntimeException(e);
                    }
                    MessageManager.SendMessage((Player) sender, "configreload");
                    break;
                }
                case "help": {
                    MessageManager.SendMessage((Player) sender, "commandlist");
                    break;
                }
                default: {
                    MessageManager.SendMessage((Player) sender, "unknowncommand");
                    break;
                }
            }
            return true;
        } else {
            MessageManager.SendMessage((Player) sender, "nopermission");
            return false;
        }
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player) || sender.hasPermission("ps.admin")) {
            if (args.length == 1) {
                List<String> completions = new ArrayList<>();
                if (args[0].isEmpty() || "reload".startsWith(args[0].toLowerCase())) {
                    completions.add("reload");
                    completions.add("help");
                }
                return completions;
            }
        }

        return null;
    }
}
