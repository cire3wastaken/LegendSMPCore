package legendsmpcore.customitems.items;

import legendsmpcore.core.utils.ColorUtils;
import legendsmpcore.core.utils.ConfigurationHelper;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class containing information for the Summoning Sword
 * */
public class SummoningSword {
    public static final List<String> DEFAULT_LORE = Arrays.asList("&6Item Ability: &eRIGHT CLICK",
            "&fSummons &cmonsters &fto protect you in combat");
    public static List<String> lore;
    public static List<String> oldLore;
    public static String name;
    public static double cooldownSeconds;
    public static int mobsToSummon;
    /**+
     * in MS
     * */
    public static long despawnAfter;
    public static boolean invulnerable;

    // summoner -> spawned entities
    private static final List<SpawnedEntity> spawnedEntities = new ArrayList<>();

    private SummoningSword(){}

    public static void update(FileConfiguration config){
        lore = ColorUtils.color(ConfigurationHelper.getStringList("Items.SummoningSword.Lore", DEFAULT_LORE));
        name = ColorUtils.color(config.getString("Items.SummoningSword.Name", "&0&kA&d &lSummoningSword &0&kA&0"));
        cooldownSeconds = config.getDouble("Items.SummoningSword.Cooldown", 30.0);
        oldLore = ColorUtils.color(ConfigurationHelper.getStringList("Items.SummoningSword.OldLore",
                Collections.singletonList("")));
        mobsToSummon = config.getInt("Items.SummoningSword.MobCount", 6);
        despawnAfter = config.getLong("Items.SummoningSword.DespawnTime", 5);
        invulnerable = config.getBoolean("Items.SummoningSword.Invulnerable", true);
    }

    public static Entity addSpawnedEntity(Entity ent, Player summoner){
        spawnedEntities.add(new SpawnedEntity(summoner, ent, System.currentTimeMillis()));

        return ent;
    }

    public static List<SpawnedEntity> getSpawnedEntities(Player summoner){
        return spawnedEntities.stream().filter(se -> se.getSummonerID().equals(summoner.getUniqueId()))
                .collect(Collectors.toList());
    }

    public static void removeEntities(Player summoner){
        spawnedEntities.removeAll(getSpawnedEntities(summoner));
    }

    public static boolean isSpawnedEntity(Entity entity){
        for (SpawnedEntity spawnedEntity : spawnedEntities){
            if (spawnedEntity.getEntityID() == entity.getUniqueId()) // more precise
                return true;
        }
        return false;
    }

    public static Player getSummoner(Entity entity){
        for (SpawnedEntity spawnedEntity : spawnedEntities){
            if (spawnedEntity.entityID == entity.getUniqueId())
                return spawnedEntity.getSummoner();
        }
        return null;
    }

    public static boolean isSummoner(Player pl, Entity entity){
        return getSummoner(entity) == pl;
    }

    private static class SpawnedEntity {
        private final UUID entityID;
        private final long spawnedTime;
        private final Player summoner;
        private final UUID summonerID;

        public SpawnedEntity(Player spawner, Entity ent, long spawnedTime){
            this.spawnedTime = spawnedTime;
            this.entityID = ent.getUniqueId(); // store rather than entity obj
            this.summoner = spawner;
            this.summonerID = spawner.getUniqueId();
        }

        public UUID getEntityID() {
            return entityID;
        }

        public long getSpawnedTime(){
            return spawnedTime;
        }

        // bugs out if not loaded. use with caution
        public Entity getEntity(){
            for (Chunk chunk : summoner.getWorld().getLoadedChunks()) {
                for (Entity entity : chunk.getEntities()) {
                    if (entity.getUniqueId().equals(entityID))
                        return entity;
                }
            }

            return null;
        }

        public Player getSummoner() {
            return summoner;
        }

        public UUID getSummonerID() {
            return summonerID;
        }
    }
}
