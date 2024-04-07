package legendsmpcore.core.check.checks.ip;

import legendsmpcore.core.check.Check;
import legendsmpcore.core.check.CheckManager;
import org.bukkit.entity.Player;

/**
 * Simplistic check to see if a plugin is tampering with IP, does not have an actual punishment
 * */
public class TypeA extends Check {
    // address recorded by event at LOW prio
    public final String lowAddress;
    // address recorded by event at NORMAL prio
    public final String normalAddress;
    // address recorded by event at HIGH prio
    public final String highAddress;
    // address recorded by event at MONITOR prio
    public final String monitorAddress;

    public TypeA(String lowAddress, String normalAddress, String highAddress, String monitorAddress, Player player) {
        super(player, "IP Spoof", "A", Severity.HIGH, Punishment.NONE);
        this.lowAddress = lowAddress;
        this.normalAddress = normalAddress;
        this.highAddress = highAddress;
        this.monitorAddress = monitorAddress;
    }

    @Override
    public void check(){
        if(!lowAddress.equalsIgnoreCase(normalAddress) || !normalAddress.equalsIgnoreCase(highAddress) ||
                !highAddress.equalsIgnoreCase(monitorAddress)){
            CheckManager.isAddressBeingTampered = true;
        }
    }

    /**
     * Generic factory class to help with creating parent class
     * */
    public static class Factory {
        private String lowAddress;
        private String normalAddress;
        private String highAddress;
        private String monitorAddress;
        private Player player;

        public void setPlayer(Player player){
            this.player = player;
        }

        public void setLowAddress(String lowAddress) {
            this.lowAddress = lowAddress;
        }

        public void setNormalAddress(String normalAddress) {
            this.normalAddress = normalAddress;
        }

        public void setHighAddress(String highAddress) {
            this.highAddress = highAddress;
        }

        public void setMonitorAddress(String monitorAddress) {
            this.monitorAddress = monitorAddress;
        }

        public TypeA build(){
            return new TypeA(this.lowAddress, this.normalAddress, this.highAddress, this.monitorAddress, this.player);
        }

        public void reset(){
            this.lowAddress = null;
            this.normalAddress = null;
            this.highAddress = null;
            this.monitorAddress = null;
            this.player = null;
        }
    }
}
