package crystalspider.torchhit.handlers;

import java.util.ArrayList;

import javax.annotation.Nullable;

import crystalspider.torchhit.config.TorchHitConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * {@link AttackEntityEvent} handler.
 */
public class AttackEntityEventHandler {
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

	public AttackEntityEventHandler() {
    directHitDuration = TorchHitConfig.getDirectHitDuration() * 20;
    indirectHitDuration = TorchHitConfig.getIndirectHitDuration() * 20;
    indirectHitToolList = TorchHitConfig.getIndirectHitToolList();
	}

  /**
   * Handles the {@link AttackEntityEvent}.
   * 
   * @param event
   */
  @SubscribeEvent()
  public void onAttackEntityEvent(AttackEntityEvent event) {
    Player player = event.getPlayer();
    if (!player.isSpectator()) {
      Entity targetedEntity = event.getTarget();
      InteractionHand torchHand = getTorchHand(player);
      if (torchHand != null && !targetedEntity.fireImmune()) {
        if (torchHand == InteractionHand.MAIN_HAND) {
          targetedEntity.setRemainingFireTicks(targetedEntity.getRemainingFireTicks() + directHitDuration);
        } else if (torchHand == InteractionHand.OFF_HAND && isAllowedTool(player.getMainHandItem().getItem())) {
          targetedEntity.setRemainingFireTicks(targetedEntity.getRemainingFireTicks() + indirectHitDuration);
        }
      }
    }
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
    return itemStack.is(Items.TORCH) || itemStack.is(Items.SOUL_TORCH);
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
