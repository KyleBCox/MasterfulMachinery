package com.ticticboooom.mods.mm.register;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.MachinePartBlock;
import com.ticticboooom.mods.mm.container.EnergyPortContainer;
import com.ticticboooom.mods.mm.container.FluidPortContainer;
import com.ticticboooom.mods.mm.container.ItemPortContainer;
import com.ticticboooom.mods.mm.loader.model.MachineFluidPortModel;
import com.ticticboooom.mods.mm.loader.model.MachineItemPortModel;
import com.ticticboooom.mods.mm.tile.FluidPortTile;
import com.ticticboooom.mods.mm.tile.ItemPortTile;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;

public class MachineFluidPortRegisterable extends Registerable {

    private RegistryObject<Block> inputBlock;
    private RegistryObject<ContainerType<FluidPortContainer>> inputContainer;
    private RegistryObject<TileEntityType<?>> inputTile;
    private RegistryObject<Item> inputBlockItem;

    private RegistryObject<Block> outputBlock;
    private RegistryObject<ContainerType<FluidPortContainer>> outputContainer;
    private RegistryObject<TileEntityType<?>> outputTile;
    private RegistryObject<Item> outputBlockItem;

    private final MachineFluidPortModel model;

    public MachineFluidPortRegisterable(MachineFluidPortModel model) {

        this.model = model;
    }

    @Override
    public void register() {
        String inputId = model.getIdPrefix() + "_fluid_port_input";
        inputBlock = BLOCKS.register(inputId, () -> new MachinePartBlock(inputTile));
        inputContainer = CONTAINERS.register(inputId, () -> IForgeContainerType.create((x, y, z) -> new FluidPortContainer(x, y, z, inputContainer.get())));
        inputTile = TILES.register(inputId, () -> TileEntityType.Builder.create(() -> new FluidPortTile(inputTile.get(), inputContainer.get(), model.getCapacity(), model.getOutputMax(), model.getInputMax(), true), inputBlock.get()).build(null));
        inputBlockItem = ITEMS.register(inputId, () -> new BlockItem(inputBlock.get(), new Item.Properties().group(MM.MM_GROUP)));

        String outputId = model.getIdPrefix() + "_fluid_port_output";
        outputBlock = BLOCKS.register(outputId, () -> new MachinePartBlock(outputTile));
        outputContainer = CONTAINERS.register(outputId, () -> IForgeContainerType.create((x, y, z) -> new FluidPortContainer(x, y, z, outputContainer.get())));
        outputTile = TILES.register(outputId, () -> TileEntityType.Builder.create(() -> new FluidPortTile(outputTile.get(), outputContainer.get(), model.getCapacity(), model.getOutputMax(), model.getInputMax(), true), outputBlock.get()).build(null));
        outputBlockItem = ITEMS.register(outputId, () -> new BlockItem(outputBlock.get(), new Item.Properties().group(MM.MM_GROUP)));
    }
    public RegistryObject<Block> getInputBlock() {
        return inputBlock;
    }

    public RegistryObject<ContainerType<FluidPortContainer>> getInputContainer() {
        return inputContainer;
    }

    public RegistryObject<Item> getInputBlockItem() {
        return inputBlockItem;
    }

    public RegistryObject<TileEntityType<?>> getInputTile() {
        return inputTile;
    }

    public RegistryObject<Block> getOutputBlock() {
        return outputBlock;
    }

    public RegistryObject<ContainerType<FluidPortContainer>> getOutputContainer() {
        return outputContainer;
    }

    public RegistryObject<Item> getOutputBlockItem() {
        return outputBlockItem;
    }

    public RegistryObject<TileEntityType<?>> getOutputTile() {
        return outputTile;
    }

    public MachineFluidPortModel getModel() {
        return model;
    }
}
