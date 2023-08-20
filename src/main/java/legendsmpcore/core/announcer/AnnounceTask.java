package legendsmpcore.core.announcer;

import legendsmpcore.core.GlobalConstants;
import legendsmpcore.core.LegendCore;
import legendsmpcore.core.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class AnnounceTask extends BukkitRunnable {

    int currentIndex = 0;
    LegendCore plugin;

    public AnnounceTask(LegendCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run(){
        List<String> messageList = MessagesClass.getMessages();

        if(messageList.size() == 0){
            MessagesClass.addMessage(null);
        }

        if (messageList.get(currentIndex) != null){
            Bukkit.broadcastMessage(GlobalConstants.START_BLOCK);
            Bukkit.broadcastMessage(ColorUtils.color(messageList.get(currentIndex)));
            Bukkit.broadcastMessage(GlobalConstants.END_BLOCK);
        }


        if(currentIndex == messageList.size()-1){
            currentIndex = 0;
        }else {
            currentIndex++;
        }

    }
}
