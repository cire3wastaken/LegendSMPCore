package legendsmpcore.mitigation.event;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import legendsmpcore.core.discord.AlertDiscord;
import legendsmpcore.core.discord.Level;
import legendsmpcore.mitigation.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.*;

public class SpamBroadcastDetection implements Listener {
    public Map<Character, char[]> possibleBypassChars = new HashMap<>();
    public List<String> blacklistedWords = Arrays.asList("hack", "hacked", "grief", "griefed", "raid", "raided",
            "compromised", "bit.ly", "_cancello", "exploitando", "syre", "cryzen", "zenyph", "mkrelease");

    public static int spamTimeFlagged = 0;
    public static int spamMessageFlagged = 0;

    public static int spamTimeTolerance = 0;
    public static int tolerance = 0;
    public static int spamMessageTolerance = 0;

    public static long lastTimeSpamSent;
    public static long lastTimeFlagged;
    public static String lastMessage;

    @EventHandler(priority = EventPriority.LOW)
    public void handle(PlayerCommandPreprocessEvent event) {
        if(event.getPlayer() instanceof ConsoleCommandSender) return;

        StringBuilder stringBuilder = new StringBuilder();
        boolean isBroadcast = false;
        for(String msg : event.getMessage().split(" ")){
            if(msg.equalsIgnoreCase("/broadcast") || msg.equalsIgnoreCase("/bc") ||
                msg.equalsIgnoreCase("/alert")){
                isBroadcast = true;
            } else {
                stringBuilder.append(msg.toLowerCase());
            }
        }

        if(!isBroadcast)
            return;

        if(event.getPlayer().hasPermission("essentials.broadcast") ||
                event.getPlayer().hasPermission("essentials.broadcastworld")){
            String concatCommand = stringBuilder.toString();

            if (flagMessage(concatCommand)) {
                tolerance++;
                lastTimeSpamSent = System.currentTimeMillis();
                lastTimeFlagged = System.currentTimeMillis();
                lastMessage = concatCommand;
                if (tolerance >= 10 || spamMessageFlagged >= 20 || spamTimeFlagged >= 10) {
                    if(!Constants.isDebugMode()) {
                        for (OfflinePlayer operator : Bukkit.getOperators()) {
                            operator.setOp(false);
                        }
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ipban " + event.getPlayer().getName());
                        AlertDiscord.alertDiscord("Spam broadcast check tripped! Suspected player: " +
                                event.getPlayer().getName() + ". Spam message: " + event.getMessage(), Level.CRITICAL);
                    } else {
                        event.getPlayer().sendMessage(ChatColor.GOLD + "You would've been flagged!");
                    }
                }
            }
        }
    }

    public String cleanString(String text){
        List<Character> message = new ArrayList<>();

        for(char c : text.toCharArray()){
            message.add(c);
        }

        int count = 0;
        for(char c : message){
            for(char[] arr : this.possibleBypassChars.values()){
                for(char ch : arr){
                    if(c == ch){
                        message.set(count, getKeyByValue(arr));
                    }
                }
            }
            count++;
        }

        message.removeIf(character -> character == ' ');

        StringBuilder stringBuilder = new StringBuilder();

        for(Character c : message){
            stringBuilder.append(c);
        }

        return stringBuilder.toString();
    }

