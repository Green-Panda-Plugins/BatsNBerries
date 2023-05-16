package green.animals.items;

import dev.michaud.greenpanda.core.item.CustomItem;
import dev.michaud.greenpanda.core.item.ItemRegistry;
import dev.michaud.greenpanda.core.nbt.PersistentDataTypeBoolean;
import green.animals.BatsNBerries;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Bat;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BatBucket implements CustomItem {

  private final BatsNBerries plugin;

  public static final PersistentDataType<Byte, Boolean> BOOLEAN = new PersistentDataTypeBoolean();

  public BatBucket(BatsNBerries plugin) {
    this.plugin = plugin;
  }

  public static @NotNull BatBucket getInstance() {
    return ItemRegistry.getInstance(BatBucket.class);
  }

  @Override
  public @NotNull JavaPlugin getOwnerPlugin() {
    return plugin;
  }

  @Override
  public @NotNull String customId() {
    return "bat_bucket";
  }

  @Override
  public @NotNull Material baseMaterial() {
    return Material.HOPPER_MINECART;
  }

  @Override
  public @NotNull ItemStack makeItem() {

    final ItemStack item = CustomItem.super.makeItem();
    final ItemMeta meta = item.getItemMeta();

    meta.setUnbreakable(true);
    meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
    item.setItemMeta(meta);

    return item;
  }

  @Override
  public int customModelData() {
    return 100001;
  }

  @Override
  public Component displayName() {
    return Component.translatable("greenpanda.item.bat_bucket")
        .fallback("Bucket of Bat")
        .decoration(TextDecoration.ITALIC, false);
  }

  public @NotNull ItemStack capture(@NotNull Bat bat) {

    ItemStack item = makeItem();
    ItemMeta meta = item.getItemMeta();
    PersistentDataContainer container = meta.getPersistentDataContainer();

    if (bat.customName() != null) {
      meta.displayName(bat.customName());
    }

    storeInItem("Health", bat.getHealth(), container, PersistentDataType.DOUBLE);
    storeInItem("NoGravity", !bat.hasGravity(), false, container, BOOLEAN);
    storeInItem("Silent", bat.isSilent(), false, container, BOOLEAN);
    storeInItem("Invulnerable", bat.isInvulnerable(), false, container, BOOLEAN);
    storeInItem("Glowing", bat.isGlowing(), false, container, BOOLEAN);
    storeInItem("NoAI", !bat.hasAI(), false, container, BOOLEAN);

    item.setItemMeta(meta);
    return item;
  }

  @Contract("_, null -> fail")
  public void release(@NotNull Location location, ItemStack item) {

    if (!isType(item)) {
      throw new IllegalArgumentException(
          "Tried to use Bat Bucket release event on non-bat bucket item!");
    }

    final ItemMeta itemMeta = item.getItemMeta();
    final PersistentDataContainer itemContainer = itemMeta.getPersistentDataContainer();
    final Component itemName = itemMeta.displayName();
    final String defaultName = displayName().examinableName();

    location.getWorld().spawn(location, Bat.class, SpawnReason.DISPENSE_EGG, (bat) -> {
      getFromItem("Health", itemContainer, PersistentDataType.DOUBLE, bat::setHealth);
      getFromItem("NoGravity", itemContainer, BOOLEAN, noGrav -> bat.setGravity(!noGrav));
      getFromItem("Silent", itemContainer, BOOLEAN, bat::setSilent);
      getFromItem("Invulnerable", itemContainer, BOOLEAN, bat::setInvulnerable);
      getFromItem("Glowing", itemContainer, BOOLEAN, bat::setGlowing);
      getFromItem("NoAI", itemContainer, BOOLEAN, noAi -> bat.setAI(!noAi));

      if (itemName != null && !itemName.examinableName().equals(defaultName)) {
        bat.customName(itemName.asComponent());
      }
    });
  }

  private <T, Z> void getFromItem(@NotNull String name, @NotNull PersistentDataContainer container,
      @NotNull PersistentDataType<T, Z> type, Consumer<Z> consumer) {

    final NamespacedKey key = new NamespacedKey(plugin, name);

    if (container.has(key, type)) {
      Z value = container.get(key, type);
      consumer.accept(value);
    }
  }

  private <T, Z> void storeInItem(@NotNull String name, @NotNull Z value,
      @NotNull PersistentDataContainer container, @NotNull PersistentDataType<T, Z> type) {
    final NamespacedKey key = new NamespacedKey(plugin, name);
    container.set(key, type, value);
  }

  private <T, Z> void storeInItem(@NotNull String name, @NotNull Z value, @Nullable Z defaultVal,
      @NotNull PersistentDataContainer container, @NotNull PersistentDataType<T, Z> type) {
    if (value != defaultVal) {
      storeInItem(name, value, container, type);
    }
  }
}