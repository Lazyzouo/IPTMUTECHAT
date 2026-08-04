package com.icu.iptmutechat.config;

import com.icu.iptmutechat.IPTMUTECHAT;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private static final int CONFIG_VERSION = 14;
    private static final int LANGUAGE_VERSION = 14;
    private static final List<String> REMOVED_CONFIGURATION_PATHS = List.of(
            "messages.ip-info-similar-title",
            "messages.ip-info-similar-entry",
            "messages.ip-info-similar-empty"
    );

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern NOTIFICATION_COLOR_PATTERN =
            Pattern.compile("(?i)(&(?:#[0-9a-f]{6}|[0-9a-f]))(?!&l)");
    private static final int[] NOTIFICATION_GRADIENT_START = {0x8A, 0x23, 0x87};
    private static final int[] NOTIFICATION_GRADIENT_END = {0xF2, 0xC9, 0x4C};
    private static final int[] IP_INFO_GRADIENT_START = {0x22, 0xD3, 0xEE};
    private static final int[] IP_INFO_GRADIENT_END = {0x3B, 0x82, 0xF6};

    public ConfigManager(IPTMUTECHAT plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void reloadConfig() {
        loadConfig();
    }

    private void loadConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        try {
            this.config = mergeBundledConfiguration(
                    configFile, "config.yml", "config-version", CONFIG_VERSION);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getConsoleLogger().warning(
                    "Unable to update config.yml safely; the existing file was not replaced: " + e.getMessage(),
                    NamedTextColor.RED);
            throw new IllegalStateException("Unable to load IPTMUTECHAT configuration", e);
        }
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
        try {
            messageConfig = mergeBundledConfiguration(
                    languageFile, "languages/en_US.yml", "language-version", LANGUAGE_VERSION);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getConsoleLogger().warning(
                    "Unable to update languages/en_US.yml safely; the existing file was not replaced. "
                            + "Bundled English messages will be used: " + e.getMessage(),
                    NamedTextColor.RED);
            try {
                messageConfig = loadBundledConfiguration("languages/en_US.yml");
            } catch (IOException | InvalidConfigurationException fallbackError) {
                language = "zh_CN";
                messageConfig = config;
            }
        }
    }

    private YamlConfiguration mergeBundledConfiguration(
            File targetFile, String resourcePath, String versionPath, int targetVersion)
            throws IOException, InvalidConfigurationException {
        YamlConfiguration bundled = loadBundledConfiguration(resourcePath);
        YamlConfiguration existing = new YamlConfiguration();
        existing.options().parseComments(true);

        boolean fileExists = targetFile.isFile();
        int previousVersion = 0;
        String existingText = null;
        if (fileExists) {
            existing.load(targetFile);
            previousVersion = existing.getInt(versionPath, 0);
            existingText = Files.readString(targetFile.toPath(), StandardCharsets.UTF_8);
            mergeUserValues(existing, bundled, versionPath);
        }

        bundled.set(versionPath, targetVersion);
        for (String removedPath : REMOVED_CONFIGURATION_PATHS) {
            bundled.set(removedPath, null);
        }

        String mergedText = bundled.saveToString();
        if (!fileExists || !mergedText.equals(existingText)) {
            Path backupPath = null;
            if (fileExists && previousVersion < targetVersion) {
                backupPath = createUpgradeBackup(targetFile.toPath(), previousVersion);
            }
            writeAtomically(targetFile.toPath(), mergedText);
            if (backupPath != null) {
                plugin.getConsoleLogger().info(
                        targetFile.getName() + " updated to schema " + targetVersion
                                + " with user values preserved. Backup: " + backupPath.getFileName(),
                        NamedTextColor.GREEN);
            }
        }
        return bundled;
    }

    private YamlConfiguration loadBundledConfiguration(String resourcePath)
            throws IOException, InvalidConfigurationException {
        InputStream resource = plugin.getResource(resourcePath);
        if (resource == null) {
            throw new IOException("Bundled resource is missing: " + resourcePath);
        }

        YamlConfiguration bundled = new YamlConfiguration();
        bundled.options().parseComments(true);
        try (InputStreamReader reader = new InputStreamReader(resource, StandardCharsets.UTF_8)) {
            bundled.load(reader);
        }
        return bundled;
    }

    private static void mergeUserValues(
            YamlConfiguration existing, YamlConfiguration bundled, String versionPath) {
        Set<String> officialPaths = new HashSet<>(bundled.getKeys(true));
        for (String path : existing.getKeys(true)) {
            if (path.equals(versionPath) || isRemovedPath(path)) {
                continue;
            }

            boolean existingSection = existing.isConfigurationSection(path);
            boolean bundledSection = bundled.isConfigurationSection(path);
            boolean bundledPath = officialPaths.contains(path);
            if (existingSection) {
                if (!bundledPath) {
                    bundled.createSection(path);
                    copyCustomComments(existing, bundled, path);
                }
                continue;
            }
            if (bundledSection) {
                continue;
            }

            bundled.set(path, existing.get(path));
            if (!bundledPath) {
                copyCustomComments(existing, bundled, path);
            }
        }
    }

    private static boolean isRemovedPath(String path) {
        for (String removedPath : REMOVED_CONFIGURATION_PATHS) {
            if (path.equals(removedPath) || path.startsWith(removedPath + ".")) {
                return true;
            }
        }
        return false;
    }

    private static void copyCustomComments(
            YamlConfiguration source, YamlConfiguration target, String path) {
        List<String> comments = source.getComments(path);
        if (!comments.isEmpty()) {
            target.setComments(path, comments);
        }
        List<String> inlineComments = source.getInlineComments(path);
        if (!inlineComments.isEmpty()) {
            target.setInlineComments(path, inlineComments);
        }
    }

    private static Path createUpgradeBackup(Path targetPath, int previousVersion) throws IOException {
        String versionLabel = previousVersion > 0 ? "v" + previousVersion : "pre-versioned";
        Path backupPath = targetPath.resolveSibling(
                targetPath.getFileName() + "." + versionLabel + ".bak");
        Files.copy(targetPath, backupPath,
                StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        return backupPath;
    }

    private static void writeAtomically(Path targetPath, String contents) throws IOException {
        Path parent = targetPath.toAbsolutePath().getParent();
        if (parent == null) {
            throw new IOException("Configuration path has no parent directory: " + targetPath);
        }
        Files.createDirectories(parent);
        Path temporaryPath = Files.createTempFile(parent, targetPath.getFileName() + ".", ".tmp");
        try {
            Files.writeString(temporaryPath, contents, StandardCharsets.UTF_8);
            try {
                Files.move(temporaryPath, targetPath,
                        StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporaryPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporaryPath);
        }
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
