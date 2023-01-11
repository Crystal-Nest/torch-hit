package crystalspider.torchhit.handlers;

import javax.annotation.Nullable;

import crystalspider.torchhit.config.TorchHitConfig;
import crystalspider.torchhit.optional.SoulFired;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

/**
 * {@link AttackEntityCallback} event handler.
 */
public class AttackEntityHandler {
  /**
   * Whether Soul Fire'd mod is installed at runtime.
   */
  private static final boolean isSoulfiredInstalled = FabricLoader.getInstance().isModLoaded("soulfired");

  /**
   * Handles the {@link AttackEntityCallback} event.
   * 
   * @param player
   * @param world
   * @param hand
   * @param target
   * @param hitResult
   * @return {@link ActionResult}.
   */
  public ActionResult handle(PlayerEntity player, World world, Hand hand, Entity target, EntityHitResult hitResult) {
    if (!player.world.isClient && !player.isSpectator()) {
      Hand interactionHand = getHand(player);
      if (interactionHand != null && !target.isFireImmune()) {
        ItemStack item = player.getStackInHand(interactionHand);
        boolean directHit = interactionHand == Hand.MAIN_HAND;
        if (directHit || isAllowedTool(player.getMainHandStack().getItem())) {
          attack(player, target, item, directHit);
        }
      }
    }
    return ActionResult.PASS;
  }

  /**
   * Attack the target entity with the torch item it on fire.
   * 
   * @param player
   * @param target
   * @param item
   * @param directHit whether the hit is direct ({@code true}) or indirect ({@code false}).
   */
  private void attack(PlayerEntity player, Entity target, ItemStack item, boolean directHit) {
    consumeItem(player, item, directHit, burn(target, item, directHit ? TorchHitConfig.getDirectHitDuration() : TorchHitConfig.getIndirectHitDuration()));
  }

  /**
   * Consumes the used item if enabled.
   * 
   * @param player
   * @param item
   * @param directHit whether the hit is direct ({@code true}) or indirect ({@code false}).
   * @param fireSeconds
   */
  private void consumeItem(PlayerEntity player, ItemStack item, boolean directHit, int fireSeconds) {
    if (
      !player.isCreative() &&
      isTorch(item) &&
      TorchHitConfig.getConsumeTorch() &&
      (directHit || TorchHitConfig.getConsumeWithIndirectHits()) &&
      (TorchHitConfig.getConsumeWithoutFire() || fireSeconds > 0)
    ) {
      item.decrement(1);
    }
  }

  /**
   * Sets the entity on fire.
   * 
   * @param target
   * @param torch
   * @param defaultDuration
   */
  private int burn(Entity target, ItemStack item, int defaultDuration) {
    int fireSeconds = getFireSeconds(item, target, defaultDuration);
    if (fireSeconds > 0) {
      if (isSoulfiredInstalled) {
        SoulFired.setOnFire(target, fireSeconds, isSoulTorch(item));
      } else {
        target.setOnFireFor(fireSeconds);
      }
    }
    return fireSeconds;
  }

  /**
   * Returns the amount of seconds the given entity should stay on fire.
   * 
   * @param item
   * @param target
   * @param fireDuration
   * @return the amount of seconds the given entity should stay on fire.
   */
  private int getFireSeconds(ItemStack item, Entity target, int fireDuration) {
    if ((Math.random() * 100) < TorchHitConfig.getFireChance()) {
      if (isSoulTorch(item)) {
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
   * @return
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
  private Hand getHand(PlayerEntity player) {
    if (isTorch(player.getMainHandStack())) {
      return Hand.MAIN_HAND;
    }
    if (isTorch(player.getOffHandStack())) {
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
    return Registry.ITEM.getKey(item).get().getValue().toString();
  }
}
