package legendsmpcore.customitems.command.subcommands.server;

import legendsmpcore.core.LegendCore;
import legendsmpcore.core.Permissions;
import legendsmpcore.customitems.ItemsConstants;
import legendsmpcore.customitems.CustomItems;
import legendsmpcore.core.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RegionSubCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!commandSender.hasPermission(Permissions.ITEM_REGIONS_PERM) && !commandSender.isOp()){
            commandSender.sendMessage(ItemsConstants.PERMISSION_DENIED);
            return;
        }

        boolean flag = false;
        if(args.length == 5){
            if(args[1].equalsIgnoreCase("whitelist")){
                if(args[2].equalsIgnoreCase("add")){
                    this.addWhitelistedRegion(commandSender, args);
                } else if (args[2].equalsIgnoreCase("remove")){
                    this.removeWhitelistedRegion(commandSender, args);
                } else {
                    flag = true;
                }
            } else if (args[1].equalsIgnoreCase("blacklist")){
                if(args[2].equalsIgnoreCase("add")){
                    this.addBlacklistedRegion(commandSender, args);
                } else if (args[2].equalsIgnoreCase("remove")){
                    this.removeBlacklistedRegion(commandSender, args);
                } else {
                    flag = true;
                }
            } else {
                flag = true;
            }
        } else if (args.length == 3){
            if(args[1].equalsIgnoreCase("whitelist")){
                if(args[2].equalsIgnoreCase("list")){
                    commandSender.sendMessage(ItemsConstants.CHAT_PREFIX + ChatColor.BOLD + ChatColor.GREEN +
                            "Whitelisted Regions in Memory: ");
                    CustomItems.getInstance().whitelistedRegions.forEach((name, set) -> {
                        for(String reg : set){
                            commandSender.sendMessage(ChatColor.YELLOW + name + ": " + reg.toLowerCase());
                        }
                    });
                } else {
                    flag = true;
                }
            } else if (args[1].equalsIgnoreCase("blacklist")){
                if(args[2].equalsIgnoreCase("list")){
                    commandSender.sendMessage(ItemsConstants.CHAT_PREFIX + ChatColor.BOLD + ChatColor.GREEN +
                            "Blacklisted Regions in Memory: ");
                    CustomItems.getInstance().protectedRegions.forEach((name, set) -> {
                        for(String reg : set){
                            commandSender.sendMessage(ChatColor.YELLOW + name + ": " + reg.toLowerCase());
                        }
                    });
                } else {
                    flag = true;
                }
            } else {
                flag = true;
            }
        } else if (args.length == 2){
            if (args[1].equalsIgnoreCase("world")){
                if(commandSender instanceof Player) {
                    commandSender.sendMessage(ItemsConstants.CHAT_PREFIX + "Applicable world name is: " + ((Player) commandSender).getWorld().getName());
                } else {
                    commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "Only players can run this command!");
                }
            }
        } else {
            flag = true;
        }
        if(flag){
            commandSender.sendMessage(ItemsConstants.UNKNOWN_SUBCOMMAND);
        }
    }

    private void removeWhitelistedRegion(CommandSender commandSender, String[] strings){
        if(!CustomItems.getInstance().whitelistedRegions.containsKey(strings[1].toLowerCase())){
            commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "World '" +
                    strings[4] + "' doesn't exist!");
            return;
        }

        if(CustomItems.getInstance().whitelistedRegions.get(strings[4].toLowerCase()).remove(strings[3].toLowerCase())){
            List<String> temp = LegendCore.getInstance().getConfig().getStringList("Items.Protected.Whitelist." +
                    strings[1].toLowerCase());

            if(temp == null){
                commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "Whitelisted region '" +
                        strings[3] + "' in world '" + strings[4] + "' doesn't exist!");
                return;
            }

            temp.remove(strings[3].toLowerCase());

            LegendCore.getInstance().getConfig().set("Items.Protected.Whitelist." + strings[4].toLowerCase(), temp);

            try {
                LegendCore.getInstance().getConfig().save(LegendCore.getInstance().getFile());

                commandSender.sendMessage(ItemsConstants.CHAT_PREFIX + "Successfully removed region '" +
                        strings[0] + "' from the list of whitelisted regions!");
            } catch (IOException e) {
                commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "Failed to save deleted region to disk, check logs!");
                commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "This change will only be in memory!");
                e.printStackTrace();
            }
        } else {
            commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "Whitelisted region '" +
                    strings[3] + "' in world '" + strings[4] + "' doesn't exist!");
        }
    }

    private void removeBlacklistedRegion(CommandSender commandSender, String[] strings){
        if(!CustomItems.getInstance().protectedRegions.containsKey(strings[1].toLowerCase())){
            commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "World '" +
                    strings[4] + "' doesn't exist!");
            return;
        }

        if(CustomItems.getInstance().protectedRegions.get(strings[4].toLowerCase()).remove(strings[3].toLowerCase())){
            List<String> temp = LegendCore.getInstance().getConfig().getStringList("Items.Protected.Blacklist." +
                    strings[1].toLowerCase());

            if(temp == null){
                commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "Blacklisted region '" +
                        strings[3] + "' in world '" + strings[4] + "' doesn't exist!");
                return;
            }

            temp.remove(strings[3].toLowerCase());

            LegendCore.getInstance().getConfig().set("Items.Protected.Blacklist." + strings[4].toLowerCase(), temp);

            try {
                LegendCore.getInstance().getConfig().save(LegendCore.getInstance().getFile());

                commandSender.sendMessage(ItemsConstants.CHAT_PREFIX + "Successfully removed region '" +
                        strings[0] + "' from the list of blacklisted regions!");
            } catch (IOException e) {
                commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "Failed to save deleted region to disk, check logs!");
                commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "This change will only be in memory!");
                e.printStackTrace();
            }
        } else {
            commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "Blacklisted region '" +
                    strings[3] + "' in world '" + strings[4] + "' doesn't exist!");
        }
    }

    private void addBlacklistedRegion(CommandSender commandSender, String[] strings){
        CustomItems.getInstance().protectedRegions.computeIfAbsent(strings[4].toLowerCase(),
                k -> new HashSet<>());

        if(CustomItems.getInstance().protectedRegions.get(strings[4]).add(strings[3].toLowerCase())){
            List<String> temp = LegendCore.getInstance().getConfig().getStringList("Items.Protected.Blacklist." +
                    strings[1].toLowerCase());

            if(temp == null){
                temp = new ArrayList<>();
            }

            temp.add(strings[3].toLowerCase());

            LegendCore.getInstance().getConfig().set("Items.Protected.Blacklist." + strings[4].toLowerCase(), temp);

            List<String> worlds = LegendCore.getInstance().getConfig().getStringList("Items.Protected.Worldlist");
            if(worlds == null){
                worlds = new ArrayList<>();
            }

            if(!worlds.contains(strings[1].toLowerCase())) {
                worlds.add(strings[1].toLowerCase());
                LegendCore.getInstance().getConfig().set("Items.Protected.Worldlist", worlds);
            }

            try {
                LegendCore.getInstance().getConfig().save(LegendCore.getInstance().getFile());
                commandSender.sendMessage(ItemsConstants.CHAT_PREFIX + "Successfully added region '" +
                        strings[0] + "' to the list of blacklisted regions!");
            } catch (IOException e) {
                commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "Failed to save deleted region to disk, check logs!");
                commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "This change will only be in memory!");
                e.printStackTrace();
            }
        } else {
            commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "Blacklisted region '" +
                    strings[0] + "' in world '" + strings[1] + "' already exists!");
        }
    }

    private void addWhitelistedRegion(CommandSender commandSender, String[] strings){
        CustomItems.getInstance().whitelistedRegions.computeIfAbsent(strings[4].toLowerCase(),
                k -> new HashSet<>());

        if(CustomItems.getInstance().whitelistedRegions.get(strings[4]).add(strings[3].toLowerCase())){
            List<String> temp = LegendCore.getInstance().getConfig().getStringList("Items.Protected.Whitelist." +
                    strings[1].toLowerCase());

            if(temp == null){
                temp = new ArrayList<>();
            }

            temp.add(strings[3].toLowerCase());

            LegendCore.getInstance().getConfig().set("Items.Protected.Whitelist." + strings[4].toLowerCase(), temp);

            List<String> worlds = LegendCore.getInstance().getConfig().getStringList("Items.Protected.Worldlist");
            if(worlds == null){
                worlds = new ArrayList<>();
            }

            if(!worlds.contains(strings[1].toLowerCase())) {
                worlds.add(strings[1].toLowerCase());
                LegendCore.getInstance().getConfig().set("Items.Protected.Worldlist", worlds);
            }

            try {
                LegendCore.getInstance().getConfig().save(LegendCore.getInstance().getFile());
                commandSender.sendMessage(ItemsConstants.CHAT_PREFIX + "Successfully added region '" +
                        strings[0] + "' to the list of whitelisted regions!");
            } catch (IOException e) {
                commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "Failed to save deleted region to disk, check logs!");
                commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "This change will only be in memory!");
                e.printStackTrace();
            }
        } else {
            commandSender.sendMessage(ItemsConstants.FAIL_PREFIX + "Whitelisted region '" +
                    strings[0] + "' in world '" + strings[1] + "' already exists!");
        }
    }
}
