package com.icu.iptmutechat;

import com.icu.iptmutechat.command.ChatCommandHandler;
import com.icu.iptmutechat.command.IpInfoCommand;
import com.icu.iptmutechat.command.IpHideCommand;
import com.icu.iptmutechat.config.ConfigManager;
import com.icu.iptmutechat.chat.cooldown.CooldownManager;
import com.icu.iptmutechat.listener.ChatListener;
import com.icu.iptmutechat.listener.ConnectionListener;
import com.icu.iptmutechat.chat.mute.MuteManager;
import com.icu.iptmutechat.chat.ignore.IgnoreManager;
import com.icu.iptmutechat.chat.reply.ReplyManager;
import com.icu.iptmutechat.ip.IpRecordManager;
import com.icu.iptmutechat.ip.SimilarIpManager;
import com.icu.iptmutechat.update.GitHubUpdateManager;
import org.bukkit.plugin.java.JavaPlugin;

public class IPTMUTECHAT extends JavaPlugin {

    private static IPTMUTECHAT instance;
    private ConfigManager configManager;
    private CooldownManager cooldownManager;
    private MuteManager muteManager;
    private IgnoreManager ignoreManager;
    private ReplyManager replyManager;
    private ChatCommandHandler chatCommandHandler;

    private IpRecordManager ipRecordManager;
    private SimilarIpManager similarIpManager;
    private GitHubUpdateManager updateManager;

    @Override
    public void onEnable() {
        instance = this;

        this.configManager = new ConfigManager(this);
        this.muteManager = new MuteManager(this);
        this.ignoreManager = new IgnoreManager(this);
        this.replyManager = new ReplyManager();
        this.cooldownManager = new CooldownManager(this);
        this.chatCommandHandler = new ChatCommandHandler(this);

        this.ipRecordManager = new IpRecordManager(this);
        this.similarIpManager = new SimilarIpManager(this);

        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);

        chatCommandHandler.registerCommands();

        var ipInfoCommand = getCommand("ipinfo");
        if (ipInfoCommand != null) {
            IpInfoCommand executor = new IpInfoCommand(this);
            ipInfoCommand.setExecutor(executor);
            ipInfoCommand.setTabCompleter(executor);
        }

        var ipHideCommand = getCommand("iphide");
        if (ipHideCommand != null) {
            ipHideCommand.setExecutor(new IpHideCommand(this));
        }

        printStartupBanner();
        this.updateManager = new GitHubUpdateManager(this);
        updateManager.checkForUpdates();
    }

    @Override
    public void onDisable() {
        restoreMissingDataFiles();

        getLogger().info("IPTMUTECHAT stopped / IPTMUTECHAT 已停用");
    }

    public static IPTMUTECHAT getInstance() { return instance; }
    public ConfigManager getConfigManager() { return configManager; }
    public CooldownManager getCooldownManager() { return cooldownManager; }
    public MuteManager getMuteManager() { return muteManager; }
    public IgnoreManager getIgnoreManager() { return ignoreManager; }
    public ReplyManager getReplyManager() { return replyManager; }
    public IpRecordManager getIpRecordManager() { return ipRecordManager; }
    public SimilarIpManager getSimilarIpManager() { return similarIpManager; }

    private void printStartupBanner() {
        String version = getDescription().getVersion();
        getLogger().info("============================================================");
        getLogger().info("                    IPTMUTECHAT v" + version);
        getLogger().info("        IP & Chat Administration / IP 与聊天管理");
        getLogger().info("------------------------------------------------------------");
        getLogger().info("  Status     : ENABLED / 已启用");
        getLogger().info("  Author     : Lazyz");
        getLogger().info("  Language   : " + configManager.getLanguage());
        getLogger().info("  Supported  : Paper/Folia 1.20.1 - 1.21.11");
        getLogger().info("  Tested     : Paper/Folia 1.21.11");
        getLogger().info("  Repository : " + GitHubUpdateManager.REPOSITORY_URL);
        getLogger().info("============================================================");
    }

    /** Recreates deleted data files from the data currently held in memory. */
    public void restoreMissingDataFiles() {
        if (muteManager != null) muteManager.saveData();
        if (ignoreManager != null) ignoreManager.saveData();
        if (ipRecordManager != null) ipRecordManager.saveData();
        if (similarIpManager != null) similarIpManager.saveData();
    }
}
