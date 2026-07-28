package com.icu.iptmutechat.listener;

import com.icu.iptmutechat.IPTMUTECHAT;
import com.icu.iptmutechat.config.ConfigManager;
import com.icu.iptmutechat.chat.cooldown.CooldownManager;
import com.icu.iptmutechat.chat.ignore.IgnoreManager;
import com.icu.iptmutechat.chat.mute.MuteData;
import com.icu.iptmutechat.chat.mute.MuteManager;
import com.icu.iptmutechat.chat.reply.ReplyManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.UUID;

public class ChatListener implements Listener {

    private final IPTMUTECHAT plugin;
    private final ConfigManager configManager;
    private final CooldownManager cooldownManager;
    private final MuteManager muteManager;
    private final IgnoreManager ignoreManager;
    private final ReplyManager replyManager;

    public ChatListener(IPTMUTECHAT plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.cooldownManager = plugin.getCooldownManager();
        this.muteManager = plugin.getMuteManager();
        this.ignoreManager = plugin.getIgnoreManager();
        this.replyManager = plugin.getReplyManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();

        if (!player.hasPermission("iptmutechat.bypass.muted") && muteManager.isMuted(playerUuid)) {
            event.setCancelled(true);
            MuteData muteData = muteManager.getMuteData(playerUuid);
            sendMuteMessage(player, muteData);
            return;
        }

        if (!player.hasPermission("iptmutechat.bypass.cooldown")) {
            if (!cooldownManager.canChat(playerUuid)) {
                event.setCancelled(true);
                int remaining = cooldownManager.getRemainingCooldown(playerUuid);
                player.sendMessage(configManager.getPrefixedMessage("cooldown", "time", String.valueOf(remaining)));
                return;
            }
            cooldownManager.updateLastChat(playerUuid);
        }
        handleIgnore(event, player);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String[] parts = event.getMessage().substring(1).split(" ");
        String command = parts[0].toLowerCase();

        if (!ConfigManager.CHAT_COMMANDS.contains(command)) return;

        UUID playerUuid = player.getUniqueId();
        if (!player.hasPermission("iptmutechat.bypass.muted") && muteManager.isMuted(playerUuid)) {
            event.setCancelled(true);
            MuteData muteData = muteManager.getMuteData(playerUuid);
            sendMuteMessage(player, muteData);
            return;
        }

        if (!player.hasPermission("iptmutechat.bypass.cooldown")) {
            if (!cooldownManager.canChat(playerUuid)) {
                event.setCancelled(true);
                int remaining = cooldownManager.getRemainingCooldown(playerUuid);
                player.sendMessage(configManager.getPrefixedMessage("cooldown", "time", String.valueOf(remaining)));
                return;
            }
            cooldownManager.updateLastChat(playerUuid);
        }

        if (isPrivateMessageCommand(command) && parts.length >= 2) {
            handlePrivateMessageIgnore(event, player, parts[1]);
        }
    }

    private boolean isPrivateMessageCommand(String command) {
        return command.equals("msg") || command.equals("tell") || command.equals("w") || command.equals("whisper");
    }

    private void handlePrivateMessageIgnore(PlayerCommandPreprocessEvent event, Player sender, String targetName) {
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) return;
        if (ignoreManager.isIgnored(target.getUniqueId(), sender.getUniqueId())) {
            event.setCancelled(true);
            sender.sendMessage(configManager.getMessage(
                    "private-message-sent", "player", target.getName(), "message", "..."));
        } else {
            replyManager.recordPrivateMessage(sender.getUniqueId(), target.getUniqueId());
        }
    }

    private void sendMuteMessage(Player player, MuteData muteData) {
        if (muteData == null) {
            player.sendMessage(configManager.getPrefixedMessage("muted-no-reason"));
            return;
        }
        String reason = configManager.localizeReason(muteData.getReason());
        player.sendMessage(configManager.getPrefixedMessage("muted", "reason", reason));
        if (muteData.isPermanent()) {
            player.sendMessage(configManager.getPrefixedMessage("mute-type-permanent-notice"));
        } else {
            player.sendMessage(configManager.getPrefixedMessage(
                    "mute-ends-in-notice", "time", configManager.formatDuration(muteData.getRemainingTime())));
        }
    }

    private void handleIgnore(AsyncPlayerChatEvent event, Player sender) {
        UUID senderUuid = sender.getUniqueId();
        event.getRecipients().removeIf(recipient -> ignoreManager.isIgnored(recipient.getUniqueId(), senderUuid));
    }
}
