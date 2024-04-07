package legendsmpcore.customitems.items;

import legendsmpcore.core.utils.ColorUtils;
import legendsmpcore.core.utils.ConfigurationHelper;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class containing information for the Vampire Blade
 * */
public class VampireBlade {
    public static final List<String> DEFAULT_LORE = Arrays.asList("&6Ability: &eLEFT CLICK",
            "&aHeal &fhalf the &cfinal damage &fyou do to others.");
    public static List<String> lore;
    public static List<String> oldLore;
    public static double toBeHealed;
    public static String name;

    private VampireBlade(){}

    public static void update(FileConfiguration config){
        lore = ColorUtils.color(ConfigurationHelper.getStringList("Items.VampireBlade.Lore", DEFAULT_LORE));
        name = ColorUtils.color(config.getString("Items.VampireBlade.Name", "&4Vampire Blade"));
        toBeHealed = config.getDouble("Items.VampireBlade.Healing", 0.5);

        oldLore = ColorUtils.color(ConfigurationHelper.getStringList("Items.VampireBlade.OldLore",
                Collections.singletonList("&6Ability: gain half as much HP as you do damage")));
    }
}
