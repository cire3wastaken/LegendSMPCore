package legendsmpcore.customitems.events;

import legendsmpcore.core.utils.PlayerUtils;
import legendsmpcore.customitems.ItemsConstants;
import legendsmpcore.customitems.CustomItems;
import legendsmpcore.customitems.items.GhastBow;
import legendsmpcore.customitems.items.Items;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * Listener for bow events to trigger ghast bow ability
 * */
public class AttackEntityByProjectileEvent implements Listener {
    //Handles ghast bow

    @EventHandler(priority = EventPriority.HIGH)
    public void onAttackEntityByProjectile(EntityDamageByEntityEvent event) {
        if(!CustomItems.getInstance().isEnabled) return;
        if(event.isCancelled()) return;

        if(event.getDamager() instanceof Projectile){
            if(((Projectile) event.getDamager()).getShooter() instanceof Player){
                Player playerShooter = (Player) ((Projectile) event.getDamager()).getShooter();
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

                    if(CustomItems.getInstance().isDisabled(Items.GHASTBOW)){
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
                        for(int i = 0; i < 15; i++)
                            world.playEffect(location, effect, 3);
                        world.playSound(location, Sound.EXPLODE, 1F, 1F);

                        Collection<Entity> collection =  world.getNearbyEntities(location, power, power, power);
                        collection.remove(playerShooter);

                        for (Entity nearby: collection) {
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
                                    Vector abilityLoc = event.getDamager().getLocation().toVector();
                                    Vector result = affectedLoc.subtract(abilityLoc).divide(new Vector(2, 2, 2));
                                    entity.setVelocity(result);
                                }
                            }
                        }
                    } else {
                        playerShooter.getWorld().createExplosion(event.getEntity().getLocation(), power);
                    }

                    event.getEntity().setCustomName("");
                    event.getEntity().setCustomNameVisible(false);
                }
            }
        }
    }
}
