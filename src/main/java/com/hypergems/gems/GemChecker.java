package com.hypergems.gems;

import com.hypergems.HyperGems;
import com.hypergems.config.GemConfigManager.GemData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
            String gemId = gemManager.getGemId(player.getInventory().getItemInMainHand());
            if (gemId == null) continue;

            GemData data = plugin.getGemConfig().getGem(gemId);
            if (data == null) continue;

            for (Map<String, Object> effect : data.getEffects()) {
                try {
                    PotionEffectType type = PotionEffectType.getByName((String) effect.get("type"));
                    if (type == null) continue;

                    int duration = (int) effect.getOrDefault("duration", 40);
                    int amplifier = (int) effect.getOrDefault("amplifier", 0);

                    player.addPotionEffect(new PotionEffect(type, duration, amplifier, true, false));
                } catch (Exception ignored) {
                }
            }
        }
    }
}
