package com.hypergems.commands;

import com.hypergems.HyperGems;
import com.hypergems.config.GemConfigManager;
import com.hypergems.gems.GemManager;
import com.hypergems.gui.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HyperGemsCommand implements CommandExecutor {

    private final HyperGems plugin;
    private final GemConfigManager gemConfig;
    private final GemManager gemManager;
    private final GuiManager guiManager;

    public HyperGemsCommand(HyperGems plugin, GemConfigManager gemConfig, GemManager gemManager, GuiManager guiManager) {
        this.plugin = plugin;
        this.gemConfig = gemConfig;
        this.gemManager = gemManager;
        this.guiManager = guiManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "give":
                handleGive(sender, args);
                break;
            case "gui":
                handleGui(sender);
                break;
            case "reload":
                handleReload(sender);
                break;
            default:
                sendHelp(sender);
        }
        return true;
    }

    private void handleGive(CommandSender sender, String[] args) {
        if (!sender.hasPermission("hypergems.give") && !sender.hasPermission("hypergems.admin")) {
            sender.sendMessage("§cBạn không có quyền dùng lệnh này.");
            return;
        }

        if (args.length < 3) {
            sender.sendMessage("§cCách dùng: /hypergems give <player> <gemId>");
            return;
        }

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage("§cNgười chơi không online.");
            return;
        }

        String gemId = args[2];
        if (!gemConfig.getGems().containsKey(gemId)) {
            sender.sendMessage("§cGem không tồn tại: " + gemId);
            return;
        }

        target.getInventory().addItem(gemManager.createGem(gemId));
        sender.sendMessage("§aĐã cho gem §e" + gemId + " §acho " + target.getName());
    }

    private void handleGui(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cChỉ người chơi mới dùng được lệnh này.");
            return;
        }

        if (!sender.hasPermission("hypergems.gui") && !sender.hasPermission("hypergems.admin")) {
            sender.sendMessage("§cBạn không có quyền mở GUI recipes.");
            return;
        }

        player.openInventory(guiManager.createRecipeGui(player));
    }

    private void handleReload(CommandSender sender) {
        if (!sender.hasPermission("hypergems.admin")) {
            sender.sendMessage("§cBạn không có quyền reload.");
            return;
        }

        plugin.reloadConfig();
        gemConfig.reload();
        sender.sendMessage("§aHyperGems đã reload thành công.");
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6§lHyperGems Commands:");
        if (sender.hasPermission("hypergems.give") || sender.hasPermission("hypergems.admin")) {
            sender.sendMessage(" §e/hypergems give <player> <gemId> §7- Cho người chơi một gem");
        }
        if (sender.hasPermission("hypergems.gui") || sender.hasPermission("hypergems.admin")) {
            sender.sendMessage(" §e/hypergems gui §7- Mở GUI recipes");
        }
        if (sender.hasPermission("hypergems.admin")) {
            sender.sendMessage(" §e/hypergems reload §7- Reload plugin");
        }
    }
}
