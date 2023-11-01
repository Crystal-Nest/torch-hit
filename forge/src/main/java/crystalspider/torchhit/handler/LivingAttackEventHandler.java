package crystalspider.torchhit.handler;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import crystalspider.torchhit.TorchHitLoader;
import crystalspider.torchhit.config.TorchHitConfig;
import crystalspider.torchhit.optional.SoulFired;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Handler for the {@link AttackEntityEvent}.
 */
@EventBusSubscriber(modid = TorchHitLoader.MODID, bus = Bus.FORGE)
public final class LivingAttackEventHandler {
  /**
   * Whether Soul Fire'd mod is installed at runtime.
   */
  private static final Supplier<Boolean> isSoulfiredInstalled = () -> ModList.get().isLoaded("soulfired");

  /**
   * Handles the {@link AttackEntityEvent}.
   * 
   * @param event
   */
  @SubscribeEvent()
  public static void handle(LivingAttackEvent event) {
    Entity entity = event.getSource().getEntity();
    Entity directEntity = event.getSource().getDirectEntity();
    if (entity instanceof LivingEntity && entity == directEntity && !entity.level.isClientSide && !entity.isSpectator()) {
      LivingEntity attacker = (LivingEntity) entity, target = event.getEntityLiving();
      if (canAttack(attacker, target)) {
        Hand interactionHand = getInteractionHand(attacker);
        if (interactionHand != null && !target.fireImmune()) {
          ItemStack item = attacker.getItemInHand(interactionHand);
          boolean directHit = interactionHand == Hand.MAIN_HAND;
          if (directHit || isAllowedTool(attacker.getMainHandItem().getItem())) {
            attack(attacker, target, item, directHit);
          }
        }
      }
    }
  }

  /**
   * Attack the entity with the torch setting it on fire.
   * 
   * @param attacker
   * @param target
   * @param item
   * @param directHit whether the hit is direct ({@code true}) or indirect ({@code false}).
   */
  private static void attack(LivingEntity attacker, Entity target, ItemStack item, boolean directHit) {
    consumeItem(attacker, item, directHit, burn(target, item, directHit ? TorchHitConfig.getDirectHitDuration() : TorchHitConfig.getIndirectHitDuration()));
  }

  /**
   * Consumes the used torch if enabled.
   * 
   * @param attacker
   * @param item
   * @param directHit whether the hit is direct ({@code true}) or indirect ({@code false}).
   * @param fireSeconds
   */
  private static void consumeItem(LivingEntity attacker, ItemStack item, boolean directHit, int fireSeconds) {
    if (
      !(attacker instanceof PlayerEntity && ((PlayerEntity) attacker).isCreative()) &&
      isTorch(item) &&
      TorchHitConfig.getConsumeTorch() &&
      (directHit || TorchHitConfig.getConsumeWithIndirectHits()) &&
      (TorchHitConfig.getConsumeWithoutFire() || fireSeconds > 0)
    ) {
      item.shrink(1);
    }
  }

  /**
   * Sets the entity on fire.
   * 
   * @param target
   * @param item
   * @param defaultDuration
   * @return amound of seconds the entity will be set on fire.
   */
  private static int burn(Entity target, ItemStack item, int defaultDuration) {
    int fireSeconds = getFireSeconds(item, target, defaultDuration);
    if (fireSeconds > 0) {
      if (isSoulfiredInstalled.get()) {
        SoulFired.setOnFire(target, fireSeconds, isSoulTorch(item));
      } else {
        target.setSecondsOnFire(fireSeconds);
      }
    }
    return fireSeconds;
  }

  /**
   * Returns the amount of seconds the given entity should stay on fire.
   * 
   * @param item
   * @param target
   * @param fireDuration
   * @return the amount of seconds the given entity should stay on fire.
   */
  private static int getFireSeconds(ItemStack item, Entity target, int fireDuration) {
    if ((Math.random() * 100) < TorchHitConfig.getFireChance()) {
      if (isSoulTorch(item)) {
        if (isSoulfiredInstalled.get()) {
          return fireDuration;
        }
        if (target instanceof AbstractPiglinEntity) {
          return fireDuration * 2;
        }
        return fireDuration + 1;
      }
      return fireDuration;
    }
    return 0;
  }

  /**
   * Checks whether the given {@link Item} is a tool that allows Indirect Hits.
   * 
   * @param item
   * @return whether the given {@link Item} is a tool that allows Indirect Hits.
   */
  private static boolean isAllowedTool(Item item) {
    return !TorchHitConfig.getIndirectHitToolList().isEmpty() && TorchHitConfig.getIndirectHitToolList().stream().filter(toolType -> getKey(item).matches(".*:([^_]+_)*" + toolType + "(_[^_]+)*")).count() > 0;
  }

  /**
   * Returns the {@link Hand} of the {@link LivingEntity} holding a torch.
   * Null if none could be found.
   * 
   * @param attacker
   * @return {@link Hand} holding a torch or null.
   */
  @Nullable
  private static Hand getInteractionHand(LivingEntity attacker) {
    if (isTorch(attacker.getMainHandItem())) {
      return Hand.MAIN_HAND;
    }
    if (isTorch(attacker.getOffhandItem())) {
      return Hand.OFF_HAND;
    }
    return null;
  }

  /**
   * Checks whether the given {@link ItemStack} is a torch.
   * 
   * @param item
   * @return whether the given {@link ItemStack} is a torch.
   */
  private static boolean isTorch(ItemStack item) {
    return (item.getItem() == Items.TORCH && TorchHitConfig.getVanillaTorchesEnabled()) || TorchHitConfig.getExtraTorchItems().contains(getKey(item.getItem())) || isSoulTorch(item);
  }

  /**
   * Checks whether the given {@link ItemStack} is a soul torch.
   * 
   * @param item
   * @return whether the given {@link ItemStack} is a soul torch.
   */
  private static boolean isSoulTorch(ItemStack item) {
    return (item.getItem() == Items.SOUL_TORCH && TorchHitConfig.getVanillaTorchesEnabled()) || TorchHitConfig.getExtraSoulTorchItems().contains(getKey(item.getItem()));
  }

  /**
   * Checks whether the {@code attacker} can actually attack the {@code target}.
   * 
   * @param attacker
   * @param target
   * @return whether the {@code attacker} can actually attack the {@code target}.
   */
  private static boolean canAttack(LivingEntity attacker, LivingEntity target) {
    return (attacker instanceof PlayerEntity || TorchHitConfig.getFireFromMobs()) && attacker.canAttack(target) && (!(attacker instanceof PlayerEntity && target instanceof PlayerEntity) || ((PlayerEntity) attacker).canHarmPlayer((PlayerEntity) target));
  }

  /**
   * Returns the in-game ID of the item passed as parameter.
   * 
   * @param item
   * @return in-game ID of the given item.
   */
  private static String getKey(Item item) {
    ResourceLocation itemLocation = ForgeRegistries.ITEMS.getKey(item);
    if (itemLocation != null) {
      return itemLocation.toString();
    }
    return "";
  }
}
