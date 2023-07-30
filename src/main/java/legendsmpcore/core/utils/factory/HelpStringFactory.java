package legendsmpcore.core.utils.factory;

import org.bukkit.ChatColor;

public class HelpStringFactory {
    public static class Mitigations {
        public static String makeHelpString(String cmd, String desc){
            return makeSubString("/mitigations " + cmd, desc);
        }

        public static String makeTitleString(String cmd){
            return ChatColor.BOLD + ChatColor.RED.toString() + "Help menu for " + cmd + ": ";
        }

        public static String makeSubString(String cmd, String desc){
            return ChatColor.GOLD + cmd + ChatColor.DARK_GRAY + " - " +
                    ChatColor.WHITE + desc;
        }
    }

    public static class CustomItems {
        public static String makeHelpString(String cmd, String desc){
            return makeSubString("/customitems " + cmd, desc);
        }

        public static String makeTitleString(String cmd){
            return ChatColor.BOLD + ChatColor.RED.toString() + "Help menu for " + cmd + ": ";
        }

        public static String makeSubString(String cmd, String desc){
            return ChatColor.GOLD + cmd + ChatColor.DARK_GRAY + " - " +
                    ChatColor.WHITE + desc;
        }
    }
}
