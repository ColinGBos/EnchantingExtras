package vapourdrive.enchantingextras.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import vapourdrive.enchantingextras.EnchantingExtras;
import vapourdrive.enchantingextras.config.ConfigSettings;
import vapourdrive.enchantingextras.content.VitaeTablet;
import vapourdrive.enchantingextras.setup.Registration;

@EventBusSubscriber(modid = EnchantingExtras.MODID)

public class ExperienceDropEvent {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onLivingXPDropEvent(LivingExperienceDropEvent event){
        int xp = event.getDroppedExperience() * ConfigSettings.VITAE_PER_ENTITY_XP.get();
        if (event.getAttackingPlayer() == null || xp <= 0){
            return;
        }
        Player player = event.getAttackingPlayer();
        tryAddVitaeToPlayer(xp, player);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onBlockXPDropEvent(BlockDropsEvent event){
        int xp = event.getDroppedExperience() * ConfigSettings.VITAE_PER_BLOCK_XP.get();
        if (xp <= 0 || event.getBreaker() == null){
            return;
        }
        if(event.getBreaker() instanceof Player player) {
            tryAddVitaeToPlayer(xp, player);
        }
    }

    private static void tryAddVitaeToPlayer(int xp, Player player){
        int xpToConsume = xp;
        for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack testStack = player.getInventory().getItem(i);
            if (testStack.is(Registration.VITAE_TABLET)) {
                VitaeTablet tablet = (VitaeTablet) testStack.getItem();
                xpToConsume = tablet.addVitae(testStack, xpToConsume, false);
                if (xpToConsume == 0) {
                    return;
                }
            }
        }
    }
}
