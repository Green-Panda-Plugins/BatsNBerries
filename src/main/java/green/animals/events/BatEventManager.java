package green.animals.events;

import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BatEventManager implements Listener {

  public static final int DISTANCE = 20;
  public static final int DURATION = 140;

  @EventHandler
  public void onBatInteract(PlayerInteractEntityEvent event) {

    ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

    if (!(event.getRightClicked() instanceof Bat bat)) {
      return;
    }

    if (item.getType() != Material.GLOW_BERRIES) {
      return;
    }

    List<LivingEntity> nearbyEntities = bat.getWorld()
        .getNearbyEntities(bat.getLocation(), DISTANCE, DISTANCE, DISTANCE,
            BatEventManager::validTarget).stream().map(e -> (LivingEntity) e).toList();

    for(LivingEntity entity : nearbyEntities) {
      entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, DURATION, 0));
    }

    event.getPlayer().playSound(bat.getLocation(), Sound.ENTITY_HORSE_EAT, 5, 2f);

    item.setAmount(item.getAmount() - 1);

  }

  private static boolean validTarget(Entity entity) {
    return entity instanceof LivingEntity;
  }
}