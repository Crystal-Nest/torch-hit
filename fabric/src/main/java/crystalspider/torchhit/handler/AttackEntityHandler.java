package crystalspider.torchhit.handler;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import crystalspider.torchhit.config.ModConfig;
import crystalspider.torchhit.optional.SoulFired;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;

/**
 * {@link AttackEntityCallback} event handler.
 */
public final class AttackEntityHandler {
  /**
   * Whether Soul Fire'd mod is installed at runtime.
   */
  private static final Supplier<Boolean> isSoulfiredInstalled = () -> FabricLoader.getInstance().isModLoaded("soulfired");

  /**
   * Handles the {@link ServerLivingEntityEvents#ALLOW_DAMAGE} event.
   * 
   * @param target
   * @param source
   * @param amount
   * @return
   */
  public static boolean handle(LivingEntity target, DamageSource source, float amount) {
    Entity attackerEntity = source.getAttacker();
    Entity directEntity = source.getSource();
    if (attackerEntity instanceof LivingEntity attacker && attackerEntity == directEntity && !attackerEntity.getWorld().isClient && !attackerEntity.isSpectator()) {
      if (canAttack(attacker, target)) {
        Hand interactionHand = getHand(attacker);
        if (interactionHand != null && !target.isFireImmune()) {
          ItemStack item = attacker.getStackInHand(interactionHand);
          boolean directHit = interactionHand == Hand.MAIN_HAND;
          if (directHit || isAllowedTool(attacker.getMainHandStack().getItem())) {
            attack(attacker, target, item, directHit);
          }
        }
      }
    }
    return true;
  }

  /**
   * Attack the target entity with the torch item it on fire.
   * 
   * @param attacker
   * @param target
   * @param item
   * @param directHit whether the hit is direct ({@code true}) or indirect ({@code false}).
   */
  private static void attack(LivingEntity attacker, Entity target, ItemStack item, boolean directHit) {
    consumeItem(attacker, item, directHit, burn(target, item, directHit ? ModConfig.getDirectHitDuration() : ModConfig.getIndirectHitDuration()));
  }

  /**
   * Consumes the used item if enabled.
   * 
   * @param attacker
   * @param item
   * @param directHit whether the hit is direct ({@code true}) or indirect ({@code false}).
   * @param fireSeconds
   */
  private static void consumeItem(LivingEntity attacker, ItemStack item, boolean directHit, int fireSeconds) {
    if (
      !(attacker instanceof PlayerEntity && ((PlayerEntity) attacker).isCreative()) &&
      ((isCandle(item) && ModConfig.getConsumeCandle()) || (isTorch(item) && ModConfig.getConsumeTorch())) &&
      (directHit || ModConfig.getConsumeWithIndirectHits()) &&
      (ModConfig.getConsumeWithoutFire() || fireSeconds > 0)
    ) {
      item.decrement(1);
    }
  }

  /**
   * Sets the entity on fire.
   * 
   * @param target
   * @param item
   * @param defaultDuration
   */
  private static int burn(Entity target, ItemStack item, int defaultDuration) {
    int fireSeconds = getFireSeconds(item, target, defaultDuration);
    if (fireSeconds > 0) {
      if (isSoulfiredInstalled.get()) {
        SoulFired.setOnFire(target, fireSeconds, isSoulTorch(item));
      } else {
        target.setOnFireFor(fireSeconds);
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
    if ((Math.random() * 100) < ModConfig.getFireChance()) {
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
   * @return
   */
  private static boolean isAllowedTool(Item item) {
    return !ModConfig.getIndirectHitToolList().isEmpty() && ModConfig.getIndirectHitToolList().stream().anyMatch(toolType -> getKey(item).matches(".*:([^_]+_)*" + toolType + "(_[^_]+)*"));
  }

  /**
   * Returns the {@link Hand} of the {@link LivingEntity} holding a torch.
   * Null if none could be found.
   * 
   * @param attacker
   * @return {@link Hand} holding a torch or null.
   */
  @Nullable
  private static Hand getHand(LivingEntity attacker) {
    if (isValidItem(attacker.getMainHandStack())) {
      return Hand.MAIN_HAND;
    }
    if (isValidItem(attacker.getOffHandStack())) {
      return Hand.OFF_HAND;
    }
    return null;
  }

  /**
   * Checks whether the given {@link ItemStack} is a valid item (torch or candle).
   * 
   * @param item
   * @return whether the given {@link ItemStack} is a valid item.
   */
  private static boolean isValidItem(ItemStack item) {
    return isTorch(item) || isCandle(item);
  }

  /**
   * Checks whether the given {@link ItemStack} is a torch.
   * 
   * @param item
   * @return whether the given {@link ItemStack} is a torch.
   */
  private static boolean isTorch(ItemStack item) {
    return (item.isOf(Items.TORCH) && ModConfig.getVanillaTorchesEnabled()) || ModConfig.getExtraTorchItems().contains(getKey(item.getItem())) || isSoulTorch(item);
  }

  /**
   * Checks whether the given {@link ItemStack} is a soul torch.
   * 
   * @param item
   * @return whether the given {@link ItemStack} is a soul torch.
   */
  private static boolean isSoulTorch(ItemStack item) {
    return (item.isOf(Items.SOUL_TORCH) && ModConfig.getVanillaTorchesEnabled()) || ModConfig.getExtraSoulTorchItems().contains(getKey(item.getItem()));
  }

  /**
   * Checks whether the given {@link ItemStack} is a candle.
   * 
   * @param item
   * @return whether the given {@link ItemStack} is a candle.
   */
  private static boolean isCandle(ItemStack item) {
    return ModConfig.getAllowCandles() && item.isIn(ItemTags.CANDLES);
  }

  /**
   * Checks whether the {@code attacker} can actually attack the {@code target}.
   * 
   * @param attacker
   * @param target
   * @return whether the {@code attacker} can actually attack the {@code target}.
   */
  private static boolean canAttack(LivingEntity attacker, LivingEntity target) {
    return (attacker instanceof PlayerEntity || ModConfig.getFireFromMobs()) && attacker.canTarget(target) && (!(attacker instanceof PlayerEntity && target instanceof PlayerEntity) || ((PlayerEntity) attacker).shouldDamagePlayer((PlayerEntity) target));
  }

  /**
   * Returns the in-game ID of the item passed as parameter.
   * 
   * @param item
   * @return in-game ID of the given item.
   */
  private static String getKey(Item item) {
    return Registries.ITEM.getKey(item).get().getValue().toString();
  }
}
