package legendsmpcore.mitigation.command.subcommands;

import legendsmpcore.core.Permissions;
import legendsmpcore.core.SubCommand;
import legendsmpcore.mitigation.command.VoteForLockdownCommand;
import org.bukkit.command.CommandSender;

public class ClearVotesSubCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!commandSender.hasPermission(Permissions.MITIGATION_FORCE_PERM) && !commandSender.isOp()){
            return;
        }

        VoteForLockdownCommand.clearAll();
    }
}
