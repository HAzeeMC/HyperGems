package com.hypergems.gems;

import com.hypergems.HyperGems;
import com.hypergems.config.GemConfigManager;
import com.hypergems.config.GemConfigManager.GemData;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class GemManager {

    private final HyperGems plugin;
    private final NamespacedKey gemKey;
    private final GemConfigManager gemConfig;

    public GemManager(HyperGems plugin, NamespacedKey gemKey, GemConfigManager gemConfig) {
        this.plugin = plugin;
        this.gemKey = gemKey;
        this.gemConfig = gemConfig;
    }

    public ItemStack createGem(String id) {
        GemData data = gemConfig.getGem(id);
        if (data == null) return null;

        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();

        String name = ChatColor.translateAlternateColorCodes('&',
                (data.getPrefix() != null ? data.getPrefix() + " " : "") + data.getDisplay());
        meta.displayName(Component.text(name));

        List<String> lore = data.getLore().stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .toList();
        meta.lore(lore.stream().map(Component::text).toList());

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(gemKey, PersistentDataType.STRING, id);

        item.setItemMeta(meta);
        return item;
    }

    public String getGemId(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().get(gemKey, PersistentDataType.STRING);
    }

    public void cleanupAll() {
        // Sau này nếu có task cần huỷ thì xử lý ở đây
    }
}
