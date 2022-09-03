package crystalspider.torchhit;

import crystalspider.torchhit.config.TorchHitConfig;
import crystalspider.torchhit.handlers.AttackEntityEventHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;


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
   * Network channel protocol version.
   */
  public static final String PROTOCOL_VERSION = "1.16-4.0";
  /**
   * {@link SimpleChannel} instance for compatibility client-server.
   */
  public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, (version) -> true);

  public TorchHitLoader() {
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TorchHitConfig.SPEC);
    MinecraftForge.EVENT_BUS.register(new AttackEntityEventHandler());
  }
}