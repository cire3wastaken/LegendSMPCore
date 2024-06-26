package legendsmpcore.core.utils;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import legendsmpcore.core.LegendCore;
import legendsmpcore.core.Permissions;
import legendsmpcore.customitems.CustomItems;
import legendsmpcore.mitigation.Mitigation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility methods to interact with players
 * */
public class PlayerUtils {
    /**
     * Returns an array, array[0] is the last block before the player's raytrace crosses a block,
     * and array[1] is the block the raytrace collides with
     * */
    public static Block[] getTargetBlock(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastAirBlock = player.getLocation().getBlock();
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastAirBlock = lastBlock;
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return new Block[]{lastAirBlock, lastBlock};
    }

    /**
     * Returns nearby entities within range of another entity/player
     * */
    public static List<LivingEntity> getNearbyLivingEntities(Entity pl, double range){
        List<LivingEntity> nearby = new ArrayList<>();
        for (Entity e : pl.getNearbyEntities(range, range, range)){
            if (e instanceof LivingEntity){
                nearby.add((LivingEntity) e);
            }
        }

        return nearby.stream().filter(e -> pl.getLocation().distanceSquared(e.getLocation()) <= range * range).collect(Collectors.toList());
    }

    /**
     * Utility method to determine whether custom items should function at the player's location
     * */
    public static boolean shouldUse(Player p){
        try {
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
            Vector vec = localPlayer.getPosition();
            RegionManager manager = WorldGuardPlugin.inst().getRegionManager(p.getWorld());
            if(manager == null){
                return true;
            }

            ApplicableRegionSet set = manager.getApplicableRegions(vec);

            String worldName = p.getWorld().getName().toLowerCase();

            if(CustomItems.getInstance().protectedRegions.containsKey(worldName)){
                for(ProtectedRegion region : set) {
                    if(CustomItems.getInstance().whitelistedRegions.containsKey(worldName)){
                        if(CustomItems.getInstance().whitelistedRegions.get(worldName).contains(region.getId().toLowerCase())){
                            return true;
                        }
                    }

                    if (CustomItems.getInstance().protectedRegions.get(worldName).contains(region.getId().toLowerCase())){
                        return false;
                    }
                }
            }

            for(ProtectedRegion region : set){
                if(Objects.equals(region.getFlag(DefaultFlag.PVP), State.DENY))
                {
                    return false;
                }
            }
        } catch (NoClassDefFoundError ignored){
        }

        return true;
    }

    /**
     * Parse and return the lore in player's held item
     * */
    public static List<String> loreInHand(Player player){
        return player.getItemInHand() != null ?
                player.getItemInHand().getItemMeta().hasLore() ?
                    player.getItemInHand().getItemMeta().getLore() : new ArrayList<>(): new ArrayList<>();
    }

    /**
     * Check if the held itemstack contains specified lore
     * */
    public static boolean containsLore(ItemStack itemStack, List<String> lore){
        for(String str : lore){
            if(!containsString(itemStack, str)){
                return false;
            }
        }

        return true;
    }

    /**
     * Check if the held itemstack contains a specific string, case-insensitive
     * */
    public static boolean containsString(ItemStack itemStack, String loreString){
        if(itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasLore()){
            return false;
        }

        List<String> itemLore = new ArrayList<>();
        for(String str : itemStack.getItemMeta().getLore()){
            itemLore.add(str.toLowerCase());
        }

        return itemLore.contains(loreString.toLowerCase());
    }

    /**
     * Return IP address of player for mitigations
     * */
    public static String lookUpRealAddress(Player player){
        return player.getAddress().getAddress().getHostAddress();
    }

    /**
     * Searches up player(s) from ip, and blacklists all of them. Also blacklists the IP
     * @see PlayerUtils#blacklistPlayer(Player)
     * */
    public static void blacklistIP(Player player){
        if(Mitigation.getInstance().blacklistedAddresses.add(PlayerUtils.lookUpRealAddress(player))) {
            List<String> temp = ConfigurationHelper.getStringList("Mitigation.Blacklisted.IPs", new ArrayList<>());
            if(!temp.contains(lookUpRealAddress(player)))
                temp.add(lookUpRealAddress(player));

            LegendCore.getInstance().getConfig().set("Mitigation.Blacklisted.IPs", temp);

            blacklistPlayer(player);
        }
    }

    /**
     * Blacklist just a player from participating in mitigation events, not the IP
     * */
    public static void blacklistPlayer(Player player){
        if(Mitigation.getInstance().blacklistedPlayers.add(player.getName())) {
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),
                    "lp user " + player.getName() + " unset " + Permissions.MITIGATION_ALLOWED_PERM);

            List<String> temp = ConfigurationHelper.getStringList("Mitigation.Blacklisted.Players", new ArrayList<>());
            if(!temp.contains(player.getName()))
                temp.add(player.getName());

            LegendCore.getInstance().getConfig().set("Mitigation.Blacklisted.Players", temp);
        }
    }

    /**
     * Un-blacklists an IP address, as well as all corresponding players
     * */
    public static void allowIP(Player player){
        if(Mitigation.getInstance().blacklistedPlayers.remove(player.getName())){
            List<String> temp = ConfigurationHelper.getStringList("Mitigation.Blacklisted.IPs", new ArrayList<>());
            temp.remove(lookUpRealAddress(player));

            LegendCore.getInstance().getConfig().set("Mitigation.Blacklisted.IPs", temp);

            allowPlayer(player);
        }
    }

    /**
     * Un-blacklists just a player, not the IP
     * */
    public static void allowPlayer(Player player){
        if(Mitigation.getInstance().blacklistedPlayers.remove(player.getName())){
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),
                "lp user " + player.getName() + " set " + Permissions.MITIGATION_ALLOWED_PERM + " true");

            List<String> temp = ConfigurationHelper.getStringList("Mitigation.Blacklisted.Players", new ArrayList<>());
            temp.remove(player.getName());

            LegendCore.getInstance().getConfig().set("Mitigation.Blacklisted.Players", temp);
        }
    }
}
