package legendsmpcore.core;

import legendsmpcore.customitems.CustomItems;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Entry point of the entire plugin
 * */
public class LegendSMPCoreInitializer extends JavaPlugin {
    /**
     * One-time switch to determine if plugin is re-enabled or being initialized
     * */
    public static boolean flag;

    @Override
    public void onLoad(){
//        LegendCore.getInstance().init(this);
        // this causes issues, no clue why LMAO
    }

    @Override
    public void onEnable(){
        if (!flag)
            LegendCore.getInstance().init(this);

        flag = true;

        // delegate enabling the plugin
        LegendCore.getInstance().enable();
    }

    @Override
    public void onDisable(){
        // DON'T DISABLE PROTECTION & MITIGATIONS ONLY CUSTOM ITEMS
        CustomItems.getInstance().disable();
    }
}
