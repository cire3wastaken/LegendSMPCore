package legendsmpcore.core.announcer.subcommands;

import legendsmpcore.core.announcer.AnnouncerSubCommand;
import legendsmpcore.core.announcer.MessagesClass;
import legendsmpcore.core.GlobalConstants;
import legendsmpcore.core.utils.ColorUtils;
import org.bukkit.entity.Player;

public class GetInterval extends AnnouncerSubCommand {
    @Override
    public String getName() {
        return "getinterval";
    }

    @Override
    public String getDescription() {
        return ColorUtils.color("&aGet the interval between broadcasts.");
    }

    @Override
    public String getSyntax() {
        return ColorUtils.color("&e/announcer getinterval ");
    }

    @Override
    public void perform(Player sender, String[] args) {
        if(args.length > 0){

            sender.sendMessage(GlobalConstants.GLOBAL_PREFIX + "Current interval: " + MessagesClass.getInterval().toString());

        }
    }
}
