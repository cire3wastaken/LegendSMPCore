package legendsmpcore.customitems.command.subcommands.server;

import legendsmpcore.core.GlobalConstants;
import legendsmpcore.core.Permissions;
import legendsmpcore.customitems.CustomItems;
import legendsmpcore.core.SubCommand;
import org.bukkit.command.CommandSender;

/**
 * Command to reload Custom Items
 * */
public class ReloadPluginSubCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!commandSender.hasPermission(Permissions.ITEM_MANAGE_PERM)){
            commandSender.sendMessage(GlobalConstants.PERMISSION_DENIED);
            return;
        }

        CustomItems.getInstance().disable();
        CustomItems.getInstance().enable();
    }
}
