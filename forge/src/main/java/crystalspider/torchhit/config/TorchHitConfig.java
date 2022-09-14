package crystalspider.torchhit.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue; 

/**
 * Torch hit! Configuration.
 */
public class TorchHitConfig {
  /**
   * {@link ForgeConfigSpec} {@link ForgeConfigSpec.Builder Builder}.
   */
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
  /**
   * Common Configuration as read from the configuration file.
   */
	public static final CommonConfig COMMON = new CommonConfig(BUILDER);
  /**
   * {@link ForgeConfigSpec}.
   */
	public static final ForgeConfigSpec SPEC = BUILDER.build();

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
  public static ArrayList<String> getIndirectHitToolList() {
		return COMMON.indirectHitToolList.get();
	}

  /**
   * Returns the value of {@link CommonConfig#moddedTorchList}.
   *
   * @return {@link CommonConfig#moddedTorchList} as read from the {@link #COMMON common} configuration file.
   */
  public static ArrayList<String> getModdedTorchList() {
		return COMMON.moddedTorchList.get();
	}

  /**
   * Returns the value of {@link CommonConfig#moddedSoulTorchList}.
   *
   * @return {@link CommonConfig#moddedSoulTorchList} as read from the {@link #COMMON common} configuration file.
   */
  public static ArrayList<String> getModdedSoulTorchList() {
		return COMMON.moddedSoulTorchList.get();
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
   * Returns the value of {@link CommonConfig#fireChance}.
   *
   * @return {@link CommonConfig#fireChance} as read from the {@link #COMMON common} configuration file.
   */
  public static Integer getFireChance() {
    return COMMON.fireChance.get();
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
    private final ConfigValue<ArrayList<String>> indirectHitToolList;
    /**
     * List of item ids that should be considered as a Torch.
     * Defaults to a list of the most common modded torches.
     */
    private final ConfigValue<ArrayList<String>> moddedTorchList;
    /**
     * List of item ids that should be considered as a Soul Torch.
     * Defaults to a list of the most common modded torches.
     */
    private final ConfigValue<ArrayList<String>> moddedSoulTorchList;
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
     * Chance (in percentage) for torches/candles to set targets on fire.
     */
    private final IntValue fireChance;

    /**
     * Defines the configuration options, their default values and their comments.
     *
     * @param builder
     */
		public CommonConfig(ForgeConfigSpec.Builder builder) {
      int maxDuration = Enchantments.FIRE_ASPECT.getMaxLevel() * 4;
			directHitDuration = builder.comment("Fire damage duration for direct (main hand) hits.").defineInRange("direct hit duration", 4, 1, maxDuration);
			indirectHitDuration = builder.comment("Fire damage duration for indirect (off hand + tool) hits.").defineInRange("indirect hit duration", 2, 1, maxDuration);
			indirectHitToolList = builder
        .comment(
          "List of tools that allow for an indirect hit when a torch is being held in the off hand.",
          "Leave empty to disable indirect hits.",
          "Insert either item categories or specific item IDs."
        )
        .define("indirect tools", new ArrayList<String>(List.of("sword", "axe", "pickaxe", "shovel", "hoe")));
      moddedTorchList = builder.comment("List of item ids that should be considered as a Torch.").define("torch list", new ArrayList<String>(List.of(
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
      )));
      moddedSoulTorchList = builder.comment("List of item ids that should be considered as a Soul Torch.").define("soul torch list", new ArrayList<String>(List.of(
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
      )));
      allowCandles = builder.comment("Whether to allow candles to act as torches.").define("allow candles", true);
      consumeCandle = builder.comment("Whether candles should break upon use.", "Effective only if [allowCandles] is enabled.").define("consume candle", true);
      consumeTorch = builder.comment("Whether torches should break upon use.").define("consume torch", false);
      consumeWithoutFire = builder
        .comment(
          "Whether to break the torch/candle upon use even if no fire was set.",
          "Effective only if [fire chance] and at least one of [consume torch] and [consume candle] are set different from default."
        )
        .define("consume without fire", false);
      fireChance = builder.comment("Chance (in percentage) for torches/candles to set targets on fire.").defineInRange("fire chance", 100, 1, 100);
		}
	}
}
