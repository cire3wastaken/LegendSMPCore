package legendsmpcore.customitems.items;

import legendsmpcore.core.utils.ColorUtils;
import legendsmpcore.core.utils.ConfigurationHelper;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ThorHammer {
    public static final List<String> DEFAULT_LORE = Arrays.asList("&6Ability: &eLEFT CLICK",
            "&fStrike &elightning &fon your target");
    public static List<String> lore;
    public static List<String> oldLore;
    public static boolean ignoreArmor;
    public static String name;
    public static double damage;
    public static double fireTicks;

    private ThorHammer(){}

    public static void update(FileConfiguration config){
        lore = ColorUtils.color(ConfigurationHelper.getStringList("Items.ThorHammer.Lore", DEFAULT_LORE));
        name = ColorUtils.color(config.getString("Items.ThorHammer.Name", "&bThor's Hammer"));
        ignoreArmor = config.getBoolean("Items.ThorHammer.IgnoreArmor", false);
        fireTicks = config.getDouble("Items.ThorHammer.FireSeconds", 10) * 20;
        damage = config.getDouble("Items.ThorHammer.Damage", 3.0);

        oldLore = ColorUtils.color(ConfigurationHelper.getStringList("Items.ThorHammer.OldLore",
                Collections.singletonList("&cAbility: incur lightning upon your victims")));
    }
}