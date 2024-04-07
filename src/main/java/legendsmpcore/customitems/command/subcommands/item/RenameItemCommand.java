package legendsmpcore.customitems.command.subcommands.item;

import legendsmpcore.core.Permissions;
import legendsmpcore.customitems.ItemsConstants;
import legendsmpcore.core.SubCommand;
import legendsmpcore.core.utils.ColorUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * (Secret) Command to rename held item
 * */
public class RenameItemCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof Player)) return;
        if(!commandSender.hasPermission(Permissions.COLORCHAT_MANAGE_PERM)) return;
        Player player = (Player) commandSender;
        if(player.getItemInHand() == null) return;

        if(args.length > 1) {
            if (args[0].equalsIgnoreCase("rename")) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String string : args) {
                    if (!string.equalsIgnoreCase("rename"))
                        stringBuilder.append(ColorUtils.color(string)).append(" ");
                }

                ItemStack currentItem = player.getItemInHand();
                ItemMeta meta = currentItem.getItemMeta();
                meta.setDisplayName(stringBuilder.toString().trim());
                currentItem.setItemMeta(meta);
            } else {
                commandSender.sendMessage(ItemsConstants.UNKNOWN_SUBCOMMAND);
            }
        } else {
            commandSender.sendMessage(ItemsConstants.UNKNOWN_COMMAND);
        }
    }
}
