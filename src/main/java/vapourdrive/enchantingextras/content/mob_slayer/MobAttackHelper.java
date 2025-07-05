package vapourdrive.enchantingextras.content.mob_slayer;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import vapourdrive.enchantingextras.config.ConfigSettings;

public class MobAttackHelper {
    public static boolean attack(Player player, Entity target, ItemStack weapon, Level level) {
        if(!target.isAttackable()){
            return false;
        }
        DamageSource damagesource = player.damageSources().playerAttack(player);
        ItemAttributeModifiers itemattributemodifiers = weapon.getAttributeModifiers();

        float d1 = (float)itemattributemodifiers.compute(player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE), EquipmentSlot.MAINHAND)+2f;
        float f1 = EnchantmentHelper.modifyDamage((ServerLevel) level, weapon, target, damagesource, d1);
        f1 += weapon.getItem().getAttackDamageBonus(target, f1, damagesource);
        boolean isHurt = target.hurt(damagesource, f1);
        if (isHurt) {
            EnchantmentHelper.doPostAttackEffects((ServerLevel) level, target, damagesource);
        }
        if(ConfigSettings.MOB_SLAYER_DAMAGES_TOOL.get()){
            weapon.hurtAndBreak(1,player,EquipmentSlot.MAINHAND);
        }
        target.setDeltaMovement(0,0,0);
        return !target.isAlive();
    }
}
