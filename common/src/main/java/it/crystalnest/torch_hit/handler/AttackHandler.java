package it.crystalnest.torch_hit.handler;

import it.crystalnest.cobweb.api.item.ItemUtils;
import it.crystalnest.torch_hit.Constants;
import it.crystalnest.torch_hit.compat.SoulFired;
import it.crystalnest.torch_hit.config.ModConfig;
import it.crystalnest.torch_hit.platform.Services;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Repairable;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Handles attack events.
 */
public final class AttackHandler {
  private AttackHandler() {}

  /**
   * Handles the attack entity event.
   *
   * @param entity entity causing the damage.
   * @param directEntity entity that actually dealt the damage.
   * @param target targeted entity.
   */
  public static void handle(Entity entity, Entity directEntity, LivingEntity target) {
    if (entity instanceof LivingEntity attacker && entity.equals(directEntity) && !entity.level().isClientSide && !entity.isSpectator() && canAttack(attacker, target)) {
      InteractionHand interactionHand = getInteractionHand(attacker);
      if (interactionHand != null && !target.fireImmune()) {
        ItemStack item = attacker.getItemInHand(interactionHand);
        boolean directHit = interactionHand == InteractionHand.MAIN_HAND;
        if (directHit || isAllowedTool(attacker.getMainHandItem().getItem())) {
          attack(attacker, target, item, directHit);
        }
      }
    }
  }

  /**
   * Attack the target entity with the item setting it on fire.
   *
   * @param attacker attacker.
   * @param target target.
   * @param item item.
   * @param directHit whether the hit is direct ({@code true}) or indirect ({@code false}).
   */
  private static void attack(LivingEntity attacker, Entity target, ItemStack item, boolean directHit) {
    consumeItem(attacker, item, directHit, torchHit(target, item, directHit ? ModConfig.getDirectHitDuration() : ModConfig.getIndirectHitDuration()));
  }

  /**
   * Consumes the used item if enabled.
   *
   * @param attacker attacker.
   * @param item item.
   * @param directHit whether the hit is direct ({@code true}) or indirect ({@code false}).
   * @param seconds fire duration.
   */
  private static void consumeItem(LivingEntity attacker, ItemStack item, boolean directHit, int seconds) {
    if (!(attacker instanceof Player player && player.isCreative())) {
      if (((isCandle(item) && ModConfig.getConsumeCandle()) || (isTorch(item) && ModConfig.getConsumeTorch())) && (directHit || ModConfig.getConsumeWithIndirectHits()) && (ModConfig.getConsumeWithoutFire() || seconds > 0)) {
        item.shrink(1);
      } else if (
        ModConfig.getIndirectHitToolDamage() > 0 &&
        attacker.getMainHandItem() instanceof ItemStack tool &&
        tool.has(DataComponents.TOOL) &&
        tool.has(DataComponents.REPAIRABLE) &&
        tool.has(DataComponents.MAX_DAMAGE) &&
        tool.get(DataComponents.MAX_DAMAGE) instanceof Integer durability &&
        tool.get(DataComponents.REPAIRABLE) instanceof Repairable repairable &&
        repairable.items().unwrapKey() instanceof Optional<TagKey<Item>> tag &&
        tag.isPresent() &&
        tag.get().toString().equals(ItemTags.WOODEN_TOOL_MATERIALS.toString())
      ) {
        tool.hurtAndBreak((durability * ModConfig.getIndirectHitToolDamage() + 99) / 100, attacker, EquipmentSlot.MAINHAND);
      }
    }
  }

  /**
   * Sets the entity on fire.
   *
   * @param target target.
   * @param item item.
   * @param defaultSeconds default fire duration.
   * @return amount of seconds the entity will be set on fire.
   */
  private static int torchHit(Entity target, ItemStack item, int defaultSeconds) {
    int seconds = getFireSeconds(item, target, defaultSeconds);
    if (seconds > 0) {
      if (Services.PLATFORM.isModLoaded("soul_fire_d")) {
        SoulFired.setOnFire(item, target, seconds);
      } else {
        target.igniteForSeconds(seconds);
      }
    }
    return seconds;
  }

  /**
   * Returns the amount of seconds the given entity should stay on fire.
   *
   * @param item item.
   * @param target target.
   * @param seconds default fire duration.
   * @return the amount of seconds the given entity should stay on fire.
   */
  private static int getFireSeconds(ItemStack item, Entity target, int seconds) {
    if (Math.random() * 100 < ModConfig.getFireChance()) {
      if (Services.PLATFORM.isModLoaded("soul_fire_d")) {
        return seconds;
      }
      if (Constants.isSoulTorch(item)) {
        return target instanceof AbstractPiglin ? seconds * 2 : seconds + 2;
      }
      return seconds;
    }
    return 0;
  }

  /**
   * Checks whether the given item is a tool that allows Indirect Hits.
   *
   * @param item item.
   * @return whether the given item is a tool that allows Indirect Hits.
   */
  private static boolean isAllowedTool(Item item) {
    return !ModConfig.getIndirectHitToolList().isEmpty() && ModConfig.getIndirectHitToolList().stream().anyMatch(toolType -> ItemUtils.getKey(item).toString().matches(".*:([^_]+_)*" + toolType + "(_[^_]+)*"));
  }

  /**
   * Returns the {@link InteractionHand} of the {@link LivingEntity} holding a torch.<br />
   * Returns {@code null} if none could be found.
   *
   * @param attacker attacker.
   * @return {@link InteractionHand} holding a torch or {@code null}.
   */
  @Nullable
  private static InteractionHand getInteractionHand(LivingEntity attacker) {
    if (isValidItem(attacker.getMainHandItem())) {
      return InteractionHand.MAIN_HAND;
    }
    if (isValidItem(attacker.getOffhandItem())) {
      return InteractionHand.OFF_HAND;
    }
    return null;
  }

  /**
   * Checks whether the given item is a valid item (torch or candle) to hit with.
   *
   * @param item item.
   * @return whether the given item is a valid item to hit with.
   */
  private static boolean isValidItem(ItemStack item) {
    return isTorch(item) || isCandle(item);
  }

  /**
   * Checks whether the given item is considered a torch.
   *
   * @param item item.
   * @return whether the given item is considered a torch.
   */
  private static boolean isTorch(ItemStack item) {
    return (item.is(Items.TORCH) && ModConfig.getVanillaTorchesEnabled()) || ModConfig.getExtraTorchItems().contains(ItemUtils.getKey(item.getItem()).toString()) || Constants.isSoulTorch(item);
  }

  /**
   * Checks whether candles are allowed and the given item is considered a candle.
   *
   * @param item item.
   * @return whether the given item is considered a candle.
   */
  private static boolean isCandle(ItemStack item) {
    return ModConfig.getAllowCandles() && item.is(ItemTags.CANDLES);
  }

  /**
   * Checks whether the {@code attacker} can actually attack the {@code target}.
   *
   * @param attacker attacker.
   * @param target target.
   * @return whether the {@code attacker} can actually attack the {@code target}.
   */
  private static boolean canAttack(LivingEntity attacker, LivingEntity target) {
    return (attacker instanceof Player || ModConfig.getFireFromMobs()) && attacker.canAttack(target) && (!(attacker instanceof Player attackerPlayer && target instanceof Player targetPlayer) || attackerPlayer.canHarmPlayer(targetPlayer));
  }
}
