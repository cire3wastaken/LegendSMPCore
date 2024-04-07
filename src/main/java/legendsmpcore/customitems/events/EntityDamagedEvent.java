package legendsmpcore.customitems.events;

import legendsmpcore.customitems.items.SummoningSword;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Listener to make summoned entities invincible for a short period of time
 * */
public class EntityDamagedEvent implements Listener {
    /**+
     * This event is fired a bunch btw, if you want to do specific stuff use
     * specific events
     * */
    @EventHandler
    public void handleDamage(EntityDamageEvent event){
        if (SummoningSword.isSpawnedEntity(event.getEntity()) && SummoningSword.invulnerable) {
            event.setCancelled(true);
            event.setDamage(-100);
        }
    }
}
