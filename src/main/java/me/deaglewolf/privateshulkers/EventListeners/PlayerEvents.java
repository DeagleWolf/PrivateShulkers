package me.deaglewolf.privateshulkers.EventListeners;

import me.deaglewolf.privateshulkers.Functions.Auth;
import me.deaglewolf.privateshulkers.MessageManager;
import me.deaglewolf.privateshulkers.PrivateShulkers;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public class PlayerEvents implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType().name().toLowerCase().contains("shulker") && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!Auth.CheckPermission(event.getPlayer(), event.getClickedBlock())) {
                MessageManager.SendMessage(event.getPlayer(), "cantinteractshulker");
                event.setCancelled(true);
            } else MessageManager.SendMessage(event.getPlayer(), "shulkeropened");
        }
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) throws IOException {
        FileConfiguration config = PrivateShulkers.config;
        if (config.getBoolean("prevent-block-shulker")) {
            Block block = event.getBlock();
            Block blockBelow = block.getLocation().subtract(0, 1, 0).getBlock();
            if (blockBelow.getType().name().toLowerCase().contains("shulker")) {
                MessageManager.SendMessage(event.getPlayer(), "shulkerplaceprevent");
                event.setCancelled(true);
            }
        }
        if (!event.getBlock().getType().name().toLowerCase().contains("shulker")) return;
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase(config.getString("worldname")) && event.getBlock().getType().name().toLowerCase().contains("shulker")) {
            Auth.RegisterPlacedShulker(event.getPlayer(), event.getBlock());
            MessageManager.SendMessage(event.getPlayer(), "shulkerplaced");
        }
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) throws IOException {
        if (!event.getBlock().getType().name().toLowerCase().contains("shulker")) return;
        FileConfiguration config = PrivateShulkers.config;
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase(config.getString("worldname"))) {
            if (Auth.CheckPermission(event.getPlayer(), event.getBlock())) {
                Auth.UnRegisterShulker(event.getBlock());
                MessageManager.SendMessage(event.getPlayer(), "shulkerbroken");
                if (config.getBoolean("auto-pickup-shulkers")) {
                    Block block = event.getBlock();
                    Player player = event.getPlayer();
                    Inventory playerInv = player.getInventory();
                    ItemStack itemStack = new ItemStack(block.getType(), 1);
                    HashMap<Integer, ItemStack> unaddedItems = playerInv.addItem(itemStack);
                    if (!unaddedItems.isEmpty()) {
                        for (ItemStack unadded : unaddedItems.values()) {
                            block.getWorld().dropItemNaturally(block.getLocation(), unadded);
                        }
                    }
                    event.setDropItems(false);
                }
            } else {
                MessageManager.SendMessage(event.getPlayer(), "cantbreakshulker");
                event.setCancelled(true);
            }
        }
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockExplode(EntityExplodeEvent event) {
        FileConfiguration config = PrivateShulkers.config;
        if (event.getLocation().getWorld().getName().equalsIgnoreCase(config.getString("worldname"))) {
            for (Block block : event.blockList()) {
                if (block.getType().name().toLowerCase().contains("shulker")) {
                    event.blockList().remove(block);
                    break;
                }
            }
        }
    }
    @EventHandler
    public void onHopperMoveItem(InventoryMoveItemEvent event) {
        FileConfiguration config = PrivateShulkers.config;
        if (event.getSource().getLocation().getWorld().getName().equals(config.getString("worldname"))) event.setCancelled(true);
    }
}
