package com.hypergems.gui;

import com.hypergems.HyperGems;
import com.hypergems.config.GemConfigManager;
import com.hypergems.config.GemConfigManager.GemData;
import com.hypergems.gems.GemManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class GuiManager implements Listener {

    private final HyperGems plugin;
    private final GemConfigManager gemConfig;
    private final GemManager gemManager;

    public GuiManager(HyperGems plugin, GemConfigManager gemConfig, GemManager gemManager) {
        this.plugin = plugin;
        this.gemConfig = gemConfig;
        this.gemManager = gemManager;
    }

    public Inventory createRecipeGui(Player player) {
        int size = 9 * ((gemConfig.getGems().size() / 9) + 1);
        Inventory gui = Bukkit.createInventory(null, size, ChatColor.GREEN + "HyperGems Recipes");

        int i = 0;
        for (Map.Entry<String, GemData> entry : gemConfig.getGems().entrySet()) {
            ItemStack gemItem = gemManager.createGem(entry.getKey());
            if (gemItem != null) {
                gui.setItem(i++, gemItem);
            }
        }

        return gui;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.GREEN + "HyperGems Recipes")) {
            event.setCancelled(true);

            if (!(event.getWhoClicked() instanceof Player player)) return;
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null) return;

            String gemId = gemManager.getGemId(clicked);
            if (gemId == null) return;

            if (player.hasPermission("hypergems.give") || player.hasPermission("hypergems.admin")) {
                player.getInventory().addItem(gemManager.createGem(gemId));
                player.sendMessage("§aBạn đã lấy gem §e" + gemId);
            } else {
                player.sendMessage("§cBạn không có quyền lấy gem từ GUI.");
            }
        }
    }
}
