package com.icu.iptmutechat.console;

import com.icu.iptmutechat.IPTMUTECHAT;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

public final class PluginConsoleLogger {

    private static final int BANNER_WIDTH = 78;
    private static final int LABEL_WIDTH = 20;
    private static final TextColor BORDER_COLOR = TextColor.color(0x27D3F2);
    private static final TextColor TITLE_COLOR = TextColor.color(0xFFD166);
    private static final TextColor LABEL_COLOR = TextColor.color(0xA7B0BE);
    private static final TextColor TEXT_COLOR = TextColor.color(0xF2F5F7);

    private final ComponentLogger logger;

    public PluginConsoleLogger(IPTMUTECHAT plugin) {
        this.logger = plugin.getComponentLogger();
    }

    public void info(String message, TextColor color) {
        logger.info(Component.text(message, color));
    }

    public void warning(String message, TextColor color) {
        logger.warn(Component.text(message, color));
    }

    public void printStartupBanner(String version, String language, String platform, String repositoryUrl) {
        border('=');
        centered("IPTMUTECHAT ADMINISTRATION v" + version, TITLE_COLOR, true);
        centered("IP & CHAT CONTROL / IP 与聊天管理", TEXT_COLOR, false);
        border('-');
        row("Version / 版本", version, NamedTextColor.GREEN);
        row("Author / 作者", "Lazyz", TITLE_COLOR);
        row("Core / 核心", platform, NamedTextColor.LIGHT_PURPLE);
        row("Tested / 测试", "Paper & Folia 1.21.11", NamedTextColor.GREEN);
        row("Language / 语言", language, NamedTextColor.AQUA);
        row("GitHub", repositoryUrl, NamedTextColor.AQUA);
        row("Open source / 开源", "No telemetry or server-data upload.", NamedTextColor.GREEN);
        row("", "无遥测，不会上传任何服务器数据。", NamedTextColor.GREEN);
        border('=');

        Component success = Component.text("» ", BORDER_COLOR)
                .append(Component.text("IPTMUTECHAT v" + version, TITLE_COLOR, TextDecoration.BOLD))
                .append(Component.text(" by Lazyz started successfully on ", NamedTextColor.GREEN))
                .append(Component.text(platform, NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(" / 已在 ", NamedTextColor.GREEN))
                .append(Component.text(platform, NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(" 核心上成功启动！", NamedTextColor.GREEN));
        logger.info(success);
    }

    private void border(char fill) {
        logger.info(Component.text("+" + String.valueOf(fill).repeat(BANNER_WIDTH - 2) + "+",
                BORDER_COLOR, TextDecoration.BOLD));
    }

    private void centered(String text, TextColor color, boolean bold) {
        int availableWidth = BANNER_WIDTH - 2;
        int totalPadding = Math.max(0, availableWidth - displayWidth(text));
        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;

        Component content = Component.text(text, color);
        if (bold) content = content.decorate(TextDecoration.BOLD);
        logger.info(Component.text("|", BORDER_COLOR, TextDecoration.BOLD)
                .append(Component.text(" ".repeat(leftPadding)))
                .append(content)
                .append(Component.text(" ".repeat(rightPadding)))
                .append(Component.text("|", BORDER_COLOR, TextDecoration.BOLD)));
    }

    private void row(String label, String value, TextColor valueColor) {
        logger.info(Component.text("| ", BORDER_COLOR, TextDecoration.BOLD)
                .append(Component.text(padRight(label, LABEL_WIDTH), LABEL_COLOR))
                .append(Component.text(": ", NamedTextColor.DARK_GRAY))
                .append(Component.text(value, valueColor)));
    }

    private String padRight(String text, int targetWidth) {
        return text + " ".repeat(Math.max(1, targetWidth - displayWidth(text)));
    }

    private int displayWidth(String text) {
        return text.codePoints().map(codePoint -> codePoint < 128 ? 1 : 2).sum();
    }
}
