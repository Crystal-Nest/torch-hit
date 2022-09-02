package crystalspider.torchhit.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.enchantment.Enchantments;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue; 

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
   * Common Configuration for Torch hit!.
   */
  public static class CommonConfig {
    /**
     * Fire Aspect Duration for Direct Hits.
     */
    private final ConfigValue<Integer> directHitDuration;
    /**
     * Fire Aspect Duration for Indirect Hits.
     */
    private final ConfigValue<Integer> indirectHitDuration;
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
     * Defines the configuration options, their default values and their comments.
     *
     * @param builder
     */
		public CommonConfig(ForgeConfigSpec.Builder builder) {
      int maxDuration = Enchantments.FIRE_ASPECT.getMaxLevel() * 4;
			directHitDuration = builder.comment("Fire damage duration for direct (main hand) hits.").defineInRange("directHitDuration", 4, 1, maxDuration);
			indirectHitDuration = builder.comment("Fire damage duration for indirect (off hand + tool) hits.").defineInRange("indirectHitDuration", 2, 1, maxDuration);
			indirectHitToolList = builder
        .comment(
          "List of tools that allow for an indirect hit when a torch is being held in the off hand.",
          "Leave empty to disable indirect hits.",
          "Insert either item categories or specific item IDs."
        )
        .define("indirectHitToolList", new ArrayList<String>(List.of("sword", "axe", "pickaxe", "shovel", "hoe")));
      moddedTorchList = builder.comment("List of item ids that should be considered as a Torch.").define("moddedTorchList", new ArrayList<String>(List.of(
        "bonetorch:bonetorch",
        "torchmaster:megatorch",
        "hardcore_torches:lit_torch",
        "magnumtorch:diamond_magnum_torch",
        "magnumtorch:emerald_magnum_torch",
        "magnumtorch:amethyst_magnum_torch",
        "magical_torches:mega_torch",
        "magical_torches:grand_torch",
        "magical_torches:medium_torch",
        "magical_torches:small_torch"
      )));
      moddedSoulTorchList = builder.comment("List of item ids that should be considered as a Soul Torch.").define("moddedSoulTorchList", new ArrayList<String>());
      allowCandles = builder.comment("Whether to allow candles to act as torches.").define("allowCandles", true);
		}
	}
}
