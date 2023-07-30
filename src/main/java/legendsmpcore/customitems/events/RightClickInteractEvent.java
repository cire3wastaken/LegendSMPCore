package legendsmpcore.customitems.events;

import legendsmpcore.customitems.ItemsConstants;
import legendsmpcore.customitems.items.Hyperion;
import legendsmpcore.core.utils.DamageUtils;
import legendsmpcore.core.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class RightClickInteractEvent implements Listener {
    public final HashMap<String, Long> cooldownsForPlayer = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            return;
        }
        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR)){
            return;
        }

        Player player = event.getPlayer();
        if(player.getItemInHand() == null) {
            return;
        }
        if(player.getItemInHand().getType() != Material.IRON_SPADE){
            return;
        }
        if(!player.getItemInHand().hasItemMeta()) {
            return;
        }
        if(!player.getItemInHand().getItemMeta().hasLore()){
            return;
        }
        if(!player.getItemInHand().getItemMeta().getLore().equals(Hyperion.lore)){
            return;
        }

        if(!PlayerUtils.shouldUse(player))
        {
            player.sendMessage(ChatColor.RED + ItemsConstants.CHAT_PREFIX + "You can not use that item here!");
            return;
        }

        int level = 0;
        boolean alreadyHadAbsorption = false;

        if(this.hasCooldown(player)){
            int timeLeft = (int) Math.ceil(this.cooldownsForPlayer.get(player.getName()) / 1000.0F);
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
        this.activateCooldown(player);

        for(LivingEntity entity : PlayerUtils.getNearbyLivingEntities(player,
                Hyperion.explosionRadius))
        {
            entity.damage(Hyperion.damage);
        }

        // and cancel the event so that the item cannot really be used / placed
        event.setCancelled(true);
        event.setUseItemInHand(Event.Result.DENY);
    }

    public boolean hasCooldown(Player player){
        return !(this.cooldownsForPlayer.get(player.getName()) < (System.currentTimeMillis() -
            Hyperion.cooldownSeconds * 1000));
    }

    public void activateCooldown(Player player){
        this.cooldownsForPlayer.remove(player.getName());
        this.cooldownsForPlayer.put(player.getName(), System.currentTimeMillis());
    }
}
