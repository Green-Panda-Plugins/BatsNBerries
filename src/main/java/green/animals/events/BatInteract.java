package green.animals.events;

import dev.michaud.greenpanda.core.item.ItemRegistry;
import green.animals.BatsNBerries;
import green.animals.items.BatBucket;
import java.util.Collection;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Bat;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class BatInteract implements Listener {

  @EventHandler(ignoreCancelled = true)
  public void onInteractEntity(@NotNull PlayerInteractEntityEvent event) {

    final Player player = event.getPlayer();
    final EquipmentSlot hand = event.getHand();
    final ItemStack item = player.getInventory().getItem(hand);

    if (!(event.getRightClicked() instanceof Bat bat)) {
      return;
    }

    if (item.getType() == Material.GLOW_BERRIES) {
      useGlowBerries(player, bat, item);
    }

    if (item.getType() == Material.BUCKET) {
      useEmptyBucket(player, bat, item);
    }
  }

  private static void useGlowBerries(@NotNull Player player, Bat bat, ItemStack berries) {
    doBatEcholocation(bat);
    if (player.getGameMode() != GameMode.CREATIVE) {
      berries.setAmount(berries.getAmount() - 1);
    }
  }

  private static void useEmptyBucket(@NotNull Player player, Bat bat, ItemStack bucket) {

    final ItemStack capturedBat = ItemRegistry.getInstance(BatBucket.class).capture(bat);

    if (player.getGameMode() != GameMode.CREATIVE) {
      bucket.setAmount(bucket.getAmount() - 1);
    }

    player.getInventory().addItem(capturedBat);
    bat.remove();
  }

  private static void doBatEcholocation(@NotNull Bat bat) {

    final Location location = bat.getLocation();
    final World world = location.getWorld();
    final Collection<LivingEntity> nearbyEntities = world.getNearbyLivingEntities(
        location, BatsNBerries.BAT_SCREECH_DISTANCE, BatInteract::validTarget);

    final PotionEffect glowing = new PotionEffect(PotionEffectType.GLOWING,
        BatsNBerries.BAT_SCREECH_GLOW_DURATION, 0, true, false);

    for (LivingEntity entity : nearbyEntities) {
      entity.addPotionEffect(glowing);
    }

    world.playSound(bat, Sound.ENTITY_HORSE_EAT, 5, 2f);
  }

  private static boolean validTarget(Entity entity) {

    //The logic here is that echo-location passes through soul-based mobs
    //Bosses are also immune
    if (entity instanceof Allay || entity instanceof Vex || entity instanceof Phantom
        || entity instanceof EnderDragon || entity instanceof Wither
        || entity instanceof ElderGuardian) {
      return false;
    }

    //Sound would bounce off of water
    if (entity.isInWater()) {
      return false;
    }

    //We don't want to see spectators
    if (entity instanceof Player player && player.getGameMode() == GameMode.SPECTATOR) {
      return false;
    }

    return entity instanceof LivingEntity;
  }

}