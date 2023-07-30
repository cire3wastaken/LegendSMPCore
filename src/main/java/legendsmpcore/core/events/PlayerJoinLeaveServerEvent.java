package legendsmpcore.core.events;

import legendsmpcore.core.GlobalConstants;
import legendsmpcore.core.LegendCore;
import legendsmpcore.core.Permissions;
import legendsmpcore.core.check.checks.TypeA;
import legendsmpcore.core.check.checks.TypeB;
import legendsmpcore.mitigation.Mitigation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static legendsmpcore.core.utils.PlayerUtils.REAL_IP_PER_ONLINE_PLAYER;

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
    public void handleJoin(PlayerLoginEvent event){
        REAL_IP_PER_ONLINE_PLAYER.put(event.getPlayer(), event.getRealAddress().getHostAddress());

        if(event.getPlayer().isOp() || event.getPlayer().hasPermission(Permissions.GLOBAL_ALERTS_PERM)){
            if(LegendCore.getInstance().status())
                event.getPlayer().sendMessage(GlobalConstants.OUTDATED_MESSAGE);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void handleLeave(PlayerQuitEvent event){
        REAL_IP_PER_ONLINE_PLAYER.remove(event.getPlayer());
    }
}
