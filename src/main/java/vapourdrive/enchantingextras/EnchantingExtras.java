package vapourdrive.enchantingextras;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
import vapourdrive.enchantingextras.config.ConfigSettings;
import vapourdrive.enchantingextras.content.enchant_remover.EnchantRemoverScreen;
import vapourdrive.enchantingextras.content.mob_slayer.MobSlayerScreen;
import vapourdrive.enchantingextras.setup.Registration;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(EnchantingExtras.MODID)
public class EnchantingExtras {
    public static final String MODID = "enchantingextras";
    public static final Logger LOGGER = LogUtils.getLogger();

    public EnchantingExtras(IEventBus modEventBus, ModContainer modContainer) {

        modContainer.registerConfig(ModConfig.Type.SERVER, ConfigSettings.SERVER_CONFIG);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ConfigSettings.CLIENT_CONFIG);

        Registration.init(modEventBus);
        modEventBus.addListener(Registration::buildContents);
        modEventBus.addListener(Registration::registerCapabilities);
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
//        LOGGER.info("HELLO from server starting");
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
//            LOGGER.info("HELLO FROM CLIENT SETUP");
//            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        @SubscribeEvent
        public static void registerMenuScreens(RegisterMenuScreensEvent event) {
            event.register(Registration.ENCHANT_REMOVER_MENU.get(), EnchantRemoverScreen::new);
            event.register(Registration.MOB_SLAYER_MENU.get(), MobSlayerScreen::new);
        }
    }

    public static void debugLog(String toLog) {
        if (isDebugMode()) {
            LOGGER.debug(toLog);
        }
    }

    public static boolean isDebugMode() {
        return java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("jdwp");
    }
}
