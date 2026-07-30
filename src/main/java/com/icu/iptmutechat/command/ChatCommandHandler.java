package com.icu.iptmutechat.command;

import com.icu.iptmutechat.IPTMUTECHAT;
import com.icu.iptmutechat.config.ConfigManager;
import com.icu.iptmutechat.chat.mute.MuteData;
import com.icu.iptmutechat.chat.mute.MuteManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ChatCommandHandler implements CommandExecutor, TabCompleter {

    private final IPTMUTECHAT plugin;
    private final ConfigManager configManager;
    private final MuteManager muteManager;

    public ChatCommandHandler(IPTMUTECHAT plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.muteManager = plugin.getMuteManager();
    }

    public void registerCommands() {
        String[] cmds = {"iptmutechat", "chatreload", "chathelp", "mute", "unmute", "muteinfo", "ignore", "ignorelist", "forcesay", "reply", "r"};
        for (String cmd : cmds) {
            var c = plugin.getCommand(cmd);
            if (c != null) {
                c.setExecutor(this);
                if (!cmd.equals("ignorelist") && !cmd.equals("reply") && !cmd.equals("r")) {
                    c.setTabCompleter(this);
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmdName = command.getName().toLowerCase();
        switch (cmdName) {
            case "mute" -> handleMute(sender, args);
            case "unmute" -> handleUnmute(sender, args);
            case "muteinfo" -> handleMuteInfo(sender, args);
            case "ignore" -> handleIgnore(sender, args);
            case "ignorelist" -> handleIgnoreList(sender);
            case "forcesay" -> handleForceSay(sender, args);
            case "reply", "r" -> handleReply(sender, args);
            case "chatreload" -> reloadConfig(sender);
            case "chathelp" -> sendHelp(sender);
            case "iptmutechat" -> {
                if (args.length >= 1 && args[0].equalsIgnoreCase("whitelist")) {
                    handleWhitelist(sender, java.util.Arrays.copyOfRange(args, 1, args.length));
                } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                    reloadConfig(sender);
                } else {
                    sendHelp(sender);
                }
            }
            default -> sender.sendMessage(configManager.getPrefixedMessage("unknown-command"));
        }
        return true;
    }

    private void sendHelp(CommandSender sender) {
        boolean whitelistMember = sender instanceof Player player
                && plugin.getWhitelistManager().isWhitelisted(player.getUniqueId());
        sender.sendMessage(configManager.getSeparatorMessage("help-header"));
        sender.sendMessage(configManager.getStyledMessage("help-title",
                "name", plugin.getDescription().getName(),
                "version", plugin.getDescription().getVersion(),
                "author", String.join(", ", plugin.getDescription().getAuthors())));
        sender.sendMessage(configManager.getStyledMessage("help-divider"));
        sender.sendMessage(configManager.getStyledMessage("help-command"));
        sender.sendMessage(configManager.getStyledMessage("help-player-section"));
        if (sender.hasPermission("iptmutechat.ignore")) {
            sender.sendMessage(configManager.getStyledMessage("help-ignore"));
            sender.sendMessage(configManager.getStyledMessage("help-ignore-list"));
        }
        sender.sendMessage(configManager.getStyledMessage("help-reply"));

        if (sender.hasPermission("iptmutechat.ipinfo") || whitelistMember
                || sender.hasPermission("iptmutechat.mute")
                || sender.hasPermission("iptmutechat.unmute")
                || sender.hasPermission("iptmutechat.muteinfo")
                || sender.hasPermission("iptmutechat.forcesay")
                || sender.hasPermission("iptmutechat.iphide")
                || sender.hasPermission("iptmutechat.whitelist")
                || sender.hasPermission("iptmutechat.admin")) {
            sender.sendMessage(configManager.getStyledMessage("help-admin-section"));
        }
        if (sender.hasPermission("iptmutechat.ipinfo") || whitelistMember) {
            sender.sendMessage(configManager.getStyledMessage("help-ipinfo"));
        }
        if (sender.hasPermission("iptmutechat.iphide")) {
            sender.sendMessage(configManager.getStyledMessage("help-iphide"));
        }
        if (sender.hasPermission("iptmutechat.mute")) {
            sender.sendMessage(configManager.getStyledMessage("help-mute"));
        }
        if (sender.hasPermission("iptmutechat.unmute")) {
            sender.sendMessage(configManager.getStyledMessage("help-unmute"));
        }
        if (sender.hasPermission("iptmutechat.muteinfo")) {
            sender.sendMessage(configManager.getStyledMessage("help-muteinfo"));
        }
        if (sender.hasPermission("iptmutechat.forcesay")) {
            sender.sendMessage(configManager.getStyledMessage("help-forcesay"));
        }
        if (sender.hasPermission("iptmutechat.whitelist")) {
            sender.sendMessage(configManager.getStyledMessage("help-whitelist"));
        }
        if (sender.hasPermission("iptmutechat.admin")) {
            sender.sendMessage(configManager.getStyledMessage("help-reload"));
        }
        sender.sendMessage(configManager.getSeparatorMessage("help-footer"));
    }

    private void reloadConfig(CommandSender sender) {
        if (!sender.hasPermission("iptmutechat.admin")) {
            sender.sendMessage(configManager.getPrefixedMessage("no-permission"));
            return;
        }
        plugin.getConfigManager().reloadConfig();
        plugin.restoreMissingDataFiles();
        sender.sendMessage(configManager.getPrefixedMessage("reload-success"));
    }

    private void handleMute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("iptmutechat.mute")) {
            sender.sendMessage(configManager.getPrefixedMessage("no-permission"));
            return;
        }
        if (args.length < 1) {
            sender.sendMessage(configManager.getPrefixedMessage("mute-usage"));
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(configManager.getPrefixedMessage("player-not-found"));
            return;
        }
        UUID targetUuid = target.getUniqueId();
        if (plugin.getWhitelistManager().isWhitelisted(targetUuid)) {
            sender.sendMessage(configManager.getPrefixedMessage(
                    "whitelist-mute-denied", "player", target.getName()));
            return;
        }
        if (muteManager.isMuted(targetUuid)) {
            sender.sendMessage(configManager.getPrefixedMessage("already-muted"));
            return;
        }

        long durationMs = -1;
        String reason = configManager.getRawMessage("default-reason");
        int reasonStartIndex = 1;

        if (args.length >= 2) {
            try {
                durationMs = parseDuration(args[1]);
                reasonStartIndex = 2;
            } catch (IllegalArgumentException e) {
                durationMs = -1;
                reasonStartIndex = 1;
            }
        }
        boolean reasonProvided = args.length > reasonStartIndex;
        if (reasonProvided) {
            reason = String.join(" ", java.util.Arrays.copyOfRange(args, reasonStartIndex, args.length));
        }

        String muterName = sender.getName();
        muteManager.mutePlayer(targetUuid, muterName, durationMs, reason);
        String targetName = target.getName();

        if (durationMs == -1) {
            if (!reasonProvided) {
                sender.sendMessage(configManager.getPrefixedMessage("mute-target", "player", targetName));
            } else {
                sender.sendMessage(configManager.getPrefixedMessage("mute-target-with-reason", "player", targetName, "reason", reason));
            }
        } else {
            String timeStr = configManager.formatDuration(durationMs);
            sender.sendMessage(configManager.getPrefixedMessage("mute-target-with-time", "player", targetName, "time", timeStr));
        }

        if (target.isOnline()) {
            MuteData data = muteManager.getMuteData(targetUuid);
            if (data != null) {
                target.sendMessage(configManager.getPrefixedMessage(
                        "muted", "reason", configManager.localizeReason(data.getReason())));
                if (!data.isPermanent()) {
                    target.sendMessage(configManager.getPrefixedMessage(
                            "mute-duration-notice", "time", configManager.formatDuration(data.getRemainingTime())));
                }
            }
        }
    }

    private void handleWhitelist(CommandSender sender, String[] args) {
        if (!sender.hasPermission("iptmutechat.whitelist")) {
            sender.sendMessage(configManager.getPrefixedMessage("no-permission"));
            return;
        }
        if (args.length < 1) {
            sender.sendMessage(configManager.getPrefixedMessage("whitelist-usage"));
            return;
        }

        switch (args[0].toLowerCase()) {
            case "add" -> addWhitelistPlayer(sender, args);
            case "remove" -> removeWhitelistPlayer(sender, args);
            case "list" -> sendWhitelist(sender);
            default -> sender.sendMessage(configManager.getPrefixedMessage("whitelist-usage"));
        }
    }

    @SuppressWarnings("deprecation")
    private void addWhitelistPlayer(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(configManager.getPrefixedMessage("whitelist-usage"));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if (!target.isOnline() && !target.hasPlayedBefore()) {
            sender.sendMessage(configManager.getPrefixedMessage(
                    "whitelist-player-not-found", "player", args[1]));
            return;
        }
        String targetName = target.getName() != null ? target.getName() : args[1];
        if (!plugin.getWhitelistManager().addPlayer(target.getUniqueId(), targetName)) {
            sender.sendMessage(configManager.getPrefixedMessage(
                    "whitelist-already-added", "player", targetName));
            return;
        }

        muteManager.unmutePlayer(target.getUniqueId());
        sender.sendMessage(configManager.getPrefixedMessage(
                "whitelist-added", "player", targetName));
    }

    private void removeWhitelistPlayer(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(configManager.getPrefixedMessage("whitelist-usage"));
            return;
        }
        if (!plugin.getWhitelistManager().removePlayer(args[1])) {
            sender.sendMessage(configManager.getPrefixedMessage(
                    "whitelist-not-found", "player", args[1]));
            return;
        }
        sender.sendMessage(configManager.getPrefixedMessage(
                "whitelist-removed", "player", args[1]));
    }

    private void sendWhitelist(CommandSender sender) {
        List<String> playerNames = plugin.getWhitelistManager().getPlayerNames();
        sender.sendMessage(configManager.getSeparatorMessage("whitelist-list-header"));
        sender.sendMessage(configManager.getMessage(
                "whitelist-list-title",
                "count", String.valueOf(playerNames.size())));
        if (playerNames.isEmpty()) {
            sender.sendMessage(configManager.getMessage("whitelist-list-empty"));
        } else {
            for (String playerName : playerNames) {
                sender.sendMessage(configManager.getMessage(
                        "whitelist-list-entry",
                        "player", playerName));
            }
        }
        sender.sendMessage(configManager.getSeparatorMessage("whitelist-list-footer"));
    }

    private void handleUnmute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("iptmutechat.unmute")) {
            sender.sendMessage(configManager.getPrefixedMessage("no-permission"));
            return;
        }
        if (args.length < 1) {
            sender.sendMessage(configManager.getPrefixedMessage("unmute-usage"));
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(configManager.getPrefixedMessage("player-not-found"));
            return;
        }
        if (muteManager.unmutePlayer(target.getUniqueId())) {
            sender.sendMessage(configManager.getPrefixedMessage("unmute-target", "player", target.getName()));
            target.sendMessage(configManager.getPrefixedMessage("unmuted-player"));
        } else {
            sender.sendMessage(configManager.getPrefixedMessage("not-muted"));
        }
    }

    private void handleMuteInfo(CommandSender sender, String[] args) {
        if (!sender.hasPermission("iptmutechat.muteinfo")) {
            sender.sendMessage(configManager.getPrefixedMessage("no-permission"));
            return;
        }
        if (args.length < 1) {
            sender.sendMessage(configManager.getPrefixedMessage("muteinfo-usage"));
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(configManager.getPrefixedMessage("player-not-found"));
            return;
        }
        MuteData data = muteManager.getMuteData(target.getUniqueId());
        if (data == null) {
            sender.sendMessage(configManager.getPrefixedMessage("not-muted"));
            return;
        }

        sender.sendMessage(configManager.getSeparatorMessage("mute-info-header"));
        sender.sendMessage(configManager.getMessage("mute-info-title"));
        sender.sendMessage(configManager.getMessage(
                "mute-info-player", "player", target.getName()));
        sender.sendMessage(configManager.getMessage(
                "mute-info-reason", "reason", configManager.localizeReason(data.getReason())));
        sender.sendMessage(configManager.getMessage(
                "mute-info-time-left", "time", configManager.formatDuration(data.getRemainingTime())));
        if (data.isPermanent()) {
            sender.sendMessage(configManager.getMessage("mute-info-permanent"));
        } else {
            sender.sendMessage(configManager.getMessage("mute-info-temporary"));
        }
        sender.sendMessage(configManager.getSeparatorMessage("mute-info-footer"));
    }

    private void handleIgnore(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(configManager.getPrefixedMessage("player-only"));
            return;
        }
        if (!player.hasPermission("iptmutechat.ignore")) {
            player.sendMessage(configManager.getPrefixedMessage("no-permission"));
            return;
        }
        if (args.length < 1) {
            player.sendMessage(configManager.getPrefixedMessage("ignore-usage"));
            return;
        }
        if (args[0].equalsIgnoreCase(player.getName())) {
            player.sendMessage(configManager.getPrefixedMessage("ignore-self"));
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(configManager.getPrefixedMessage("player-not-found"));
            return;
        }
        if (target.hasPermission("iptmutechat.ignore.exempt")) {
            player.sendMessage(configManager.getPrefixedMessage("ignore-exempt"));
            return;
        }

        UUID playerUuid = player.getUniqueId();
        UUID targetUuid = target.getUniqueId();
        if (plugin.getIgnoreManager().isIgnored(playerUuid, targetUuid)) {
            plugin.getIgnoreManager().removeIgnore(playerUuid, targetUuid);
            player.sendMessage(configManager.getPrefixedMessage("ignore-removed", "player", target.getName()));
        } else {
            plugin.getIgnoreManager().addIgnore(playerUuid, targetUuid);
            player.sendMessage(configManager.getPrefixedMessage("ignore-added", "player", target.getName()));
        }
    }

    private void handleIgnoreList(CommandSender sender) {
        if (!(sender instanceof Player player)) return;
        Set<UUID> ignoredPlayers = plugin.getIgnoreManager().getIgnoredPlayers(player.getUniqueId());
        if (ignoredPlayers.isEmpty()) {
            player.sendMessage(configManager.getPrefixedMessage("ignore-list-empty"));
            return;
        }
        player.sendMessage(configManager.getSeparatorMessage("ignore-list-header"));
        player.sendMessage(configManager.getMessage("ignore-list-title"));
        for (UUID uuid : ignoredPlayers) {
            Player ignored = Bukkit.getPlayer(uuid);
            String name = ignored != null ? ignored.getName() : configManager.getRawMessage("unknown-player");
            player.sendMessage(configManager.getMessage(
                    "ignore-list-format", "player", name));
        }
        player.sendMessage(configManager.getSeparatorMessage("ignore-list-footer"));
    }

    private void handleForceSay(CommandSender sender, String[] args) {
        if (!sender.hasPermission("iptmutechat.forcesay")) return;
        if (args.length < 2) {
            sender.sendMessage(configManager.getPrefixedMessage("forcesay-usage"));
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(configManager.getPrefixedMessage("player-not-found"));
            return;
        }
        String message = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
        target.chat(message);
        sender.sendMessage(configManager.getPrefixedMessage(
                "forcesay-success", "player", target.getName(), "message", message));
    }

    private void handleReply(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;
        if (args.length < 1) {
            sender.sendMessage(configManager.getPrefixedMessage("reply-usage"));
            return;
        }
        UUID lastReceiverUuid = plugin.getReplyManager().getLastReceiver(player.getUniqueId());
        if (lastReceiverUuid == null) {
            sender.sendMessage(configManager.getPrefixedMessage("reply-none"));
            return;
        }
        Player target = Bukkit.getPlayer(lastReceiverUuid);
        if (target == null) {
            sender.sendMessage(configManager.getPrefixedMessage("reply-offline"));
            return;
        }
        if (!player.hasPermission("iptmutechat.bypass.muted")
                && !plugin.getWhitelistManager().isWhitelisted(player.getUniqueId())
                && muteManager.isMuted(player.getUniqueId())) {
            sender.sendMessage(configManager.getPrefixedMessage("reply-muted"));
            return;
        }
        if (!player.hasPermission("iptmutechat.bypass.cooldown")) {
            if (!plugin.getCooldownManager().canChat(player.getUniqueId())) {
                int remaining = plugin.getCooldownManager().getRemainingCooldown(player.getUniqueId());
                sender.sendMessage(configManager.getPrefixedMessage("cooldown", "time", String.valueOf(remaining)));
                return;
            }
            plugin.getCooldownManager().updateLastChat(player.getUniqueId());
        }
        if (plugin.getIgnoreManager().isIgnored(target.getUniqueId(), player.getUniqueId())) {
            sender.sendMessage(configManager.getMessage(
                    "private-message-sent", "player", target.getName(), "message", String.join(" ", args)));
            return;
        }
        String message = String.join(" ", args);
        sender.sendMessage(configManager.getMessage(
                "private-message-sent", "player", target.getName(), "message", message));
        target.sendMessage(configManager.getMessage(
                "private-message-received", "player", player.getName(), "message", message));
        plugin.getReplyManager().recordPrivateMessage(player.getUniqueId(), target.getUniqueId());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("iptmutechat")) {
            if (args.length == 1) {
                String partial = args[0].toLowerCase();
                List.of("help", "reload", "whitelist").stream()
                        .filter(value -> !value.equals("whitelist")
                                || sender.hasPermission("iptmutechat.whitelist"))
                        .filter(value -> value.startsWith(partial))
                        .forEach(completions::add);
            } else if (args.length == 2 && args[0].equalsIgnoreCase("whitelist")
                    && sender.hasPermission("iptmutechat.whitelist")) {
                String partial = args[1].toLowerCase();
                List.of("add", "remove", "list").stream()
                        .filter(value -> value.startsWith(partial))
                        .forEach(completions::add);
            } else if (args.length == 3 && args[0].equalsIgnoreCase("whitelist")
                    && sender.hasPermission("iptmutechat.whitelist")) {
                String partial = args[2].toLowerCase();
                if (args[1].equalsIgnoreCase("add")) {
                    Bukkit.getOnlinePlayers().stream()
                            .filter(player -> !plugin.getWhitelistManager().isWhitelisted(player.getUniqueId()))
                            .map(Player::getName)
                            .filter(name -> name.toLowerCase().startsWith(partial))
                            .forEach(completions::add);
                } else if (args[1].equalsIgnoreCase("remove")) {
                    plugin.getWhitelistManager().getPlayerNames().stream()
                            .filter(name -> name.toLowerCase().startsWith(partial))
                            .forEach(completions::add);
                }
            }
            return completions;
        }
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(partial))
                    .forEach(completions::add);
        } else if (args.length == 2 && command.getName().equalsIgnoreCase("mute")) {
            String partial = args[1].toLowerCase();
            List.of("30s", "1m", "5m", "10m", "30m", "1h", "6h", "12h", "1d", "7d", "30d", "permanent")
                    .stream().filter(d -> d.startsWith(partial)).forEach(completions::add);
        }
        return completions;
    }

    private long parseDuration(String durationStr) throws IllegalArgumentException {
        if (durationStr.equalsIgnoreCase("permanent")) return -1;
        char unit = durationStr.charAt(durationStr.length() - 1);
        long value;
        try {
            value = Long.parseLong(durationStr.substring(0, durationStr.length() - 1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid duration");
        }
        if (value <= 0) throw new IllegalArgumentException("duration must be positive");
        return switch (unit) {
            case 's' -> value * 1000L;
            case 'm' -> value * 60 * 1000L;
            case 'h' -> value * 60 * 60 * 1000L;
            case 'd' -> value * 24 * 60 * 60 * 1000L;
            default -> throw new IllegalArgumentException("invalid unit");
        };
    }
}
