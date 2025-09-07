package com.hypergems.recipes;

import com.hypergems.HyperGems;
import com.hypergems.config.GemConfigManager;
import com.hypergems.config.GemConfigManager.GemData;
import com.hypergems.gems.GemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Map;

public class RecipeManager {

    private final HyperGems plugin;
    private final GemConfigManager gemConfig;

    public RecipeManager(HyperGems plugin, GemConfigManager gemConfig) {
        this.plugin = plugin;
        this.gemConfig = gemConfig;
    }

    public void registerRecipes() {
        for (GemData data : gemConfig.getGems().values()) {
            ItemStack result = plugin.getGemManager().createGem(data.getId());
            if (result == null) continue;

            NamespacedKey key = new NamespacedKey(plugin, "gem_" + data.getId());
            ShapedRecipe recipe = new ShapedRecipe(key, result);

            if (data.getShape() != null && !data.getShape().isEmpty()) {
                recipe.shape(data.getShape().toArray(new String[0]));

                for (Map.Entry<String, String> entry : data.getIngredients().entrySet()) {
                    Material mat = Material.matchMaterial(entry.getValue());
                    if (mat != null) {
                        recipe.setIngredient(entry.getKey().charAt(0), mat);
                    }
                }

                Bukkit.addRecipe(recipe);
            }
        }
    }
}
