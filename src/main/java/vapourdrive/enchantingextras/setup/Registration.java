package vapourdrive.enchantingextras.setup;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import vapourdrive.enchantingextras.EnchantingExtras;
import vapourdrive.enchantingextras.content.VitaeTablet;
import vapourdrive.enchantingextras.content.enchant_remover.EnchantRemoverBlock;
import vapourdrive.enchantingextras.content.enchant_remover.EnchantRemoverBlockEntity;
import vapourdrive.enchantingextras.content.enchant_remover.EnchantRemoverItem;
import vapourdrive.enchantingextras.content.enchant_remover.EnchantRemoverMenu;
import vapourdrive.vapourware.VapourWare;
import vapourdrive.vapourware.shared.base.BaseInfoItem;

import java.util.function.Supplier;

public class Registration {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, VapourWare.MODID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(EnchantingExtras.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(EnchantingExtras.MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, EnchantingExtras.MODID);
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, EnchantingExtras.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> VITAE_DATA = DATA_COMPONENTS.registerComponentType(
            "vitae", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );
    public static final DeferredBlock<Block> ENCHANT_REMOVER_BLOCK = BLOCKS.register("enchant_remover", () -> new EnchantRemoverBlock(BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(5.0F, 1200.0F).requiresCorrectToolForDrops()));
    public static final DeferredItem<BlockItem> ENCHANT_REMOVER_ITEM = ITEMS.register("enchant_remover", () -> new EnchantRemoverItem(ENCHANT_REMOVER_BLOCK.get(), new Item.Properties()));
    public static final Supplier<BlockEntityType<EnchantRemoverBlockEntity>> ENCHANT_REMOVER_BLOCK_ENTITY = BLOCK_ENTITIES.register("enchant_remover", () -> BlockEntityType.Builder.of(EnchantRemoverBlockEntity::new, ENCHANT_REMOVER_BLOCK.get()).build(null));
    public static final Supplier<MenuType<EnchantRemoverMenu>> ENCHANT_REMOVER_MENU = MENUS.register("enchant_remover",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                Level world = inv.player.getCommandSenderWorld();
                return new EnchantRemoverMenu(windowId, world, pos, inv, inv.player, new SimpleContainerData(2));
            }));

    public static final DeferredItem<BaseInfoItem> VITAE_TABLET = ITEMS.register("vitae_tablet", ()-> new VitaeTablet(new Item.Properties().stacksTo(1)));

    public static void init(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        DATA_COMPONENTS.register(eventBus);
        ITEMS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        MENUS.register(eventBus);
    }

    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        // Add to ingredients tab
        if (event.getTab() == vapourdrive.vapourware.setup.Registration.VAPOUR_GROUP.get()) {
            event.accept(ENCHANT_REMOVER_ITEM.get().getDefaultInstance());
            event.accept(VITAE_TABLET.get());
        }
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ENCHANT_REMOVER_BLOCK_ENTITY.get(), EnchantRemoverBlockEntity::getItemHandler);
    }

}
