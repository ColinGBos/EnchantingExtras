package vapourdrive.enchantingextras.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import vapourdrive.enchantingextras.EnchantingExtras;


public class ConfigSettings {
    public static final ModConfigSpec SERVER_CONFIG;
    public static final ModConfigSpec CLIENT_CONFIG;
    public static final String ENCHANT_REMOVER = "enchant_remover";
    public static ModConfigSpec.IntValue ENCHANT_REMOVER_VITAE_STORAGE;
    public static ModConfigSpec.IntValue ENCHANT_REMOVER_VITAE_TO_WORK;
    public static ModConfigSpec.IntValue ENCHANT_REMOVER_PROCESS_TIME;

    public static final String VITAE_TABLET = "vitae_tablet";
    public static ModConfigSpec.IntValue VITAE_TABLET_VITAE_STORAGE;

    public static final String VITAE_GENERATION = "vitae_generation";
    public static ModConfigSpec.IntValue VITAE_PER_ENTITY_XP;
    public static ModConfigSpec.IntValue VITAE_PER_BLOCK_XP;

    static {
        EnchantingExtras.LOGGER.info("Initiating Config for Enchanting Extras");
        ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();
        ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();
        setupFirstBlockConfig(SERVER_BUILDER, CLIENT_BUILDER);

        SERVER_CONFIG = SERVER_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupFirstBlockConfig(ModConfigSpec.Builder SERVER_BUILDER, ModConfigSpec.Builder CLIENT_BUILDER) {
        SERVER_BUILDER.comment("Enchant Remover Settings").push(ENCHANT_REMOVER);
        ENCHANT_REMOVER_VITAE_STORAGE = SERVER_BUILDER.comment("Vitae Storage for the Enchant Remover").defineInRange("enchantRemoverVitaeStorage", 25000, 250, 2500000);
        ENCHANT_REMOVER_VITAE_TO_WORK = SERVER_BUILDER.comment("Vitae for one base unit of work").defineInRange("enchantRemoverVitaeBaseConsumption", 200, 10, 10000);
        ENCHANT_REMOVER_PROCESS_TIME = SERVER_BUILDER.comment("Ticks to remove an enchant").defineInRange("enchantRemoverProcessDuration", 200, 5, 1000);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.comment("Vitae Tablet Settings").push(VITAE_TABLET);
        VITAE_TABLET_VITAE_STORAGE = SERVER_BUILDER.comment("Vitae Storage for the Vitae Tablet").defineInRange("vitaeTabletVitaeStorage", 25000, 250, 2500000);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.comment("Vitae Generation Settings").push(VITAE_GENERATION);
        VITAE_PER_ENTITY_XP = SERVER_BUILDER.comment("Vitae generation from xp dropped from entities (0 to disable)").defineInRange("vitaeGenerationRateFromEntities", 25, 0, 2500);
        VITAE_PER_BLOCK_XP = SERVER_BUILDER.comment("Vitae generation from xp dropped from blocks (0 to disable)").defineInRange("vitaeGenerationRateFromBlocks", 10, 0, 2500);
        SERVER_BUILDER.pop();
    }
}
