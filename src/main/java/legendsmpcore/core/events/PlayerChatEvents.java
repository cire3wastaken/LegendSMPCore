package legendsmpcore.core.events;

import legendsmpcore.core.LegendCore;
import legendsmpcore.core.utils.ColorUtils;
import legendsmpcore.core.utils.ConfigurationHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

/**
 * Listener to allow for different chatting mechanics for different permissions
 * */
public class PlayerChatEvents implements Listener {
    private final Map<Player, String> lastMessages = new HashMap<>();
    private final List<String> allowedPlayers;
    public static boolean isActivated;

    @EventHandler(priority = EventPriority.HIGH)
    public void handleChat(AsyncPlayerChatEvent event){
        if(!isActivated) return;
        if(!allowedPlayers.contains(event.getPlayer().getName().toLowerCase())) return;

        if(!this.lastMessages.containsKey(event.getPlayer())){
            return;
        }

        event.setMessage(ColorUtils.color(this.lastMessages.get(event.getPlayer())));
        this.lastMessages.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void storeChat(AsyncPlayerChatEvent event){
        if(!isActivated) return;
        if(!allowedPlayers.contains(event.getPlayer().getName().toLowerCase())) return;

        this.lastMessages.put(event.getPlayer(), event.getMessage().trim());
    }

    public PlayerChatEvents(){
        isActivated = Boolean.parseBoolean(LegendCore.getInstance().getConfig().getString("Core.ColorChat.Enabled", "false"));
        this.allowedPlayers = ConfigurationHelper.getStringList("Core.ColorChat.AllowedPlayers", new ArrayList<>());
    }
}