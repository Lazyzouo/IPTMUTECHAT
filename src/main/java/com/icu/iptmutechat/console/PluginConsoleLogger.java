package com.icu.iptmutechat.console;

import com.icu.iptmutechat.IPTMUTECHAT;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.ConsoleCommandSender;

public final class PluginConsoleLogger {

    private static final int BANNER_WIDTH = 66;
    private static final int LABEL_WIDTH = 18;
    private static final TextColor PREFIX_BRACKET_COLOR = TextColor.color(0x8A2387);
    private static final TextColor PREFIX_NAME_COLOR = TextColor.color(0xE62028);
    private static final TextColor BORDER_COLOR = TextColor.color(0x27D3F2);
    private static final TextColor TITLE_COLOR = TextColor.color(0xFFD166);
    private static final TextColor TEXT_COLOR = TextColor.color(0xF2F5F7);

    private static final Component CONSOLE_PREFIX = Component.text("[", PREFIX_BRACKET_COLOR, TextDecoration.BOLD)
            .append(Component.text("IPTMUTECHAT", PREFIX_NAME_COLOR, TextDecoration.BOLD))
            .append(Component.text("] ", PREFIX_BRACKET_COLOR, TextDecoration.BOLD))
            .append(Component.text("\u00bb ", NamedTextColor.DARK_GRAY, TextDecoration.BOLD));

    private final ConsoleCommandSender console;

    public PluginConsoleLogger(IPTMUTECHAT plugin) {
        this.console = plugin.getServer().getConsoleSender();
    }

    public void info(String message, TextColor color) {
        send(Component.text(message, color));
    }

    public void warning(String message, TextColor color) {
        send(Component.text(message, color));
    }

    public void printStartupBanner(String version, String language, String platform, String repositoryUrl) {
        border('=');
        centered("IPTMUTECHAT v" + version, TITLE_COLOR, true);
        centered("IP & CHAT CONTROL / IP 与聊天管理", TEXT_COLOR, false);
        border('-');
        row("Version / 版本", version, NamedTextColor.WHITE, NamedTextColor.WHITE);
        row("Author / 作者", "Lazyz", NamedTextColor.WHITE, NamedTextColor.WHITE);
        row("Core / 核心", platform, NamedTextColor.WHITE, NamedTextColor.WHITE);
        row("Tested / 测试", "Paper & Folia 1.21.11", NamedTextColor.WHITE, NamedTextColor.WHITE);
        row("Language / 语言", language, NamedTextColor.WHITE, NamedTextColor.WHITE);
        row("GitHub", repositoryUrl, NamedTextColor.AQUA, NamedTextColor.AQUA);
        row("Open source / 开源", "No telemetry or server-data upload.", NamedTextColor.GREEN, NamedTextColor.GREEN);
        border('=');

        Component success = Component.text("» ", BORDER_COLOR)
                .append(Component.text("IPTMUTECHAT v" + version, TITLE_COLOR, TextDecoration.BOLD))
                .append(Component.text(" by Lazyz started successfully on ", NamedTextColor.GREEN))
                .append(Component.text(platform, NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(" / 已在 ", NamedTextColor.GREEN))
                .append(Component.text(platform, NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(" 核心上成功启动！", NamedTextColor.GREEN));
        send(success);
    }

    private void border(char fill) {
        send(Component.text("+" + String.valueOf(fill).repeat(BANNER_WIDTH - 2) + "+",
                BORDER_COLOR, TextDecoration.BOLD));
    }

    private void centered(String text, TextColor color, boolean bold) {
        int availableWidth = BANNER_WIDTH - 2;
        int totalPadding = Math.max(0, availableWidth - displayWidth(text));
        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;

        Component content = Component.text(text, color);
        if (bold) content = content.decorate(TextDecoration.BOLD);
        send(Component.text("|", BORDER_COLOR, TextDecoration.BOLD)
                .append(Component.text(" ".repeat(leftPadding)))
                .append(content)
                .append(Component.text(" ".repeat(rightPadding)))
                .append(Component.text("|", BORDER_COLOR, TextDecoration.BOLD)));
    }

    private void row(String label, String value, TextColor labelColor, TextColor valueColor) {
        String paddedLabel = padRight(label, LABEL_WIDTH);
        int contentWidth = 1 + displayWidth(paddedLabel) + 2 + displayWidth(value);
        int rightPadding = Math.max(0, BANNER_WIDTH - 2 - contentWidth);

        send(Component.text("| ", BORDER_COLOR, TextDecoration.BOLD)
                .append(Component.text(paddedLabel, labelColor))
                .append(Component.text(": ", NamedTextColor.DARK_GRAY))
                .append(Component.text(value, valueColor))
                .append(Component.text(" ".repeat(rightPadding)))
                .append(Component.text("|", BORDER_COLOR, TextDecoration.BOLD)));
    }

    private void send(Component message) {
        console.sendMessage(CONSOLE_PREFIX.append(message));
    }

    private String padRight(String text, int targetWidth) {
        return text + " ".repeat(Math.max(0, targetWidth - displayWidth(text)));
    }

    private int displayWidth(String text) {
        return text.codePoints().map(codePoint -> codePoint < 128 ? 1 : 2).sum();
    }
}
