package it.crystalnest.torch_hit.config;

import it.crystalnest.cobweb.api.config.CommonConfig;
import it.crystalnest.torch_hit.Constants;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * Mod common configuration.
 */
@ApiStatus.Internal
public final class ModConfig extends CommonConfig {
  /**
   * Mod common configuration.
   */
  public static final ModConfig CONFIG = register(Constants.MOD_ID, ModConfig::new);

  /**
   * Fire Aspect Duration for Direct Hits.
   */
  private ModConfigSpec.IntValue directHitDuration;

  /**
   * Fire Aspect Duration for Indirect Hits.
   */
  private ModConfigSpec.IntValue indirectHitDuration;

  /**
   * List of tools that can be used to deal Indirect Hits.
   * Empty if Indirect Hits are disabled.
   */
  private ModConfigSpec.ConfigValue<List<? extends String>> indirectHitToolList;

  /**
   * How much Indirect Hits should damage wooden tools.<br />
   * Must be positive, {@code 0} to disable.
   */
  private ModConfigSpec.IntValue indirectHitToolDamage;

  /**
   * List of item ids that should be considered as a Torch.
   * Defaults to a list of the most common modded torches.
   */
  private ModConfigSpec.ConfigValue<List<? extends String>> extraTorchItems;

  /**
   * List of item ids that should be considered as a Soul Torch.
   * Defaults to a list of the most common modded torches.
   */
  private ModConfigSpec.ConfigValue<List<? extends String>> extraSoulTorchItems;

  /**
   * List of item ids that should be considered as a Copper Torch.
   * Defaults to a list of the most common modded torches.
   */
  private ModConfigSpec.ConfigValue<List<? extends String>> extraCopperTorchItems;

  /**
   * Whether Vanilla torches can set targets on fire.
   */
  private ModConfigSpec.BooleanValue vanillaTorchesEnabled;

  /**
   * Whether to allow candles to act as torches.
   */
  private ModConfigSpec.ConfigValue<Boolean> allowCandles;

  /**
   * Whether candles should break upon use.
   */
  private ModConfigSpec.ConfigValue<Boolean> consumeCandle;

  /**
   * Whether torches should break upon use.
   */
  private ModConfigSpec.BooleanValue consumeTorch;

  /**
   * Whether to break the torch/candle upon use even if no fire was set.
   */
  private ModConfigSpec.BooleanValue consumeWithoutFire;

  /**
   * Whether to break the torch/candle upon indirect hits.
   */
  private ModConfigSpec.BooleanValue consumeWithIndirectHit;

  /**
   * Chance (in percentage) for torches/candles to set targets on fire.
   */
  private ModConfigSpec.IntValue fireChance;

  /**
   * Whether mobs wielding a torch can set their targets on fire.
   */
  private ModConfigSpec.BooleanValue fireFromMobs;

  /**
   * @param builder configuration builder.
   */
  private ModConfig(ModConfigSpec.Builder builder) {
    super(builder);
  }

  /**
   * Returns the value of {@link #directHitDuration}.
   *
   * @return {@link #directHitDuration} as read from the configuration file.
   */
  public static Integer getDirectHitDuration() {
    return CONFIG.directHitDuration.get();
  }

  /**
   * Returns the value of {@link #indirectHitDuration}.
   *
   * @return {@link #indirectHitDuration} as read from the configuration file.
   */
  public static Integer getIndirectHitDuration() {
    return CONFIG.indirectHitDuration.get();
  }

  /**
   * Returns the value of {@link #indirectHitToolList}.
   *
   * @return {@link #indirectHitToolList} as read from the configuration file.
   */
  public static List<? extends String> getIndirectHitToolList() {
    return CONFIG.indirectHitToolList.get();
  }

  /**
   * Returns the value of {@link #indirectHitToolDamage}.
   *
   * @return {@link #indirectHitToolDamage} as read from the configuration file.
   */
  public static Integer getIndirectHitToolDamage() {
    return CONFIG.indirectHitToolDamage.get();
  }

  /**
   * Returns the value of {@link #extraTorchItems}.
   *
   * @return {@link #extraTorchItems} as read from the configuration file.
   */
  public static List<? extends String> getExtraTorchItems() {
    return CONFIG.extraTorchItems.get();
  }

  /**
   * Returns the value of {@link #extraSoulTorchItems}.
   *
   * @return {@link #extraSoulTorchItems} as read from the configuration file.
   */
  public static List<? extends String> getExtraSoulTorchItems() {
    return CONFIG.extraSoulTorchItems.get();
  }

