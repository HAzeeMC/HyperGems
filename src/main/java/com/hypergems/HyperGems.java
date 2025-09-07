package com.hypergems;

import com.hypergems.commands.HyperGemsCommand;
import com.hypergems.config.GemConfigManager;
import com.hypergems.data.PlayerDataManager;
import com.hypergems.gems.*;
import com.hypergems.gui.GuiManager;
import com.hypergems.recipes.RecipeManager;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class HyperGems extends JavaPlugin {
    private static HyperGems instance;
    private NamespacedKey gemKey;

    private GemConfigManager gemConfig;
    private GemManager gemManager;
    private PlayerDataManager playerDataManager;
    private RecipeManager recipeManager;
    private GuiManager guiManager;

    @Override
    public void onEnable() {
        instance = this;
        this.gemKey = new NamespacedKey(this, "hypergem_id");

        saveDefaultConfig();

        this.gemConfig = new GemConfigManager(this);
        this.gemManager = new GemManager(this, gemKey, gemConfig);
        this.playerDataManager = new PlayerDataManager(this);
        this.recipeManager = new RecipeManager(this, gemConfig);
        this.guiManager = new GuiManager(this, gemConfig, gemManager);

        getServer().getPluginManager().registerEvents(new GemListener(this, gemManager, playerDataManager, guiManager), this);
        getServer().getPluginManager().registerEvents(guiManager, this);

        getCommand("hypergems").setExecutor(new HyperGemsCommand(this, gemConfig, gemManager, guiManager));

        getServer().getScheduler().runTaskTimer(this, new GemChecker(this, gemManager), 0L, 10L);

        recipeManager.registerRecipes();
    }

    @Override
    public void onDisable() {
        gemManager.cleanupAll();
        playerDataManager.save();
    }

    public static HyperGems get() {
        return instance;
    }

    public NamespacedKey getGemKey() {
        return gemKey;
    }
}
