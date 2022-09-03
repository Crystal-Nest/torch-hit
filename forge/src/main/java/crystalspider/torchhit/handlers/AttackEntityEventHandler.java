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
    if (!player.isSpectator()) {
      Entity entity = event.getTarget();
      Hand torchHand = getTorchHand(player);
      if (torchHand != null && !entity.fireImmune()) {
        ItemStack torch = player.getItemInHand(torchHand);
        if (torchHand == Hand.MAIN_HAND) {
          burn(entity, torch, TorchHitConfig.getDirectHitDuration());
        } else if (isAllowedTool(player.getMainHandItem().getItem())) {
          burn(entity, torch, TorchHitConfig.getIndirectHitDuration());
        }
      }
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
      if (entity instanceof AbstractPiglinEntity) {
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
   * Returns the {@link Hand} of the {@link PlayerEntity} holding a torch.
   * Null if none could be found.
   * 
   * @param player
   * @return {@link Hand} holding a torch or null.
   */
  @Nullable
  private Hand getTorchHand(PlayerEntity player) {
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
   * @param itemStack
   * @return whether the given {@link ItemStack} is a torch.
   */
  private boolean isTorch(ItemStack itemStack) {
    return itemStack.getItem() == Items.SOUL_TORCH || TorchHitConfig.getModdedTorchList().contains(getKey(itemStack.getItem())) || isSoulTorch(itemStack);
  }

  /**
   * Checks whether the given {@link ItemStack} is a soul torch.
   * 
   * @param itemStack
   * @return whether the given {@link ItemStack} is a soul torch.
   */
  private boolean isSoulTorch(ItemStack itemStack) {
    return itemStack.getItem() == Items.SOUL_TORCH || TorchHitConfig.getModdedSoulTorchList().contains(getKey(itemStack.getItem()));
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
