package vapourdrive.enchantingextras.content;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import org.jetbrains.annotations.NotNull;
import vapourdrive.enchantingextras.EnchantingExtras;

public class BlockEntityContentHolder {
    final ContainerData data;
    final String id;
    final int index;
    final int max;
    final int processDuration;
    int toAdd = 0;
    int incrementalToAdd = 0;

    public BlockEntityContentHolder(String id, int index, int max, int processDuration, ContainerData data){
        this.index = index;
        this.max = max;
        this.data = data;
        this.processDuration = processDuration;
        this.id = id;
    }

    public ContainerData getData(){
        return this.data;
    }

    public int getFromContainerData(int index){
        if (getData().getCount()>index){
            return getData().get(index);
        }
        EnchantingExtras.debugLog("tried to get: "+ index+" from "+getId() +" from outside of bounds ("+getData().getCount()+")");
        return -1;
    }

    public void setToContainerData(int index, int amount){
        if (getData().getCount()>index){
            getData().set(index, amount);
            return;
        }
        EnchantingExtras.debugLog("tried to set: " + amount + " to: "+ index+" from outside of bounds ("+getData().getCount()+")");
    }

    public int getDataIndex(){
        return this.index;
    }

    public int getProcessDuration() {
        return processDuration;
    }

    public String getId() {
        return id;
    }

    public int getTimer() {
        return getFromContainerData(getDataIndex()+1);
//        return getData().get(getDataIndex()+1);
    }

    public void setTimer(int amount) {
        setToContainerData(getDataIndex()+1, Math.max(0, amount));
//        getData().set(getDataIndex()+1, Math.max(0, amount));
    }

    public void tickTimer() {
        setTimer(Math.max(0, this.getTimer()-1));
    }

    private void setToAdd(int amount){
        this.toAdd = amount;
    }

    public int getToAdd(){
        return this.toAdd;
    }

    public void setIncrementalToAdd(int amount){
        this.incrementalToAdd = amount;
    }

    public int getIncrementalToAdd(){
        return this.incrementalToAdd;
    }

    public int getMax(){
        return this.max;
    }

    public int getCurrent(){
        return getFromContainerData(getDataIndex());
//        return getData().get(getDataIndex());
    }

    public int getSpace(){
        return getMax()-getCurrent();
    }

    public void setCurrent(int amount){
        setToContainerData(getDataIndex(), Math.min(this.getMax(), amount));
//        getData().set(getDataIndex(), Math.min(this.getMax(), amount));
    }

    public float getPercentFull(){
        return (float) getCurrent()/(float) this.getMax();
    }

    public float getProgressPercentage() {
        EnchantingExtras.debugLog("Timer: "+getTimer());

        return (float) (getProcessDuration() - getTimer()) / (float) getProcessDuration();
    }

    public boolean add(int amount, boolean simulate){
        if(amount + getCurrent() > getMax()){
            return false;
        }
        else if (!simulate){
            setCurrent(getCurrent()+amount);
        }
        return true;
    }

    public boolean consume(int amount, boolean simulate){
        if(getCurrent() < amount){
            return false;
        }
        else if (!simulate){
            setCurrent(getCurrent()-amount);
        }
        return true;
    }

    public void loadAdditional(@NotNull CompoundTag tag){
        setCurrent(tag.getInt(getId()+"vitae"));
        setTimer(tag.getInt(getId()+"timer"));
        setToAdd(tag.getInt(getId()+"toAdd"));
        setIncrementalToAdd(tag.getInt(getId()+"incrToAdd"));
    }
    public void saveAdditional(@NotNull CompoundTag tag){
        tag.putInt(getId()+"vitae", getCurrent());
        tag.putInt(getId()+"timer", getTimer());
        tag.putInt(getId()+"toAdd", getToAdd());
        tag.putInt(getId()+"incrToAdd", getIncrementalToAdd());
    }


}
