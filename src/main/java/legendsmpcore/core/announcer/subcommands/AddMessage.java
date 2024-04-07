package legendsmpcore.core.announcer.subcommands;

import legendsmpcore.core.announcer.AnnouncerSubCommand;
import legendsmpcore.core.announcer.MessagesClass;
import legendsmpcore.core.GlobalConstants;
import legendsmpcore.core.utils.ColorUtils;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class AddMessage extends AnnouncerSubCommand {
    private String newMsg;
    @Override
    public String getName() {
        return "addmsg";
    }

    @Override
    public String getDescription() {
        return ColorUtils.color("&aAdd a scheduled message.");
    }

    @Override
    public String getSyntax() {
        return ColorUtils.color("&e/announcer addmsg <message>");
    }

    @Override
    public void perform(Player sender, String[] args) {
        if(args.length > 0){
            String[] split = Arrays.copyOfRange(args, 1, args.length);
            newMsg = String.join(" ", split);
            MessagesClass.addMessage(newMsg);
            sender.sendMessage(GlobalConstants.START_BLOCK);
            sender.sendMessage("Added '" + ColorUtils.color(newMsg) + "' to scheduled announcement list.");
            sender.sendMessage(GlobalConstants.END_BLOCK);
        }else if(args.length == 1){
            sender.sendMessage(ColorUtils.color(GlobalConstants.GLOBAL_FAIL_PREFIX + "&cPlease provide all the required arguments."));
        }
    }
}
