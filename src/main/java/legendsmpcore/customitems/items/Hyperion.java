package legendsmpcore.customitems.items;

import legendsmpcore.core.utils.ColorUtils;
import legendsmpcore.core.utils.ConfigurationHelper;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Hyperion {
    public static final List<String> DEFAULT_LORE = Arrays.asList("&6Item Ability: &eRIGHT CLICK",
            "&fTeleports &a10 blocks &fahead of you. Then implode dealing damage to nearby enemies.",
            "&fAlso applies X% of this item's final damage as an &6Absorption &fshield.",
            "&6Cooldown: 30s");
    public static List<String> lore;
    public static List<String> oldLore;
    public static String name;
    public static double damage;
    public static double explosionPower;
    public static double explosionRadius;
    public static double cooldownSeconds;
    public static double percentage;
    public static double shieldDurationTicks;
    public static boolean ignoreArmor;

    private Hyperion(){}

    public static void update(FileConfiguration config){
        lore = ColorUtils.color(ConfigurationHelper.getStringList("Items.Hyperion.Lore", DEFAULT_LORE));
        name = ColorUtils.color(config.getString("Items.Hyperion.Name", "&dHyperion"));
        explosionPower = config.getDouble("Items.Hyperion.Power", 3.0);
        explosionRadius = config.getDouble("Items.Hyperion.Radius", 6.0);
        damage = config.getDouble("Items.Hyperion.Damage",  10.0);
        cooldownSeconds = config.getDouble("Items.Hyperion.Cooldown", 30.0);
        percentage = config.getDouble("Items.Hyperion.Amount", 0.25);
        ignoreArmor = config.getBoolean("Items.Hyperion.IgnoreArmor", false);
        shieldDurationTicks = config.getDouble("Items.Hyperion.Duration", 5) * 20;

        oldLore = ColorUtils.color(ConfigurationHelper.getStringList("Items.Hyperion.OldLore",
                Collections.singletonList("temp")));
    }
}
