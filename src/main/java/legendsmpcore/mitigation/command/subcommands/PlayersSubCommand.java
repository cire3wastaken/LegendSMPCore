package legendsmpcore.mitigation.command.subcommands;

import legendsmpcore.core.GlobalConstants;
import legendsmpcore.core.Permissions;
import legendsmpcore.core.SubCommand;
import legendsmpcore.core.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Commands to blacklist/un-blacklist IPs & players from participating in helping mitigate hackers,
 * typically used against trolls
 * */
public class PlayersSubCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!commandSender.hasPermission(Permissions.MITIGATION_FORCE_PERM)){
            return;
        }

        boolean flag = false;
        if(args.length == 3){
            Player target = Bukkit.getPlayerExact(args[2]);

            if(target == null){
                commandSender.sendMessage(GlobalConstants.GLOBAL_FAIL_PREFIX + "Unknown player!");
                return;
            }

            if(args[1].equalsIgnoreCase("disallow")){
                PlayerUtils.blacklistPlayer(target);
                commandSender.sendMessage(GlobalConstants.GLOBAL_PREFIX + "Successfully blacklisted " + target.getName());
            } else if(args[1].equalsIgnoreCase("allow")){
                PlayerUtils.allowPlayer(target);
                commandSender.sendMessage(GlobalConstants.GLOBAL_PREFIX + "Successfully allowed " + target.getName());
            } else {
                flag = true;
            }
        } else {
            flag = true;
        }

        if(flag)
            commandSender.sendMessage(GlobalConstants.UNKNOWN_SUBCOMMAND);
    }
}
