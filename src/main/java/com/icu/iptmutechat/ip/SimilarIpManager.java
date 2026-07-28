package com.icu.iptmutechat.ip;
import com.icu.iptmutechat.IPTMUTECHAT;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimilarIpManager {
    private final IPTMUTECHAT plugin;
    private final ConcurrentHashMap<String, SimilarIpGroup> similarGroups = new ConcurrentHashMap<>();
    private File dataFile;
    private FileConfiguration dataConfig;

    public SimilarIpManager(IPTMUTECHAT plugin) {
        this.plugin = plugin;
        loadData();
    }
    public void analyzeAndAdd(String ip, String playerName) {
        String ipPrefix = getIpPrefix(ip);
        if (ipPrefix == null) return;
        SimilarIpGroup group = similarGroups.computeIfAbsent(ipPrefix, k -> new SimilarIpGroup(ipPrefix));
        group.addIpRecord(ip, playerName);
        saveData();
    }
    private String getIpPrefix(String ip) {
        if (ip == null || ip.isEmpty()) return null;
        String[] parts = ip.split("\\.");
        if (parts.length < 3) return null;
        return parts[0] + "." + parts[1] + "." + parts[2];
    }
    public Set<String> getSimilarPlayers(String playerName, String ip) {
        String ipPrefix = getIpPrefix(ip);
        if (ipPrefix == null) return Collections.emptySet();
        SimilarIpGroup group = similarGroups.get(ipPrefix);
        if (group == null) return Collections.emptySet();
        Set<String> players = new HashSet<>();
        for (String ipInGroup : group.getIps()) {
            if (!ipInGroup.equals(ip)) players.addAll(group.getPlayersByIp(ipInGroup));
        }
        players.remove(playerName);
        return players;
    }
    private void loadData() {
        File ipInfoFolder = new File(plugin.getDataFolder(), "ipinfo");
        dataFile = new File(ipInfoFolder, "similar_ips.yml");
        if (!dataFile.exists()) {
            try { ipInfoFolder.mkdirs(); dataFile.createNewFile(); } catch (IOException e) { return; }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        ConfigurationSection gs = dataConfig.getConfigurationSection("groups");
        if (gs != null) {
            for (String groupKey : gs.getKeys(false)) {
                ConfigurationSection gd = gs.getConfigurationSection(groupKey);
                if (gd != null) {
                    SimilarIpGroup group = new SimilarIpGroup(groupKey);
                    ConfigurationSection is = gd.getConfigurationSection("ips");
                    if (is != null) {
                        for (String ip : is.getKeys(false)) {
                            for (String player : is.getStringList(ip)) group.addIpRecord(ip, player);
                        }
                    }
                    similarGroups.put(groupKey, group);
                }
            }
        }
    }
    public synchronized void saveData() {
        if (dataConfig == null) return;
        dataConfig.set("groups", null);
        int groupIndex = 1;
        for (Map.Entry<String, SimilarIpGroup> entry : similarGroups.entrySet()) {
            String basePath = "groups.分组" + groupIndex + ".";
            dataConfig.set(basePath + "prefix", entry.getKey());
            SimilarIpGroup group = entry.getValue();
            for (String ip : group.getIps()) {
                dataConfig.set(basePath + "ips." + ip.replace(".", "_"), new ArrayList<>(group.getPlayersByIp(ip)));
            }
            groupIndex++;
        }
        try { dataConfig.save(dataFile); } catch (IOException e) {}
    }
}
