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
    public static ModConfigSpec.IntValue VITAE_TABLET_TRANSFER_RATE;

    public static final String VITAE_GENERATION = "vitae_generation";
    public static ModConfigSpec.IntValue VITAE_PER_ENTITY_XP;
    public static ModConfigSpec.IntValue VITAE_PER_BLOCK_XP;

    public static final String MOB_SLAYER = "mob_slayer";
    public static ModConfigSpec.IntValue MOB_SLAYER_VITAE_STORAGE;
    public static ModConfigSpec.IntValue MOB_SLAYER_FUEL_STORAGE;
    public static ModConfigSpec.IntValue MOB_SLAYER_FUEL_TO_WORK;
    public static ModConfigSpec.IntValue MOB_SLAYER_PROCESS_TIME;
    public static ModConfigSpec.BooleanValue MOB_SLAYER_DAMAGES_TOOL;
    public static ModConfigSpec.BooleanValue MOB_SLAYER_ATTACKS_PLAYERS;

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
        VITAE_TABLET_TRANSFER_RATE = SERVER_BUILDER.comment("Rate at which the Vitae Tablet transfers to and from blocks that hold Vitae").defineInRange("vitaeTabletTransferRate", 100, 10, 10000);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.comment("Vitae Generation Settings").push(VITAE_GENERATION);
        VITAE_PER_ENTITY_XP = SERVER_BUILDER.comment("Vitae generation from xp dropped from entities (0 to disable)").defineInRange("vitaeGenerationRateFromEntities", 25, 0, 2500);
        VITAE_PER_BLOCK_XP = SERVER_BUILDER.comment("Vitae generation from xp dropped from blocks (0 to disable)").defineInRange("vitaeGenerationRateFromBlocks", 10, 0, 2500);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.comment("Mob Slayer Settings").push(MOB_SLAYER);
        MOB_SLAYER_VITAE_STORAGE = SERVER_BUILDER.comment("Vitae Storage for the Mob Slayer").defineInRange("mobSlayerVitaeStorage", 25000, 250, 2500000);
        MOB_SLAYER_FUEL_STORAGE = SERVER_BUILDER.comment("Fuel Storage for the Mob Slayer").defineInRange("mobSlayerFuelStorage", 100000, 250, 2500000);
        MOB_SLAYER_FUEL_TO_WORK = SERVER_BUILDER.comment("Fuel for one base unit of work").defineInRange("mobSlayerFuelBaseConsumption", 1000, 10, 10000);
        MOB_SLAYER_PROCESS_TIME = SERVER_BUILDER.comment("Tick interval for attacks (Attacks one mob once)").defineInRange("mobSlayerProcessInterval", 5, 1, 1000);
        MOB_SLAYER_DAMAGES_TOOL = SERVER_BUILDER.comment("Does the mob slayer damage the tool on use?").define("mobSlayerDamagesTool", false);
        MOB_SLAYER_ATTACKS_PLAYERS = SERVER_BUILDER.comment("Does the mob slayer attack players?").define("mobSlayerAttacksPlayers", true);
        SERVER_BUILDER.pop();
    }
}
