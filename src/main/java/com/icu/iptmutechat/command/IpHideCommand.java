package com.icu.iptmutechat.command;

import com.icu.iptmutechat.IPTMUTECHAT;
import com.icu.iptmutechat.config.ConfigManager;
import com.icu.iptmutechat.ip.IpRecordManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IpHideCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final IpRecordManager ipRecordManager;

    public IpHideCommand(IPTMUTECHAT plugin) {
        this.configManager = plugin.getConfigManager();
        this.ipRecordManager = plugin.getIpRecordManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("iptmutechat.iphide")) {
            sender.sendMessage(configManager.getPrefixedIpInfoMessage("no-permission"));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(configManager.getPrefixedIpInfoMessage("ip-hide-player-only"));
            return true;
        }

        boolean hidden = ipRecordManager.togglePlayerHidden(player.getName());
        sender.sendMessage(configManager.getPrefixedIpInfoMessage(
                hidden ? "ip-hide-enabled" : "ip-hide-disabled"));
        return true;
    }
}
