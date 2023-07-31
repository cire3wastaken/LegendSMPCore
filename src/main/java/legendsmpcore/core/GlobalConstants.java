package legendsmpcore.core;

import org.bukkit.ChatColor;

public final class GlobalConstants {
    public static final String GLOBAL_PREFIX = ChatColor.BOLD.toString() + ChatColor.GREEN +
            "[LegendSMP] >> " + ChatColor.RESET;
    public static final String GLOBAL_FAIL_PREFIX = ChatColor.BOLD.toString() + ChatColor.RED +
            "[LegendSMP] >> " + ChatColor.RESET;

    public static final String UNKNOWN_COMMAND = GLOBAL_FAIL_PREFIX + "Unknown command!";
    public static final String UNKNOWN_SUBCOMMAND = GLOBAL_FAIL_PREFIX + "Unknown sub command!";

    public static final String VERSION_FILE_URL = "https://raw.githubusercontent.com/cire3wastaken/legendsmpcorever/main/version.txt";
    public static final String GITHUB_REPO = "https://github.com/cire3wastaken/LegendSmpCore/tree/master";
    public static final String PLUGIN_VERSION = "1.0.0";
    public static final String OUTDATED_MESSAGE = GLOBAL_FAIL_PREFIX +
        "Legend SMP Core is not up to date! Build it yourself from " + GITHUB_REPO + " or download it from Releases!";
}
