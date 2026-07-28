package com.icu.iptmutechat.listener;

import com.icu.iptmutechat.IPTMUTECHAT;
import com.icu.iptmutechat.ip.IpRecordManager;
import com.icu.iptmutechat.ip.SimilarIpManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    private final IPTMUTECHAT plugin;
    private final IpRecordManager ipRecordManager;
    private final SimilarIpManager similarIpManager;

    public ConnectionListener(IPTMUTECHAT plugin) {
        this.plugin = plugin;
        this.ipRecordManager = plugin.getIpRecordManager();
        this.similarIpManager = plugin.getSimilarIpManager();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        String ip = event.getPlayer().getAddress().getAddress().getHostAddress();

        ipRecordManager.recordPlayerIp(playerName, ip);
        similarIpManager.analyzeAndAdd(ip, playerName);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getCooldownManager().cleanupPlayer(event.getPlayer().getUniqueId());
    }
}
