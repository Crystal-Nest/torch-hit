package crystalspider.torchhit.config;

import java.util.ArrayList;
import java.util.List;

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
		}
	}
}
