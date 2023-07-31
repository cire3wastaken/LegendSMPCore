package legendsmpcore.core.check.checks.ip;

import legendsmpcore.core.check.Check;
import legendsmpcore.core.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simplistic check for spoofed and invalid IPs
 * */
public class TypeB extends Check {
    public String monitorAddress;
    public String lowAddress;

    public TypeB(Player player, String monitorAddress, String lowAddress) {
        super(player, "IP Spoof", "B", Severity.MEDIUM, Punishment.KICK);
        this.monitorAddress = monitorAddress;
        this.lowAddress = lowAddress;
    }

    @Override
    public void debug(){
        for(Player player1 : Bukkit.getOnlinePlayers()){
            if(player1.hasPermission(Permissions.GLOBAL_ALERTS_PERM) || player1.isOp()){
                player1.sendMessage(String.format("Low IP Address: %s", this.lowAddress));
                player1.sendMessage(String.format("Monitor IP Address: %s", this.monitorAddress));
            }
        }
    }

    @Override
    public void check(){
        if(!isValidIPAddress(monitorAddress) && !isValidIPAddress(lowAddress)){
            flag();
            debug();
        }
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

    public static class Factory {
        private String lowAddress;
        private String monitorAddress;
        private Player player;

        public void setPlayer(Player player){
            this.player = player;
        }

        public void setLowAddress(String lowAddress) {
            this.lowAddress = lowAddress;
        }

        public void setMonitorAddress(String monitorAddress) {
            this.monitorAddress = monitorAddress;
        }

        public TypeB build(){
            return new TypeB(this.player, this.lowAddress, this.monitorAddress);
        }

        public void reset(){
            this.lowAddress = null;
            this.monitorAddress = null;
            this.player = null;
        }
    }
}
