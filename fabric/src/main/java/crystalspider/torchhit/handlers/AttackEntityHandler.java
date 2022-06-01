package crystalspider.torchhit.handlers;

import java.util.ArrayList;

import javax.annotation.Nullable;

import crystalspider.torchhit.config.TorchHitConfig;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
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
   * Fire Aspect Duration for Direct Hits.
   */
  private final Integer directHitDuration;
  /**
   * Fire Aspect Duration for Indirect Hits.
   */
  private final Integer indirectHitDuration;
  /**
   * List of tools that can be used to deal Indirect Hits.
   * Empty if Indirect Hits are disabled.
   */
  private final ArrayList<String> indirectHitToolList;

	public AttackEntityHandler() {
    directHitDuration = TorchHitConfig.getDirectHitDuration() * 20;
    indirectHitDuration = TorchHitConfig.getIndirectHitDuration() * 20;
    indirectHitToolList = TorchHitConfig.getIndirectHitToolList();
	}

  /**
   * Handles the {@link AttackEntityCallback} event.
   * 
   * @param player
   * @param world
   * @param hand
   * @param entity
   * @param hitResult
   * @return {@link ActionResult}.
   */
  public ActionResult handle(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
    if (!player.isSpectator()) {
      Hand torchHand = getTorchHand(player);
      if (torchHand != null && !entity.isFireImmune()) {
        ItemStack torch = player.getStackInHand(torchHand);
        if (torchHand == Hand.MAIN_HAND) {
          entity.setFireTicks(getFireTicks(torch, entity, directHitDuration));
        } else if (isAllowedTool(player.getMainHandStack().getItem())) {
          entity.setFireTicks(getFireTicks(torch, entity, indirectHitDuration));
        }
      }
    }
    return ActionResult.PASS;
  }

  /**
   * Returns the amount of ticks the given entity should stay on fire.
   * 
   * @param torch
   * @param entity
   * @param fireDuration
   * @return
   */
  private int getFireTicks(ItemStack torch, Entity entity, int fireDuration) {
    if (torch.isOf(Items.SOUL_TORCH)) {
      if (entity instanceof AbstractPiglinEntity) {
        return entity.getFireTicks() + fireDuration * 2;
      }
      return entity.getFireTicks() + fireDuration + 20;
    }
    return entity.getFireTicks() + fireDuration;
  }

  /**
   * Checks whether the given {@link Item} is a tool that allows Indirect Hits.
   * 
   * @param item
   * @return
   */
  private boolean isAllowedTool(Item item) {
    return !indirectHitToolList.isEmpty() && indirectHitToolList.stream().filter(toolType -> getKey(item).matches(".*:([^_]+_)*" + toolType + "(_[^_]+)*")).count() > 0;
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
   * @param itemStack
   * @return whether the given {@link ItemStack} is a torch.
   */
  private boolean isTorch(ItemStack itemStack) {
    return itemStack.isOf(Items.TORCH) || itemStack.isOf(Items.SOUL_TORCH);
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
