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
    if (!player.isSpectator()) {
      Entity entity = event.getTarget();
      InteractionHand torchHand = getTorchHand(player);
      if (torchHand != null && !entity.fireImmune()) {
        ItemStack torch = player.getItemInHand(torchHand);
        if (torchHand == InteractionHand.MAIN_HAND) {
          attack(player, entity, torch, TorchHitConfig.getDirectHitDuration());
        } else if (isAllowedTool(player.getMainHandItem().getItem())) {
          attack(player, entity, torch, TorchHitConfig.getIndirectHitDuration());
        }
      }
    }
  }

  /**
   * Attack the entity with the torch setting it on fire.
   * 
   * @param player
   * @param entity
   * @param torch
   * @param defaultDuration
   */
  private void attack(Player player, Entity entity, ItemStack torch, int defaultDuration) {
    burn(entity, torch, defaultDuration);
    breakCandle(player, torch);
  }

  /**
   * Breaks the used candle if enabled.
   * 
   * @param torch
   */
  private void breakCandle(Player player, ItemStack torch) {
    if (!player.isCreative() && isCandle(torch) && TorchHitConfig.getBreakCandles()) {
      torch.shrink(1);
    }
  }

  /**
   * Sets the entity on fire.
   * 
   * @param entity
   * @param torch
   * @param defaultDuration
   */
  private void burn(Entity entity, ItemStack torch, int defaultDuration) {
    entity.setSecondsOnFire(getFireSeconds(torch, entity, defaultDuration));
    setFireId(entity, torch);
  }

  /**
   * If Soul Fire'd is installed, sets the correct Fire Id.
   * 
   * @param entity
   * @param torch
   */
  private void setFireId(Entity entity, ItemStack torch) {
    if (isSoulfiredInstalled) {
      if (isSoulTorch(torch)) {
        SoulFired.setOnSoulFire(entity);
      } else {
        SoulFired.setOnFire(entity);
      }
    }
  }

  /**
   * Returns the amount of seconds the given entity should stay on fire.
   * 
   * @param torch
   * @param entity
   * @param fireDuration
   * @return the amount of seconds the given entity should stay on fire.
   */
  private int getFireSeconds(ItemStack torch, Entity entity, int fireDuration) {
    if (isSoulTorch(torch)) {
      if (isSoulfiredInstalled) {
        return fireDuration;
      }
      if (entity instanceof AbstractPiglin) {
        return fireDuration * 2;
      }
      return fireDuration + 1;
    }
    return fireDuration;
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
  private InteractionHand getTorchHand(Player player) {
    if (isTorch(player.getMainHandItem())) {
      return InteractionHand.MAIN_HAND;
    }
    if (isTorch(player.getOffhandItem())) {
      return InteractionHand.OFF_HAND;
    }
    return null;
  }

  /**
   * Checks whether the given {@link ItemStack} is a torch.
   * 
   * @param itemStack
   * @return whether the given {@link ItemStack} is a torch.
   */
  private boolean isTorch(ItemStack itemStack) {
    return itemStack.is(Items.TORCH) || isCandle(itemStack) || TorchHitConfig.getModdedTorchList().contains(getKey(itemStack.getItem())) || isSoulTorch(itemStack);
  }

  /**
   * Checks whether the given {@link ItemStack} is a soul torch.
   * 
   * @param itemStack
   * @return whether the given {@link ItemStack} is a soul torch.
   */
  private boolean isSoulTorch(ItemStack itemStack) {
    return itemStack.is(Items.SOUL_TORCH) || TorchHitConfig.getModdedSoulTorchList().contains(getKey(itemStack.getItem()));
  }

  /**
   * Checks whether the given {@link ItemStack} is a candle.
   * 
   * @param itemStack
   * @return whether the given {@link ItemStack} is a candle.
   */
  private boolean isCandle(ItemStack itemStack) {
    return TorchHitConfig.getAllowCandles() && itemStack.is(ItemTags.CANDLES);
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
