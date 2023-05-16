package green.animals;

import dev.michaud.greenpanda.core.item.ItemRegistry;
import green.animals.events.BatInteract;
import green.animals.events.FoxEat;
import green.animals.events.PlayerEat;
import green.animals.events.UseBatBucket;
import green.animals.items.BatBucket;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class BatsNBerries extends JavaPlugin {

  public static double BAT_SCREECH_DISTANCE;
  public static int BAT_SCREECH_GLOW_DURATION;
  public static int PLAYER_EAT_BERRIES_DURATION;
  public static int FOX_EAT_BERRIES_DURATION;

  @Override
  public void onEnable() {

    //Config
    FileConfiguration config = this.getConfig();
    config.addDefault("BatSonarDistance", 20);
    config.addDefault("BatSonarGlowDuration", 140);
    config.addDefault("PlayerEatBerriesGlowDuration", 200);
    config.addDefault("FoxEatBerriesGlowDuration", 200);
    config.options().copyDefaults(true);
    saveDefaultConfig();

    BAT_SCREECH_DISTANCE = config.getDouble("BatSonarDistance");
    BAT_SCREECH_GLOW_DURATION = config.getInt("BatSonarGlowDuration");
    PLAYER_EAT_BERRIES_DURATION = config.getInt("PlayerEatBerriesGlowDuration");
    FOX_EAT_BERRIES_DURATION = config.getInt("FoxEatBerriesGlowDuration");

    //Events
    getServer().getPluginManager().registerEvents(new BatInteract(), this);
    getServer().getPluginManager().registerEvents(new PlayerEat(), this);
    getServer().getPluginManager().registerEvents(new FoxEat(), this);
    getServer().getPluginManager().registerEvents(new UseBatBucket(), this);

    //Items
    ItemRegistry.register(BatBucket.class, new BatBucket(this));
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }

}