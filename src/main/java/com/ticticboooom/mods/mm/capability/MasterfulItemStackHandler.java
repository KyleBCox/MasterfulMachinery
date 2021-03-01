package com.ticticboooom.mods.mm.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class MasterfulItemStackHandler extends ItemStackHandler {
    private final int slots;
    private final int extractSpeed;
    private final int insertSpeed;

    public MasterfulItemStackHandler(int slots, int extractSpeed, int insertSpeed) {
        super(slots);
        this.slots = slots;
        this.extractSpeed = extractSpeed;
        this.insertSpeed = insertSpeed;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.getCount() > this.insertSpeed){
            super.insertItem(slot, new ItemStack(stack.getItem(), insertSpeed), simulate);
            return new ItemStack(stack.getItem(), stack.getCount() - insertSpeed);
        }
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount > this.extractSpeed) {
            ItemStack stackInSlot = this.getStackInSlot(slot);
            if (!simulate) {
                stackInSlot.setCount(stackInSlot.getCount() - extractSpeed);
            }
            return new ItemStack(stackInSlot.getItem(), stackInSlot.getCount() - extractSpeed);
        }
        return super.extractItem(slot, amount, simulate);
    }

    public NonNullList<ItemStack> getItemStacks(){
        return stacks;
    }
    public void setStacks(NonNullList<ItemStack> stacks) {
        this.stacks = stacks;
    }
}
