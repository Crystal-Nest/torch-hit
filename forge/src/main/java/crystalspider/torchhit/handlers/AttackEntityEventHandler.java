package crystalspider.torchhit.handlers;

import javax.annotation.Nullable;

import crystalspider.torchhit.config.TorchHitConfig;
import crystalspider.torchhit.optional.SoulFired;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
    Player player = event.getPlayer();
    if (!player.level.isClientSide && !player.isSpectator()) {
      Entity target = event.getTarget();
      InteractionHand interactionHand = getInteractionHand(player);
      if (interactionHand != null && !target.fireImmune()) {
        ItemStack item = player.getItemInHand(interactionHand);
        if (interactionHand == InteractionHand.MAIN_HAND) {
          attack(player, target, item, TorchHitConfig.getDirectHitDuration());
        } else if (isAllowedTool(player.getMainHandItem().getItem())) {
          attack(player, target, item, TorchHitConfig.getIndirectHitDuration());
        }
      }
    }
  }

  /**
   * Attack the target entity with the item setting it on fire.
   * 
   * @param player
   * @param target
   * @param item
   * @param defaultDuration
   */
  private void attack(Player player, Entity target, ItemStack item, int defaultDuration) {
    consumeItem(player, item, burn(target, item, defaultDuration));
  }

  /**
   * Consumes the used item if enabled.
   * 
   * @param player
   * @param item
   * @param fireSeconds
   */
  private void consumeItem(Player player, ItemStack item, int fireSeconds) {
    if (
      !player.isCreative() &&
      ((isCandle(item) && TorchHitConfig.getConsumeCandle()) || (isTorch(item) && TorchHitConfig.getConsumeTorch())) &&
      (TorchHitConfig.getConsumeWithoutFire() || fireSeconds > 0)
    ) {
      item.shrink(1);
    }
  }

  /**
   * Sets the entity on fire.
   * 
   * @param target
   * @param item
   * @param defaultDuration
   * @return amound of seconds the entity will be set on fire.
   */
  private int burn(Entity target, ItemStack item, int defaultDuration) {
    int fireSeconds = getFireSeconds(item, target, defaultDuration);
    if (fireSeconds > 0) {
      target.setSecondsOnFire(fireSeconds);
      setFireId(target, item);
    }
    return fireSeconds;
  }

  /**
   * If Soul Fire'd is installed, sets the correct Fire Id.
   * 
   * @param target
   * @param item
   */
  private void setFireId(Entity target, ItemStack item) {
    if (isSoulfiredInstalled) {
      if (isSoulTorch(item)) {
        SoulFired.setOnSoulFire(target);
      } else {
        SoulFired.setOnFire(target);
      }
    }
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
        if (target instanceof AbstractPiglin) {
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
   * Returns the {@link InteractionHand} of the {@link Player} holding a torch.
   * Null if none could be found.
   * 
   * @param player
   * @return {@link InteractionHand} holding a torch or null.
   */
  @Nullable
  private InteractionHand getInteractionHand(Player player) {
    if (isValidItem(player.getMainHandItem())) {
      return InteractionHand.MAIN_HAND;
    }
    if (isValidItem(player.getOffhandItem())) {
      return InteractionHand.OFF_HAND;
    }
    return null;
  }

  /**
   * Checks whether the given {@link ItemStack} is a valid item (torch or candle).
   * 
   * @param item
   * @return whether the given {@link ItemStack} is a valid item.
   */
  private boolean isValidItem(ItemStack item) {
    return isTorch(item) || isCandle(item);
  }

  /**
   * Checks whether the given {@link ItemStack} is a torch.
   * 
   * @param item
   * @return whether the given {@link ItemStack} is a torch.
   */
  private boolean isTorch(ItemStack item) {
    return item.is(Items.TORCH) || TorchHitConfig.getModdedTorchList().contains(getKey(item.getItem())) || isSoulTorch(item);
  }

  /**
   * Checks whether the given {@link ItemStack} is a soul torch.
   * 
   * @param item
   * @return whether the given {@link ItemStack} is a soul torch.
   */
  private boolean isSoulTorch(ItemStack item) {
    return item.is(Items.SOUL_TORCH) || TorchHitConfig.getModdedSoulTorchList().contains(getKey(item.getItem()));
  }

  /**
   * Checks whether the given {@link ItemStack} is a candle.
   * 
   * @param item
   * @return whether the given {@link ItemStack} is a candle.
   */
  private boolean isCandle(ItemStack item) {
    return TorchHitConfig.getAllowCandles() && item.is(ItemTags.CANDLES);
  }

  /**
   * Returns the in-game ID of the item passed as parameter.
   * 
   * @param item
   * @return in-game ID of the given item.
   */
  private String getKey(Item item) {
    return ForgeRegistries.ITEMS.getKey(item).toString();
  }
}
