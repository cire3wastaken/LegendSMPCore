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
                if(isValidIPAddress(args[2])){
                    Set<Player> players = PlayerUtils.getPlayersByAddress(args[2]);
                    for(Player pl : players){
                        PlayerUtils.blacklistIP(pl);
                        commandSender.sendMessage(GlobalConstants.GLOBAL_PREFIX +
                                "Successfully blacklisted " + pl.getName() + ", with IP " + args[2]);
                    }
                } else {
                    PlayerUtils.blacklistIP(player);
                    commandSender.sendMessage(GlobalConstants.GLOBAL_PREFIX +
                        "Successfully blacklisted " + player.getName() + ", with IP " + PlayerUtils.lookUpRealAddress(player));
                }
            } else if(args[1].equalsIgnoreCase("allow")){
                if(isValidIPAddress(args[2])){
                    Set<Player> players = PlayerUtils.getPlayersByAddress(args[2]);
                    for(Player pl : players){
                        PlayerUtils.allowIP(pl);
                        commandSender.sendMessage(GlobalConstants.GLOBAL_PREFIX +
                                "Successfully allowed " + pl.getName() + ", with IP " + args[2]);
                    }
                } else {
                    PlayerUtils.allowIP(player);
                    commandSender.sendMessage(GlobalConstants.GLOBAL_PREFIX +
                        "Successfully allowed " + player.getName() + ", with IP " + PlayerUtils.lookUpRealAddress(player));
                }
            } else {
                flag = true;
            }
        } else {
            flag = true;
        }

        if(flag)
            commandSender.sendMessage(GlobalConstants.UNKNOWN_SUBCOMMAND);
    }

    public static boolean isValidIPAddress(String ip)
    {
        String zeroTo255 = "(\\d{1,2}|(0|1)\\" + "d{2}|2[0-4]\\d|25[0-5])";
        String regex= zeroTo255 + "\\."+ zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
        Pattern p = Pattern.compile(regex);
        if (ip == null)
            return false;

        Matcher m = p.matcher(ip);
        return m.matches();
    }
}
