package com.ticticboooom.mods.mm.tile;

import com.ticticboooom.mods.mm.capability.MasterfulItemStackHandler;
import com.ticticboooom.mods.mm.container.ItemPortContainer;
import com.ticticboooom.mods.mm.net.MachineTileSyncPacket;
import com.ticticboooom.mods.mm.net.PacketHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

public class ItemPortTile extends LockableLootTileEntity implements ITickableTileEntity {
    private ContainerType<ItemPortContainer> containerType;
    private Vector3i slots;
    private final boolean isInput;
    private final MasterfulItemStackHandler inventory;
    private final LazyOptional<MasterfulItemStackHandler> inventoryLO;

    public ItemPortTile(TileEntityType<?> tileEntityTypeIn, ContainerType<ItemPortContainer> containerType, int[] slots, int extractSpeed, int insertSpeed, boolean isInput) {
        super(tileEntityTypeIn);
        this.containerType = containerType;
        this.slots = new Vector3i(slots[0], slots[1], 0);
        this.isInput = isInput;
        inventory = new MasterfulItemStackHandler(slots[0] * slots[1], extractSpeed, insertSpeed);
        inventoryLO = LazyOptional.of(() -> inventory);
    }


    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return inventoryLO.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        inventoryLO.invalidate();
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.masterfulmachinery.item_port");
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.inventory.getItemStacks();
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        int counter = 0;
        for (ItemStack stack : itemsIn) {
            this.inventory.getItemStacks().set(counter, stack);
            counter++;
        }
    }


    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new ItemPortContainer(id, player, this, containerType);
    }

    public Vector3i getSlotDimensions() {
        return this.slots;
    }

    @Override
    public int getSizeInventory() {
        return this.slots.getY() * this.slots.getX();
    }

    public boolean isInput() {
        return isInput;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        NonNullList<ItemStack> itemStacks = NonNullList.withSize(this.inventory.getSlots(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, itemStacks);
        this.inventory.setStacks(itemStacks);
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ItemStackHelper.saveAllItems(compound, this.inventory.getItemStacks());
        return super.write(compound);
    }

    @Override
    public void tick() {
        if (world instanceof ServerWorld) {
            for (PlayerEntity player : world.getPlayers()) {
                PacketHandler.sendTo(new MachineTileSyncPacket(write(new CompoundNBT())), (ServerPlayerEntity) player);
            }
        }
    }
}
