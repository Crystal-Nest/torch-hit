package crystalspider.torchhit.handlers;

import java.util.ArrayList;

import javax.annotation.Nullable;

import crystalspider.torchhit.TorchHitLoader;
import crystalspider.torchhit.config.TorchHitConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AttackEntityEventHandler {
  /**
   * Fire Aspect Level for Direct Hits.
   */
  private final Integer directHitLevel;
  /**
   * Fire Aspect Duration for Direct Hits.
   */
  private final Double directHitDuration;
  /**
   * Fire Aspect Level for Indirect Hits.
   */
  private final Integer indirectHitLevel;
  /**
   * Fire Aspect Duration for Indirect Hits.
   */
  private final Double indirectHitDuration;
  /**
   * List of tools that can be used to deal Indirect Hits.
   * An empty list with {@link #indirectHitEnabled} set to true indicates that any item should allow for an Indirect Hit.
   */
  private final ArrayList<String> indirectHitToolList;
  /**
   * Whether Indirect Hits are enabled.
   */
  private final Boolean indirectHitEnabled;

	public AttackEntityEventHandler() {
		directHitLevel = TorchHitConfig.getDirectHitLevel();
    directHitDuration = TorchHitConfig.getDirectHitDuration();
    indirectHitLevel = TorchHitConfig.getIndirectHitLevel();
    indirectHitDuration = TorchHitConfig.getIndirectHitDuration();
    indirectHitToolList = TorchHitConfig.getIndirectHitToolList();
    indirectHitEnabled = TorchHitConfig.getIndirectHitEnabled();
	}

  @SubscribeEvent(priority = EventPriority.HIGH)
  public void onAttackEntityEvent(AttackEntityEvent event) {
    Player player = event.getPlayer();
    if (!player.isSpectator()) {
      Entity targetedEntity = event.getTarget();
      InteractionHand torchHand = getTorchHand(player);
      if (torchHand != null && !targetedEntity.fireImmune()) {
        if (torchHand == InteractionHand.MAIN_HAND) {
          // targetedEntity.setSecondsOnFire();
        } else if (torchHand == InteractionHand.OFF_HAND) {

        }
      }
    }
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
}