  /**
   * Returns the value of {@link #extraCopperTorchItems}.
   *
   * @return {@link #extraCopperTorchItems} as read from the configuration file.
   */
  public static List<? extends String> getExtraCopperTorchItems() {
    return CONFIG.extraCopperTorchItems.get();
  }

  /**
   * Returns the value of {@link #vanillaTorchesEnabled}.
   *
   * @return {@link #vanillaTorchesEnabled} as read from the configuration file.
   */
  public static Boolean getVanillaTorchesEnabled() {
    return CONFIG.vanillaTorchesEnabled.get();
  }

  /**
   * Returns the value of {@link #allowCandles}.
   *
   * @return {@link #allowCandles} as read from the configuration file.
   */
  public static Boolean getAllowCandles() {
    return CONFIG.allowCandles.get();
  }

  /**
   * Returns the value of {@link #consumeCandle}.
   *
   * @return {@link #consumeCandle} as read from the configuration file.
   */
  public static Boolean getConsumeCandle() {
    return CONFIG.consumeCandle.get();
  }

  /**
   * Returns the value of {@link #consumeTorch}.
   *
   * @return {@link #consumeTorch} as read from the configuration file.
   */
  public static Boolean getConsumeTorch() {
    return CONFIG.consumeTorch.get();
  }

  /**
   * Returns the value of {@link #consumeWithoutFire}.
   *
   * @return {@link #consumeWithoutFire} as read from the configuration file.
   */
  public static Boolean getConsumeWithoutFire() {
    return CONFIG.consumeWithoutFire.get();
  }

  /**
   * Returns the value of {@link #consumeWithIndirectHit}.
   *
   * @return {@link #consumeWithIndirectHit} as read from the configuration file.
   */
  public static Boolean getConsumeWithIndirectHits() {
    return CONFIG.consumeWithIndirectHit.get();
  }

  /**
   * Returns the value of {@link #fireChance}.
   *
   * @return {@link #fireChance} as read from the configuration file.
   */
  public static Integer getFireChance() {
    return CONFIG.fireChance.get();
  }

  /**
   * Returns the value of {@link #fireFromMobs}.
   *
   * @return {@link #fireFromMobs} as read from the configuration file.
   */
  public static Boolean getFireFromMobs() {
    return CONFIG.fireFromMobs.get();
  }

