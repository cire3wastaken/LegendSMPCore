package legendsmpcore.customitems.command.subcommands.server;

import legendsmpcore.core.GlobalConstants;
import legendsmpcore.core.Permissions;
import legendsmpcore.customitems.ItemsConstants;
import legendsmpcore.customitems.CustomItems;
import legendsmpcore.core.SubCommand;
import org.bukkit.command.CommandSender;

public class DisableSubCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!commandSender.hasPermission(Permissions.ITEM_MANAGE_PERM)){
            commandSender.sendMessage(GlobalConstants.PERMISSION_DENIED);
            return;
        }

        CustomItems.getInstance().isEnabled = false;
        commandSender.sendMessage(ItemsConstants.CHAT_PREFIX + "Plugin <Custom Items> has been disabled!");
    }
}
