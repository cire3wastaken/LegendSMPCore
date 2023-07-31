package legendsmpcore.mitigation.event;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.*;

public class SpamBroadcastDetection implements Listener {
    public Map<Character, char[]> possibleBypassChars = new HashMap<>();
    public List<String> blacklistedWords = Arrays.asList("hack", "hacked", "grief", "griefed", "raid", "raided",
            "compromised", "bit.ly");

    public int tolerance = 0;
    public long lastTimeSpamSent;
    public String lastMessage;

    @EventHandler(priority = EventPriority.LOW)
    public void handle(PlayerCommandPreprocessEvent event) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isBroadcast = false;
        for(String msg : event.getMessage().split(" ")){
            if(msg.equalsIgnoreCase("broadcast") || msg.equalsIgnoreCase("bc")){
                isBroadcast = true;
            } else {
                stringBuilder.append(msg);
            }
        }

        if(!isBroadcast){
            return;
        }

        if (flagMessage(stringBuilder.toString(), System.currentTimeMillis())) {
            this.tolerance++;
            this.lastTimeSpamSent = System.currentTimeMillis();
            this.lastMessage = stringBuilder.toString();
            if (this.tolerance >= 10) {
                for (OfflinePlayer operator : Bukkit.getOperators()) {
                    operator.setOp(false);
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ipban " + event.getPlayer().getName());
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

    public boolean flagMessage(String message, long l){
        String msg = message.toLowerCase();

        // Check A
        for(String flag : this.blacklistedWords){
            if(msg.contains(flag)){
                return true;
            }
        }

        msg = cleanString(msg);

        // Check B
        for(String flag : this.blacklistedWords){
            if(msg.contains(flag)){
                return true;
            }
        }

        // Check C
        if(l - this.lastTimeSpamSent < 1000){
            return true;
        }

        // Check D
        return message.equalsIgnoreCase(this.lastMessage);
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