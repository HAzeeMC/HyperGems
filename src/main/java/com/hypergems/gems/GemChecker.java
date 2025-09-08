package com.hypergems.gems;

import com.hypergems.HyperGems;
import com.hypergems.config.GemConfigManager.GemData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class GemChecker implements Runnable {

    private final HyperGems plugin;
    private final GemManager gemManager;

    public GemChecker(HyperGems plugin, GemManager gemManager) {
        this.plugin = plugin;
        this.gemManager = gemManager;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            // Check cả main hand và off hand
            applyEffects(player, player.getInventory().getItemInMainHand());
            applyEffects(player, player.getInventory().getItemInOffHand());
        }
    }

    private void applyEffects(Player player, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return;

        String gemId = gemManager.getGemId(item);
        if (gemId == null) return;

        GemData data = plugin.getGemConfig().getGem(gemId);
        if (data == null) return;

        for (Map<String, Object> effect : data.getEffects()) {
            try {
                String typeName = (String) effect.get("type");
                PotionEffectType type = PotionEffectType.getByName(typeName);
                if (type == null) continue;

                int duration = (int) effect.getOrDefault("duration", 200);
                int amplifier = (int) effect.getOrDefault("amplifier", 0);

                PotionEffect potion = new PotionEffect(type, duration, amplifier, true, false, true);
                player.addPotionEffect(potion);
            } catch (Exception ignored) {
            }
        }
    }
}
