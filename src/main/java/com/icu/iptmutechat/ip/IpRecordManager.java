package com.icu.iptmutechat.ip;
import com.icu.iptmutechat.IPTMUTECHAT;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class IpRecordManager {
    private final IPTMUTECHAT plugin;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ConcurrentHashMap<String, IpRecord> ipRecords = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Set<String>> playerIps = new ConcurrentHashMap<>();
    private final Set<String> hiddenPlayers = ConcurrentHashMap.newKeySet();
    private File dataFile;
    private FileConfiguration dataConfig;

    public IpRecordManager(IPTMUTECHAT plugin) {
        this.plugin = plugin;
        loadData();
    }
    public void recordPlayerIp(String playerName, String ip) {
        String timestamp = LocalDateTime.now().format(formatter);
        IpRecord record = ipRecords.computeIfAbsent(ip, k -> new IpRecord(ip));
        record.addPlayer(playerName, timestamp);
        playerIps.computeIfAbsent(playerName.toLowerCase(Locale.ROOT), k -> ConcurrentHashMap.newKeySet()).add(ip);
        saveData();
    }
    public Set<String> getPlayerIps(String playerName) { return playerIps.getOrDefault(playerName.toLowerCase(Locale.ROOT), Collections.emptySet()); }
    public String getPlayerLatestIp(String playerName) {
        Set<String> ips = getPlayerIps(playerName);
        if (ips.isEmpty()) return null;
        String latestIp = null;
        String latestTime = null;
        for (String ip : ips) {
            IpRecord record = ipRecords.get(ip);
            if (record != null) {
                String time = record.getPlayerLastTime(playerName);
                if (time != null && (latestTime == null || time.compareTo(latestTime) > 0)) {
                    latestTime = time; latestIp = ip;
                }
            }
        }
        return latestIp;
    }
    public Set<String> getPlayersByIp(String ip) {
        IpRecord record = ipRecords.get(ip);
        if (record == null) return Collections.emptySet();
        Set<String> players = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        players.addAll(record.getPlayers());
        return players;
    }
    public boolean togglePlayerHidden(String playerName) {
        String normalizedName = playerName.toLowerCase(Locale.ROOT);
        boolean hidden;
        if (hiddenPlayers.remove(normalizedName)) {
            hidden = false;
        } else {
            hiddenPlayers.add(normalizedName);
            hidden = true;
        }
        saveData();
        return hidden;
    }
    public boolean isPlayerHidden(String playerName) {
        return hiddenPlayers.contains(playerName.toLowerCase(Locale.ROOT));
    }
    private void loadData() {
        File ipInfoFolder = new File(plugin.getDataFolder(), "ipinfo");
        dataFile = new File(ipInfoFolder, "ip_records.yml");
        if (!dataFile.exists()) {
            try { ipInfoFolder.mkdirs(); dataFile.createNewFile(); } catch (IOException e) { return; }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        for (String playerName : dataConfig.getStringList("hidden-players")) {
            hiddenPlayers.add(playerName.toLowerCase(Locale.ROOT));
        }
        ConfigurationSection ipSection = dataConfig.getConfigurationSection("ips");
        if (ipSection != null) {
            loadIpRecords(ipSection, new ArrayList<>());
        }
    }

    private void loadIpRecords(ConfigurationSection section, List<String> pathParts) {
        for (String key : section.getKeys(false)) {
            ConfigurationSection child = section.getConfigurationSection(key);
            if (child == null) continue;

            List<String> childPath = new ArrayList<>(pathParts);
            childPath.add(key);
            ConfigurationSection playersSection = child.getConfigurationSection("players");
            if (playersSection == null) {
                loadIpRecords(child, childPath);
                continue;
            }

            String ip = child.getString("address");
            if (ip == null || ip.isBlank()) {
                ip = String.join(".", childPath);
            }
            IpRecord record = ipRecords.computeIfAbsent(ip, IpRecord::new);
            for (String playerName : playersSection.getKeys(false)) {
                String timestamp = playersSection.getString(playerName);
                record.addPlayer(playerName, timestamp != null ? timestamp : "未知时间");
                playerIps.computeIfAbsent(playerName.toLowerCase(Locale.ROOT), ignored -> ConcurrentHashMap.newKeySet()).add(ip);
            }
        }
    }

    private String encodeIp(String ip) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(ip.getBytes(StandardCharsets.UTF_8));
    }

    public synchronized void saveData() {
        if (dataConfig == null) return;
        dataConfig.set("hidden-players", hiddenPlayers.stream().sorted().toList());
        dataConfig.set("ips", null);
        for (Map.Entry<String, IpRecord> entry : ipRecords.entrySet()) {
            String basePath = "ips." + encodeIp(entry.getKey()) + ".";
            dataConfig.set(basePath + "address", entry.getKey());
            for (String playerName : entry.getValue().getPlayers()) {
                dataConfig.set(basePath + "players." + playerName, entry.getValue().getPlayerLastTime(playerName));
            }
        }
        try { dataConfig.save(dataFile); } catch (IOException e) {}
    }
}
