package vapourdrive.enchantingextras.integrations.jade;

import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import vapourdrive.enchantingextras.EnchantingExtras;
import vapourdrive.enchantingextras.content.enchant_remover.EnchantRemoverBlock;
import vapourdrive.enchantingextras.content.enchant_remover.EnchantRemoverBlockEntity;
import vapourdrive.enchantingextras.content.mob_slayer.MobSlayerBlock;
import vapourdrive.enchantingextras.content.mob_slayer.MobSlayerBlockEntity;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {

    public static final ResourceLocation ENCHANTMENT_REMOVER = ResourceLocation.fromNamespaceAndPath(EnchantingExtras.MODID, "vitae_holder");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(VitaeContentProvider.INSTANCE, EnchantRemoverBlockEntity.class);
        registration.registerBlockDataProvider(VitaeContentProvider.INSTANCE, MobSlayerBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(VitaeContentProvider.INSTANCE, EnchantRemoverBlock.class);
        registration.registerBlockComponent(VitaeContentProvider.INSTANCE, MobSlayerBlock.class);
    }

}
