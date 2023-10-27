package legendsmpcore.customitems.events;

import legendsmpcore.core.LegendCore;
import legendsmpcore.customitems.ItemsConstants;
import legendsmpcore.customitems.items.Hyperion;
import legendsmpcore.core.utils.DamageUtils;
import legendsmpcore.core.utils.PlayerUtils;
import legendsmpcore.customitems.items.SummoningSword;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Random;

public class RightClickInteractEvent implements Listener {
    // prevent fire twice
    private static RightClickInteractEvent instance;

    public final HashMap<String, Long> hyperionCooldowns = new HashMap<>();
    public final HashMap<String, Long> summoningSwordCooldown = new HashMap<>();
    private int lastEvent;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (instance != this) return;
//        IllegalItemsPatch.
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (lastEvent == event.hashCode())
            return;
        lastEvent = event.hashCode();

        Player player = event.getPlayer();
        boolean flag = false;

        if(!PlayerUtils.shouldUse(player))
        {
            player.sendMessage(ChatColor.RED + ItemsConstants.CHAT_PREFIX + "You can not use that item here!");
            return;
        }

        if (PlayerUtils.containsLore(player.getItemInHand(), Hyperion.lore)) {
            int level = 0;
            boolean alreadyHadAbsorption = false;

            if(this.hyperionCooldowns.containsKey(player.getName()) &&
                !(this.hyperionCooldowns.get(player.getName()) < (System.currentTimeMillis() -
                    Hyperion.cooldownSeconds * 1000))){
                int timeLeft = 30 - (int) Math.ceil((System.currentTimeMillis() - this.hyperionCooldowns.get(player.getName())) / 1000.0F);
                player.sendMessage(
                        ChatColor.RED + ItemsConstants.CHAT_PREFIX + "You can use this item's ability again in " + timeLeft + "s.");
                return;
            }

            for (PotionEffect effect : player.getActivePotionEffects()){
                if(effect.getType().equals(PotionEffectType.ABSORPTION)){
                    alreadyHadAbsorption = true;
                    level = effect.getAmplifier() + 1;
                    break;
                }
            }

            player.teleport(PlayerUtils.getTargetBlock(event.getPlayer(), 10)[0].getLocation());
            if(alreadyHadAbsorption) player.removePotionEffect(PotionEffectType.ABSORPTION);

            int amplifier = (int)
                    Math.floor(((DamageUtils.getAttackDamage(player.getItemInHand()) + 6) * DamageUtils.strengthIncrease(player)
                            * Hyperion.percentage) / 4F);

            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,
                    (int) Hyperion.shieldDurationTicks, amplifier + level));
            player.sendMessage(ChatColor.GOLD + ItemsConstants.CHAT_PREFIX +
                    "Your Hyperion has given you a shield for " + amplifier * 4 + " HP!");
            this.hyperionCooldowns.remove(player.getName());
            this.hyperionCooldowns.put(player.getName(), System.currentTimeMillis());

            for(LivingEntity entity : PlayerUtils.getNearbyLivingEntities(player,
                    Hyperion.explosionRadius))
            {
                entity.damage(Hyperion.damage);
            }
            flag = true;
        } else if (PlayerUtils.containsLore(player.getItemInHand(), SummoningSword.lore)){
            if (this.summoningSwordCooldown.containsKey(player.getName()) &&
                    !(this.summoningSwordCooldown.get(player.getName()) < (System.currentTimeMillis() -
                    SummoningSword.cooldownSeconds * 1000))){
                int timeLeft = 30 - (int) Math.ceil((System.currentTimeMillis() - this.summoningSwordCooldown.get(player.getName())) / 1000.0F);
                player.sendMessage(
                        ChatColor.RED + ItemsConstants.CHAT_PREFIX + "You can use this item's ability again in " + timeLeft + "s.");
                return;
            }

            int mobType = new Random().nextInt(3);
            EntityType mob = mobType == 0 ? EntityType.SKELETON : mobType == 1 ? EntityType.ZOMBIE : EntityType.SPIDER;

            Location loc = player.getLocation();

            // use string representations for most accurate thing
            BigDecimal angleBetweenSpawns = BigDecimal.valueOf(360 / SummoningSword.mobsToSummon);
            BigDecimal currentAngle = new BigDecimal("0");
            while (currentAngle.compareTo(new BigDecimal("360")) < 0){
                int radius = 3; // subject to change

                double x = radius * Math.sin(currentAngle.floatValue());
                double z = radius * Math.cos(currentAngle.floatValue());

                Location spawnLoc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
                spawnLoc.add(x, 0, z);

                // prevent npe on separate thread
                final Entity spawnedEntity = player.getWorld().spawnEntity(spawnLoc, mob);

                currentAngle = currentAngle.add(angleBetweenSpawns);

                if (spawnedEntity == null) continue;

                spawnedEntity.setCustomName(ChatColor.GOLD + "5s");
                spawnedEntity.setCustomNameVisible(true);

                SummoningSword.addSpawnedEntity(spawnedEntity, player);

                if (SummoningSword.invulnerable){
                    LegendCore.getInstance().registerTask(spawnedEntity::remove, SummoningSword.despawnAfter);
                }
            }

            this.summoningSwordCooldown.remove(player.getName());
            this.summoningSwordCooldown.put(player.getName(), System.currentTimeMillis());
            flag = true;
        }

        // and cancel the event so that the item cannot really be used / placed
        if (flag){
            event.setCancelled(true);
            event.setUseItemInHand(Event.Result.DENY);
        }
    }

    public static RightClickInteractEvent getInstance(){
        return instance == null ? instance = new RightClickInteractEvent() : instance;
    }
}
