package crystalspider.torchhit.handlers;

import javax.annotation.Nullable;

import crystalspider.torchhit.config.TorchHitConfig;
import crystalspider.torchhit.optional.SoulFired;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * {@link AttackEntityEvent} handler.
 */
public class AttackEntityEventHandler {
  /**
   * Whether Soul Fire'd mod is installed at runtime.
   */
  private static final boolean isSoulfiredInstalled = ModList.get().isLoaded("soulfired");

  /**
   * Handles the {@link AttackEntityEvent}.
   * 
   * @param event
   */
  @SubscribeEvent()
  public void handle(AttackEntityEvent event) {
    PlayerEntity player = event.getPlayer();
    if (!player.level.isClientSide && !player.isSpectator()) {
      Entity target = event.getTarget();
      Hand interactionHand = getInteractionHand(player);
      if (interactionHand != null && !target.fireImmune()) {
        ItemStack torch = player.getItemInHand(interactionHand);
        if (interactionHand == Hand.MAIN_HAND) {
          attack(player, target, torch, TorchHitConfig.getDirectHitDuration());
        } else if (isAllowedTool(player.getMainHandItem().getItem())) {
          attack(player, target, torch, TorchHitConfig.getIndirectHitDuration());
        }
      }
    }
  }

  /**
   * Attack the entity with the torch setting it on fire.
   * 
   * @param player
   * @param target
   * @param torch
   * @param defaultDuration
   */
  private void attack(PlayerEntity player, Entity target, ItemStack torch, int defaultDuration) {
    consumeItem(player, torch, burn(target, torch, defaultDuration));
  }

  /**
   * Consumes the used torch if enabled.
   * 
   * @param player
   * @param torch
   * @param fireSeconds
   */
  private void consumeItem(PlayerEntity player, ItemStack torch, int fireSeconds) {
    if (
      !player.isCreative() &&
      isTorch(torch) &&
      TorchHitConfig.getConsumeTorch() &&
      (TorchHitConfig.getConsumeWithoutFire() || fireSeconds > 0)
    ) {
      torch.shrink(1);
    }
  }

  /**
   * Sets the entity on fire.
   * 
   * @param target
   * @param torch
   * @param defaultDuration
   * @return amound of seconds the entity will be set on fire.
   */
  private int burn(Entity target, ItemStack torch, int defaultDuration) {
    int fireSeconds = getFireSeconds(torch, target, defaultDuration);
    if (fireSeconds > 0) {
      target.setSecondsOnFire(fireSeconds);
      setFireId(target, torch);
    }
    return fireSeconds;
  }

  /**
   * If Soul Fire'd is installed, sets the correct Fire Id.
   * 
   * @param target
   * @param torch
   */
  private void setFireId(Entity target, ItemStack torch) {
    if (isSoulfiredInstalled) {
      if (isSoulTorch(torch)) {
        SoulFired.setOnSoulFire(target);
      } else {
        SoulFired.setOnFire(target);
      }
    }
  }

  /**
   * Returns the amount of seconds the given entity should stay on fire.
   * 
   * @param torch
   * @param target
   * @param fireDuration
   * @return the amount of seconds the given entity should stay on fire.
   */
  private int getFireSeconds(ItemStack torch, Entity target, int fireDuration) {
    if ((Math.random() * 100) < TorchHitConfig.getFireChance()) {
      if (isSoulTorch(torch)) {
        if (isSoulfiredInstalled) {
          return fireDuration;
        }
        if (target instanceof AbstractPiglinEntity) {
          return fireDuration * 2;
        }
        return fireDuration + 1;
      }
      return fireDuration;
    }
    return 0;
  }

  /**
   * Checks whether the given {@link Item} is a tool that allows Indirect Hits.
   * 
   * @param item
   * @return whether the given {@link Item} is a tool that allows Indirect Hits.
   */
  private boolean isAllowedTool(Item item) {
    return !TorchHitConfig.getIndirectHitToolList().isEmpty() && TorchHitConfig.getIndirectHitToolList().stream().filter(toolType -> getKey(item).matches(".*:([^_]+_)*" + toolType + "(_[^_]+)*")).count() > 0;
  }

  /**
   * Returns the {@link Hand} of the {@link PlayerEntity} holding a torch.
   * Null if none could be found.
   * 
   * @param player
   * @return {@link Hand} holding a torch or null.
   */
  @Nullable
  private Hand getInteractionHand(PlayerEntity player) {
    if (isTorch(player.getMainHandItem())) {
      return Hand.MAIN_HAND;
    }
    if (isTorch(player.getOffhandItem())) {
      return Hand.OFF_HAND;
    }
    return null;
  }

  /**
   * Checks whether the given {@link ItemStack} is a torch.
   * 
   * @param item
   * @return whether the given {@link ItemStack} is a torch.
   */
  private boolean isTorch(ItemStack item) {
    return (item.getItem() == Items.TORCH && TorchHitConfig.getVanillaTorchesEnabled()) || TorchHitConfig.getExtraTorchItems().contains(getKey(item.getItem())) || isSoulTorch(item);
  }

  /**
   * Checks whether the given {@link ItemStack} is a soul torch.
   * 
   * @param item
   * @return whether the given {@link ItemStack} is a soul torch.
   */
  private boolean isSoulTorch(ItemStack item) {
    return (item.getItem() == Items.SOUL_TORCH && TorchHitConfig.getVanillaTorchesEnabled()) || TorchHitConfig.getExtraSoulTorchItems().contains(getKey(item.getItem()));
  }

  /**
   * Returns the in-game ID of the item passed as parameter.
   * 
   * @param item
   * @return in-game ID of the given item.
   */
  private String getKey(Item item) {
    ResourceLocation itemLocation = ForgeRegistries.ITEMS.getKey(item);
    if (itemLocation != null) {
      return itemLocation.toString();
    }
    return "";
  }
}
