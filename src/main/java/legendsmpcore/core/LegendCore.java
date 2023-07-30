package legendsmpcore.core;

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
import org.bukkit.permissions.PermissionAttachment;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public enum LegendCore {
    INSTANCE;

    public final Map<String, PermissionAttachment> perms = new HashMap<>();

    private boolean outdated = false;
    public boolean isEnabled;

    private File configFile;
    private FileConfiguration configuration;

    private Initializer plugin;

    public static LegendCore getInstance(){
        return INSTANCE;
    }

    public void init(Initializer plugin){
        this.plugin = plugin;

        Mitigation.getInstance().init(plugin);
        CustomItems.getInstance().init(plugin);

        if(!this.isUpToDate())
            this.outdated = true;
        else
            this.updateConfig();


        this.isEnabled = false;

        plugin.saveConfig();
        this.defineConfig();

        for(Player p : Bukkit.getOnlinePlayers()){
            this.perms.put(p.getName(), p.addAttachment(plugin));
        }

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinLeaveServerEvent(), plugin);
    }

    public void enable(){
        CustomItems.getInstance().enable();
    }

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
            Bukkit.getLogger().info(ChatColor.DARK_RED + GlobalConstants.GLOBAL_FAIL_PREFIX +
                    "Failed to download version.txt, please manually verify this is up to date!" + GlobalConstants.GITHUB_REPO);
            e.printStackTrace();
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
        double[] versionConfig = FileUtils.versionParse(this.configuration.getString("Plugin.Version"));

        if(versionCurrent == null || versionConfig == null){
            return;
        }

        for(int i = 0; i < versionConfig.length && i < versionCurrent.length; i++){
            if(versionConfig[i] < versionCurrent[i]){
                if(this.configFile.delete()){
                    this.plugin.saveResource("config.yml", false);
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

    public boolean status(){
        return this.outdated;
    }

    public Initializer getPlugin(){
        return this.plugin;
    }
}
