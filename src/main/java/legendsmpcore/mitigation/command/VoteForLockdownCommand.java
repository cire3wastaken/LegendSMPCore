package legendsmpcore.mitigation.command;

import legendsmpcore.core.Permissions;
import legendsmpcore.core.utils.ColorUtils;
import legendsmpcore.core.utils.PlayerUtils;
import legendsmpcore.core.discord.AlertDiscord;
import legendsmpcore.mitigation.event.SpamBroadcastDetection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import legendsmpcore.core.discord.Level;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VoteForLockdownCommand implements CommandExecutor {
    public static long lastVoteTime;
    public static final Map<Player, String> votees = new HashMap<>();
    public static final Set<Player> needConfirm = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!command.getName().equalsIgnoreCase("lockdown")){
            return false;
        }

        if(commandSender instanceof ConsoleCommandSender){
            AlertDiscord.alertDiscord("Force shutdown by console", Level.CRITICAL);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
            return true;
        }

        if(!(commandSender instanceof Player)){
            commandSender.sendMessage(ChatColor.BOLD + ChatColor.RED.toString() + "Only players can vote!");
            return true;
        }

        Player sender = (Player) commandSender;

        if(commandSender.hasPermission(Permissions.MITIGATION_FORCE_PERM)){
            AlertDiscord.alertDiscord("Force shutdown by " + sender.getName(), Level.CRITICAL);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
            lastVoteTime = System.currentTimeMillis();
            return true;
        }


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
                        if(votees.containsKey(sender)){
                            commandSender.sendMessage(ChatColor.BOLD + ChatColor.RED.toString() + "You have already voted!");
                            return true;
                        }

                        votees.put(sender, PlayerUtils.lookUpRealAddress(sender));
                        AlertDiscord.alertDiscord(sender.getName() + " voted to shutdown", Level.NORMAL);
                        lastVoteTime = System.currentTimeMillis();

                        if(votees.size() >= 10){
                            AlertDiscord.alertDiscord("Force shutdown by vote", Level.CRITICAL);

                            for (Player player : Bukkit.getOnlinePlayers()){
                                player.kickPlayer(ColorUtils.color("Server is currently on lockdown mode. Please report this to a moderator in the official discord."));
                            }

                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                        }

                        commandSender.sendMessage(ChatColor.GREEN + "Your vote has been successfully registered!");
                        commandSender.sendMessage(ChatColor.YELLOW + String.valueOf(votees.size()) + " people including you have voted. A total of 10 votes is required to effectively lockdown the server.");
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
        if(System.currentTimeMillis() - lastVoteTime > 60000) {
            votees.clear();
            needConfirm.clear();
        }
        if(System.currentTimeMillis() - SpamBroadcastDetection.lastTimeFlagged > 60000) {
            SpamBroadcastDetection.spamTimeTolerance = 0;
            SpamBroadcastDetection.spamMessageTolerance = 0;
            SpamBroadcastDetection.tolerance = 0;

            SpamBroadcastDetection.lastTimeFlagged = 0;
            SpamBroadcastDetection.spamMessageFlagged = 0;
            SpamBroadcastDetection.spamTimeFlagged = 0;

            SpamBroadcastDetection.lastMessage = "";
            SpamBroadcastDetection.lastTimeSpamSent = 0;
        }
    }
}
