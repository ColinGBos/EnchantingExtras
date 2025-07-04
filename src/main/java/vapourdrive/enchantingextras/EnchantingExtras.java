package vapourdrive.enchantingextras;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
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
import vapourdrive.enchantingextras.setup.Registration;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(EnchantingExtras.MODID)
public class EnchantingExtras {
    public static final String MODID = "enchantingextras";
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "enchantingextras" namespace


    // Creates a creative tab with the id "enchantingextras:example_tab" for the example item, that is placed after the combat tab

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public EnchantingExtras(IEventBus modEventBus, ModContainer modContainer) {

        modContainer.registerConfig(ModConfig.Type.SERVER, ConfigSettings.SERVER_CONFIG);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ConfigSettings.CLIENT_CONFIG);

        Registration.init(modEventBus);
        modEventBus.addListener(Registration::buildContents);
        modEventBus.addListener(Registration::registerCapabilities);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (EnchantingExtras) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        @SubscribeEvent
        public static void registerMenuScreens(RegisterMenuScreensEvent event) {
            event.register(Registration.ENCHANT_REMOVER_MENU.get(), EnchantRemoverScreen::new);
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
