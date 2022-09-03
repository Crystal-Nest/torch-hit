package crystalspider.torchhit.optional;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTypeChanger;
import net.minecraft.entity.Entity;

/**
 * Proxy for Soul Fire'd mod.
 */
public abstract class SoulFired {
  /**
   * Sets the Fire id of the given entity to {@link FireManager#BASE_FIRE_ID}.
   * 
   * @param entity
   */
  public static void setOnFire(Entity entity) {
    setFireId(entity, FireManager.BASE_FIRE_ID);
  }

  /**
   * Sets the Fire id of the given entity to {@link FireManager#SOUL_FIRE_ID}.
   * 
   * @param entity
   */
  public static void setOnSoulFire(Entity entity) {
    setFireId(entity, FireManager.SOUL_FIRE_ID);
  }

  /**
   * Sets the Fire id of the given entity to the given id.
   * 
   * @param entity
   * @param id
   */
  public static void setFireId(Entity entity, String id) {
    if (FireManager.isFireId(id)) {
      ((FireTypeChanger) entity).setFireId(id);
    }
  }
}