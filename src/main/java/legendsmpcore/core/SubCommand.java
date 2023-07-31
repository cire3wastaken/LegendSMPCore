package legendsmpcore.core;

import org.bukkit.command.CommandSender;

public interface SubCommand{
    void execute(CommandSender commandSender, String[] args);
}
