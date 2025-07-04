package vapourdrive.enchantingextras.integrations.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.world.item.ItemStack;
import vapourdrive.enchantingextras.config.ConfigSettings;
import vapourdrive.enchantingextras.setup.Registration;

@EmiEntrypoint
public class EMI_Plugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        ItemStack stack = new ItemStack(Registration.VITAE_TABLET.get());
        stack.set(Registration.VITAE_DATA, ConfigSettings.VITAE_TABLET_VITAE_STORAGE.get());
        registry.addEmiStack(EmiStack.of(stack));
    }
}
