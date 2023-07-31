package legendsmpcore.customitems.command.subcommands.server;

import legendsmpcore.core.Permissions;
import legendsmpcore.customitems.CustomItems;
import legendsmpcore.core.SubCommand;
import org.bukkit.command.CommandSender;

public class ReloadPluginSubCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!commandSender.hasPermission(Permissions.ITEM_MANAGE_PERM) && !commandSender.isOp()){
            return;
        }

        CustomItems.getInstance().disable();
        CustomItems.getInstance().enable();
    }
}
