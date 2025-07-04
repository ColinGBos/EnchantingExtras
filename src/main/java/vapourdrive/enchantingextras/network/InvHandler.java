package vapourdrive.enchantingextras.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import vapourdrive.enchantingextras.content.enchant_remover.EnchantRemoverBlockEntity;

public class InvHandler {
    static void startProcess(final Network.StartProcessNotification data, IPayloadContext ctx) {
        final Player sender = ctx.player();
        if (sender instanceof ServerPlayer serverPlayer) {
            ctx.enqueueWork(() -> {
                BlockEntity tileEntity = serverPlayer.level().getBlockEntity(data.blockPos());
                if (tileEntity instanceof EnchantRemoverBlockEntity be) {
                    if(be.getVITAE().getTimer() <= 0) {
                        be.startEnchantRemovalProcess();
                    }
                }
            });
        }
    }
}