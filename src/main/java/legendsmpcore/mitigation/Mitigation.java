package legendsmpcore.mitigation;

import legendsmpcore.core.Initializer;
import legendsmpcore.mitigation.command.MitigationCommands;
import legendsmpcore.mitigation.command.VoteForLockdownCommand;
import legendsmpcore.core.utils.ConfigurationHelper;

import java.util.*;

public enum Mitigation {
    INSTANCE;

    public static Mitigation getInstance(){
        return INSTANCE;
    }

    public Set<String> blacklistedAddresses;
    public Set<String> blacklistedPlayers;

    public VoteForLockdownCommand voteForLockdownCommand;
    public MitigationCommands mitigationCommands;

    public void init(Initializer plugin){
        this.voteForLockdownCommand = new VoteForLockdownCommand();
        this.mitigationCommands = new MitigationCommands();

        this.loadBlacklists();
        plugin.getCommand("lockdown").setExecutor(this.voteForLockdownCommand);
        plugin.getCommand("mitigation").setExecutor(this.mitigationCommands);
    }

    private void loadBlacklists(){
        this.blacklistedAddresses = new HashSet<>(ConfigurationHelper.getStringList("Mitigation.Blacklisted.IPs",
                new ArrayList<>()));
        this.blacklistedPlayers = new HashSet<>(ConfigurationHelper.getStringList("Mitigation.Blacklisted.Players",
                new ArrayList<>()));
    }
}
