package legendsmpcore.customitems.events;

import legendsmpcore.customitems.ItemsConstants;
import legendsmpcore.customitems.CustomItems;
import legendsmpcore.customitems.items.GhastBow;
import legendsmpcore.customitems.items.Items;
import legendsmpcore.core.utils.PlayerUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.util.Collection;

public class ProjectileHitBlockEvent implements Listener {
    @EventHandler
    public void onProjectileHitEvent(ProjectileHitEvent event){
        BlockIterator iterator = new BlockIterator(event.getEntity().getWorld(),
                event.getEntity().getLocation().toVector(), event.getEntity().getVelocity().normalize(),
                0.0D, 4);
        Block hitBlock = null;

        while (iterator.hasNext()) {
            hitBlock = iterator.next();

            if (hitBlock.getType() != Material.AIR) {
                break;
            }
        }
        if(event.getEntity().getShooter() instanceof Player){
            Player playerShooter = (Player) event.getEntity().getShooter();
            if("aksjfuaqialfkiaGhastBowShot".equalsIgnoreCase(event.getEntity().getCustomName())){
                if(!PlayerUtils.shouldUse(playerShooter))
                {
                    if(CustomItems.getInstance().hasCooldown(playerShooter)) return;

                    playerShooter.sendMessage(ChatColor.RED + ItemsConstants.CAN_NOT_USE);
                    CustomItems.getInstance().activateCooldown(playerShooter);
                    return;
                }

                if(PlayerUtils.containsLore(playerShooter.getItemInHand(), GhastBow.oldLore)){
                    playerShooter.sendMessage(ItemsConstants.FAIL_PREFIX +
                            "This items abilities are nullified due to being outdated. " +
                            "Use /updateitem while holding it to update it.");
                    return;
                }

                if(CustomItems.getInstance().isBlacklisted(playerShooter)){
                    if(CustomItems.getInstance().hasCooldown(playerShooter)) return;

                    playerShooter.sendMessage(ChatColor.RED + ItemsConstants.BLACKLISTED);
                    CustomItems.getInstance().activateCooldown(playerShooter);
                    return;
                }

                if(CustomItems.getInstance().isDisabled(Items.WITCHSCYHTE)){
                    playerShooter.sendMessage(ItemsConstants.DISABLED_ITEM);
                    return;
                }

                World world = playerShooter.getWorld();
                Location location = event.getEntity().getLocation();

                float power;
                Effect effect;

                switch((int) GhastBow.explosionPowerConfig){
                    case 2: power = 5; effect = Effect.EXPLOSION_LARGE; break;
                    case 3: power = 6; effect = Effect.EXPLOSION_HUGE; break;
                    default: effect = Effect.EXPLOSION; power = 4; break;
                }

                if(!GhastBow.explosion){
                    world.playEffect(location, effect, 3);
                    world.playSound(location, Sound.EXPLODE, 1F, 1F);

                    Collection<Entity> collection =  world.getNearbyEntities(location, power, power, power);
                    collection.remove(playerShooter);

                    for (Entity nearby : collection) {
                        if (nearby instanceof LivingEntity) {
                            if(nearby instanceof Player){
                                if(!PlayerUtils.shouldUse((Player) nearby)){
                                    continue;
                                }
                            }

                            LivingEntity entity = (LivingEntity) nearby;
                            BigDecimal healthBefore = BigDecimal.valueOf(entity.getHealth());

                            entity.damage(GhastBow.damageConfig * ((100F -
                                            entity.getLocation().distanceSquared(event.getEntity().getLocation()) * 3) / 100F),
                                    playerShooter
                            );

                            BigDecimal healthAfter = BigDecimal.valueOf(entity.getHealth());

                            // Checks that it wasn't cancelled
                            if(healthAfter.compareTo(healthBefore) < 0){
                                Vector affectedLoc = entity.getLocation().toVector();
                                Vector abilityLoc = event.getEntity().getLocation().toVector();
                                Vector result = affectedLoc.subtract(abilityLoc).divide(new Vector(2, 2, 2));
                                entity.setVelocity(result);
                            }
                        }
                    }
                } else {
                    playerShooter.getWorld().createExplosion(event.getEntity().getLocation(), power);
                }

            }
        }
    }
}
