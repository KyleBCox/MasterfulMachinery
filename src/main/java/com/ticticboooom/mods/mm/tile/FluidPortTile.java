package com.ticticboooom.mods.mm.tile;


import com.ticticboooom.mods.mm.capability.MasterfulFluidTank;
import com.ticticboooom.mods.mm.container.FluidPortContainer;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class FluidPortTile extends LockableLootTileEntity implements ITickableTileEntity {
    private ContainerType<FluidPortContainer> containerType;
    private final boolean isInput;

    private final MasterfulFluidTank tank;
    private final LazyOptional<MasterfulFluidTank> tankLO;

    public FluidPortTile(TileEntityType<?> tileEntityTypeIn, ContainerType<FluidPortContainer> containerType, int tankCapacity, int extractSpeed, int insertSpeed, boolean isInput) {
        super(tileEntityTypeIn);
        this.containerType = containerType;
        this.isInput = isInput;
        tank = new MasterfulFluidTank(tankCapacity, extractSpeed, insertSpeed);
        tankLO = LazyOptional.of(() -> tank);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new StringTextComponent("");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new FluidPortContainer(id, player, this, containerType);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return tankLO.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        tankLO.invalidate();
    }

    public MasterfulFluidTank getTank() {
        return this.tank;
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
    public CompoundNBT write(CompoundNBT compound) {
        tank.getFluidInTank(0).writeToNBT(compound);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        tank.setStack(FluidStack.loadFluidStackFromNBT(nbt));
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
