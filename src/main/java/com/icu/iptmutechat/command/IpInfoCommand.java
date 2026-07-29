package com.icu.iptmutechat.command;

import com.icu.iptmutechat.IPTMUTECHAT;
import com.icu.iptmutechat.config.ConfigManager;
import com.icu.iptmutechat.ip.IpRecordManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class IpInfoCommand implements CommandExecutor, TabCompleter {

    private final IPTMUTECHAT plugin;
    private final ConfigManager configManager;
    private final IpRecordManager ipRecordManager;

    public IpInfoCommand(IPTMUTECHAT plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.ipRecordManager = plugin.getIpRecordManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("iptmutechat.ipinfo")) {
            sender.sendMessage(configManager.getPrefixedIpInfoMessage("no-permission"));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(configManager.getPrefixedIpInfoMessage("ip-info-usage"));
            return true;
        }

        String playerName = args[0];
        if (ipRecordManager.isPlayerHidden(playerName)) {
            sender.sendMessage(configManager.getPrefixedIpInfoAccentMessage(
                    "ip-info-hidden", "player", playerName, "#FACC15"));
            return true;
        }
        String latestIp = ipRecordManager.getPlayerLatestIp(playerName);
        if (latestIp == null) {
            sender.sendMessage(configManager.getPrefixedIpInfoAccentMessage(
                    "ip-info-not-found", "player", playerName, "#FACC15"));
            return true;
        }

        List<String> sameIpPlayers = ipRecordManager.getPlayersByIp(latestIp).stream()
                .filter(player -> !player.equalsIgnoreCase(playerName))
                .filter(player -> !ipRecordManager.isPlayerHidden(player))
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
        sender.sendMessage(configManager.getIpInfoSeparatorMessage("ip-info-header"));
        sender.sendMessage(configManager.getCenteredIpInfoMessage("ip-info-header", "ip-info-title"));
        sender.sendMessage(configManager.getCenteredIpInfoAccentMessage(
                "ip-info-header", "ip-info-player", "player", playerName, "#FACC15"));
        sender.sendMessage(configManager.getCenteredIpInfoAccentMessage(
                "ip-info-header", "ip-info-address", "ip", latestIp, "#EF4444"));
        sender.sendMessage(configManager.getCenteredIpInfoMessage(
                "ip-info-header", "ip-info-same-title", "count", String.valueOf(sameIpPlayers.size())));
        if (sameIpPlayers.isEmpty()) {
            sender.sendMessage(configManager.getCenteredIpInfoMessage("ip-info-header", "ip-info-same-empty"));
        } else {
            for (String player : sameIpPlayers) {
                sender.sendMessage(configManager.getCenteredIpInfoAccentMessage(
                        "ip-info-header", "ip-info-same-entry", "player", player, "#22C55E"));
            }
        }
        sender.sendMessage(configManager.getIpInfoSeparatorMessage("ip-info-footer"));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(partial))
                    .forEach(completions::add);
        }
        return completions;
    }
}
