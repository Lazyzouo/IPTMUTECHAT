package com.icu.iptmutechat.config;

import com.icu.iptmutechat.IPTMUTECHAT;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;

public class ConfigManager {

    private final IPTMUTECHAT plugin;
    private FileConfiguration config;
    private FileConfiguration messageConfig;
    private String language = "zh_CN";

    public static final int DEFAULT_COOLDOWN = 2;
    public static final String PREFIX = "&#8A2387&l[&#E62028&lIPTMUTECHAT&#8A2387&l] &8&l» &7";
    public static final List<String> CHAT_COMMANDS = List.of("msg", "tell", "w", "whisper", "me", "say");
    private static final int CONFIG_VERSION = 13;
    private static final int LANGUAGE_VERSION = 13;
    private static final List<String> NOTIFICATION_STYLE_KEYS = List.of(
            "reload-success", "no-permission", "player-not-found", "unknown-command", "player-only",
            "cooldown", "muted", "muted-no-reason", "mute-usage", "mute-duration-notice",
            "mute-type-permanent-notice", "mute-ends-in-notice", "mute-target",
            "mute-target-with-time", "mute-target-with-reason", "unmute-target", "not-muted",
            "already-muted", "unmute-usage", "unmuted-player", "muteinfo-usage",
            "whitelist-usage", "whitelist-player-not-found", "whitelist-added",
            "whitelist-already-added", "whitelist-removed", "whitelist-not-found",
            "whitelist-mute-denied", "ignore-added", "ignore-removed", "ignore-already",
            "ignore-not", "ignore-self", "ignore-exempt", "ignore-usage", "ignore-list-empty",
            "ip-info-usage", "ip-info-not-found", "ip-info-hidden", "ip-hide-player-only",
            "ip-hide-enabled", "ip-hide-disabled", "forcesay-usage", "forcesay-success",
            "reply-usage", "reply-none", "reply-offline", "reply-muted"
    );
    private static final List<String> HELP_STYLE_KEYS = List.of(
            "help-header", "help-title", "help-divider", "help-command", "help-player-section",
            "help-ignore", "help-ignore-list", "help-reply", "help-admin-section", "help-ipinfo",
            "help-iphide", "help-mute", "help-unmute", "help-muteinfo", "help-forcesay",
            "help-whitelist", "help-reload", "help-footer"
    );
    private static final List<String> LEFT_ALIGNMENT_STYLE_KEYS = List.of(
            "ip-info-same-entry", "ip-info-same-empty"
    );
    private static final List<String> MENU_STYLE_KEYS = List.of(
            "mute-info-header", "mute-info-title", "mute-info-player", "mute-info-reason",
            "mute-info-time-left", "mute-info-permanent", "mute-info-temporary", "mute-info-footer",
            "ignore-list-header", "ignore-list-title", "ignore-list-empty", "ignore-list-format", "ignore-list-footer",
            "ip-info-usage", "ip-info-not-found", "ip-info-hidden", "ip-hide-player-only", "ip-hide-enabled",
            "ip-hide-disabled", "ip-info-header", "ip-info-title", "ip-info-player",
            "ip-info-address", "ip-info-same-title", "ip-info-same-entry", "ip-info-same-empty", "ip-info-footer",
            "help-header", "help-title", "help-author", "help-divider", "help-command", "help-ignore", "help-ignore-list",
            "help-reply", "help-iphide", "help-whitelist", "help-reload", "help-footer",
            "whitelist-list-header", "whitelist-list-title", "whitelist-list-empty",
            "whitelist-list-entry", "whitelist-list-footer"
    );

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern NOTIFICATION_COLOR_PATTERN =
            Pattern.compile("(?i)(&(?:#[0-9a-f]{6}|[0-9a-f]))(?!&l)");
    private static final int[] NOTIFICATION_GRADIENT_START = {0x8A, 0x23, 0x87};
    private static final int[] NOTIFICATION_GRADIENT_END = {0xF2, 0xC9, 0x4C};
    private static final int[] IP_INFO_GRADIENT_START = {0x22, 0xD3, 0xEE};
    private static final int[] IP_INFO_GRADIENT_END = {0x3B, 0x82, 0xF6};
    private static final List<String> REMOVED_MESSAGE_KEYS = List.of(
            "ip-info-similar-title", "ip-info-similar-entry", "ip-info-similar-empty"
    );

