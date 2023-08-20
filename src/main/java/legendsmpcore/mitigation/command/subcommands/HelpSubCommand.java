package legendsmpcore.mitigation.command.subcommands;

import legendsmpcore.core.GlobalConstants;
import legendsmpcore.core.Permissions;
import legendsmpcore.core.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import static legendsmpcore.core.GlobalConstants.UNKNOWN_SUBCOMMAND;
import static legendsmpcore.core.utils.factory.HelpStringFactory.Mitigations.makeHelpString;
import static legendsmpcore.core.utils.factory.HelpStringFactory.Mitigations.makeSubString;

public class HelpSubCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        boolean unknownSCMD = false;
        if (commandSender.hasPermission(Permissions.ITEM_UPDATE_PERM) || commandSender.isOp()) {
            boolean isOp = commandSender.isOp();
            if (args.length == 1 || args.length == 0) {
                commandSender.sendMessage(GlobalConstants.GLOBAL_PREFIX + "Mitigations Help Menu:");
                commandSender.sendMessage(makeHelpString("help", "Opens this help menu"));
                commandSender.sendMessage("");

                if(commandSender.hasPermission(Permissions.MITIGATION_FORCE_PERM) || isOp){
                    commandSender.sendMessage(makeHelpString("clear", "Clears all active votes"));
                    
                    commandSender.sendMessage(makeHelpString("ip [allow/disallow] <player>",
                            "Disallow or allow an player's IP to vote"));
                    commandSender.sendMessage(makeHelpString("players [allow/disallow] <player>",
                            "Disallow or allow a player to vote"));

                    commandSender.sendMessage(GlobalConstants.GLOBAL_PREFIX +
                            "You have the ability to lock down the server in an emergency!");
                }

                if(commandSender instanceof ConsoleCommandSender){
                    commandSender.sendMessage(makeHelpString("debug <true/false>",
                            "Sets mitigation debug mode"));
                }
            } else if (args.length == 2){
                if(args[0].equalsIgnoreCase("ip")){
                    if(args[1].equalsIgnoreCase("allow")){
                        commandSender.sendMessage(makeSubString("allow <ip>", "Allows an IP to vote"));
                        commandSender.sendMessage(makeSubString("allow <player>", "Calls 'allow <ip>'"));
                    } else if (args[1].equalsIgnoreCase("disallow")){
                        commandSender.sendMessage(makeSubString("disallow <ip>", "Disallows an IP to vote"));
                        commandSender.sendMessage(makeSubString("disallow <player>", "Calls 'disallow <ip>'"));
                    } else {
                        unknownSCMD = true;
                    }
                } else if (args[0].equalsIgnoreCase("players")){
                    if(args[1].equalsIgnoreCase("allow")){
                        commandSender.sendMessage(makeSubString("disallow <player>", "Allows a player to vote"));
                    } else if (args[1].equalsIgnoreCase("disallow")){
                        commandSender.sendMessage(makeSubString("disallow <player>", "Disallows a player to vote"));
                    } else {
                        unknownSCMD = true;
                    }
                } else {
                    unknownSCMD = true;
                }
            }

            if(unknownSCMD)
                commandSender.sendMessage(UNKNOWN_SUBCOMMAND);
        }
    }
}