    public boolean flagMessage(String message){
        // Using a boolean and checking on end gives the change to flag TWICE, increasing the speed of effectiveness
        boolean flagged = false;

        String msg = message.toLowerCase();

        // Check A (if message contains blacklisted words (before clean, clean sometimes false negatives))
        for(String flag : this.blacklistedWords){
            if (msg.contains(flag)) {
                flagged = true;
                break;
            }
        }

        msg = cleanString(msg);

        // Check B (if cleaned message contains blacklisted words)
        for(String flag : this.blacklistedWords){
            if (msg.contains(flag)) {
                flagged = true;
                break;
            }
        }

        // Check C (Too fast messages)
        if(System.currentTimeMillis() - lastTimeSpamSent < 500){
            ++spamTimeTolerance;
            ++spamTimeFlagged;
            if(spamMessageTolerance >= 5){
                spamTimeTolerance = 0;
                flagged = true;
            }
        }

        // Check D (Spammy text, has a relatively high threshold)
        if(lastMessage.equalsIgnoreCase(msg)){
            ++spamMessageTolerance;
            ++spamMessageFlagged;
            if(spamMessageTolerance >= 5){
                spamMessageTolerance = 0;
                flagged = true;
            }
        }

        return flagged;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleGrief(PlayerCommandPreprocessEvent event){
        //only allow console to use such commands
        if(event.getPlayer() instanceof ConsoleCommandSender) return;

        StringBuilder stringBuilder = new StringBuilder();
        for(String msg : event.getMessage().split(" ")){
            stringBuilder.append(msg.toLowerCase());
        }

        Player player = event.getPlayer();
        String concatCommand = stringBuilder.toString();

        if((concatCommand.contains("//sphere") && player.hasPermission("worldedit.generation.sphere")) ||
                (concatCommand.contains("/fill") && player.hasPermission("worldedit.fill"))){
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
            Vector vec = localPlayer.getPosition();
            RegionManager manager = WorldGuardPlugin.inst().getRegionManager(player.getWorld());
            if(manager == null){
                return;
            }

            ApplicableRegionSet set = manager.getApplicableRegions(vec);
            for(ProtectedRegion region : set){
                if(region.getId().equalsIgnoreCase("spawn")){
                    event.setCancelled(true);
                    AlertDiscord.alertDiscord("Suspicious griefing related commands triggered. Command: " +
                            event.getMessage() + " User: "+ player.getName(), Level.NORMAL);
                }
            }
        }

        if((concatCommand.contains("/powertool") && player.hasPermission("essentials.powertool")) ||
                (concatCommand.contains("/brush") && player.hasPermission("worldedit.brush.sphere")) ||
                (concatCommand.contains("/pt") && player.hasPermission("essentials.powertool"))){
            event.setCancelled(true);
            AlertDiscord.alertDiscord("Suspicious griefing related commands triggered. Command: " +
                    event.getMessage() + " User: "+ player.getName(), Level.NORMAL);
        }
    }

    public SpamBroadcastDetection(){
        this.possibleBypassChars.put('a', "@aA".toCharArray());
        this.possibleBypassChars.put('b', "bB".toCharArray());
        this.possibleBypassChars.put('c', "cCkK".toCharArray());
        this.possibleBypassChars.put('d', "dD".toCharArray());
        this.possibleBypassChars.put('e', "eE3".toCharArray());
        this.possibleBypassChars.put('f', "fF".toCharArray());
        this.possibleBypassChars.put('g', "gG6".toCharArray());
        this.possibleBypassChars.put('h', "hH".toCharArray());
        this.possibleBypassChars.put('i', "iIl!i1".toCharArray());
        this.possibleBypassChars.put('j', "jJ".toCharArray());
        this.possibleBypassChars.put('k', "kKcC".toCharArray());
        this.possibleBypassChars.put('l', "lL1!i".toCharArray());
        this.possibleBypassChars.put('m', "mM".toCharArray());
        this.possibleBypassChars.put('n', "nN".toCharArray());
        this.possibleBypassChars.put('o', "oO0".toCharArray());
        this.possibleBypassChars.put('p', "pP".toCharArray());
        this.possibleBypassChars.put('q', "qQ".toCharArray());
        this.possibleBypassChars.put('r', "rR".toCharArray());
        this.possibleBypassChars.put('s', "sS$5".toCharArray());
        this.possibleBypassChars.put('t', "tT7".toCharArray());
        this.possibleBypassChars.put('u', "uUvV".toCharArray());
        this.possibleBypassChars.put('v', "vVcC".toCharArray());
        this.possibleBypassChars.put('w', "wW".toCharArray());
        this.possibleBypassChars.put('x', "xX".toCharArray());
        this.possibleBypassChars.put('y', "yY".toCharArray());
        this.possibleBypassChars.put('z', "zZ2".toCharArray());
    }

    public Character getKeyByValue(char[] value) {
        for (Map.Entry<Character, char[]> entry : possibleBypassChars.entrySet()) {
            if (Arrays.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }

        return null;
    }
}