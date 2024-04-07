package legendsmpcore.customitems.command.subcommands.player;

import legendsmpcore.core.GlobalConstants;
import legendsmpcore.core.Permissions;
import legendsmpcore.customitems.ItemsConstants;
import legendsmpcore.customitems.CustomItems;
import legendsmpcore.core.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

import static legendsmpcore.core.utils.factory.HelpStringFactory.CustomItems.*;

/**
 * Command to generate & display help menu
 * */
public class HelpSubCommand implements SubCommand {
    public final List<String> itemSub = Arrays.asList("ghastbow", "hyperion", "vampireblade", "thorhammer", "witchscythe");

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender.hasPermission(Permissions.ITEM_UPDATE_PERM)) {
            boolean flag3 = false;
            boolean flag = false;
            if (args.length == 1 || args.length == 0) {
                commandSender.sendMessage(ItemsConstants.CHAT_PREFIX + "Custom Items Help Menu:");
                commandSender.sendMessage(makeHelpString("help", "Opens this help menu"));
                commandSender.sendMessage("");

                if(commandSender.hasPermission(Permissions.ITEM_GIVE_PERM)) {
                    commandSender.sendMessage(makeHelpString("ghastbow [give/toggle/state]",
                            "Commands for the Ghast Bow"));
                    commandSender.sendMessage(makeHelpString("hyperion [give/toggle/state]",
                            "Commands for the Hyperion"));
                    commandSender.sendMessage(makeHelpString("witchscythe [give/toggle/state]",
                            "Commands for the Witch Scythe"));
                    commandSender.sendMessage(makeHelpString("vampireblade [give/toggle/state]",
                            "Commands for the Vampire Blade"));
                    commandSender.sendMessage(makeHelpString("thorhammer [give/toggle/state]",
                            "Commands for the Thor Hammer"));
                    commandSender.sendMessage(makeHelpString("summoningsword [give/toggle/state]",
                            "Commands for the Summoning Sword"));
                    commandSender.sendMessage("");
                }

                if(commandSender.hasPermission(Permissions.ITEM_MANAGE_PERM)) {
                    commandSender.sendMessage(makeHelpString("reload", "Reloads the plugin"));
                    commandSender.sendMessage(makeHelpString("disable", "Disables item functionality"));
                    commandSender.sendMessage(makeHelpString("enable", "Enables item functionality"));
                    commandSender.sendMessage("");
                }

                if(commandSender.hasPermission(Permissions.ITEM_PLAYERS_PERM)) {
                    commandSender.sendMessage(makeHelpString("players [disallow/allow/list]",
                            "Commands to manipulate players"));
                }
                if(commandSender.hasPermission(Permissions.ITEM_REGIONS_PERM)){
                    commandSender.sendMessage(makeHelpString("regions [whitelist/blacklist/world]",
                            "Commands to manipulate regions"));
                }
            } else if(args.length == 2) {
                for (String str : CustomItems.getInstance().itemCommands.subCommands.keySet()) {
                    if (str.equalsIgnoreCase(args[1])) {

                        if (this.itemSub.contains(str.toLowerCase())) {
                            if(commandSender.hasPermission(Permissions.ITEM_GIVE_PERM)) {
                                commandSender.sendMessage(makeTitleString(str));
                                commandSender.sendMessage(makeSubString("give <player>", "Gives a player the item"));
                                commandSender.sendMessage(makeSubString("toggle", "Toggles usage of this item"));
                                commandSender.sendMessage(makeSubString("state",
                                        "Returns the usage state of this item"));
                            } else {
                                flag3 = true;
                            }

                        } else if (str.equalsIgnoreCase("regions")) {
                            if(commandSender.hasPermission(Permissions.ITEM_REGIONS_PERM)) {
                                commandSender.sendMessage(makeTitleString("regions"));
                                commandSender.sendMessage(makeSubString("whitelist [add/remove/list]",
                                        "Commands to manipulate whitelisted regions"));
                                commandSender.sendMessage(makeSubString("blacklist [add/remove/list]",
                                        "Commands to manipulate blacklisted regions"));
                                commandSender.sendMessage(makeSubString("world", "Returns the world ID"));
                            } else {
                                flag3 = true;
                            }

                        } else if (str.equalsIgnoreCase("players")) {
                            if(commandSender.hasPermission(Permissions.ITEM_GIVE_PERM)) {
                                commandSender.sendMessage(makeTitleString("players"));
                                commandSender.sendMessage(makeSubString("disallow <player>",
                                        "Prevents a player from using custom items"));
                                commandSender.sendMessage(makeSubString("allow <player>",
                                        "Allows a player to use custom items"));
                                commandSender.sendMessage(makeSubString("list", "Lists disallowed players"));
                            } else {
                                flag3 = true;
                            }

                        } else if (str.equalsIgnoreCase("reload")) {
                            if(commandSender.hasPermission(Permissions.ITEM_MANAGE_PERM)) {
                                commandSender.sendMessage(makeTitleString("reload"));
                                commandSender.sendMessage(makeHelpString("reload", "Reloads the plugin"));
                            } else {
                                flag3 = true;
                            }

                        } else if (str.equalsIgnoreCase("disable")) {
                            if(commandSender.hasPermission(Permissions.ITEM_MANAGE_PERM)) {
                                commandSender.sendMessage(makeTitleString("disable"));
                                commandSender.sendMessage(makeHelpString("disable", "Disables item functionality"));
                            } else{
                                flag3 = true;
                            }

                        } else if (str.equalsIgnoreCase("enable")) {
                            if(commandSender.hasPermission(Permissions.ITEM_MANAGE_PERM)) {
                                commandSender.sendMessage(makeTitleString("enable"));
                                commandSender.sendMessage(makeHelpString("enable", "Enables item functionality"));
                            } else {
                                flag3 = true;
                            }

                        }

                        break;
                    }
                }
                if(!CustomItems.getInstance().itemCommands.subCommands.containsKey(args[1])) flag = true;
            } else if (args.length == 3){
                if(commandSender.hasPermission(Permissions.ITEM_REGIONS_PERM)) {
                    if (args[1].equalsIgnoreCase("regions")) {
                        if (args[2].equalsIgnoreCase("whitelist")) {
                            commandSender.sendMessage(makeTitleString("whitelist"));
                            commandSender.sendMessage(makeSubString("add <region> <world>",
                                    "Adds a whitelisted region"));
                            commandSender.sendMessage(makeSubString("remove <region> <world>",
                                    "Removes a whitelisted region"));
                            commandSender.sendMessage(makeSubString("list",
                                    "Lists whitelisted regions"));

                        } else if (args[2].equalsIgnoreCase("blacklist")) {
                            commandSender.sendMessage(makeSubString("add <region> <world>",
                                    "Adds a blacklisted region"));
                            commandSender.sendMessage(makeSubString("remove <region> <world>",
                                    "Removes a blacklisted region"));
                            commandSender.sendMessage(makeSubString("list", "Lists blacklisted regions"));

                        } else if (args[2].equalsIgnoreCase("world")) {
                            commandSender.sendMessage(makeSubString("world", "Returns the applicable world ID"));
                        } else {
                            flag = true;
                        }
                    } else {
                        flag = true;
                    }
                } else {
                    flag3 = true;
                }
            } else {
                flag = true;
            }

            if(flag3)
                commandSender.sendMessage(GlobalConstants.PERMISSION_DENIED);

            if(flag)
                commandSender.sendMessage(ItemsConstants.UNKNOWN_SUBCOMMAND);
        }
    }
}
