package legendsmpcore.mitigation.command.subcommands;

import legendsmpcore.core.GlobalConstants;
import legendsmpcore.core.SubCommand;
import legendsmpcore.mitigation.command.VoteForLockdownCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ClearVotesSubCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ConsoleCommandSender)
            VoteForLockdownCommand.clearAll();
        else
            commandSender.sendMessage(GlobalConstants.GLOBAL_FAIL_PREFIX + ChatColor.RED + "Only console can do this!");
    }
}
