package legendsmpcore.mitigation.command;

import legendsmpcore.core.GlobalConstants;
import legendsmpcore.core.SubCommand;
import legendsmpcore.customitems.command.subcommands.player.PlayerSubCommand;
import legendsmpcore.mitigation.command.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class MitigationCommands implements CommandExecutor {
    public Map<String, SubCommand> subCommands = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        this.execute(commandSender, strings);
        return true;
    }

    public void execute(CommandSender commandSender, String[] strings){
        if(strings.length == 0){
            this.subCommands.get("help").execute(commandSender, strings);
            return;
        }

        SubCommand subCommand = this.subCommands.get(strings[0]);

        if(subCommand == null){
            commandSender.sendMessage(GlobalConstants.UNKNOWN_COMMAND);
            commandSender.sendMessage(GlobalConstants.GLOBAL_FAIL_PREFIX + "/mitigations help");
            return;
        }

        subCommand.execute(commandSender, strings);
    }

    public MitigationCommands(){
        this.subCommands.put("help", new HelpSubCommand());
        this.subCommands.put("ip", new AddressSubCommand());
        this.subCommands.put("players", new PlayersSubCommand());
        this.subCommands.put("clear", new ClearVotesSubCommand());
        this.subCommands.put("testwebhook", new TestWebhook());
    }
}
