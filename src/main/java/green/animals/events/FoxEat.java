package green.animals.events;

import static green.animals.BatsNBerries.FOX_EAT_BERRIES_DURATION;
import static org.bukkit.potion.PotionEffectType.GLOWING;

import dev.michaud.greenpanda.core.event.FoxConsumeItemEvent;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

public class FoxEat implements Listener {

  @EventHandler(ignoreCancelled = true)
  public void onFoxEnterLoveMode(@NotNull EntityEnterLoveModeEvent event) {

    if (event.getEntityType() != EntityType.FOX) {
      return;
    }

    final Fox foxEntity = (Fox) event.getEntity();
    final HumanEntity player = event.getHumanEntity();

    if (player == null) {
      return;
    }

    Material mainHand = player.getInventory().getItemInMainHand().getType();
    Material offhand = player.getInventory().getItemInOffHand().getType();

    if (mainHand == Material.SWEET_BERRIES) {
      return;
    }

    if (mainHand == Material.GLOW_BERRIES || offhand == Material.GLOW_BERRIES) {
      addFoxGlowEffect(foxEntity);
    }

  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerInteractBabyFox(@NotNull PlayerInteractEntityEvent event) {

    if (!(event.getRightClicked() instanceof Fox foxEntity)) {
      return;
    }

    if (foxEntity.isAdult() || foxEntity.isDead() || !foxEntity.isValid()) {
      return;
    }

    final Player player = event.getPlayer();
    final ItemStack itemInHand = player.getInventory().getItem(event.getHand());

    if (itemInHand.getType() == Material.GLOW_BERRIES) {
      addFoxGlowEffect(foxEntity);
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onFoxConsumeItem(@NotNull FoxConsumeItemEvent event) {
    if (event.getItemConsumed().getType() == Material.GLOW_BERRIES) {
      addFoxGlowEffect(event.getEntity());
    }
  }

  private static void addFoxGlowEffect(@NotNull Fox fox) {
    fox.addPotionEffect(new PotionEffect(GLOWING, FOX_EAT_BERRIES_DURATION, 0, true, false));
  }
}