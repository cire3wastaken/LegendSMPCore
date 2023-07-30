package legendsmpcore.core;

import legendsmpcore.customitems.CustomItems;
import org.bukkit.plugin.java.JavaPlugin;

public class Initializer extends JavaPlugin {
    private boolean hasBeenInitialized = false;

    @Override
    public void onEnable(){
        if(hasBeenInitialized){
            LegendCore.getInstance().enable();
        } else {
            LegendCore.getInstance().init(this);
            hasBeenInitialized = true;
        }
    }

    @Override
    public void onDisable(){
        CustomItems.getInstance().disable();
    }
}
