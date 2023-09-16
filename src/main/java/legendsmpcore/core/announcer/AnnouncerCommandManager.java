package legendsmpcore.core.announcer;

import legendsmpcore.core.announcer.subcommands.*;
import legendsmpcore.core.GlobalConstants;
import legendsmpcore.core.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class AnnouncerCommandManager implements CommandExecutor {

    private ArrayList<AnnouncerSubCommand> subCommands = new ArrayList<>();

    public AnnouncerCommandManager(){
        subCommands.add(new GetInterval());
        subCommands.add(new ListMessages());
        subCommands.add(new AddMessage());
//        subCommands.add(new Reloader());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(commandSender instanceof Player){
            Player p = (Player) commandSender;

            if(args.length > 0){
                for(int i = 0; i < getSubCommands().size(); i++){
                    if(args[0].equalsIgnoreCase(getSubCommands().get(i).getName())){
                        getSubCommands().get(i).perform(p, args);
                    }
                }
            }else if(args.length == 0){
                p.sendMessage(GlobalConstants.START_BLOCK);
                for (int i = 0; i < getSubCommands().size(); i++){
                    p.sendMessage(ColorUtils.color("&a" + getSubCommands().get(i).getSyntax() + " &r - &e" + getSubCommands().get(i).getDescription() ));
                }
                p.sendMessage(GlobalConstants.END_BLOCK);
            }
        }

        return true;
    }

    public ArrayList<AnnouncerSubCommand> getSubCommands(){
        return subCommands;
    }
}
