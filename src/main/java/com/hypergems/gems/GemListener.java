package com.hypergems.gems;

import com.hypergems.HyperGems;
import com.hypergems.data.PlayerDataManager;
import com.hypergems.gui.GuiManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class GemListener implements Listener {

    private final HyperGems plugin;
    private final GemManager gemManager;
    private final PlayerDataManager playerDataManager;
    private final GuiManager guiManager;

    public GemListener(HyperGems plugin, GemManager gemManager, PlayerDataManager playerDataManager, GuiManager guiManager) {
        this.plugin = plugin;
        this.gemManager = gemManager;
        this.playerDataManager = playerDataManager;
        this.guiManager = guiManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (playerDataManager.hasReceived(player.getUniqueId())) return;

        // Random một gem
        List<String> gemIds = plugin.getGemConfig().getGems().keySet().stream().toList();
        if (gemIds.isEmpty()) return;

        String randomGem = gemIds.get((int) (Math.random() * gemIds.size()));
        player.getInventory().addItem(gemManager.createGem(randomGem));
        player.sendMessage("§aBạn đã nhận được gem §e" + randomGem + " §alần đầu vào server!");

        playerDataManager.markReceived(player.getUniqueId());
    }
}
