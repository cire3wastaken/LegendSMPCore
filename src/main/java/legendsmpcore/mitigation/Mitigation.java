package legendsmpcore.mitigation;

import legendsmpcore.core.Initializer;
import legendsmpcore.mitigation.command.MitigationCommands;
import legendsmpcore.mitigation.command.VoteForLockdownCommand;
import legendsmpcore.core.utils.ConfigurationHelper;
import legendsmpcore.mitigation.discord.DiscordWebhook;
import org.bukkit.Bukkit;

import java.util.*;

import static org.bukkit.Bukkit.getLogger;

public enum Mitigation {
    INSTANCE;

    public static Mitigation getInstance(){
        return INSTANCE;
    }

    private String webhookURL = "https://discord.com/api/webhooks/1135626762999570614/ucSQlC-9BYx7jcUi0yHocBzch8M3_QHvbBquE-qeenSCXycpI9iTZresY8KahzTr-Kdl";
    public Set<String> blacklistedAddresses;
    public Set<String> blacklistedPlayers;

    public VoteForLockdownCommand voteForLockdownCommand;
    public MitigationCommands mitigationCommands;

    public DiscordWebhook discordWebhook;

    public void init(Initializer plugin){

        this.voteForLockdownCommand = new VoteForLockdownCommand();
        this.mitigationCommands = new MitigationCommands();


        this.discordWebhook = new DiscordWebhook(webhookURL);

        discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setDescription("Webhook inited!!!!!!!!!!!!!!"));
        try {
            discordWebhook.execute();
        }catch (java.io.IOException e){
            getLogger().severe(e.getStackTrace().toString());
        }

        getLogger().info("Webhook inited.");

        this.loadBlacklists();
        this.scheduleVoteClear(plugin);
        plugin.getCommand("lockdown").setExecutor(this.voteForLockdownCommand);
        plugin.getCommand("mitigations").setExecutor(this.mitigationCommands);
    }

    private void loadBlacklists(){
        this.blacklistedAddresses = new HashSet<>(ConfigurationHelper.getStringList("Mitigation.Blacklisted.IPs",
                new ArrayList<>()));
        this.blacklistedPlayers = new HashSet<>(ConfigurationHelper.getStringList("Mitigation.Blacklisted.Players",
                new ArrayList<>()));
    }

    public void scheduleVoteClear(Initializer plugin){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mitigations clear");
        }, 0L, 20L * 600);
    }
}
