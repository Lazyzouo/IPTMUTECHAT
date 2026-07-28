package com.icu.iptmutechat.chat.reply;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
public class ReplyManager {
    private final ConcurrentHashMap<UUID, UUID> lastReceiverMap = new ConcurrentHashMap<>();
    public void recordPrivateMessage(UUID sender, UUID receiver) { lastReceiverMap.put(sender, receiver); }
    public UUID getLastReceiver(UUID sender) { return lastReceiverMap.get(sender); }
    public void clearLastReceiver(UUID sender) { lastReceiverMap.remove(sender); }
}
