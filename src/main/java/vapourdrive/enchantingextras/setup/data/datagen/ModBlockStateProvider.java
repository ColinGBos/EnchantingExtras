package vapourdrive.enchantingextras.setup.data.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import vapourdrive.enchantingextras.EnchantingExtras;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, EnchantingExtras.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
//        simpleCube(Registration.DUSKBLOOM_GLOB_BLOCK.get());

    }

    protected void simpleCube(Block block){
        simpleBlockWithItem(block, cubeAll(block));
    }

}
