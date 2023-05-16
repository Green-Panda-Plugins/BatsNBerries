package green.animals.events;

import dev.michaud.greenpanda.core.item.ItemRegistry;
import dev.michaud.greenpanda.core.util.MaterialInfo;
import green.animals.items.BatBucket;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class UseBatBucket implements Listener {

  public static double DISTANCE_TO_SPAWN = 0.5f;

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onPlayerInteract(@NotNull PlayerInteractEvent event) {

    final ItemStack item = event.getItem();

    if (ItemRegistry.isCustomItem(BatBucket.class, item)) {
      try {
        useBatBucket(item, event.getPlayer(), event.getClickedBlock(), event.getBlockFace(),
            event.getInteractionPoint());
      } finally {
        event.setCancelled(true);
      }
    }
  }

  private void useBatBucket(ItemStack item, Player player, Block block, BlockFace clickedFace,
      Location interactionPoint) {

    if (block == null || block.isEmpty()) {
      return;
    }

    if (MaterialInfo.isInteractable(block.getBlockData()) && !player.isSneaking()) {
      return;
    }

    if (interactionPoint == null) {
      return;
    }

    final Vector direction = clickedFace.getDirection().multiply(DISTANCE_TO_SPAWN);
    final Location spawnLocation = interactionPoint.add(direction);

    BatBucket.getInstance().release(spawnLocation, item);

    if (player.getGameMode() != GameMode.CREATIVE) {
      item.setAmount(0);
    }

    player.getInventory().addItem(new ItemStack(Material.BUCKET));
  }

}