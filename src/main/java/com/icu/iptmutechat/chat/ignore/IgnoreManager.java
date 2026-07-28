package com.icu.iptmutechat.chat.ignore;
import com.icu.iptmutechat.IPTMUTECHAT;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
public class IgnoreManager {
    private final IPTMUTECHAT plugin;
    private final ConcurrentHashMap<UUID, CopyOnWriteArraySet<UUID>> ignoreList = new ConcurrentHashMap<>();
    private File dataFile;
    private FileConfiguration dataConfig;

    public IgnoreManager(IPTMUTECHAT plugin) {
        this.plugin = plugin;
        loadData();
    }
    public boolean addIgnore(UUID ignorerUuid, UUID ignoredUuid) {
        ignoreList.computeIfAbsent(ignorerUuid, k -> new CopyOnWriteArraySet<>());
        boolean added = ignoreList.get(ignorerUuid).add(ignoredUuid);
        if (added) saveData();
        return added;
    }
    public boolean removeIgnore(UUID ignorerUuid, UUID ignoredUuid) {
        CopyOnWriteArraySet<UUID> ignored = ignoreList.get(ignorerUuid);
        if (ignored == null) return false;
        boolean removed = ignored.remove(ignoredUuid);
        if (removed && ignored.isEmpty()) { ignoreList.remove(ignorerUuid); saveData(); }
        else if (removed) { saveData(); }
        return removed;
    }
    public boolean isIgnored(UUID ignorerUuid, UUID ignoredUuid) {
        CopyOnWriteArraySet<UUID> ignored = ignoreList.get(ignorerUuid);
        return ignored != null && ignored.contains(ignoredUuid);
    }
    public Set<UUID> getIgnoredPlayers(UUID playerUuid) {
        return ignoreList.getOrDefault(playerUuid, new CopyOnWriteArraySet<>());
    }
    private void loadData() {
        dataFile = new File(plugin.getDataFolder(), "ignore_list.yml");
        if (!dataFile.exists()) {
            try { plugin.getDataFolder().mkdirs(); dataFile.createNewFile(); } catch (IOException e) { return; }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        ConfigurationSection section = dataConfig.getConfigurationSection("ignores");
        if (section != null) {
            for (String ignorerStr : section.getKeys(false)) {
                try {
                    UUID ignorerUuid = UUID.fromString(ignorerStr);
                    CopyOnWriteArraySet<UUID> ignoredUuids = new CopyOnWriteArraySet<>();
                    for (String ignoredStr : section.getStringList(ignorerStr)) {
                        try { ignoredUuids.add(UUID.fromString(ignoredStr)); } catch (IllegalArgumentException e) {}
                    }
                    if (!ignoredUuids.isEmpty()) ignoreList.put(ignorerUuid, ignoredUuids);
                } catch (IllegalArgumentException e) {}
            }
        }
    }
    public synchronized void saveData() {
        if (dataConfig == null) return;
        dataConfig.set("ignores", null);
        for (java.util.Map.Entry<UUID, CopyOnWriteArraySet<UUID>> entry : ignoreList.entrySet()) {
            java.util.List<String> lst = entry.getValue().stream().map(UUID::toString).collect(java.util.stream.Collectors.toList());
            dataConfig.set("ignores." + entry.getKey().toString(), lst);
        }
        try { dataConfig.save(dataFile); } catch (IOException e) {}
    }
}
