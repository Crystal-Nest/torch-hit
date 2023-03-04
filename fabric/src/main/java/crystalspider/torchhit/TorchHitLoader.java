package crystalspider.torchhit;

import crystalspider.torchhit.config.TorchHitConfig;
import crystalspider.torchhit.handlers.AttackEntityHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

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
    ModLoadingContext.registerConfig(MODID, ModConfig.Type.COMMON, TorchHitConfig.SPEC);
    AttackEntityCallback.EVENT.register(AttackEntityHandler::handle);
  }
}