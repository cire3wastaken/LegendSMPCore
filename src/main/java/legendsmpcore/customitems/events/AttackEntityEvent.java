package legendsmpcore.customitems.events;

import legendsmpcore.customitems.ItemsConstants;
import legendsmpcore.customitems.CustomItems;
import legendsmpcore.customitems.items.*;
import legendsmpcore.core.utils.DamageUtils;
import legendsmpcore.core.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

/**
 * Listener for combat events to trigger special abilities
 * */
public class AttackEntityEvent implements org.bukkit.event.Listener {
    private final Map<Player, Long> thorHammerPatch = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityAttack(EntityDamageByEntityEvent event) {
        if(!CustomItems.getInstance().isEnabled) return;
        if(event.isCancelled()) return;

        if(event.getDamager() instanceof Player) {
            Player playerAttacker = (Player) event.getDamager();

            if(CustomItems.getInstance().isBlacklisted(playerAttacker)){
                if(CustomItems.getInstance().hasCooldown(playerAttacker)) return;

                playerAttacker.sendMessage(ChatColor.RED + ItemsConstants.BLACKLISTED);
                CustomItems.getInstance().activateCooldown(playerAttacker);
                return;
            }
            Entity victim = event.getEntity();

            if(PlayerUtils.containsLore(playerAttacker.getItemInHand(), ThorHammer.oldLore) ||
                PlayerUtils.containsLore(playerAttacker.getItemInHand(), WitchScythe.oldLore) ||
                PlayerUtils.containsLore(playerAttacker.getItemInHand(), VampireBlade.oldLore) )
            {
                playerAttacker.sendMessage(ItemsConstants.FAIL_PREFIX +
                        "This items abilities are nullified due to being outdated. " +
                        "Use /updateitem while holding it to update it.");
                return;
            }

            double[] c = DamageUtils.damageCalculator(victim, event.getFinalDamage() - 2);
            c[1] = c[1] * DamageUtils.strengthIncrease(playerAttacker);

            // Handles vamp blade
            if (PlayerUtils.containsLore(playerAttacker.getItemInHand(), VampireBlade.lore) &&
                    playerAttacker.getItemInHand().getType().equals(Material.DIAMOND_SWORD))
            {
                if(CustomItems.getInstance().isDisabled(Items.VAMPIREBLADE)){
                    playerAttacker.sendMessage(ItemsConstants.DISABLED_ITEM);
                    return;
                }

                if(!PlayerUtils.shouldUse(playerAttacker))
                {
                    if(CustomItems.getInstance().hasCooldown(playerAttacker)) return;

                    playerAttacker.sendMessage(ChatColor.RED + ItemsConstants.CAN_NOT_USE);
                    CustomItems.getInstance().activateCooldown(playerAttacker);
                    return;
                }

                playerAttacker.setHealth(Math.min(playerAttacker.getHealth() +
                    Math.min(Math.max(DamageUtils.calcDamage((int) c[0], c[1], 0, (int) c[3],
                            (int) c[4]) * VampireBlade.toBeHealed,
                        playerAttacker.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE) ?
                            (Math.random() >= 0.25 ? 3F : 2F) :
                                (Math.random() >= 0.5 ? 2.0F : 1.0F)), 6.0F), 20.0F));
            }

            // Handles Thor Hammer
            if (PlayerUtils.containsLore(playerAttacker.getItemInHand(), ThorHammer.lore) &&
                    playerAttacker.getItemInHand().getType().equals(Material.GOLD_AXE))
            {
                if(CustomItems.getInstance().isDisabled(Items.THORHAMMER)){
                    playerAttacker.sendMessage(ItemsConstants.DISABLED_ITEM);
                    return;
                }

                if(!this.hasCooldown(playerAttacker))
                {
                    playerAttacker.getWorld().strikeLightningEffect(victim.getLocation());
                    this.activateCooldown(playerAttacker);
                }

                if(victim instanceof LivingEntity){
                    LivingEntity target = (LivingEntity) victim;

                    if(!PlayerUtils.shouldUse(playerAttacker))
                    {
                        if(CustomItems.getInstance().hasCooldown(playerAttacker)) return;

                        playerAttacker.sendMessage(ChatColor.RED + ItemsConstants.CAN_NOT_USE);
                        CustomItems.getInstance().activateCooldown(playerAttacker);
                        return;
                    }

                    target.damage(ThorHammer.damage);
                    target.setFireTicks(((int) Math.floor(ThorHammer.fireTicks)));
                }
            }

            // Handles witch scythe
            if (PlayerUtils.containsLore(playerAttacker.getItemInHand(), WitchScythe.lore) &&
                    playerAttacker.getItemInHand().getType().equals(Material.GOLD_HOE))
            {
                if(!PlayerUtils.shouldUse(playerAttacker))
                {
                    if(CustomItems.getInstance().hasCooldown(playerAttacker)) return;

                    playerAttacker.sendMessage(ChatColor.RED + ItemsConstants.CAN_NOT_USE);
                    CustomItems.getInstance().activateCooldown(playerAttacker);
                    return;
                }

                if(CustomItems.getInstance().isDisabled(Items.WITCHSCYHTE)){
                    playerAttacker.sendMessage(ItemsConstants.DISABLED_ITEM);
                    return;
                }

                if(victim instanceof LivingEntity){
                    LivingEntity playerVictim = (LivingEntity) victim;

                    if(playerVictim.hasPotionEffect(PotionEffectType.POISON)){
                        playerVictim.removePotionEffect(PotionEffectType.POISON);
                    }
                    playerVictim.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
                            (int) Math.ceil(WitchScythe.secondsOfEffect * 20), 4));
                }
            }
        }

        if (SummoningSword.isSpawnedEntity(event.getEntity()))
            event.setCancelled(true);
    }

    public void activateCooldown(Player player){
        this.thorHammerPatch.remove(player);
        this.thorHammerPatch.put(player, System.currentTimeMillis());
    }

    public boolean hasCooldown(Player player){
        if(!this.thorHammerPatch.containsKey(player)){
            return false;
        }

        return !(this.thorHammerPatch.get(player) < (System.currentTimeMillis() - 500));
    }
}

