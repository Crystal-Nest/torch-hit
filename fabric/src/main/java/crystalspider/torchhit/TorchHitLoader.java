package crystalspider.torchhit;

import crystalspider.torchhit.config.TorchHitConfig;
import crystalspider.torchhit.handlers.AttackEntityHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraftforge.fml.config.ModConfig;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;

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
    ForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.COMMON, TorchHitConfig.SPEC);
    AttackEntityCallback.EVENT.register(AttackEntityHandler::handle);
  }
}