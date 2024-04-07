package legendsmpcore.core.utils;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to help color messages & convert them into colored or back
 * */
public class ColorUtils {
    public static String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public static List<String> color(List<String> lore){
        return lore.stream().map(ColorUtils::color).collect(Collectors.toList());
    }
}
