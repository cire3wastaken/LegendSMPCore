package legendsmpcore.mitigation.command.subcommands;

import legendsmpcore.core.SubCommand;
import legendsmpcore.mitigation.Constants;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class DebugSubCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ConsoleCommandSender) {
            if (args.length >= 2) {
                Constants.setDebugMode(Boolean.parseBoolean(args[1]));
                commandSender.sendMessage(ChatColor.GRAY + "Mitigations debug mode: " + ChatColor.GREEN + Boolean.parseBoolean(args[1]));
            }
        }
    }
}
