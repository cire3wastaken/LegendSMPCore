package legendsmpcore.customitems.events;

import legendsmpcore.customitems.ItemsConstants;
import legendsmpcore.customitems.CustomItems;
import legendsmpcore.customitems.items.GhastBow;
import legendsmpcore.customitems.items.Items;
import legendsmpcore.core.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class ShootBowEvent implements Listener {
    @EventHandler
    public void entityShootBow(EntityShootBowEvent event){
        if(!(event.getEntity() instanceof Player)){
            return;
        }

        Player playerShooter = (Player) event.getEntity();

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

        if(PlayerUtils.containsLore(playerShooter.getItemInHand(), GhastBow.lore)
                && playerShooter.getItemInHand().getType().equals(Material.BOW))
        {
            if(CustomItems.getInstance().isDisabled(Items.WITCHSCYHTE)){
                playerShooter.sendMessage(ItemsConstants.DISABLED_ITEM);
                return;
            }

            event.getProjectile().setCustomName("aksjfuaqialfkiaGhastBowShot");
            event.getEntity().setCustomNameVisible(false);
        }
    }
}
