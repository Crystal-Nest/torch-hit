package crystalspider.torchhit;

import crystalspider.torchhit.config.ModConfig;
import crystalspider.torchhit.handler.AttackEntityHandler;
import net.fabricmc.api.ModInitializer;
import fuzs.forgeconfigapiport.api.config.v3.ForgeConfigRegistry;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.neoforged.fml.config.ModConfig.Type;

/**
 * Torch hit! mod loader.
 */
public class ModLoader implements ModInitializer {
  /**
   * ID of this mod.
   */
  public static final String MOD_ID = "torchhit";

  @Override
	public void onInitialize() {
    ForgeConfigRegistry.INSTANCE.register(MOD_ID, Type.COMMON, ModConfig.SPEC);
    ServerLivingEntityEvents.ALLOW_DAMAGE.register(AttackEntityHandler::handle);
  }
}
