package legendsmpcore.mitigation.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class SpamCommandDetection implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void handle(PlayerCommandPreprocessEvent event){
        //legend do your checks here
    }
}
