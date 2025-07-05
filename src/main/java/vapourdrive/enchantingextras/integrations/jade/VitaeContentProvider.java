package vapourdrive.enchantingextras.integrations.jade;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import vapourdrive.enchantingextras.EnchantingExtras;
import vapourdrive.enchantingextras.content.enchant_remover.EnchantRemoverBlockEntity;
import vapourdrive.enchantingextras.content.mob_slayer.MobSlayerBlockEntity;
import vapourdrive.vapourware.shared.utils.CompUtils;

import java.text.DecimalFormat;

public enum VitaeContentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;
    private final DecimalFormat df = new DecimalFormat("#,###");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig pluginConfig) {
        if (blockAccessor.getServerData().contains("vitae")) {
            int i = blockAccessor.getServerData().getInt("vitae");
            MutableComponent comp = CompUtils.getArgComp(EnchantingExtras.MODID,"vitae_amount", df.format(i)).withStyle(ChatFormatting.LIGHT_PURPLE);
            tooltip.add(comp);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadePlugin.ENCHANTMENT_REMOVER;
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof EnchantRemoverBlockEntity be) {
            tag.putInt("vitae", be.getVITAE().getCurrent());
        } else if (blockAccessor.getBlockEntity() instanceof MobSlayerBlockEntity be) {
            tag.putInt("vitae", be.getVITAE().getCurrent());
        }
    }
}
