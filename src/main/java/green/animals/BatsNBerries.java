package green.animals;

import green.animals.events.BatEventManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BatsNBerries extends JavaPlugin {

  @Override
  public void onEnable() {
    getServer().getPluginManager().registerEvents(new BatEventManager(), this);
  }

}