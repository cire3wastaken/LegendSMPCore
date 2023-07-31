package legendsmpcore.customitems;

import legendsmpcore.core.LegendCore;
import legendsmpcore.customitems.command.ConvertCommand;
import legendsmpcore.customitems.command.ItemCommands;
import legendsmpcore.customitems.command.LFixCommand;
import legendsmpcore.customitems.events.*;
import legendsmpcore.customitems.items.*;
import legendsmpcore.core.Initializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;

public enum CustomItems {
    INSTANCE;

    public final Map<String, Set<String>> protectedRegions = new HashMap<>();
    public final Map<String, Set<String>> whitelistedRegions = new HashMap<>();
    public final Map<String, Long> tillNextMessage = new HashMap<>();
    public final Map<Items, Boolean> toggledItems = new HashMap<>();
    public final Set<String> blacklistedPlayers = new HashSet<>();

    public boolean isEnabled;

    public ConvertCommand convertCommand;
    public LFixCommand lFixCommand;
    public ItemCommands itemCommands;

    private Initializer plugin;

    public static CustomItems getInstance() {
        return INSTANCE;
    }

    public void init(Initializer plugin) {
        this.plugin = plugin;

        this.convertCommand = new ConvertCommand();
        this.lFixCommand = new LFixCommand();
        this.itemCommands = new ItemCommands();

        this.isEnabled = true;

        plugin.saveConfig();
        this.register(plugin);
        this.loadRegions();
        this.items();
    }

    public void enable() {
        this.register(this.plugin);

        this.isEnabled = true;
    }

    public void disable(){
        this.isEnabled = false;
    }

    private void register(Initializer plugin){
        plugin.getCommand("updateitem").setExecutor(this.convertCommand);
        plugin.getCommand("lfix").setExecutor(this.lFixCommand);
        plugin.getCommand("customitems").setExecutor(this.itemCommands);

        ThorHammer.update(LegendCore.getInstance().getConfig());
        VampireBlade.update(LegendCore.getInstance().getConfig());
        WitchScythe.update(LegendCore.getInstance().getConfig());
        GhastBow.update(LegendCore.getInstance().getConfig());
        Hyperion.update(LegendCore.getInstance().getConfig());

        Bukkit.getServer().getPluginManager().registerEvents(new AttackEntityEvent(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new AttackEntityByProjectileEvent(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new ProjectileHitBlockEvent(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new RightClickInteractEvent(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new ShootBowEvent(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerChatEvents(), plugin);
    }

    private void loadRegions(){
        try {
            List<String> worlds = LegendCore.getInstance().getConfig().getStringList("Items.Protected.Worldlist");
            for (String name : worlds) {
                String worldName = name.toLowerCase();
                List<String> whitelist = LegendCore.getInstance().getConfig().getStringList("Items.Protected.Whitelist."
                        + worldName);

                List<String> blacklist = LegendCore.getInstance().getConfig().getStringList("Items.Protected.Blacklist."
                        + worldName);

                if (whitelist != null) {
                    for (String region : whitelist) {
                        this.whitelistedRegions.computeIfAbsent(worldName, k -> new HashSet<>());
                        Set<String> temp = this.whitelistedRegions.get(worldName);
                        temp.add(region.toLowerCase());

                        this.whitelistedRegions.put(worldName, temp);
                    }
                }

                if (blacklist != null) {
                    for (String region : blacklist) {
                        this.protectedRegions.computeIfAbsent(worldName, k -> new HashSet<>());
                        Set<String> temp = this.protectedRegions.get(worldName);
                        temp.add(region.toLowerCase());

                        this.protectedRegions.put(worldName, temp);
                    }
                }
            }
        } catch (Exception e){
            Bukkit.getLogger().log(Level.SEVERE, "Unknown error, check logs!");
            e.printStackTrace();
        }

        this.protectedRegions.computeIfAbsent("survival", k -> new HashSet<>());
        this.whitelistedRegions.computeIfAbsent("survival", k -> new HashSet<>());
        this.protectedRegions.get("survival").add("spawn");
        this.whitelistedRegions.get("survival").add("pvparena");
    }

    private void items(){
        this.toggledItems.putIfAbsent(Items.GHASTBOW, true);
        this.toggledItems.putIfAbsent(Items.VAMPIREBLADE, true);
        this.toggledItems.putIfAbsent(Items.HYPERION, true);
        this.toggledItems.putIfAbsent(Items.WITCHSCYHTE, true);
        this.toggledItems.putIfAbsent(Items.THORHAMMER, true);
    }

    public void activateCooldown(Player player){
        this.tillNextMessage.remove(player.getName());
        this.tillNextMessage.put(player.getName(), System.currentTimeMillis());
    }

    public boolean hasCooldown(Player player){
        if(!this.tillNextMessage.containsKey(player.getName())){
            return false;
        }

        return !(this.tillNextMessage.get(player.getName()) < (System.currentTimeMillis() - 5000));
    }

    public boolean isBlacklisted(Player p){
        return this.blacklistedPlayers.contains(p.getName().toLowerCase());
    }

    public boolean isDisabled(Items item){
        return !this.toggledItems.get(item);
    }
}
