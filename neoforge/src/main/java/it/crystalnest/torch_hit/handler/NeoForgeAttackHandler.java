package it.crystalnest.torch_hit.handler;

import it.crystalnest.torch_hit.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

/**
 * NeoForge attack handler.
 */
@EventBusSubscriber(modid = Constants.MOD_ID)
public final class NeoForgeAttackHandler {
  private NeoForgeAttackHandler() {}

  /**
   * Handles the {@link LivingIncomingDamageEvent}.
   *
   * @param event {@link LivingIncomingDamageEvent}.
   */
  @SubscribeEvent
  public static void handle(LivingIncomingDamageEvent event) {
    AttackHandler.handle(event.getSource().getEntity(), event.getSource().getDirectEntity(), event.getEntity());
  }
}
