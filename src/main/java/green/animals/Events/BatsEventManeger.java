package green.animals.Events;

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

public class BatsEventManeger implements Listener {
    @EventHandler
    public void OnInteractBat(PlayerInteractEntityEvent event){
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        Bat bat = (Bat) event.getRightClicked();

        try{
            if(bat != null){
                if(item != null){
                    if(item.getType() == Material.GLOW_BERRIES){
                        double distance = 20;
                        LivingEntity nearbyEntities = (LivingEntity) bat.getWorld().getNearbyEntities(bat.getLocation(), distance, distance, distance, BatsEventManeger::validTarget);
                        if(nearbyEntities != null){
                            nearbyEntities.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 140, 1));
                        }

                        event.getPlayer().playSound(bat.getLocation(), Sound.ENTITY_HORSE_EAT, 5, 2f);

                        item.setAmount(item.getAmount() - 1);
                    }
                }
            }

        } catch (Exception e){
            // Do nothing
        }
    }

    private static boolean validTarget(Entity entity) {

        if (!(entity instanceof LivingEntity e)) {
            return false;
        }

        return true;
    }
}
