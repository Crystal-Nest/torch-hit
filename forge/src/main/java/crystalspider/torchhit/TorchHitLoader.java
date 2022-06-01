package crystalspider.torchhit;

import com.mojang.logging.LogUtils;

import org.slf4j.Logger;

import crystalspider.torchhit.config.TorchHitConfig;
import crystalspider.torchhit.handlers.AttackEntityEventHandler;
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

  /**
   * Logger.
   */
  public static final Logger LOGGER = LogUtils.getLogger();

  public TorchHitLoader() {
    MinecraftForge.EVENT_BUS.register(new AttackEntityEventHandler());
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TorchHitConfig.SPEC);
  }
}