  @Override
  protected void define(ModConfigSpec.Builder builder) {
    // Max Fire Aspect level (2) times Fire Aspect duration (4).
    int maxDuration = 8;
    directHitDuration = builder.comment(" Fire damage duration for direct (main hand) hits.").defineInRange("direct hit duration", 4, 1, maxDuration);
    indirectHitDuration = builder.comment(" Fire damage duration for indirect (offhand + tool) hits.").defineInRange("indirect hit duration", 2, 1, maxDuration);
    indirectHitToolList = builder
      .comment(
        " List of tools that allow for an indirect hit when a torch is being held in the offhand.",
        " Leave empty to disable indirect hits.",
        " Insert either item categories or specific item IDs."
      )
      .defineListAllowEmpty("indirect tools", () -> List.of("sword", "axe", "pickaxe", "shovel", "hoe"), () -> "", this::stringListValidator);
    indirectHitToolDamage = builder.comment(" How much Indirect Hits should damage wooden tools (in percentage).", " 0 to disable.").defineInRange("indirect hit tool damage", 33, 0, 100);
    extraTorchItems = builder.comment(" List of item ids that should be considered as a Torch.").defineListAllowEmpty(
      "extra torch items",
      () -> List.of(
        "bonetorch:bonetorch",
        "torchmaster:megatorch",
        "hardcore_torches:lit_torch",
        "magnumtorch:diamond_magnum_torch",
        "magnumtorch:emerald_magnum_torch",
        "magnumtorch:amethyst_magnum_torch",
        "magical_torches:mega_torch",
        "magical_torches:grand_torch",
        "magical_torches:medium_torch",
        "magical_torches:small_torch",
        "pgwbandedtorches:banded_torch_white",
        "pgwbandedtorches:banded_torch_orange",
        "pgwbandedtorches:banded_torch_magenta",
        "pgwbandedtorches:banded_torch_light_blue",
        "pgwbandedtorches:banded_torch_yellow",
        "pgwbandedtorches:banded_torch_lime",
        "pgwbandedtorches:banded_torch_pink",
        "pgwbandedtorches:banded_torch_gray",
        "pgwbandedtorches:banded_torch_light_gray",
        "pgwbandedtorches:banded_torch_cyan",
        "pgwbandedtorches:banded_torch_purple",
        "pgwbandedtorches:banded_torch_blue",
        "pgwbandedtorches:banded_torch_brown",
        "pgwbandedtorches:banded_torch_green",
        "pgwbandedtorches:banded_torch_red",
        "pgwbandedtorches:banded_torch_black"
      ),
      () -> "mod_id:item_id",
      this::stringListValidator
    );
    extraSoulTorchItems = builder.comment(" List of item ids that should be considered as a Soul Torch.").defineListAllowEmpty(
      "extra soul torch items",
      () -> List.of(
        "pgwbandedtorches:banded_soul_torch_white",
        "pgwbandedtorches:banded_soul_torch_orange",
        "pgwbandedtorches:banded_soul_torch_magenta",
        "pgwbandedtorches:banded_soul_torch_light_blue",
        "pgwbandedtorches:banded_soul_torch_yellow",
        "pgwbandedtorches:banded_soul_torch_lime",
        "pgwbandedtorches:banded_soul_torch_pink",
        "pgwbandedtorches:banded_soul_torch_gray",
        "pgwbandedtorches:banded_soul_torch_light_gray",
        "pgwbandedtorches:banded_soul_torch_cyan",
        "pgwbandedtorches:banded_soul_torch_purple",
        "pgwbandedtorches:banded_soul_torch_blue",
        "pgwbandedtorches:banded_soul_torch_brown",
        "pgwbandedtorches:banded_soul_torch_green",
        "pgwbandedtorches:banded_soul_torch_red",
        "pgwbandedtorches:banded_soul_torch_black"
      ),
      () -> "mod_id:item_id",
      this::stringListValidator
    );
    extraCopperTorchItems = builder.comment(" List of item ids that should be considered as a Copper Torch.").defineListAllowEmpty(
      "extra copper torch items",
      () -> List.of(
        "pgwbandedtorches:banded_copper_torch_white",
        "pgwbandedtorches:banded_copper_torch_orange",
        "pgwbandedtorches:banded_copper_torch_magenta",
        "pgwbandedtorches:banded_copper_torch_light_blue",
        "pgwbandedtorches:banded_copper_torch_yellow",
        "pgwbandedtorches:banded_copper_torch_lime",
        "pgwbandedtorches:banded_copper_torch_pink",
        "pgwbandedtorches:banded_copper_torch_gray",
        "pgwbandedtorches:banded_copper_torch_light_gray",
        "pgwbandedtorches:banded_copper_torch_cyan",
        "pgwbandedtorches:banded_copper_torch_purple",
        "pgwbandedtorches:banded_copper_torch_blue",
        "pgwbandedtorches:banded_copper_torch_brown",
        "pgwbandedtorches:banded_copper_torch_green",
        "pgwbandedtorches:banded_copper_torch_red",
        "pgwbandedtorches:banded_copper_torch_black"
      ),
      () -> "mod_id:item_id",
      this::stringListValidator
    );
    vanillaTorchesEnabled = builder
      .comment(
        " Whether Vanilla torches can set targets on fire.",
        " If false, only the items specified by [extra torch items] and [extra soul torch items] will set targets on fire."
      )
      .define("vanilla torches enabled", true);
    allowCandles = builder.comment(" Whether to allow candles to act as torches.").define("allow candles", true);
    consumeCandle = builder.comment(" Whether candles should break upon use.", "Effective only if [allowCandles] is enabled.").define("consume candle", true);
    consumeTorch = builder.comment(" Whether torches should break upon use.").define("consume torch", false);
    consumeWithoutFire = builder
      .comment(
        " Whether to break the torch/candle upon use even if no fire was set.",
        " Effective only if [fire chance] and at least one of [consume torch] and [consume candle] are set different from default."
      )
      .define("consume without fire", false);
    consumeWithIndirectHit = builder
      .comment(
        " Whether to break the torch/candle upon indirect hits.",
        " Effective only if [consume torch] is set to true."
      )
      .define("consume with indirect hits", false);
    fireChance = builder.comment(" Chance (in percentage) for torches/candles to set targets on fire.").defineInRange("fire chance", 100, 1, 100);
    fireFromMobs = builder.comment(" Whether mobs wielding a torch can set their targets on fire.", " Generally useful only when other mods tweak mobs to wield torches.").define("fire from mobs", true);
  }
}
