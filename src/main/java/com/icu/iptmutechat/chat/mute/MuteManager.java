package com.icu.iptmutechat.chat.mute;
import com.icu.iptmutechat.IPTMUTECHAT;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class MuteManager {
    private final IPTMUTECHAT plugin;
    private final ConcurrentHashMap<UUID, MuteData> mutedPlayers = new ConcurrentHashMap<>();
    private File dataFile;
    private FileConfiguration dataConfig;

    public MuteManager(IPTMUTECHAT plugin) {
        this.plugin = plugin;
        loadData();
    }
    public void mutePlayer(UUID targetUuid, String muterName, long duration, String reason) {
        MuteData muteData = new MuteData(targetUuid, muterName, duration, reason);
        mutedPlayers.put(targetUuid, muteData);
        saveData();
    }
    public boolean unmutePlayer(UUID targetUuid) {
        if (mutedPlayers.remove(targetUuid) != null) {
            saveData();
            return true;
        }
        return false;
    }
    public boolean isMuted(UUID playerUuid) {
        MuteData data = mutedPlayers.get(playerUuid);
        if (data == null) return false;
        if (data.isExpired()) {
            mutedPlayers.remove(playerUuid);
            saveData();
            return false;
        }
        return true;
    }
    public MuteData getMuteData(UUID playerUuid) {
        MuteData data = mutedPlayers.get(playerUuid);
        if (data != null && data.isExpired()) {
            mutedPlayers.remove(playerUuid);
            saveData();
            return null;
        }
        return data;
    }
    private void loadData() {
        dataFile = new File(plugin.getDataFolder(), "muted_players.yml");
        if (!dataFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                dataFile.createNewFile();
            } catch (IOException e) { return; }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        ConfigurationSection section = dataConfig.getConfigurationSection("mutes");
        if (section != null) {
            for (String uuidStr : section.getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    ConfigurationSection ps = section.getConfigurationSection(uuidStr);
                    MuteData data = new MuteData(uuid, ps.getString("muter", "Console"),
                            ps.getLong("mute_time", 0), ps.getLong("duration", -1), ps.getString("reason", "未指定"));
                    if (!data.isExpired()) mutedPlayers.put(uuid, data);
                } catch (IllegalArgumentException e) {}
            }
        }
    }
    public synchronized void saveData() {
        if (dataConfig == null) return;
        dataConfig.set("mutes", null);
        for (java.util.Map.Entry<UUID, MuteData> entry : mutedPlayers.entrySet()) {
            String path = "mutes." + entry.getKey().toString() + ".";
            dataConfig.set(path + "muter", entry.getValue().getMuterName());
            dataConfig.set(path + "mute_time", entry.getValue().getMuteTime());
            dataConfig.set(path + "duration", entry.getValue().getDuration());
            dataConfig.set(path + "reason", entry.getValue().getReason());
        }
        try { dataConfig.save(dataFile); } catch (IOException e) {}
    }
}
