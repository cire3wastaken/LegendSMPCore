package legendsmpcore.customitems.command.subcommands.server;

import legendsmpcore.core.GlobalConstants;
import legendsmpcore.core.Permissions;
import legendsmpcore.customitems.ItemsConstants;
import legendsmpcore.customitems.CustomItems;
import legendsmpcore.core.SubCommand;
import org.bukkit.command.CommandSender;

/**
 * Command to enable Custom Items
 * */
public class EnableSubCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!commandSender.hasPermission(Permissions.ITEM_MANAGE_PERM)){
            commandSender.sendMessage(GlobalConstants.PERMISSION_DENIED);
            return;
        }

        CustomItems.getInstance().isEnabled = true;
        CustomItems.getInstance().itemCommands.subCommands.get("reload").execute(commandSender, args);

        commandSender.sendMessage(ItemsConstants.CHAT_PREFIX + "Plugin <Custom Items> is now enabled!");
    }
}
