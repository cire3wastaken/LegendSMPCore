package legendsmpcore.customitems.events;

import legendsmpcore.core.utils.ColorUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

public class PlayerChatEvents implements Listener {
    private final Map<Player, String> lastMessages = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void handleChat(AsyncPlayerChatEvent event){
        if(!this.lastMessages.containsKey(event.getPlayer())){
            return;
        }

        event.setMessage(ColorUtils.color(this.lastMessages.get(event.getPlayer())));
        this.lastMessages.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void storeChat(AsyncPlayerChatEvent event){
        boolean flag = false;
        String[] messages = event.getMessage().split(" ");
        List<String> possibleFinalMessage = new ArrayList<>();
        for(String message : messages){
            if(!message.equalsIgnoreCase("colorthismessage")){
                possibleFinalMessage.add(message);
            } else {
                flag = true;
            }
        }

        if(!flag) return;

        StringBuilder finalMessage = new StringBuilder();
        for(String msg : possibleFinalMessage){
            finalMessage.append(msg).append(" ");
        }
        this.lastMessages.put(event.getPlayer(), finalMessage.toString());
    }
}