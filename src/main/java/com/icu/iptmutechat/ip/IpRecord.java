package com.icu.iptmutechat.ip;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
public class IpRecord {
    private final String ip;
    private final ConcurrentHashMap<String, String> playerRecords = new ConcurrentHashMap<>();
    public IpRecord(String ip) { this.ip = ip; }
    public void addPlayer(String playerName, String timestamp) {
        String existingName = playerRecords.keySet().stream()
                .filter(name -> name.equalsIgnoreCase(playerName))
                .findFirst()
                .orElse(null);
        if (existingName != null && !existingName.equals(playerName)) {
            playerRecords.remove(existingName);
        }
        playerRecords.put(playerName, timestamp);
    }
    public String getIp() { return ip; }
    public Set<String> getPlayers() { return playerRecords.keySet(); }
    public String getPlayerLastTime(String playerName) {
        String timestamp = playerRecords.get(playerName);
        if (timestamp != null) return timestamp;
        return playerRecords.entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(playerName))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }
    public Map<String, String> getAllPlayerRecords() { return playerRecords; }
    public int getPlayerCount() { return playerRecords.size(); }
}
