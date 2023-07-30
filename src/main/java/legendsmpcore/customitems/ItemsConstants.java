package legendsmpcore.customitems;

import org.bukkit.ChatColor;

public class ItemsConstants {
    public static final String CHAT_PREFIX = ChatColor.BOLD.toString() + ChatColor.GREEN +
            "[CustomItems] >> " + ChatColor.RESET;
    public static final String FAIL_PREFIX = ChatColor.BOLD.toString() + ChatColor.RED + "[CustomItems] >> " + ChatColor.RESET;
    public static final String DISABLED_MESSAGE = FAIL_PREFIX + "Plugin <CustomItems> is currently disabled!";
    public static final String PERMISSION_DENIED = FAIL_PREFIX + "You do not have permissions!";
    public static final String UNKNOWN_COMMAND = FAIL_PREFIX + "Unknown command!";
    public static final String UNKNOWN_SUBCOMMAND = FAIL_PREFIX + "Unknown sub command!";
    public static final String CAN_NOT_USE = FAIL_PREFIX + "You can not use this item here!";
    public static final String BLACKLISTED = FAIL_PREFIX + "You are blacklisted from using custom items!";
    public static final String DISABLED_ITEM = FAIL_PREFIX + "This item is disabled!";
}
