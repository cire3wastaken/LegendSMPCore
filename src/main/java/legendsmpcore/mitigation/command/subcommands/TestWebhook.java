package legendsmpcore.mitigation.command.subcommands;

import javafx.scene.control.Alert;
import legendsmpcore.core.SubCommand;
import legendsmpcore.mitigation.discord.AlertDiscord;
import legendsmpcore.mitigation.discord.Level;
import org.bukkit.command.CommandSender;

public class TestWebhook implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args){
        sender.sendMessage("testing webhook");
        AlertDiscord.alertDiscord("amongus", Level.CRITICAL);
    }
}
