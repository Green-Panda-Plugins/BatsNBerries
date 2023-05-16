package green.animals.events;

import green.animals.BatsNBerries;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class PlayerEat implements Listener {

  @EventHandler(ignoreCancelled = true)
  public void onPlayerItemConsume(@NotNull PlayerItemConsumeEvent event) {

    final ItemStack consumedItem = event.getItem();
    final Player player = event.getPlayer();

    if (consumedItem.getType() == Material.GLOW_BERRIES) {
      final PotionEffect glowing = new PotionEffect(PotionEffectType.GLOWING,
          BatsNBerries.PLAYER_EAT_BERRIES_DURATION, 0, true, false);
      player.addPotionEffect(glowing);
    }
  }

}