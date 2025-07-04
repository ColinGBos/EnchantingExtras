package vapourdrive.enchantingextras.integrations.jade;

import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import vapourdrive.enchantingextras.EnchantingExtras;
import vapourdrive.enchantingextras.content.enchant_remover.EnchantRemoverBlock;
import vapourdrive.enchantingextras.content.enchant_remover.EnchantRemoverBlockEntity;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {

    public static final ResourceLocation ENCHANTMENT_REMOVER = ResourceLocation.fromNamespaceAndPath(EnchantingExtras.MODID, "enchantment_remover");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(CrateContentProvider.INSTANCE, EnchantRemoverBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(CrateContentProvider.INSTANCE, EnchantRemoverBlock.class);
    }

}
