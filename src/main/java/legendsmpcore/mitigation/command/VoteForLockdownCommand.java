package legendsmpcore.mitigation.command;

import legendsmpcore.core.Permissions;
import legendsmpcore.core.utils.PlayerUtils;
import legendsmpcore.mitigation.discord.AlertDiscord;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import legendsmpcore.mitigation.discord.Level;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VoteForLockdownCommand implements CommandExecutor {
    private static final Map<Player, String> votees = new HashMap<>();
    private static final Set<Player> needConfirm = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!command.getName().equalsIgnoreCase("lockdown")){
            return false;
        }

        if(!(commandSender instanceof Player)){
            commandSender.sendMessage(ChatColor.BOLD + ChatColor.RED.toString() + "Only players can vote!");
            return true;
        }

        Player sender = (Player) commandSender;

        if(commandSender.hasPermission(Permissions.MITIGATION_ALLOWED_PERM) || commandSender.isOp()){
            if(needConfirm.add(sender)){
                commandSender.sendMessage(ChatColor.BOLD + ChatColor.GREEN.toString() +
                        "Please confirm your vote with /lockdown confirm!");
                commandSender.sendMessage("Do be noted, that if your IP has already sent an lockdown vote, " +
                        "your entire IP will be blacklisted from voting!");

                return true;
            } else {
                if(strings.length == 1 && strings[0].equalsIgnoreCase("confirm")){
                    if(this.isAbusing(sender)){
                        commandSender.sendMessage(ChatColor.BOLD.toString() + ChatColor.ITALIC
                                + ChatColor.DARK_RED + "You have been detected to be abusing this function, " +
                                "your IP is now permanently blacklisted!");
                        commandSender.sendMessage("Contact Legend or cire3 to appeal this!");

                        PlayerUtils.blacklistIP(sender);
                        return true;
                    } else {
                        if(commandSender.hasPermission(Permissions.MITIGATION_FORCE_PERM)){
                            AlertDiscord.alertDiscord("Force shutdown by " + sender.getName(), Level.CRITICAL);
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                            return true;
                        }

                        if(votees.containsKey(sender)){
                            commandSender.sendMessage(ChatColor.BOLD + ChatColor.RED.toString() + "You have already voted!");
                            return true;
                        }

                        votees.put(sender, PlayerUtils.lookUpRealAddress(sender));
                        AlertDiscord.alertDiscord(sender.getName() + " voted to shutdown", Level.NORMAL);

                        if(votees.size() >= 10){
                            AlertDiscord.alertDiscord("Force shutdown by vote", Level.CRITICAL);

                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                        }

                        commandSender.sendMessage(ChatColor.GREEN + "Your vote has been successfully registered!");
                    }
                } else {
                    commandSender.sendMessage(ChatColor.BOLD + ChatColor.RED.toString() + "Use /lockdown confirm!");
                    return true;
                }
            }

        } else {
            commandSender.sendMessage(ChatColor.BOLD + ChatColor.RED.toString() + "You are not allowed to vote!");
        }
        return true;

    }

    private boolean isAbusing(Player player){
        return votees.containsValue(PlayerUtils.lookUpRealAddress(player)) && !votees.containsKey(player);
    }

    public static void clearAll(){
        votees.clear();
        needConfirm.clear();
    }


}
