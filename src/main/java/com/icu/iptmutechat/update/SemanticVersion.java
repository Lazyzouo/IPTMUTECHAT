package com.icu.iptmutechat.update;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record SemanticVersion(int major, int minor, int patch) implements Comparable<SemanticVersion> {

    private static final Pattern VERSION_PATTERN = Pattern.compile(
            "^v?(\\d+)\\.(\\d+)\\.(\\d+)(?:[-+].*)?$");

    public static SemanticVersion parse(String value) {
        Objects.requireNonNull(value, "value");
        Matcher matcher = VERSION_PATTERN.matcher(value.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid semantic version: " + value);
        }
        return new SemanticVersion(
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)));
    }

    @Override
    public int compareTo(SemanticVersion other) {
        int majorComparison = Integer.compare(major, other.major);
        if (majorComparison != 0) return majorComparison;
        int minorComparison = Integer.compare(minor, other.minor);
        if (minorComparison != 0) return minorComparison;
        return Integer.compare(patch, other.patch);
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }
}
