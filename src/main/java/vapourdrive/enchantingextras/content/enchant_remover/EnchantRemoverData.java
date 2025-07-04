package vapourdrive.enchantingextras.content.enchant_remover;

import net.minecraft.world.inventory.ContainerData;

public class EnchantRemoverData implements ContainerData {
    private final int[] data = {0};

    @Override
    public int get(int index) {
        if (index >= data.length || index < 0) {
            return 0;
        } else {
            return data[index];
        }
    }

    @Override
    public void set(int index, int amount) {
        if (index < data.length && index >= 0) {
            data[index] = amount;
        }
    }

    @Override
    public int getCount() {
        return data.length;
    }
}
