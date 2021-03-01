package com.ticticboooom.mods.mm.net;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MachineTileSyncPacket {
    private CompoundNBT tag;

    public MachineTileSyncPacket(CompoundNBT tag) {
        this.tag = tag;
    }

    public static void encode(MachineTileSyncPacket pkt, PacketBuffer buf) {
        buf.writeCompoundTag(pkt.tag);
    }

    public static MachineTileSyncPacket decode(PacketBuffer buf) {
        return new MachineTileSyncPacket(buf.readCompoundTag());
    }

    public CompoundNBT getTag() {
        return tag;
    }

    public static void handle(final MachineTileSyncPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            CompoundNBT nbt = pkt.tag;
            int x = nbt.getInt("x");
            int y = nbt.getInt("y");
            int z = nbt.getInt("z");
            TileEntity te = Minecraft.getInstance().world.getTileEntity(new BlockPos(x, y, z));
            BlockState state = Minecraft.getInstance().world.getBlockState(new BlockPos(x, y, z));
            if (te != null) {
                te.read(state, nbt);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
