package legendsmpcore.core.announcer.subcommands;

import legendsmpcore.core.announcer.AnnouncerSubCommand;
import legendsmpcore.core.announcer.MessagesClass;
import legendsmpcore.core.GlobalConstants;
import legendsmpcore.core.utils.ColorUtils;
import org.bukkit.entity.Player;

import java.util.List;

public class ListMessages extends AnnouncerSubCommand {
    @Override
    public String getName() {
        return "listmsgs";
    }

    @Override
    public String getDescription() {
        return ColorUtils.color("&aView all currently scheduled announcements.");
    }

    @Override
    public String getSyntax() {
        return ColorUtils.color("&e/announcer listmsgs");
    }

    @Override
    public void perform(Player sender, String[] args) {
        if(args.length > 0){
            List<String> messageList = MessagesClass.getMessages();
            sender.sendMessage(GlobalConstants.START_BLOCK);
            for (String msg : messageList){
                sender.sendMessage(ColorUtils.color(msg));
            }
            sender.sendMessage(GlobalConstants.END_BLOCK);
        }else if(args.length == 1){
            sender.sendMessage(ColorUtils.color(GlobalConstants.GLOBAL_FAIL_PREFIX + "&cPlease provide all the required arguments."));
        }
    }
}
