package legendsmpcore.customitems.command.subcommands.player;

import legendsmpcore.core.GlobalConstants;
import legendsmpcore.core.Permissions;
import legendsmpcore.customitems.ItemsConstants;
import legendsmpcore.customitems.CustomItems;
import legendsmpcore.core.SubCommand;
import org.bukkit.command.CommandSender;

/**
 * Commands to blacklist/un-blacklist players from using custom items (typically used against dupers/cheaters)
 * */
public class PlayerSubCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!commandSender.hasPermission(Permissions.ITEM_PLAYERS_PERM)){
            commandSender.sendMessage(GlobalConstants.PERMISSION_DENIED);
            return;
        }

        boolean flag = false;

        if(args.length == 3){
            if(args[1].equalsIgnoreCase("disallow")){
                if(CustomItems.getInstance().blacklistedPlayers.add(args[2].toLowerCase())) {
                    commandSender.sendMessage(ItemsConstants.CHAT_PREFIX + "Successfully disallowed " +
                            args[2] + " from using custom items!");
                } else {
                    commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "Player " + args[2] + " is already disallowed!");
                }
            } else if(args[1].equalsIgnoreCase("allow")){
                if(CustomItems.getInstance().blacklistedPlayers.remove(args[2].toLowerCase())) {
                    commandSender.sendMessage(ItemsConstants.CHAT_PREFIX + "Successfully allowed " +
                            args[2] + " to use custom items!");
                } else {
                    commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "Player " + args[2] + " is already allowed!");
                }
            } else {
                flag = true;
            }
        } else if (args.length == 2){
            if(args[1].equalsIgnoreCase("list")){
                commandSender.sendMessage(ItemsConstants.CHAT_PREFIX + "Disallowed players: ");
                int count = 1;
                for(String name : CustomItems.getInstance().blacklistedPlayers){
                    commandSender.sendMessage("#" + count + ": " + name);
                    count++;
                }
            } else {
                flag = true;
            }
        } else {
            flag = true;
        }

        if(flag){
            commandSender.sendMessage(ItemsConstants.UNKNOWN_SUBCOMMAND);
        }
    }
}
