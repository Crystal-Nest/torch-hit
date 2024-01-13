package crystalspider.torchhit.config;

import net.minecraft.enchantment.Enchantments;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

import java.util.List;

/**
 * Torch hit! Configuration.
 */
public class ModConfig {
  /**
   * {@link ModConfigSpec} {@link ModConfigSpec.Builder Builder}.
   */
  private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
  /**
   * Common Configuration as read from the configuration file.
   */
  public static final CommonConfig COMMON = new CommonConfig(BUILDER);
  /**
   * {@link ModConfigSpec}.
   */
  public static final ModConfigSpec SPEC = BUILDER.build();

  /**
   * Returns the value of {@link CommonConfig#directHitDuration}.
   *
   * @return {@link CommonConfig#directHitDuration} as read from the {@link #COMMON common} configuration file.
   */
  public static Integer getDirectHitDuration() {
    return COMMON.directHitDuration.get();
  }

  /**
   * Returns the value of {@link CommonConfig#indirectHitDuration}.
   *
   * @return {@link CommonConfig#indirectHitDuration} as read from the {@link #COMMON common} configuration file.
   */
  public static Integer getIndirectHitDuration() {
    return COMMON.indirectHitDuration.get();
  }

  /**
   * Returns the value of {@link CommonConfig#indirectHitToolList}.
   *
   * @return {@link CommonConfig#indirectHitToolList} as read from the {@link #COMMON common} configuration file.
   */
  public static List<? extends String> getIndirectHitToolList() {
    return COMMON.indirectHitToolList.get();
  }

  /**
   * Returns the value of {@link CommonConfig#extraTorchItems}.
   *
   * @return {@link CommonConfig#extraTorchItems} as read from the {@link #COMMON common} configuration file.
   */
  public static List<? extends String> getExtraTorchItems() {
    return COMMON.extraTorchItems.get();
  }

  /**
   * Returns the value of {@link CommonConfig#extraSoulTorchItems}.
   *
   * @return {@link CommonConfig#extraSoulTorchItems} as read from the {@link #COMMON common} configuration file.
   */
  public static List<? extends String> getExtraSoulTorchItems() {
    return COMMON.extraSoulTorchItems.get();
  }

  /**
   * Returns the value of {@link CommonConfig#vanillaTorchesEnabled}.
   *
   * @return {@link CommonConfig#vanillaTorchesEnabled} as read from the {@link #COMMON common} configuration file.
   */
  public static Boolean getVanillaTorchesEnabled() {
    return COMMON.vanillaTorchesEnabled.get();
  }

  /**
   * Returns the value of {@link CommonConfig#allowCandles}.
   *
   * @return {@link CommonConfig#allowCandles} as read from the {@link #COMMON common} configuration file.
   */
  public static Boolean getAllowCandles() {
    return COMMON.allowCandles.get();
  }

  /**
   * Returns the value of {@link CommonConfig#consumeCandle}.
   *
   * @return {@link CommonConfig#consumeCandle} as read from the {@link #COMMON common} configuration file.
   */
  public static Boolean getConsumeCandle() {
    return COMMON.consumeCandle.get();
  }

  /**
   * Returns the value of {@link CommonConfig#consumeTorch}.
   *
   * @return {@link CommonConfig#consumeTorch} as read from the {@link #COMMON common} configuration file.
   */
  public static Boolean getConsumeTorch() {
    return COMMON.consumeTorch.get();
  }

  /**
   * Returns the value of {@link CommonConfig#consumeWithoutFire}.
   *
   * @return {@link CommonConfig#consumeWithoutFire} as read from the {@link #COMMON common} configuration file.
   */
  public static Boolean getConsumeWithoutFire() {
    return COMMON.consumeWithoutFire.get();
  }

  /**
   * Returns the value of {@link CommonConfig#consumeWithIndirectHit}.
   *
   * @return {@link CommonConfig#consumeWithIndirectHit} as read from the {@link #COMMON common} configuration file.
   */
  public static Boolean getConsumeWithIndirectHits() {
    return COMMON.consumeWithIndirectHit.get();
  }

  /**
   * Returns the value of {@link CommonConfig#fireChance}.
   *
   * @return {@link CommonConfig#fireChance} as read from the {@link #COMMON common} configuration file.
   */
  public static Integer getFireChance() {
    return COMMON.fireChance.get();
  }

  /**
   * Returns the value of {@link CommonConfig#fireFromMobs}.
   *
   * @return {@link CommonConfig#fireFromMobs} as read from the {@link #COMMON common} configuration file.
   */
  public static Boolean getFireFromMobs() {
    return COMMON.fireFromMobs.get();
  }

