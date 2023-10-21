package legendsmpcore.customitems.events;

import legendsmpcore.core.utils.PlayerUtils;
import legendsmpcore.customitems.items.SummoningSword;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.List;
import java.util.stream.Collectors;

public class EntityTargetAnotherEntityEvent implements Listener {
    @EventHandler
    public void handleTarget(EntityTargetEvent event){
        if (!(event.getTarget() instanceof Player)) return;
        if (!SummoningSword.isSpawnedEntity(event.getEntity())) return;

        List<LivingEntity> playerTargets = PlayerUtils.getNearbyLivingEntities(event.getEntity(), 15).stream()
                .filter(e -> SummoningSword.getSummoner(event.getEntity()) != e)
                .filter(e -> e instanceof Player)
                .collect(Collectors.toList());

        if (playerTargets.isEmpty()) {
            event.setTarget(null);
            event.setCancelled(true);
        } else
            event.setTarget(playerTargets.get(0));
    }
}
