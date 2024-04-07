package legendsmpcore.mitigation;

import legendsmpcore.core.LegendCore;
import legendsmpcore.core.LegendSMPCoreInitializer;
import legendsmpcore.mitigation.command.MitigationCommands;
import legendsmpcore.mitigation.command.VoteForLockdownCommand;
import legendsmpcore.core.utils.ConfigurationHelper;
import legendsmpcore.mitigation.event.SpamBroadcastDetection;
import org.bukkit.Bukkit;

import java.util.*;

/**
 * Entry-point to set up Mitigation actions & checks, called by {@link LegendCore}
 * */
public enum Mitigation {
    INSTANCE;

    public static Mitigation getInstance(){
        return INSTANCE;
    }

    public Set<String> blacklistedAddresses;
    public Set<String> blacklistedPlayers;

    public VoteForLockdownCommand voteForLockdownCommand;
    public MitigationCommands mitigationCommands;

    public void init(LegendSMPCoreInitializer plugin){
        this.voteForLockdownCommand = new VoteForLockdownCommand();
        this.mitigationCommands = new MitigationCommands();

        this.loadBlacklists();
        LegendCore.getInstance().registerTask(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mitigations clear"), 600 * 1000);

        Bukkit.getServer().getPluginManager().registerEvents(new SpamBroadcastDetection(), plugin);

        plugin.getCommand("lockdown").setExecutor(this.voteForLockdownCommand);
        plugin.getCommand("mitigations").setExecutor(this.mitigationCommands);
    }

    private void loadBlacklists(){
        this.blacklistedAddresses = new HashSet<>(ConfigurationHelper.getStringList("Mitigation.Blacklisted.IPs",
                new ArrayList<>()));
        this.blacklistedPlayers = new HashSet<>(ConfigurationHelper.getStringList("Mitigation.Blacklisted.Players",
                new ArrayList<>()));
    }
}