  /**
   * Common Configuration for Torch hit!.
   */
  public static class CommonConfig {
    /**
     * Fire Aspect Duration for Direct Hits.
     */
    private final IntValue directHitDuration;
    /**
     * Fire Aspect Duration for Indirect Hits.
     */
    private final IntValue indirectHitDuration;
    /**
     * List of tools that can be used to deal Indirect Hits.
     * Empty if Indirect Hits are disabled.
     */
    private final ConfigValue<List<? extends String>> indirectHitToolList;
    /**
     * List of item ids that should be considered as a Torch.
     * Defaults to a list of the most common modded torches.
     */
    private final ConfigValue<List<? extends String>> extraTorchItems;
    /**
     * List of item ids that should be considered as a Soul Torch.
     * Defaults to a list of the most common modded torches.
     */
    private final ConfigValue<List<? extends String>> extraSoulTorchItems;
    /**
     * Whether Vanilla torches can set targets on fire.
     */
    private final BooleanValue vanillaTorchesEnabled;
    /**
     * Whether to allow candles to act as torches.
     */
    private final ConfigValue<Boolean> allowCandles;
    /**
     * Whether candles should break upon use.
     */
    private final ConfigValue<Boolean> consumeCandle;
    /**
     * Whether torches should break upon use.
     */
    private final BooleanValue consumeTorch;
    /**
     * Whether to break the torch/candle upon use even if no fire was set.
     */
    private final BooleanValue consumeWithoutFire;
    /**
     * Whether to break the torch/candle upon indirect hits.
     */
    private final BooleanValue consumeWithIndirectHit;
    /**
     * Chance (in percentage) for torches/candles to set targets on fire.
     */
    private final IntValue fireChance;
    /**
     * Whether mobs wielding a torch can set their targets on fire.
     */
    private final BooleanValue fireFromMobs;

    /**
     * Defines the configuration options, their default values and their comments.
     *
     * @param builder
     */
    public CommonConfig(ModConfigSpec.Builder builder) {
      int maxDuration = Enchantments.FIRE_ASPECT.getMaxLevel() * 4;
      directHitDuration = builder.comment("Fire damage duration for direct (main hand) hits.").defineInRange("direct hit duration", 4, 1, maxDuration);
      indirectHitDuration = builder.comment("Fire damage duration for indirect (off hand + tool) hits.").defineInRange("indirect hit duration", 2, 1, maxDuration);
      indirectHitToolList = builder
        .comment(
          "List of tools that allow for an indirect hit when a torch is being held in the off hand.",
          "Leave empty to disable indirect hits.",
          "Insert either item categories or specific item IDs."
        )
        .defineListAllowEmpty(List.of("indirect tools"), () -> List.of("sword", "axe", "pickaxe", "shovel", "hoe"), (element) -> element instanceof String && !((String) element).isBlank());
      extraTorchItems = builder.comment("List of item ids that should be considered as a Torch.").defineListAllowEmpty(
        List.of("extra torch items"),
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
        (element) -> element instanceof String && !((String) element).isBlank()
      );
      extraSoulTorchItems = builder.comment("List of item ids that should be considered as a Soul Torch.").defineListAllowEmpty(
        List.of("extra soul torch items"),
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
        (element) -> element instanceof String && !((String) element).isBlank()
      );
      vanillaTorchesEnabled = builder
        .comment(
          "Whether Vanilla torches can set targets on fire.",
          "If false, only the items specified by [extra torch items] and [extra soul torch items] will set targets on fire."
        )
        .define("vanilla torches enabled", true);
      allowCandles = builder.comment("Whether to allow candles to act as torches.").define("allow candles", true);
      consumeCandle = builder.comment("Whether candles should break upon use.", "Effective only if [allowCandles] is enabled.").define("consume candle", true);
      consumeTorch = builder.comment("Whether torches should break upon use.").define("consume torch", false);
      consumeWithoutFire = builder
        .comment(
          "Whether to break the torch/candle upon use even if no fire was set.",
          "Effective only if [fire chance] and at least one of [consume torch] and [consume candle] are set different from default."
        )
        .define("consume without fire", false);
      consumeWithIndirectHit = builder
        .comment(
          "Whether to break the torch/candle upon indirect hits.",
          "Effective only if [consume torch] is set to true."
        )
        .define("consume with indirect hits", false);
      fireChance = builder.comment("Chance (in percentage) for torches/candles to set targets on fire.").defineInRange("fire chance", 100, 1, 100);
      fireFromMobs = builder.comment("Whether mobs wielding a torch can set their targets on fire.", "Generally useful only when other mods tweak mobs to wield torches.").define("fire from mobs", true);
    }
  }
}
