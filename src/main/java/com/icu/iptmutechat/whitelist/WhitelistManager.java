package com.icu.iptmutechat.whitelist;

import com.icu.iptmutechat.IPTMUTECHAT;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class WhitelistManager {

    private final IPTMUTECHAT plugin;
    private final Map<UUID, String> whitelistedPlayers = new ConcurrentHashMap<>();
    private File dataFile;
    private FileConfiguration dataConfig;

    public WhitelistManager(IPTMUTECHAT plugin) {
        this.plugin = plugin;
        loadData();
    }

    public boolean isWhitelisted(UUID playerUuid) {
        return whitelistedPlayers.containsKey(playerUuid);
    }

    public boolean addPlayer(UUID playerUuid, String playerName) {
        if (whitelistedPlayers.putIfAbsent(playerUuid, playerName) != null) {
            return false;
        }
        saveData();
        return true;
    }

    public boolean removePlayer(String playerName) {
        UUID playerUuid = whitelistedPlayers.entrySet().stream()
                .filter(entry -> entry.getValue().equalsIgnoreCase(playerName))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        if (playerUuid == null) {
            return false;
        }
        whitelistedPlayers.remove(playerUuid);
        saveData();
        return true;
    }

    public List<String> getPlayerNames() {
        return whitelistedPlayers.values().stream()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    private void loadData() {
        dataFile = new File(plugin.getDataFolder(), "whitelist.yml");
        if (!dataFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Unable to create whitelist.yml", e);
                return;
            }
        }

        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        ConfigurationSection section = dataConfig.getConfigurationSection("players");
        if (section == null) {
            return;
        }
        for (String uuidText : section.getKeys(false)) {
            try {
                UUID playerUuid = UUID.fromString(uuidText);
                String playerName = section.getString(uuidText + ".name");
                if (playerName != null && !playerName.isBlank()) {
                    whitelistedPlayers.put(playerUuid, playerName);
                }
            } catch (IllegalArgumentException ignored) {
                plugin.getLogger().warning("Ignored invalid whitelist UUID: " + uuidText);
            }
        }
    }

    public synchronized void saveData() {
        if (dataConfig == null) {
            return;
        }
        dataConfig.set("players", null);
        whitelistedPlayers.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, String.CASE_INSENSITIVE_ORDER))
                .forEach(entry -> dataConfig.set(
                        "players." + entry.getKey() + ".name", entry.getValue()));
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Unable to save whitelist.yml", e);
        }
    }
}
