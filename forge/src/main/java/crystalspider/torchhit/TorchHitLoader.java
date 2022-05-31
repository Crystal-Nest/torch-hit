package crystalspider.torchhit;

import crystalspider.torchhit.config.TorchHitConfig;
import crystalspider.torchhit.handlers.RightClickBlockHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

/**
 * Torch hit! mod loader.
 */
@Mod(TorchHitLoader.MODID)
public class TorchHitLoader {
  /**
   * ID of this mod.
   */
  public static final String MODID = "torchhit";

  public TorchHitLoader() {
    MinecraftForge.EVENT_BUS.register(new RightClickBlockHandler());
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TorchHitConfig.SPEC);
  }
}