package legendsmpcore.customitems.items;

import legendsmpcore.core.utils.ColorUtils;
import legendsmpcore.core.utils.ConfigurationHelper;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WitchScythe {
    public static final List<String> DEFAULT_LORE = Arrays.asList("&6Ability: &eLEFT CLICK",
            "&fInflict &apoison &fon your victims");
    public static double secondsOfEffect;
    public static List<String> lore;
    public static List<String> oldLore;
    public static String name;

    private WitchScythe(){}

    public static void update(FileConfiguration fileConfiguration){
        lore = ColorUtils.color(ConfigurationHelper.getStringList("Items.WitchScythe.Lore", DEFAULT_LORE));
        name = ColorUtils.color(fileConfiguration.getString("Items.WitchScythe.Name", "&aWitch's Scythe"));
        secondsOfEffect = fileConfiguration.getDouble("Items.WitchScythe.Seconds", 3);

        oldLore = ColorUtils.color(ConfigurationHelper.getStringList("Items.WitchScythe.OldLore",
                Collections.singletonList("&cAbility: incur lightning upon your victims")));
    }
}
