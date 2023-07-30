package legendsmpcore.customitems.command.subcommands.item;

import legendsmpcore.core.Permissions;
import legendsmpcore.customitems.ItemsConstants;
import legendsmpcore.customitems.CustomItems;
import legendsmpcore.core.SubCommand;
import legendsmpcore.customitems.items.GhastBow;
import legendsmpcore.customitems.items.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class GhastBowCommand implements SubCommand {

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(!commandSender.hasPermission(Permissions.ITEM_GIVE_PERM) && !commandSender.isOp()){
            commandSender.sendMessage(ItemsConstants.PERMISSION_DENIED);
            return;
        }

        boolean flag = false;
        if(strings.length == 3){
            if(strings[1].equalsIgnoreCase("give")) {
                if(this.giveItem(commandSender, strings))
                    commandSender.sendMessage(ItemsConstants.CHAT_PREFIX + "Successfully gave " + strings[2] + " a Ghast Bow!");
            } else {
                flag = true;
            }
        } else if (strings.length == 2) {
            if(strings[1].equalsIgnoreCase("toggle")) {
                boolean temp = CustomItems.getInstance().toggledItems.get(Items.GHASTBOW);
                CustomItems.getInstance().toggledItems.remove(Items.GHASTBOW, temp);
                CustomItems.getInstance().toggledItems.put(Items.GHASTBOW, !temp);

                commandSender.sendMessage(ItemsConstants.CHAT_PREFIX + "Ghast Bows are now " +
                        (temp ? "disabled!" : "enabled!"));
            } else if (strings[1].equalsIgnoreCase("state")){
                commandSender.sendMessage(ItemsConstants.CHAT_PREFIX + "Ghast Bows are " +
                        (CustomItems.getInstance().toggledItems.get(Items.GHASTBOW) ? "enabled!" : "disabled!"));
            } else {
                flag = true;
            }
        } else {
            flag = true;
        }

        if(flag){
            commandSender.sendMessage(ItemsConstants.UNKNOWN_COMMAND);
            commandSender.sendMessage("/customitems help");
        }
    }

    public boolean giveItem(CommandSender commandSender, String[] args) {
        return this.giveItem(commandSender, args, null);
    }

    public boolean giveItem(CommandSender commandSender, String[] args, ItemMeta metaToSave) {
        ItemStack item = new ItemStack(Material.BOW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(GhastBow.nameConfig);
        meta.setLore(GhastBow.lore);

        Player target = Bukkit.getPlayerExact(args[2]);
        if (target == null) {
            commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + args[2] + " is not online!");
            return false;
        }

        if (metaToSave != null) {
            Map<Enchantment, Integer> enchants = metaToSave.getEnchants();
            if (enchants != null) {
                enchants.forEach((enchant, level) -> {
                    meta.addEnchant(enchant, level, false);
                });
            }

            String name = metaToSave.getDisplayName();
            if (name != null) {
                meta.setDisplayName(name);
            }
        }

        item.setItemMeta(meta);
        target.getInventory().addItem(item);
        return true;
    }
}