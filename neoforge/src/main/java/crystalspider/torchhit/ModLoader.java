package crystalspider.torchhit;

import crystalspider.torchhit.config.ModConfig;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.neoforge.network.NetworkRegistry.ChannelBuilder;
import net.neoforged.neoforge.network.simple.SimpleChannel;

/**
 * Torch hit! mod loader.
 */
@Mod(ModLoader.MOD_ID)
public class ModLoader {
  /**
   * ID of this mod.
   */
  public static final String MOD_ID = "torchhit";

  /**
   * Network channel protocol version.
   */
  public static final String PROTOCOL_VERSION = "1.20.2-6.0";
  /**
   * {@link SimpleChannel} instance for compatibility client-server.
   */
  public static final SimpleChannel INSTANCE = ChannelBuilder.named(new ResourceLocation(MOD_ID, "main")).networkProtocolVersion(() -> PROTOCOL_VERSION).serverAcceptedVersions(version -> true).clientAcceptedVersions(PROTOCOL_VERSION::equals).simpleChannel();

  public ModLoader() {
    ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.SPEC);
  }
}
