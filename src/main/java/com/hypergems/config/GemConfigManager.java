package com.hypergems.config;

import com.hypergems.HyperGems;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class GemConfigManager {

    private final HyperGems plugin;
    private Map<String, GemData> gems;

    public GemConfigManager(HyperGems plugin) {
        this.plugin = plugin;
        load();
    }

    /**
     * Load gems từ config.yml
     */
    public void load() {
        gems = new HashMap<>();
        FileConfiguration config = plugin.getConfig();

        ConfigurationSection section = config.getConfigurationSection("gems");
        if (section == null) {
            plugin.getLogger().warning("Không tìm thấy mục 'gems' trong config.yml");
            return;
        }

        for (String id : section.getKeys(false)) {
            String display = section.getString(id + ".display", id);
            String prefix = section.getString(id + ".prefix", "");
            List<String> lore = section.getStringList(id + ".lore");

            // Effects
            List<Map<String, Object>> effects = new ArrayList<>();
            ConfigurationSection effectsSec = section.getConfigurationSection(id + ".effects");
            if (effectsSec != null) {
                for (String key : effectsSec.getKeys(false)) {
                    Map<String, Object> effectData = new HashMap<>();
                    effectData.put("type", effectsSec.getString(key + ".type"));
                    effectData.put("duration", effectsSec.getInt(key + ".duration", 40));
                    effectData.put("amplifier", effectsSec.getInt(key + ".amplifier", 0));
                    effects.add(effectData);
                }
            } else {
                // Also support list format
                List<Map<?, ?>> list = (List<Map<?, ?>>) section.getList(id + ".effects");
                if (list != null) {
                    for (Map<?, ?> map : list) {
                        Map<String, Object> effectData = new HashMap<>();
                        effectData.put("type", map.get("type"));
                        effectData.put("duration", map.get("duration"));
                        effectData.put("amplifier", map.get("amplifier"));
                        effects.add(effectData);
                    }
                }
            }

            // Recipe
            List<String> shape = section.getStringList(id + ".recipe.shape");
            Map<String, String> ingredients = new HashMap<>();
            ConfigurationSection ingSec = section.getConfigurationSection(id + ".recipe.ingredients");
            if (ingSec != null) {
                for (String key : ingSec.getKeys(false)) {
                    ingredients.put(key, ingSec.getString(key));
                }
            }

            gems.put(id, new GemData(id, display, prefix, lore, effects, shape, ingredients));
        }

        plugin.getLogger().info("Đã load " + gems.size() + " gems từ config.yml");
    }

    public void reload() {
        plugin.reloadConfig();
        load();
    }

    public Map<String, GemData> getGems() {
        return gems;
    }

    public GemData getGem(String id) {
        return gems.get(id);
    }

    /**
     * Dữ liệu gem
     */
    public static class GemData {
        private final String id;
        private final String display;
        private final String prefix;
        private final List<String> lore;
        private final List<Map<String, Object>> effects;
        private final List<String> shape;
        private final Map<String, String> ingredients;

        public GemData(String id, String display, String prefix, List<String> lore,
                       List<Map<String, Object>> effects, List<String> shape, Map<String, String> ingredients) {
            this.id = id;
            this.display = display;
            this.prefix = prefix;
            this.lore = lore;
            this.effects = effects;
            this.shape = shape;
            this.ingredients = ingredients;
        }

        public String getId() {
            return id;
        }

        public String getDisplay() {
            return display;
        }

        public String getPrefix() {
            return prefix;
        }

        public List<String> getLore() {
            return lore;
        }

        public List<Map<String, Object>> getEffects() {
            return effects;
        }

        public List<String> getShape() {
            return shape;
        }

        public Map<String, String> getIngredients() {
            return ingredients;
        }
    }
}
