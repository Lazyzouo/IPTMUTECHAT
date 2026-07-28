package com.icu.iptmutechat.ip;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
public class SimilarIpGroup {
    private final String ipPrefix;
    private final ConcurrentHashMap<String, Set<String>> ipPlayers = new ConcurrentHashMap<>();
    public SimilarIpGroup(String ipPrefix) { this.ipPrefix = ipPrefix; }
    public void addIpRecord(String ip, String playerName) {
        ipPlayers.computeIfAbsent(ip, k -> new CopyOnWriteArraySet<>()).add(playerName);
    }
    public Set<String> getIps() { return ipPlayers.keySet(); }
    public Set<String> getPlayersByIp(String ip) { return ipPlayers.getOrDefault(ip, Set.of()); }
}
