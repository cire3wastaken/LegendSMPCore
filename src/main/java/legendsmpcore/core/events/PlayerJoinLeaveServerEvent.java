package legendsmpcore.core.events;

import legendsmpcore.core.GlobalConstants;
import legendsmpcore.core.LegendCore;
import legendsmpcore.core.Permissions;
import legendsmpcore.core.check.Check;
import legendsmpcore.core.check.checks.ip.TypeA;
import legendsmpcore.core.check.checks.ip.TypeB;
import legendsmpcore.mitigation.command.VoteForLockdownCommand;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listener for when a player joins to create checks & run them to detect suspicious activity
 * */
public class PlayerJoinLeaveServerEvent implements Listener {
    private final TypeA.Factory factoryA = new TypeA.Factory();
    private final TypeB.Factory factoryB = new TypeB.Factory();

    /**
     * Check construction events
     * */

    @EventHandler(priority = EventPriority.LOW)
    public void handleJoinLow(PlayerLoginEvent event){
        this.factoryA.setPlayer(event.getPlayer());
        this.factoryA.setLowAddress(event.getRealAddress().getHostAddress());

        this.factoryB.setPlayer(event.getPlayer());
        this.factoryB.setLowAddress(event.getRealAddress().getHostAddress());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleJoinNormal(PlayerLoginEvent event){
        this.factoryA.setNormalAddress(event.getRealAddress().getHostAddress());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleJoinHigh(PlayerLoginEvent event){
        this.factoryA.setHighAddress(event.getRealAddress().getHostAddress());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void handleJoinMonitor(PlayerLoginEvent event){
        this.factoryA.setMonitorAddress(event.getRealAddress().getHostAddress());
        this.factoryB.setMonitorAddress(event.getRealAddress().getHostAddress());

        this.factoryA.build().check();
        this.factoryB.build().check();

        this.factoryA.reset();
        this.factoryB.reset();
    }

    /**
     * Logging events
     * */
    @EventHandler(priority = EventPriority.LOW)
    public void handleLeave(PlayerQuitEvent event){
        VoteForLockdownCommand.votees.remove(event.getPlayer());
        VoteForLockdownCommand.needConfirm.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleRenderJoin(PlayerJoinEvent event){
        if(event.getPlayer().hasPermission(Permissions.GLOBAL_ALERTS_PERM)){
            if(LegendCore.getInstance().status())
                event.getPlayer().sendMessage(GlobalConstants.OUTDATED_MESSAGE);
        }
    }
}
