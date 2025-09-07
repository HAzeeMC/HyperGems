package com.hypergems.data;

import com.hypergems.HyperGems;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerDataManager {

    private final HyperGems plugin;
    private final Set<UUID> received;

    public PlayerDataManager(HyperGems plugin) {
        this.plugin = plugin;
        this.received = new HashSet<>();

        FileConfiguration config = plugin.getConfig();
        for (String uuid : config.getStringList("received")) {
            try {
                received.add(UUID.fromString(uuid));
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    public boolean hasReceived(UUID uuid) {
        return received.contains(uuid);
    }

    public void markReceived(UUID uuid) {
        if (received.add(uuid)) {
            save();
        }
    }

    public void save() {
        plugin.getConfig().set("received", received.stream().map(UUID::toString).toList());
        plugin.saveConfig();
    }
}
