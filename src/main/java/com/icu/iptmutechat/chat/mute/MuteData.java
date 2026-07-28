package com.icu.iptmutechat.chat.mute;
import java.util.UUID;
public class MuteData {
    private final UUID targetUuid;
    private final String muterName;
    private final long muteTime;
    private final long duration;
    private final String reason;
    public MuteData(UUID targetUuid, String muterName, long duration, String reason) {
        this.targetUuid = targetUuid;
        this.muterName = muterName;
        this.muteTime = System.currentTimeMillis();
        this.duration = duration;
        this.reason = reason != null ? reason : "未指定";
    }
    public MuteData(UUID targetUuid, String muterName, long muteTime, long duration, String reason) {
        this.targetUuid = targetUuid;
        this.muterName = muterName;
        this.muteTime = muteTime;
        this.duration = duration;
        this.reason = reason != null ? reason : "未指定";
    }
    public UUID getTargetUuid() { return targetUuid; }
    public String getMuterName() { return muterName; }
    public long getMuteTime() { return muteTime; }
    public long getDuration() { return duration; }
    public String getReason() { return reason; }
    public boolean isPermanent() { return duration == -1; }
    public boolean isExpired() {
        if (isPermanent()) return false;
        return System.currentTimeMillis() >= muteTime + duration;
    }
    public long getRemainingTime() {
        if (isPermanent()) return -1;
        long remaining = (muteTime + duration) - System.currentTimeMillis();
        return Math.max(0, remaining);
    }
}
