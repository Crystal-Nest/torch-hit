package crystalspider.torchhit.config;

import java.util.ArrayList;

import net.minecraft.world.item.enchantment.Enchantments;
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
   * Returns the value of {@link CommonConfig#directHitLevel}.
   *
   * @return {@link CommonConfig#directHitLevel} as read from the {@link #COMMON common} configuration file.
   */
  public static Integer getDirectHitLevel() {
		return COMMON.directHitLevel.get();
	}

  /**
   * Returns the value of {@link CommonConfig#directHitDuration}.
   *
   * @return {@link CommonConfig#directHitDuration} as read from the {@link #COMMON common} configuration file.
   */
  public static Double getDirectHitDuration() {
		return COMMON.directHitDuration.get();
	}

  /**
   * Returns the value of {@link CommonConfig#indirectHitLevel}.
   *
   * @return {@link CommonConfig#indirectHitLevel} as read from the {@link #COMMON common} configuration file.
   */
  public static Integer getIndirectHitLevel() {
		return COMMON.indirectHitLevel.get();
	}

  /**
   * Returns the value of {@link CommonConfig#indirectHitDuration}.
   *
   * @return {@link CommonConfig#indirectHitDuration} as read from the {@link #COMMON common} configuration file.
   */
  public static Double getIndirectHitDuration() {
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
   * Returns the value of {@link CommonConfig#indirectHitEnabled}.
   *
   * @return {@link CommonConfig#indirectHitEnabled} as read from the {@link #COMMON common} configuration file.
   */
  public static Boolean getIndirectHitEnabled() {
		return COMMON.indirectHitEnabled.get();
	}

  /**
   * Common Configuration for Torch hit!.
   */
  public static class CommonConfig {
    /**
     * Fire Aspect Level for Direct Hits.
     */
    private final ConfigValue<Integer> directHitLevel;
    /**
     * Fire Aspect Duration for Direct Hits.
     */
    private final ConfigValue<Double> directHitDuration;
    /**
     * Fire Aspect Level for Indirect Hits.
     */
    private final ConfigValue<Integer> indirectHitLevel;
    /**
     * Fire Aspect Duration for Indirect Hits.
     */
    private final ConfigValue<Double> indirectHitDuration;
    /**
     * List of tools that can be used to deal Indirect Hits.
     * An empty list with {@link #indirectHitEnabled} set to true indicates that any item should allow for an Indirect Hit.
     * TODO: Better define this property.
     */
    private final ConfigValue<ArrayList<String>> indirectHitToolList;
    /**
     * Whether Indirect Hits are enabled.
     */
    private final ConfigValue<Boolean> indirectHitEnabled;

    /**
     * Defines the configuration options, their default values and their comments.
     *
     * @param builder
     */
		public CommonConfig(ForgeConfigSpec.Builder builder) {
      int maxLevel = Enchantments.FIRE_ASPECT.getMaxLevel();
			directHitLevel = builder
        .comment(
          "Fire Aspect level for direct (main hand) hits.",
          "From 1 to " + maxLevel + "."
        )
        .defineInRange("directHitLevel", 1, 1, maxLevel);
			directHitDuration = builder
        .comment(
          "Fire Aspect duration multiplier for direct (main hand) hits.",
          "From 0 to 2, Fire Aspect duration will be multiplied by this."
        )
        .defineInRange("directHitDuration", 0.5, 0.0, 2.0);
			indirectHitLevel = builder
        .comment(
          "Fire Aspect level for indirect (off hand + tool) hits.",
          "From 1 to " + maxLevel + "."
        )
        .defineInRange("indirectHitLevel", 1, 1, maxLevel);
			indirectHitDuration = builder
        .comment(
          "Fire Aspect duration multiplier for indirect (off hand + tool) hits.",
          "From 0 to 2, Fire Aspect duration will be multiplied by this."
        )
        .defineInRange("indirectHitDuration", 1.0, 0.0, 2.0);
			indirectHitToolList = builder
        .comment(
          "List of tools that allow for an indirect hit when a torch is being held in the off hand.",
          "Leave empty to allow indirect hits with any item (not only tools)."
        )
        .define("indirectHitEnabled", new ArrayList<>());
			indirectHitEnabled = builder
        .comment("Whether to enable indirect (off hand + tool) hits.")
        .define("indirectHitEnabled", true);
      // TODO: Add soul & regular configuration options.
		}
	}
}