    public ConfigManager(IPTMUTECHAT plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void reloadConfig() {
        loadConfig();
    }

    private void loadConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.isFile()) {
            plugin.saveResource("config.yml", false);
        }
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        config.options().copyDefaults(true);
        migrateMenuStyle();
        plugin.saveConfig();
        loadLanguageMessages();
    }

    private void loadLanguageMessages() {
        String configuredLanguage = config.getString("language", "zh_CN").trim();
        if (!configuredLanguage.equalsIgnoreCase("en_US")) {
            if (!configuredLanguage.equalsIgnoreCase("zh_CN")) {
                plugin.getConsoleLogger().warning(
                        "Unsupported language '" + configuredLanguage + "'; using zh_CN.",
                        NamedTextColor.YELLOW);
            }
            language = "zh_CN";
            messageConfig = config;
            return;
        }

        language = "en_US";
        File languageFile = new File(plugin.getDataFolder(), "languages/en_US.yml");
        if (!languageFile.isFile()) {
            plugin.saveResource("languages/en_US.yml", false);
        }
        YamlConfiguration localizedMessages = YamlConfiguration.loadConfiguration(languageFile);
        int currentLanguageVersion = localizedMessages.getInt("language-version", 0);
        try (InputStreamReader reader = new InputStreamReader(
                plugin.getResource("languages/en_US.yml"), StandardCharsets.UTF_8)) {
            YamlConfiguration defaults = YamlConfiguration.loadConfiguration(reader);
            localizedMessages.setDefaults(defaults);
            localizedMessages.options().copyDefaults(true);
            if (currentLanguageVersion < 11) {
                for (String key : HELP_STYLE_KEYS) {
                    localizedMessages.set("messages." + key, defaults.getString("messages." + key));
                }
            }
            if (currentLanguageVersion < 12) {
                for (String key : NOTIFICATION_STYLE_KEYS) {
                    localizedMessages.set("messages." + key, defaults.getString("messages." + key));
                }
            }
            if (currentLanguageVersion < 13) {
                for (String key : LEFT_ALIGNMENT_STYLE_KEYS) {
                    localizedMessages.set("messages." + key, defaults.getString("messages." + key));
                }
            }
            if (currentLanguageVersion < LANGUAGE_VERSION) {
                localizedMessages.set("language-version", LANGUAGE_VERSION);
            }
            localizedMessages.save(languageFile);
        } catch (IOException | NullPointerException e) {
            plugin.getConsoleLogger().warning(
                    "Unable to update en_US language defaults: " + e.getMessage(), NamedTextColor.RED);
        }
        messageConfig = localizedMessages;
    }

    /** Applies the latest notification and menu styles to configurations from older versions. */
    private void migrateMenuStyle() {
        int currentVersion = config.getInt("config-version", 0);
        if (currentVersion >= CONFIG_VERSION) {
            return;
        }

        Configuration defaults = config.getDefaults();
        if (currentVersion < 8) {
            if (defaults != null) {
                for (String key : MENU_STYLE_KEYS) {
                    config.set("messages." + key, defaults.getString("messages." + key));
                }
            }
            for (String key : REMOVED_MESSAGE_KEYS) {
                config.set("messages." + key, null);
            }
        }
        if (currentVersion < 11 && defaults != null) {
            for (String key : HELP_STYLE_KEYS) {
                config.set("messages." + key, defaults.getString("messages." + key));
            }
        }
        if (currentVersion < 12 && defaults != null) {
            for (String key : NOTIFICATION_STYLE_KEYS) {
                config.set("messages." + key, defaults.getString("messages." + key));
            }
        }
        if (currentVersion < 13 && defaults != null) {
            for (String key : LEFT_ALIGNMENT_STYLE_KEYS) {
                config.set("messages." + key, defaults.getString("messages." + key));
            }
        }
        config.set("config-version", CONFIG_VERSION);
    }

    public int getCooldownSeconds() { return config.getInt("chat.cooldown-seconds", DEFAULT_COOLDOWN); }
    public String getLanguage() { return language; }
    public boolean isUpdaterEnabled() { return config.getBoolean("updater.enabled", true); }
    public boolean isAutoDownloadEnabled() { return config.getBoolean("updater.auto-download", true); }
    public int getUpdateConnectTimeoutSeconds() {
        return Math.max(3, config.getInt("updater.connect-timeout-seconds", 10));
    }
    public int getUpdateReadTimeoutSeconds() {
        return Math.max(5, config.getInt("updater.read-timeout-seconds", 30));
    }
    public String getPrefix() {
        return leftAlignMessage(formatConfiguredNotification(config.getString("chat.prefix", PREFIX)));
    }

    public String getPrefixedMessage(String path, String... replacements) {
        return leftAlignMessage(formatConfiguredNotification(config.getString("chat.prefix", PREFIX)
                + replacePlaceholders(getMessageTemplate(path), replacements)));
    }

    public String getPrefixedIpInfoMessage(String path, String... replacements) {
        return leftAlignMessage(formatConfiguredNotification(config.getString("chat.prefix", PREFIX)
                + replacePlaceholders(getMessageTemplate(path), replacements)));
    }

    public String formatPrefixedNotification(String text) {
        return leftAlignMessage(formatConfiguredNotification(config.getString("chat.prefix", PREFIX) + text));
    }

    public String getMessage(String path) {
        return leftAlignMessage(formatNotification(getMessageTemplate(path)));
    }

    public String getMessage(String path, String... replacements) {
        return leftAlignMessage(formatNotification(replacePlaceholders(getMessageTemplate(path), replacements)));
    }

    public String getStyledMessage(String path, String... replacements) {
        return leftAlignMessage(ChatColor.BOLD + colorize(
                replacePlaceholders(getMessageTemplate(path), replacements)));
    }

    public String getIpInfoMessage(String path, String... replacements) {
        return leftAlignMessage(formatIpInfoNotification(
                replacePlaceholders(getMessageTemplate(path), replacements)));
    }

    public String getIpInfoAccentMessage(String path, String placeholder, String value, String accentColor) {
        return leftAlignMessage(formatIpInfoAccent(
                getMessageTemplate(path), placeholder, value, accentColor));
    }

    public String getPrefixedIpInfoAccentMessage(String path, String placeholder, String value, String accentColor) {
        return leftAlignMessage(formatConfiguredAccent(
                config.getString("chat.prefix", PREFIX) + getMessageTemplate(path),
                placeholder, value, accentColor));
    }

    public String getSeparatorMessage(String path) {
        return leftAlignMessage(formatSeparator(
                getMessageTemplate(path), NOTIFICATION_GRADIENT_START, NOTIFICATION_GRADIENT_END));
    }

    public String getIpInfoSeparatorMessage(String path) {
        int[] start = parseGradientColor("style.ip-info-gradient-start", IP_INFO_GRADIENT_START);
        int[] end = parseGradientColor("style.ip-info-gradient-end", IP_INFO_GRADIENT_END);
        return leftAlignMessage(formatSeparator(getMessageTemplate(path), start, end));
    }

    private static String leftAlignMessage(String message) {
        if (message == null || message.isEmpty()) return "";

        StringBuilder aligned = new StringBuilder(message.length());
        int index = 0;
        boolean lineStart = true;
        while (index < message.length()) {
            if (lineStart) {
                StringBuilder formattingCodes = new StringBuilder();
                while (index < message.length() && message.charAt(index) != '\n') {
                    char current = message.charAt(index);
                    if (current == ChatColor.COLOR_CHAR && index + 1 < message.length()) {
                        formattingCodes.append(current).append(message.charAt(index + 1));
                        index += 2;
                        continue;
                    }

                    int codePoint = message.codePointAt(index);
                    if (!Character.isWhitespace(codePoint)) break;
                    index += Character.charCount(codePoint);
                }
                aligned.append(formattingCodes);
                lineStart = false;
                if (index >= message.length()) break;
            }

            char current = message.charAt(index++);
            aligned.append(current);
            if (current == '\n') lineStart = true;
        }
        return aligned.toString();
    }

    private String formatSeparator(String separator, int[] start, int[] end) {
        int starIndex = separator.indexOf('✧');
        if (starIndex < 0) return formatGradient(separator, start, end);

        return formatGradient(separator.substring(0, starIndex), start, end)
                + formatSolidBold("✧", "#FACC15")
                + formatGradient(separator.substring(starIndex + 1), end, start);
    }

    private String replacePlaceholders(String message, String... replacements) {
        String result = message;
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                result = result.replace("{" + replacements[i] + "}", replacements[i + 1]);
            }
        }
        return result;
    }

    private String getMessageTemplate(String path) {
        String key = "messages." + path;
        String localized = messageConfig != null ? messageConfig.getString(key) : null;
        return localized != null ? localized : config.getString(key, "");
    }

    public String getRawMessage(String path, String... replacements) {
        return replacePlaceholders(getMessageTemplate(path), replacements);
    }

    public String localizeReason(String reason) {
        if (reason == null || reason.isBlank() || reason.equals("未指定") || reason.equalsIgnoreCase("Not specified")) {
            return getRawMessage("default-reason");
        }
        return reason;
    }

    public String formatDuration(long milliseconds) {
        if (milliseconds < 0) return getRawMessage("duration-permanent");
        if (milliseconds == 0) return getRawMessage("duration-expired");

        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        if (days > 0) {
            return getRawMessage("duration-days", "days", String.valueOf(days), "hours", String.valueOf(hours % 24));
        }
        if (hours > 0) {
            return getRawMessage("duration-hours", "hours", String.valueOf(hours), "minutes", String.valueOf(minutes % 60));
        }
        if (minutes > 0) {
            return getRawMessage("duration-minutes", "minutes", String.valueOf(minutes), "seconds", String.valueOf(seconds % 60));
        }
        return getRawMessage("duration-seconds", "seconds", String.valueOf(seconds));
    }

    private String formatConfiguredAccent(String message, String placeholder, String value, String accentColor) {
        String token = "{" + placeholder + "}";
        int tokenIndex = message.indexOf(token);
        if (tokenIndex < 0) return formatConfiguredNotification(message);
        return formatConfiguredNotification(message.substring(0, tokenIndex))
                + formatSolidBold(value, accentColor)
                + formatConfiguredNotification(message.substring(tokenIndex + token.length()));
    }

    private String formatConfiguredNotification(String text) {
        if (text == null || text.isEmpty()) return "";
        String boldText = NOTIFICATION_COLOR_PATTERN.matcher(text).replaceAll("$1&l")
                .replaceAll("(?i)&r(?!&l)", "&r&l");
        return colorize("&l" + boldText);
    }

    private String formatIpInfoAccent(String message, String placeholder, String value, String accentColor) {
        String token = "{" + placeholder + "}";
        int tokenIndex = message.indexOf(token);
        if (tokenIndex < 0) return formatIpInfoNotification(message);
        return formatIpInfoNotification(message.substring(0, tokenIndex))
                + formatSolidBold(value, accentColor)
                + formatIpInfoNotification(message.substring(tokenIndex + token.length()));
    }

    private String formatSolidBold(String text, String color) {
        try {
            return ChatColor.of(color).toString() + ChatColor.BOLD + text;
        } catch (IllegalArgumentException e) {
            return ChatColor.WHITE.toString() + ChatColor.BOLD + text;
        }
    }

    public String formatNotification(String text) {
        return formatGradient(text, NOTIFICATION_GRADIENT_START, NOTIFICATION_GRADIENT_END);
    }

    public String formatIpInfoNotification(String text) {
        int[] start = parseGradientColor("style.ip-info-gradient-start", IP_INFO_GRADIENT_START);
        int[] end = parseGradientColor("style.ip-info-gradient-end", IP_INFO_GRADIENT_END);
        return formatGradient(text, start, end);
    }

    private String formatGradient(String text, int[] start, int[] end) {
        if (text == null || text.isEmpty()) return "";

        String plainText = ChatColor.stripColor(colorize(text));
        int characterCount = (int) plainText.codePoints()
                .filter(codePoint -> !Character.isWhitespace(codePoint))
                .count();
        if (characterCount == 0) return plainText;

        StringBuilder formatted = new StringBuilder(plainText.length() * 16);
        int characterIndex = 0;
        for (int offset = 0; offset < plainText.length();) {
            int codePoint = plainText.codePointAt(offset);
            offset += Character.charCount(codePoint);

            if (Character.isWhitespace(codePoint)) {
                formatted.appendCodePoint(codePoint);
                continue;
            }

            double ratio = characterCount == 1 ? 0.0 : (double) characterIndex / (characterCount - 1);
            int red = interpolate(start[0], end[0], ratio);
            int green = interpolate(start[1], end[1], ratio);
            int blue = interpolate(start[2], end[2], ratio);
            String hex = String.format("#%02X%02X%02X", red, green, blue);
            formatted.append(ChatColor.of(hex)).append(ChatColor.BOLD).appendCodePoint(codePoint);
            characterIndex++;
        }
        return formatted.toString();
    }

    private int[] parseGradientColor(String path, int[] fallback) {
        String color = config.getString(path, "").trim();
        if (color.startsWith("#")) color = color.substring(1);
        if (!color.matches("[A-Fa-f0-9]{6}")) return fallback;
        return new int[]{
                Integer.parseInt(color.substring(0, 2), 16),
                Integer.parseInt(color.substring(2, 4), 16),
                Integer.parseInt(color.substring(4, 6), 16)
        };
    }

    private static int interpolate(int start, int end, double ratio) {
        return (int) Math.round(start + (end - start) * ratio);
    }

    public static String colorize(String text) {
        if (text == null) return "";
        Matcher matcher = HEX_PATTERN.matcher(text);
        while (matcher.find()) {
            String hexCode = matcher.group();
            String matched = matcher.group(1);
            try {
                text = text.replace(hexCode, ChatColor.of("#" + matched).toString());
            } catch (Exception e) {
            }
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
