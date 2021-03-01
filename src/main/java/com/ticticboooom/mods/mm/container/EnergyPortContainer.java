package com.ticticboooom.mods.mm.container;

import com.ticticboooom.mods.mm.tile.EnergyPortTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

public class EnergyPortContainer extends Container {
    private EnergyPortTile tile;

    public EnergyPortContainer(int p_createMenu_1_, PlayerInventory inv, EnergyPortTile energyPortTile, ContainerType<EnergyPortContainer> type) {
        super(type, p_createMenu_1_);
        this.tile = energyPortTile;

        // Main Player Inventory
        int slotSizePlus2 = 18;
        int startX = 8;
        int playerStartY = 141;
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(inv, 9 + (row * 9) + column, startX + (column * slotSizePlus2), playerStartY + (row * slotSizePlus2)));
            }
        }

        // Hotbar
        int hatBarY = 200;
        for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(inv, column, startX + (column * slotSizePlus2), hatBarY));
        }
    }

    public EnergyPortContainer(int windowId, PlayerInventory player, PacketBuffer buffer, ContainerType<?> type) {
        this(windowId, player, (EnergyPortTile) player.player.world.getTileEntity(buffer.readBlockPos()), (ContainerType<EnergyPortContainer>) type);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public EnergyPortTile getTile() {
        return this.tile;
    }
}
