package com.icu.iptmutechat.chat.cooldown;
import com.icu.iptmutechat.IPTMUTECHAT;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
public class CooldownManager {
    private final IPTMUTECHAT plugin;
    private final ConcurrentHashMap<UUID, Long> lastChatTime = new ConcurrentHashMap<>();
    public CooldownManager(IPTMUTECHAT plugin) { this.plugin = plugin; }
    public boolean canChat(UUID playerUuid) {
        long cooldownMs = plugin.getConfigManager().getCooldownSeconds() * 1000L;
        Long lastTime = lastChatTime.get(playerUuid);
        if (lastTime == null) return true;
        return System.currentTimeMillis() - lastTime >= cooldownMs;
    }
    public void updateLastChat(UUID playerUuid) { lastChatTime.put(playerUuid, System.currentTimeMillis()); }
    public int getRemainingCooldown(UUID playerUuid) {
        long cooldownMs = plugin.getConfigManager().getCooldownSeconds() * 1000L;
        Long lastTime = lastChatTime.get(playerUuid);
        if (lastTime == null) return 0;
        long remaining = cooldownMs - (System.currentTimeMillis() - lastTime);
        return (int) Math.ceil(remaining / 1000.0);
    }
    public void cleanupPlayer(UUID playerUuid) { lastChatTime.remove(playerUuid); }
}
