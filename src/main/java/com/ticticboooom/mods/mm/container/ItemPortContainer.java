package com.ticticboooom.mods.mm.container;

import com.ticticboooom.mods.mm.tile.ItemPortTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class ItemPortContainer extends Container {
    private ItemPortTile tile;

    public ItemPortContainer(int p_createMenu_1_, PlayerInventory inv, ItemPortTile itemPortTile, ContainerType<ItemPortContainer> type) {
        super(type, p_createMenu_1_);
        this.tile = itemPortTile;

        setupSlots();

        // Main Player Inventory
        int slotSizePlus2 = 18;
        int startX = 8;
        int startY = 17;
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

    public ItemPortContainer(int windowId, PlayerInventory player, PacketBuffer buffer, ContainerType<?> type) {
        this(windowId, player, (ItemPortTile) player.player.world.getTileEntity(buffer.readBlockPos()), (ContainerType<ItemPortContainer>) type);
    }

    private void setupSlots() {
        int centreX = 87;
        int centreY = 62;
        int slotSizePlus2 = 18;
        int width = tile.getSlotDimensions().getX();
        int height = tile.getSlotDimensions().getY();
        int centreOffsetX = centreX - ((width * slotSizePlus2) / 2);
        int centreOffsetY = centreY - ((height * slotSizePlus2) / 2);
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.addSlot(new Slot(tile, index, centreOffsetX + (slotSizePlus2 * x), centreOffsetY + (slotSizePlus2 * y)));
                index++;
            }
        }

    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }


    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();
            if (index < this.tile.getSizeInventory()) {
                if (!this.mergeItemStack(itemStack1, this.tile.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemStack1, 0, this.tile.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemStack;
    }
}
