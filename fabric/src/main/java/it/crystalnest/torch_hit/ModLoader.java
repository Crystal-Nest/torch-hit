package it.crystalnest.torch_hit;

import it.crystalnest.torch_hit.handler.FabricAttackHandler;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import it.crystalnest.torch_hit.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.neoforged.fml.config.ModConfig.Type;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import org.jetbrains.annotations.ApiStatus;

/**
 * Mod loader.
 */
@ApiStatus.Internal
public final class ModLoader implements ModInitializer {
  @Override
  public void onInitialize() {
    ConfigRegistry.INSTANCE.register(Constants.MOD_ID, Type.COMMON, ModConfig.SPEC, ModConfig.FILE_NAME);
    CommonModLoader.init();
    ServerLivingEntityEvents.ALLOW_DAMAGE.register(FabricAttackHandler::handle);
  }
}
