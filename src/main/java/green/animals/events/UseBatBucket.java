package green.animals.events;

import dev.michaud.greenpanda.core.item.ItemRegistry;
import dev.michaud.greenpanda.core.util.MaterialInfo;
import green.animals.items.BatBucket;
import io.papermc.paper.event.block.BlockPreDispenseEvent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class UseBatBucket implements Listener {

  public static double DISTANCE_TO_SPAWN = 0.5f;

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onPlayerInteract(@NotNull PlayerInteractEvent event) {

    if (!event.getAction().isRightClick()) {
      return;
    }

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

  @EventHandler(ignoreCancelled = true)
  public void onDispenserUse(@NotNull BlockPreDispenseEvent event) {

    final ItemStack item = event.getItemStack();
    final Block block = event.getBlock();

    if (!(block.getState() instanceof Dispenser dispenser)) {
      return;
    }

    if (!ItemRegistry.isCustomItem(BatBucket.class, item)) {
      return;
    }

    event.setCancelled(true);
    dispenseBatBucket(dispenser, item);
  }

  private void dispenseBatBucket(@NotNull Dispenser dispenser, @NotNull ItemStack item) {

    final Block block = dispenser.getBlock();
    final Directional directional = (Directional) dispenser.getBlockData();
    final BlockFace facing = directional.getFacing();
    final Block blockInFront = block.getRelative(facing);

    final Vector direction = facing.getDirection().multiply(DISTANCE_TO_SPAWN);
    final Location location = dispenser.getLocation().add(direction);

    if (blockInFront.getType().isSolid()) {
      dispenseBatBucketItem(item, location, direction);
      item.setType(Material.AIR);
    } else {
      ItemRegistry.getInstance(BatBucket.class).release(location, item);
      item.setType(Material.BUCKET);
    }

    location.getWorld().playSound(dispenser.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE,
        SoundCategory.BLOCKS, 1, 1);
    //TODO: Currently doesn't show any dispensing particles.
  }

  private void dispenseBatBucketItem(@NotNull ItemStack itemStack,
      @NotNull Location location, @NotNull Vector direction) {

    location.getWorld().spawn(location, Item.class, item -> {
      item.setItemStack(new ItemStack(itemStack));
      item.setVelocity(direction);
    });
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

  @EventHandler(ignoreCancelled = true)
  public void onBlockPreDispense(BlockPreDispenseEvent event) {
  }
}