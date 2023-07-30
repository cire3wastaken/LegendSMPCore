package legendsmpcore.core.check;

import legendsmpcore.core.Permissions;
import legendsmpcore.core.utils.PlayerUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Check {
    public Player player;
    public String name;
    public String type;
    public Severity severity;
    public Punishment punishment;
    public String format = ChatColor.GOLD + "LegendSMP" + ChatColor.DARK_GRAY +">>" +
            ChatColor.RESET + " %s failed %s (Check %s) (Severity %s) " + "[1/1]";

    public Check(Player player, String name, String type, Severity severity, Punishment punishment) {
        this.player = player;
        this.name = name;
        this.type = type;
        this.severity = severity;
        this.punishment = punishment;
    }

    public void check(){

    }

    public void debug(){

    }

    public void flag(){
        flag(this.format);
    }

    public void flag(String format){
        for(Player player1 : Bukkit.getOnlinePlayers()){
            if(player1.hasPermission(Permissions.GLOBAL_ALERTS_PERM) || player1.isOp()){
                player1.sendMessage(String.format(format,
                        player1, name, type, severity));
            }
        }
        punish();
    }

    public void punish(){
        switch (this.punishment){
            case BAN: {
                Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(),
                        String.format("You have been banned for %s (Type %s)",
                        this.name, this.type), null, "LegendSMP");
            }
            case KICK: {
                player.kickPlayer(String.format("You have been kicked for %s (Type %s)",
                        this.name, this.type));
            }
            case BLACKLIST: {
                PlayerUtils.blacklistIP(player);
            }
        }
    }

    public enum Severity {
        HIGH(3),
        MEDIUM(2),
        LOW(1);

        public final int severity;
        Severity(int severity){
            this.severity = severity;
        }
    }

    public enum Punishment {
        BLACKLIST("blacklist"),
        NONE("none"),
        KICK("kick"),
        BAN("ban");

        public final String punishment;
        Punishment(String punishment){
            this.punishment = punishment;
        }
    }
}
