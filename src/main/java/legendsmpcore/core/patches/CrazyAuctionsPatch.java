package legendsmpcore.core.patches;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CrazyAuctionsPatch implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void handlePatch(PlayerCommandPreprocessEvent event){
        if(!event.getMessage().startsWith("/crazyauctions buy") && !event.getMessage().startsWith("/crazyauctionsplus buy")
                && !event.getMessage().startsWith("/ca buy") && !event.getMessage().startsWith("/ah buy") &&
                !event.getMessage().startsWith("/cap buy") && !event.getMessage().startsWith("/crazyauction buy")){
            return;
        }

        String[] cmd = event.getMessage().split(" ");

        if(cmd.length < 4)
            return;

        if(Double.parseDouble(cmd[3]) <= 0 || Double.parseDouble(cmd[3]) > 64){
            event.setCancelled(true);
        }
    }
}
