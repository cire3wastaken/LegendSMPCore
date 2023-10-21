package legendsmpcore.core;

import legendsmpcore.customitems.CustomItems;
import org.bukkit.plugin.java.JavaPlugin;

public class LegendSMPCoreInitializer extends JavaPlugin {
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
        LegendCore.getInstance().enable();
    }

    @Override
    public void onDisable(){
        CustomItems.getInstance().disable();
    }
}
