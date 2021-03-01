package com.ticticboooom.mods.mm.tile;

import com.ticticboooom.mods.mm.capability.MasterfulEnergyStorage;
import com.ticticboooom.mods.mm.container.EnergyPortContainer;
import com.ticticboooom.mods.mm.net.MachineTileSyncPacket;
import com.ticticboooom.mods.mm.net.PacketHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyPortTile extends LockableLootTileEntity implements ITickableTileEntity {
    private ContainerType<EnergyPortContainer> containerType;
    private final boolean isInput;
    private final MasterfulEnergyStorage buffer;
    private final LazyOptional<IEnergyStorage> bufferLO;


    public EnergyPortTile(TileEntityType<?> tileEntityTypeIn, ContainerType<EnergyPortContainer> containerType, int energyCapacity, int extractSpeed, int insertSpeed, boolean isInput) {
        super(tileEntityTypeIn);
        this.containerType = containerType;
        this.isInput = isInput;
        buffer = new MasterfulEnergyStorage(energyCapacity, insertSpeed, extractSpeed);
        bufferLO = LazyOptional.of(() -> buffer);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new StringTextComponent("");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new EnergyPortContainer(id, player, this, containerType);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityEnergy.ENERGY){
            return bufferLO.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        bufferLO.invalidate();
    }

    public IEnergyStorage getEnergyStorage() {
        return this.buffer;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return null;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {

    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        int stored = nbt.getInt("energyStored");
        this.buffer.setStored(stored);
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("energyStored", this.buffer.getEnergyStored());
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

    public boolean isInput() {
        return isInput;
    }
}
