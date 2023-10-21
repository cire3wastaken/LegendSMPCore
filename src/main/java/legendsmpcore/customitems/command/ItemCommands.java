package legendsmpcore.customitems.command;

import legendsmpcore.core.Permissions;
import legendsmpcore.core.SubCommand;
import legendsmpcore.customitems.command.subcommands.item.*;
import legendsmpcore.customitems.command.subcommands.player.HelpSubCommand;
import legendsmpcore.customitems.command.subcommands.player.PlayerSubCommand;
import legendsmpcore.customitems.command.subcommands.server.DisableSubCommand;
import legendsmpcore.customitems.command.subcommands.server.EnableSubCommand;
import legendsmpcore.customitems.command.subcommands.server.RegionSubCommand;
import legendsmpcore.customitems.command.subcommands.server.ReloadPluginSubCommand;
import legendsmpcore.customitems.ItemsConstants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.json.simple.JSONObject;

import java.util.*;

import static legendsmpcore.customitems.CustomItems.*;

public class ItemCommands implements CommandExecutor {
    public final Map<String, SubCommand> subCommands = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!command.getName().equalsIgnoreCase("customitems")){
            return false;
        }

        this.execute(commandSender, strings);
        return true;
    }

    public void execute(CommandSender commandSender, String[] args){
        if(args.length == 0){
            this.subCommands.get("help").execute(commandSender, args);
            return;
        }

        SubCommand subCommand = this.subCommands.get(args[0]);

        if(subCommand == null){
            commandSender.sendMessage(ItemsConstants.UNKNOWN_COMMAND);
            commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "/customitems help");
            return;
        }

        boolean flag = false;
        if(subCommand instanceof EnableSubCommand || args[0].equalsIgnoreCase("enable")){
            subCommand.execute(commandSender, args);
        }

        if(getInstance().isEnabled) {
            subCommand.execute(commandSender, args);
        } else {
            flag = true;
        }
        if(flag){
            commandSender.sendMessage(ItemsConstants.DISABLED_MESSAGE);
        }
    }

    public ItemCommands(){
        this.subCommands.put("ghastbow", new GhastBowCommand()); // FINISHED
        this.subCommands.put("hyperion", new HypeCommand()); // FINISHED
        this.subCommands.put("thorhammer", new ThorHammerCommand()); // FINISHED
        this.subCommands.put("vampireblade", new VampireBladeCommand()); // FINISHED
        this.subCommands.put("witchscythe", new WitchScytheCommand()); // FINISHED
        this.subCommands.put("summoningsword", new SummoningSwordCommand()); // TEST

        this.subCommands.put("help", new HelpSubCommand()); // FINISHED

        this.subCommands.put("reload", new ReloadPluginSubCommand()); // FINISHED
        this.subCommands.put("enable", new EnableSubCommand()); // FINISHED
        this.subCommands.put("disable", new DisableSubCommand()); // FINISHED

        this.subCommands.put("players", new PlayerSubCommand()); // FINISHED

        this.subCommands.put("regions", new RegionSubCommand()); // FINISHED

        this.subCommands.put("rename", new RenameItemCommand()); // secret
    }

    public static class TabCompleter implements org.bukkit.command.TabCompleter {
        // Map<Command, Permission>
        public static final Map<String, String> levelOneCommands = new HashMap<>();

        // Map<SubCommand, ParentCommand>
        public static final Map<String, String> levelTwoCommands = new HashMap<>();

        // Map<SubCommand, ParentCommand>
        public static final Map<String, String> levelThreeCommands = new HashMap<>();

        @Override
        public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
            Set<String> subcommands = new HashSet<>();
            Set<String> applicableSubcommands = new HashSet<>();

            switch (strings.length){
                case 1: subcommands = levelOneCommands.keySet(); break;
                case 2: StringUtil.copyPartialMatches(strings[1], levelTwoCommands.keySet(), subcommands); break;
                case 3: StringUtil.copyPartialMatches(strings[1], levelThreeCommands.keySet(), subcommands); break;
            }

            if(strings.length == 1) {
                for (String cmd : subcommands) {
                    if (commandSender.hasPermission(levelOneCommands.get(cmd))){
                        applicableSubcommands.add(cmd);
                    }
                }
            } else if (strings.length == 2){
                for (String cmd : subcommands){
                    if(levelTwoCommands.get(cmd).equalsIgnoreCase("item")
                            && commandSender.hasPermission(Permissions.ITEM_GIVE_PERM))
                    {
                        applicableSubcommands.add(cmd);
                    } else if (commandSender.hasPermission(levelOneCommands.get(levelTwoCommands.get(cmd)))){
                        applicableSubcommands.add(cmd);
                    }
                }
            } else if (strings.length == 3){
                for (String cmd : subcommands){
                    if (commandSender.hasPermission(levelOneCommands.get(levelThreeCommands.get(cmd)))){
                        applicableSubcommands.add(cmd);
                    }
                }
            }

            return new ArrayList<>(applicableSubcommands);
        }

        public TabCompleter(){
            levelOneCommands.put("hyperion", Permissions.ITEM_GIVE_PERM);
            levelOneCommands.put("ghastbow", Permissions.ITEM_GIVE_PERM);
            levelOneCommands.put("thorhammer", Permissions.ITEM_GIVE_PERM);
            levelOneCommands.put("vampireblade", Permissions.ITEM_GIVE_PERM);
            levelOneCommands.put("witchscythe", Permissions.ITEM_GIVE_PERM);

            levelOneCommands.put("help", Permissions.ITEM_UPDATE_PERM);

            levelOneCommands.put("reload", Permissions.ITEM_MANAGE_PERM);
            levelOneCommands.put("disable", Permissions.ITEM_MANAGE_PERM);
            levelOneCommands.put("enable", Permissions.ITEM_MANAGE_PERM);

            levelOneCommands.put("players", Permissions.ITEM_PLAYERS_PERM);

            levelOneCommands.put("regions", Permissions.ITEM_REGIONS_PERM);


            levelTwoCommands.put("toggle", "item");
            levelTwoCommands.put("give", "item");

            levelTwoCommands.put("whitelist", "regions");
            levelTwoCommands.put("blacklist", "regions");
            levelTwoCommands.put("world", "regions");

            levelTwoCommands.put("disallow", "players");
            levelTwoCommands.put("allow", "players");
            levelTwoCommands.put("list", "players");

            levelThreeCommands.put("add", "regions");
            levelThreeCommands.put("remove", "regions");
            levelThreeCommands.put("list", "regions");
        }
    }
}
