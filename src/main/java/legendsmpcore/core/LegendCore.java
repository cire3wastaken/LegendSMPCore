package legendsmpcore.core;

import legendsmpcore.core.announcer.AnnounceTask;
import legendsmpcore.core.announcer.AnnouncerCommandManager;
import legendsmpcore.core.announcer.MessagesClass;
import legendsmpcore.core.events.PlayerChatEvents;
import legendsmpcore.core.patches.CrazyAuctionsPatch;
import legendsmpcore.core.patches.IllegalItemsPatch;
import legendsmpcore.customitems.CustomItems;
import legendsmpcore.customitems.ItemsConstants;
import legendsmpcore.core.events.PlayerJoinLeaveServerEvent;
import legendsmpcore.mitigation.Mitigation;
import legendsmpcore.core.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Main class of the plugin, everything starts here
 * */
public enum LegendCore {
    INSTANCE;

    private boolean outdated = false;
    public boolean isEnabled;

    private File configFile;
    private FileConfiguration configuration;

    // reference to called class, for later usages involving said class
    private LegendSMPCoreInitializer plugin;

    public static LegendCore getInstance(){
        return INSTANCE;
    }

    public void init(LegendSMPCoreInitializer plugin){
        this.plugin = plugin;
        this.defineConfig();

        Mitigation.getInstance().init(plugin);
        CustomItems.getInstance().init(plugin);

        // register patches to exploits & checks
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinLeaveServerEvent(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new CrazyAuctionsPatch(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new IllegalItemsPatch(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerChatEvents(), plugin);

        // legend why did you do this ?
        plugin.getCommand("announcer").setExecutor(new AnnouncerCommandManager());

        // TODO
        BukkitTask broadcastTask = new AnnounceTask(this).runTaskTimer(plugin, 0L, 20*MessagesClass.getInterval());

        if(!this.isUpToDate())
            this.outdated = true;
        else
            this.updateConfig();

        this.isEnabled = false;

        // register permissions
        for(Player p : Bukkit.getOnlinePlayers()){
            p.addAttachment(plugin);
        }
    }

    public void enable(){
        CustomItems.getInstance().enable();
    }

    // very scuffed update detection system
    public boolean isUpToDate() {
        File downloaded = new File(this.plugin.getDataFolder(), "versionDownloaded.txt");
        File current = new File(this.plugin.getDataFolder(), "versionCurrent.txt");

        if(!FileUtils.createNewFile(current) || !FileUtils.createNewFile(downloaded)){
            return false;
        }

        try {
            FileUtils.downloadUsingStream(GlobalConstants.VERSION_FILE_URL, downloaded);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE,
                    "Failed to download version.txt, please manually verify this is up to date!" + GlobalConstants.GITHUB_REPO);
            return false;
        }
        if(!FileUtils.createNewFile(current)){
            return false;
        }

        double[] versionDownloaded = FileUtils.versionParse(FileUtils.getVersion(downloaded));
        double[] versionCurrent = FileUtils.versionParse(FileUtils.getVersion(current));

        if(versionCurrent == null || versionDownloaded == null){
            return false;
        }

        for(int i = 0; i < versionDownloaded.length && i < versionCurrent.length; i++){
            double downloadedVer = versionDownloaded[i];
            double currentVer = versionCurrent[i];
            if(downloadedVer > currentVer){
                return false;
            }
        }
        return true;
    }

    private void updateConfig(){
        File current = new File(this.plugin.getDataFolder(), "versionCurrent.txt");

        double[] versionCurrent = FileUtils.versionParse(FileUtils.getVersion(current));
        double[] versionConfig = FileUtils.versionParse(this.configuration.getString("Plugin.Version",
                GlobalConstants.PLUGIN_VERSION));

        if(versionCurrent == null || versionConfig == null){
            return;
        }

        for(int i = 0; i < versionConfig.length && i < versionCurrent.length; i++){
            if(versionConfig[i] < versionCurrent[i]){
                if(this.configFile.delete()){
                    this.plugin.saveResource("config.yml", true);
                } else {
                    Bukkit.getLogger().info(ChatColor.DARK_RED + ItemsConstants.CHAT_PREFIX +
                        "Failed to update config.yml!\n" +
                        "Grab the latest config.yml from " + GlobalConstants.GITHUB_REPO + " and replace the current one!");
                }
                return;
            } else if (versionConfig[i] > versionCurrent[i]){
                break;
            }
        }
    }

    /**
     * Creates a config if one is not already created
     * */
    public void defineConfig(){
        this.configFile = new File(this.plugin.getDataFolder(), "config.yml");
        if(!this.configFile.exists()){
            this.configuration = this.plugin.getConfig();
            this.plugin.saveDefaultConfig();
        } else {
            this.configuration = YamlConfiguration.loadConfiguration(this.configFile);
        }
    }


    public File getFile() {
        return this.configFile;
    }

    public FileConfiguration getConfig() {
        return this.configuration;
    }

    /**
     * @return if plugin is outdated
     * */
    public boolean status(){
        return this.outdated;
    }

    public LegendSMPCoreInitializer getPlugin(){
        return this.plugin;
    }

    /**
     * Registers a task, and does NOT run instantly
     * @param delay (in MS)
     */
    public void registerTask(RunnableTask task, long delay){
        registerTask(task, delay, false);
    }

    /**
     * Registers a task
     * @param instant instantly run or not
     * @param delay (in MS)
     */
    public void registerTask(RunnableTask task, long delay, boolean instant){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task::run, instant ? 0L : 20L * delay / 1000,
            20L * delay / 1000);
    }
}
