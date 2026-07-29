package com.icu.iptmutechat;

import com.icu.iptmutechat.command.ChatCommandHandler;
import com.icu.iptmutechat.command.IpInfoCommand;
import com.icu.iptmutechat.command.IpHideCommand;
import com.icu.iptmutechat.config.ConfigManager;
import com.icu.iptmutechat.console.PluginConsoleLogger;
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
    private PluginConsoleLogger consoleLogger;
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
        this.consoleLogger = new PluginConsoleLogger(this);

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
    public PluginConsoleLogger getConsoleLogger() { return consoleLogger; }

    private void printStartupBanner() {
        consoleLogger.printStartupBanner(
                getDescription().getVersion(),
                configManager.getLanguage(),
                getServer().getName(),
                GitHubUpdateManager.REPOSITORY_URL);
    }

    /** Recreates deleted data files from the data currently held in memory. */
    public void restoreMissingDataFiles() {
        if (muteManager != null) muteManager.saveData();
        if (ignoreManager != null) ignoreManager.saveData();
        if (ipRecordManager != null) ipRecordManager.saveData();
        if (similarIpManager != null) similarIpManager.saveData();
    }
}
