package legendsmpcore.core.events;

import legendsmpcore.core.GlobalConstants;
import legendsmpcore.core.LegendCore;
import legendsmpcore.core.Permissions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static legendsmpcore.core.utils.PlayerUtils.REAL_IP_PER_ONLINE_PLAYER;

public class PlayerJoinLeaveServerEvent implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void handleJoin(PlayerLoginEvent event){
        REAL_IP_PER_ONLINE_PLAYER.put(event.getPlayer(), event.getRealAddress().getHostAddress());

        if(event.getPlayer().isOp() || event.getPlayer().hasPermission(Permissions.GLOBAL_ALERTS_PERM)){
            if(LegendCore.getInstance().status())
                event.getPlayer().sendMessage(GlobalConstants.OUTDATED_MESSAGE);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void handleLeave(PlayerQuitEvent event){
        REAL_IP_PER_ONLINE_PLAYER.remove(event.getPlayer());
    }
}
