package green.animals;
import green.animals.Events.BatsEventManeger;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class BatsNBerries extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[BatsNBerries] is enabled");
        getServer().getPluginManager().registerEvents(new BatsEventManeger(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[BatsNBerries] is disabled");
    }
}
