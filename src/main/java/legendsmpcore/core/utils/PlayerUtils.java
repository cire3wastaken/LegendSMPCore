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
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerUtils {
    public static final Map<Player, String> REAL_IP_PER_ONLINE_PLAYER = new HashMap<>();

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

    public static ArrayList<LivingEntity> getNearbyLivingEntities(Player pl, double range){
        ArrayList<LivingEntity> nearby = new ArrayList<>();
        for (Entity e : pl.getNearbyEntities(range, range, range)){
            if (e instanceof LivingEntity){
                nearby.add((LivingEntity) e);
            }
        }
        return nearby;
    }

    public static boolean shouldUse(Player p){
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

        return true;
    }

    public static boolean containsLore(ItemStack itemStack, List<String> lore){
        for(String str : lore){
            if(!containsString(itemStack, str)){
                return false;
            }
        }

        return true;
    }

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

    public static String lookUpRealAddress(Player player){
        return REAL_IP_PER_ONLINE_PLAYER.get(player);
    }

    /**
     * @see PlayerUtils#blacklistPlayer(Player)
     * Calls PlayerUtils#blacklistPlayer(Player) too
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

    public static void blacklistPlayer(Player player){
        if(Mitigation.getInstance().blacklistedPlayers.add(player.getName())) {
            LegendCore.getInstance().perms.get(player.getName()).unsetPermission(Permissions.MITIGATION_ALLOWED_PERM);

            List<String> temp = ConfigurationHelper.getStringList("Mitigation.Blacklisted.Players", new ArrayList<>());
            if(!temp.contains(player.getName()))
                temp.add(player.getName());

            LegendCore.getInstance().getConfig().set("Mitigation.Blacklisted.Players", temp);
        }
    }

    public static void allowIP(Player player){
        if(Mitigation.getInstance().blacklistedPlayers.remove(player.getName())){
            List<String> temp = ConfigurationHelper.getStringList("Mitigation.Blacklisted.IPs", new ArrayList<>());
            temp.remove(lookUpRealAddress(player));

            LegendCore.getInstance().getConfig().set("Mitigation.Blacklisted.IPs", temp);

            allowPlayer(player);
        }
    }

    public static void allowPlayer(Player player){
        if(Mitigation.getInstance().blacklistedPlayers.remove(player.getName())){
            LegendCore.getInstance().perms.get(player.getName()).setPermission(Permissions.MITIGATION_ALLOWED_PERM, true);

            List<String> temp = ConfigurationHelper.getStringList("Mitigation.Blacklisted.Players", new ArrayList<>());
            temp.remove(player.getName());

            LegendCore.getInstance().getConfig().set("Mitigation.Blacklisted.Players", temp);
        }
    }

    public static Set<Player> getPlayersByAddress(String value) {
        return REAL_IP_PER_ONLINE_PLAYER.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
