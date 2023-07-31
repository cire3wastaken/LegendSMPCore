package legendsmpcore.mitigation.command.subcommands;

import legendsmpcore.core.GlobalConstants;
import legendsmpcore.core.Permissions;
import legendsmpcore.core.SubCommand;
import legendsmpcore.core.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressSubCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!commandSender.hasPermission(Permissions.MITIGATION_FORCE_PERM) && !commandSender.isOp()){
            return;
        }

        boolean flag = false;
        if(args.length == 3){
            Player player = Bukkit.getPlayerExact(args[2]);

            if(player == null){
                commandSender.sendMessage(GlobalConstants.GLOBAL_FAIL_PREFIX + "Unknown player!");
                return;
            }

            if(args[1].equalsIgnoreCase("disallow")){
                PlayerUtils.blacklistIP(player);
                commandSender.sendMessage(GlobalConstants.GLOBAL_PREFIX +
                    "Successfully blacklisted " + player.getName() + ", with IP " + PlayerUtils.lookUpRealAddress(player));
            } else if(args[1].equalsIgnoreCase("allow")){
                PlayerUtils.allowIP(player);
                commandSender.sendMessage(GlobalConstants.GLOBAL_PREFIX +
                    "Successfully allowed " + player.getName() + ", with IP " + PlayerUtils.lookUpRealAddress(player));
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
