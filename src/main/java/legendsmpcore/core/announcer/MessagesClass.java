package legendsmpcore.core.announcer;

import legendsmpcore.core.LegendCore;
import legendsmpcore.core.utils.ConfigurationHelper;

import java.io.IOException;
import java.util.List;

import static org.bukkit.Bukkit.getLogger;

/**
 * Manages announcements via saving and loading them
 * */
public class MessagesClass {
    public static Long interval = new Long(LegendCore.getInstance().getConfig().getInt("Announcer.Interval", 300));
    public static List<String> messages = LegendCore.getInstance().getConfig().getStringList("Announcer.Messages");

    public static List<String> getMessages(){
        return messages;
    }

    public static void addMessage(String newMessage){
        messages.add(newMessage);
        List<String> yes = ConfigurationHelper.getStringList("Announcer.Messages", messages);
        LegendCore.getInstance().getConfig().set("Announcer.Messages", messages);
        try {
            LegendCore.getInstance().getConfig().save(LegendCore.getInstance().getFile());
        }catch (IOException e){
            getLogger().info(e.toString());
        }
    }


    public static Long getInterval(){
        return interval;
    }

    /* Still in dev.
    public static void setInterval(String newInterval){
        interval = Long.parseLong(newInterval);
        LegendCore.getInstance().getConfig().set("Announcer.Interval", 10);
        try {
            LegendCore.getInstance().getConfig().save(LegendCore.getInstance().getFile());
        }catch (IOException e){
            getLogger().info(e.toString());
        }

        LegendCore.getInstance().init(LegendCore.getInstance().getPlugin());
    }
    */

}

