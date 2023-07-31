package legendsmpcore.customitems.command;

import legendsmpcore.core.Permissions;
import legendsmpcore.customitems.command.subcommands.item.GhastBowCommand;
import legendsmpcore.customitems.command.subcommands.item.ThorHammerCommand;
import legendsmpcore.customitems.command.subcommands.item.VampireBladeCommand;
import legendsmpcore.customitems.command.subcommands.item.WitchScytheCommand;
import legendsmpcore.customitems.ItemsConstants;
import legendsmpcore.customitems.CustomItems;
import legendsmpcore.customitems.items.GhastBow;
import legendsmpcore.customitems.items.ThorHammer;
import legendsmpcore.customitems.items.VampireBlade;
import legendsmpcore.customitems.items.WitchScythe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ConvertCommand implements CommandExecutor {

    public static final List<List<String>> OLD_LORE = Arrays.asList(VampireBlade.oldLore, ThorHammer.oldLore,
            GhastBow.oldLore, WitchScythe.oldLore);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!CustomItems.getInstance().isEnabled){
            commandSender.sendMessage(ItemsConstants.DISABLED_MESSAGE);
            return true;
        }

        if(!command.getName().equalsIgnoreCase("updateitem")) {
            commandSender.sendMessage(ItemsConstants.UNKNOWN_COMMAND);
            return false;
        }
        if(strings.length != 0){
            commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "This command requires no arguments");
            return false;
        }

        if(commandSender.hasPermission(Permissions.ITEM_UPDATE_PERM) || commandSender.isOp()){
            if(commandSender instanceof Player){
                Player target = (Player) commandSender;
                if(target.getItemInHand() == null || !target.getItemInHand().hasItemMeta() ||
                        !target.getItemInHand().getItemMeta().hasLore())
                {
                    commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "Please hold an old item to update!");
                    return true;
                }

                ItemMeta meta = target.getItemInHand().getItemMeta();
                for(List<String> lore : OLD_LORE){
                    if(meta.getLore().equals(lore)){
                        if(lore.equals(VampireBlade.oldLore)){
                            ((VampireBladeCommand) CustomItems.getInstance().itemCommands.subCommands.get("vampireblade"))
                                .giveItem(commandSender, new String[]{ "", "", target.getName()}
                            );
                        } else if (lore.equals(ThorHammer.oldLore)){
                            ((ThorHammerCommand) CustomItems.getInstance().itemCommands.subCommands.get("thorhammer"))
                                .giveItem(commandSender, new String[]{ "", "", target.getName()}
                            );
                        } else if (lore.equals(GhastBow.oldLore)){
                            ((GhastBowCommand) CustomItems.getInstance().itemCommands.subCommands.get("ghastbow"))
                                .giveItem(commandSender, new String[]{ "", "", target.getName()}
                            );
                        } else if (lore.equals(WitchScythe.oldLore)){
                            ((WitchScytheCommand) CustomItems.getInstance().itemCommands.subCommands.get("witchscythe"))
                                .giveItem(commandSender, new String[]{ "", "", target.getName()}
                            );
                        }
                    }
                }

                commandSender.sendMessage(ItemsConstants.FAIL_PREFIX +
                        "Failed to update item, ensure you are holding an old item!");
            }
        } else {
            commandSender.sendMessage(ItemsConstants.PERMISSION_DENIED);
        }
        return true;
    }
}
