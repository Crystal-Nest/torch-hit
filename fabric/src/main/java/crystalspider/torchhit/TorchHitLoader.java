package crystalspider.torchhit;

import crystalspider.config.FabricConfig;
import crystalspider.torchhit.config.TorchHitConfig;
import crystalspider.torchhit.handlers.AttackEntityHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;

/**
 * Torch hit! mod loader.
 */
public class TorchHitLoader implements ModInitializer {
  /**
   * ID of this mod.
   */
  public static final String MODID = "torchhit";

  @Override
	public void onInitialize() {
    FabricConfig.register(TorchHitConfig.BUILDER);
    AttackEntityCallback.EVENT.register(new AttackEntityHandler()::handle);
  }
}