package crystalspider.torchhit.config;

import java.util.ArrayList;
import java.util.List;

import crystalspider.config.AbstractConfig;
import crystalspider.config.ConfigProperty;
import crystalspider.config.FabricConfig;
import crystalspider.torchhit.TorchHitLoader;
import net.minecraft.enchantment.Enchantments;

/**
 * Torch hit! Configuration.
 */
public class TorchHitConfig {
  /**
   * Configuration.
   */
	private static final Config CONFIG = new Config();
  /**
   * {@link FabricConfig} {@link FabricConfig.Builder Builder}.
   */
  public static final FabricConfig.Builder BUILDER = new FabricConfig.Builder(TorchHitLoader.MODID, CONFIG);

  /**
   * Returns the value of {@link Config#directHitDuration}.
   *
   * @return {@link Config#directHitDuration} as read from the {@link #CONFIG configuration} file.
   */
  public static Integer getDirectHitDuration() {
		return CONFIG.directHitDuration.getValue();
	}

  /**
   * Returns the value of {@link Config#indirectHitDuration}.
   *
   * @return {@link Config#indirectHitDuration} as read from the {@link #CONFIG configuration} file.
   */
  public static Integer getIndirectHitDuration() {
		return CONFIG.indirectHitDuration.getValue();
	}

  /**
   * Returns the value of {@link Config#indirectHitToolList}.
   *
   * @return {@link Config#indirectHitToolList} as read from the {@link #CONFIG configuration} file.
   */
  public static ArrayList<String> getIndirectHitToolList() {
		return CONFIG.indirectHitToolList.getValue();
	}

  /**
   * Configuration for Torch hit!.
   */
	private static class Config implements AbstractConfig {
    /**
     * Fire Aspect Duration for Direct Hits.
     */
    private ConfigProperty<Integer> directHitDuration;
    /**
     * Fire Aspect Duration for Indirect Hits.
     */
    private ConfigProperty<Integer> indirectHitDuration;
    /**
     * List of tools that can be used to deal Indirect Hits.
     * Empty if Indirect Hits are disabled.
     */
    private ConfigProperty<ArrayList<String>> indirectHitToolList;

    @Override
    public void register(FabricConfig.Builder builder) {
      int maxDuration = Enchantments.FIRE_ASPECT.getMaxLevel() * 4;
      directHitDuration = builder.registerProperty("directHitDuration", 4, 1, maxDuration, "Fire damage duration for direct (main hand) hits.");
      indirectHitDuration = builder.registerProperty("indirectHitDuration", 2, 1, maxDuration, "Fire damage duration for indirect (off hand + tool) hits.");
      indirectHitToolList = builder.registerProperty(
        "indirectHitToolList",
        new ArrayList<String>(List.of("sword", "axe", "pickaxe", "shovel", "hoe")),
        "List of tools that allow for an indirect hit when a torch is being held in the off hand.",
        "Leave empty to disable indirect hits.",
        "Insert either item categories or specific item IDs."
      );
    }
	}
}
