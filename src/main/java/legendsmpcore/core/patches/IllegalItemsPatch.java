package legendsmpcore.core.patches;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class IllegalItemsPatch implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void handleUse(PlayerInteractEvent event){
        if(event.getItem() == null) return;

        if(!event.getItem().equals(fixItem(event.getItem()))) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);

            int index = 0;
            for(ItemStack item : event.getPlayer().getInventory()){
                if(item != null) {
                    if (!item.equals(fixItem(item))) {
                        event.getPlayer().getInventory().setItem(index, fixItem(item));
                        event.getPlayer().updateInventory();
                    }
                }
                index++;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleConsume(PlayerItemConsumeEvent event){
        if(event.getItem() == null) return;

        if(!event.getItem().equals(fixItem(event.getItem()))) {
            event.setCancelled(true);
            event.getPlayer().updateInventory();

            int index = 0;
            for(ItemStack item : event.getPlayer().getInventory()){
                if(item != null) {
                    if (!item.equals(fixItem(item))) {
                        event.getPlayer().getInventory().setItem(index, fixItem(item));
                        event.getPlayer().updateInventory();
                    }
                }
                index++;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleUpdate(InventoryOpenEvent event){
        if(!(event.getPlayer() instanceof Player)) return;

        Player player = (Player) event.getPlayer();

        int index = 0;
        for(ItemStack item : event.getInventory()){
            if(item != null) {
                if (!item.equals(fixItem(item))) {
                    player.getInventory().setItem(index, fixItem(item));
                    player.updateInventory();
                }
            }
            index++;
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        if(event instanceof EntityDamageByEntityEvent){
            handleDamageByOther((EntityDamageByEntityEvent) event);
            return;
        }

        Player player = (Player) event.getEntity();

        int index = 0;
        for(ItemStack item : player.getInventory()){
            if(item != null) {
                if (!item.equals(fixItem(item))) {
                    player.getInventory().setItem(index, fixItem(item));
                    player.updateInventory();
                }
            }

            index++;
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleDamageByOther(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            int index = 0;
            for (ItemStack item : player.getInventory()) {
                if(item != null) {
                    if (!item.equals(fixItem(item))) {
                        player.getInventory().setItem(index, fixItem(item));
                        player.updateInventory();
                    }
                }
                index++;
            }
        }

        if(event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();

            int index = 0;
            for (ItemStack item : player.getInventory()) {
                if(item != null) {
                    if (!item.equals(fixItem(item))) {
                        player.getInventory().setItem(index, fixItem(item));
                        player.updateInventory();
                    }
                }
                index++;
            }
        }
    }

    @Nullable
    public static ItemStack fixItem(@NotNull ItemStack item){
        return fixItem(item, false);
    }

    /**
     * @param removeItem - Whether to delete the item, defaults: remove item if less than 0, else set to max, remove bad enchants
     * */
    @Nullable
    public static ItemStack fixItem(@NotNull ItemStack item, boolean removeItem){
        if(item.getAmount() <= 0){
            return null;
        }

        if(item.getAmount() > item.getMaxStackSize()){
            item.setAmount(item.getMaxStackSize());
        }

        return item;
    }

    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
    public @interface NotNull {
    }
}
