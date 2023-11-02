package crystalspider.torchhit;

import crystalspider.torchhit.config.TorchHitConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

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
  public static final int PROTOCOL_VERSION = 1_20_2__6_0;
  /**
   * {@link SimpleChannel} instance for compatibility client-server.
   */
  public static final SimpleChannel INSTANCE = ChannelBuilder.named(new ResourceLocation(MODID, "main")).networkProtocolVersion(PROTOCOL_VERSION).optionalClient().simpleChannel();

  public TorchHitLoader() {
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TorchHitConfig.SPEC);
  }
}
