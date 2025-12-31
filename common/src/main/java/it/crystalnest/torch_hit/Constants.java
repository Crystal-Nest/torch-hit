package it.crystalnest.torch_hit;

import it.crystalnest.cobweb.api.item.ItemUtils;
import it.crystalnest.torch_hit.config.ModConfig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common shared constants across all loaders.
 */
@ApiStatus.Internal
public final class Constants {
  /**
   * Mod ID.
   */
  public static final String MOD_ID = "torch_hit";

  /**
   * Mod logger.
   */
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  private Constants() {}

  /**
   * Whether the given item is considered a normal torch.
   *
   * @param item item.
   * @return whether the given item is considered a normal torch.
   */
  public static boolean isNormalTorch(ItemStack item) {
    return (item.is(Items.TORCH) && ModConfig.getVanillaTorchesEnabled()) || ModConfig.getExtraTorchItems().contains(ItemUtils.getKey(item.getItem()).toString());
  }

  /**
   * Whether the given item is considered a soul torch.
   *
   * @param item item.
   * @return whether the given item is considered a soul torch.
   */
  public static boolean isSoulTorch(ItemStack item) {
    return (item.is(Items.SOUL_TORCH) && ModConfig.getVanillaTorchesEnabled()) || ModConfig.getExtraSoulTorchItems().contains(ItemUtils.getKey(item.getItem()).toString());
  }

  /**
   * Whether the given item is considered a copper torch.
   *
   * @param item item.
   * @return whether the given item is considered a copper torch.
   */
  public static boolean isCopperTorch(ItemStack item) {
    return (item.is(Items.COPPER_TORCH) && ModConfig.getVanillaTorchesEnabled()) || ModConfig.getExtraCopperTorchItems().contains(ItemUtils.getKey(item.getItem()).toString());
  }
}
