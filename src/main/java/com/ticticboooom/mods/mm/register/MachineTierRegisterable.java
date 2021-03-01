package com.ticticboooom.mods.mm.register;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.MachinePartBlock;
import com.ticticboooom.mods.mm.container.ControllerContainer;
import com.ticticboooom.mods.mm.loader.model.MachineTierModel;
import com.ticticboooom.mods.mm.tile.ControllerTile;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;

public class MachineTierRegisterable extends Registerable {

    private RegistryObject<Block> controllerBlock;
    private RegistryObject<ContainerType<ControllerContainer>> controllerContainer;
    private RegistryObject<TileEntityType<?>> controllerTile;
    private RegistryObject<Item> controllerBlockItem;

    private final MachineTierModel model;

    public MachineTierRegisterable(MachineTierModel model) {
        this.model = model;
    }

    @Override
    public void register() {
        String id = model.getIdPrefix() + "_controller";
        controllerBlock = BLOCKS.register(id, () -> new MachinePartBlock(controllerTile));
        controllerContainer = CONTAINERS.register(id, () -> IForgeContainerType.create((x, y, z) -> new ControllerContainer(x, y, z, controllerContainer.get())));
        controllerTile = TILES.register(id, () ->  TileEntityType.Builder.create(() -> new ControllerTile(controllerTile.get()), controllerBlock.get()).build(null));
        controllerBlockItem = ITEMS.register(id, () -> new BlockItem(controllerBlock.get(), new Item.Properties().group(MM.MM_GROUP)));
    }

    public RegistryObject<Block> getControllerBlock() {
        return controllerBlock;
    }

    public RegistryObject<ContainerType<ControllerContainer>> getControllerContainer() {
        return controllerContainer;
    }

    public RegistryObject<Item> getControllerBlockItem() {
        return controllerBlockItem;
    }

    public RegistryObject<TileEntityType<?>> getControllerTile() {
        return controllerTile;
    }

    public MachineTierModel getModel() {
        return model;
    }

}
