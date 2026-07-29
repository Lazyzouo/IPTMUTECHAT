package com.icu.iptmutechat.config;

import net.md_5.bungee.api.ChatColor;

final class PanelTextFormatter {

    private static final char CENTER_MARKER = '✧';
    private static final int SPACE_ADVANCE = 4;

    private PanelTextFormatter() {
    }

    static String centerOnMarker(String formattedMessage, String separatorTemplate) {
        String message = formattedMessage == null ? "" : formattedMessage.stripLeading();
        String plainSeparator = ChatColor.stripColor(ConfigManager.colorize(separatorTemplate));
        int markerIndex = plainSeparator.indexOf(CENTER_MARKER);
        if (markerIndex < 0) {
            return message;
        }

        int markerCenter = pixelWidth(plainSeparator.substring(0, markerIndex), true)
                + pixelWidth(String.valueOf(CENTER_MARKER), true) / 2;
        String plainMessage = ChatColor.stripColor(message).strip();
        int messageWidth = pixelWidth(plainMessage, true);
        int leftPadding = Math.max(0, markerCenter - messageWidth / 2);
        int spaces = Math.round((float) leftPadding / SPACE_ADVANCE);
        return ChatColor.BOLD + " ".repeat(spaces) + message;
    }

    static int pixelWidth(String text, boolean bold) {
        int width = 0;
        for (int offset = 0; offset < text.length();) {
            int codePoint = text.codePointAt(offset);
            offset += Character.charCount(codePoint);
            width += glyphAdvance(codePoint, bold);
        }
        return width;
    }

    private static int glyphAdvance(int codePoint, boolean bold) {
        if (Character.isWhitespace(codePoint)) {
            return SPACE_ADVANCE;
        }
        int type = Character.getType(codePoint);
        if (type == Character.NON_SPACING_MARK || type == Character.CONTROL) {
            return 0;
        }

        int baseWidth = codePoint > 0x7F ? 8 : asciiBaseWidth((char) codePoint);
        return baseWidth + 1 + (bold ? 1 : 0);
    }

    private static int asciiBaseWidth(char character) {
        return switch (character) {
            case '!', '\'', ',', '.', ':', ';', 'i', 'l', '|' -> 1;
            case '`' -> 2;
            case '"', '(', ')', 'I', '[', ']', 't' -> 3;
            case '<', '>', 'f', 'k', '{', '}' -> 4;
            case '@', '~' -> 6;
            default -> 5;
        };
    }
}
