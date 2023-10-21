package crystalspider.torchhit.optional;

import crystalspider.soulfired.api.FireManager;
import net.minecraft.entity.Entity;

/**
 * Proxy for Soul Fire'd mod.
 */
public final class SoulFired {
  /**
   * Sets on fire the given entity, for the given seconds, with the correct Fire Type.
   * 
   * @param entity
   * @param seconds
   * @param soul
   */
  public static void setOnFire(Entity entity, int seconds, boolean soul) {
    FireManager.setOnFire(entity, seconds, soul ? FireManager.SOUL_FIRE_TYPE : FireManager.DEFAULT_FIRE_TYPE);
  }
}