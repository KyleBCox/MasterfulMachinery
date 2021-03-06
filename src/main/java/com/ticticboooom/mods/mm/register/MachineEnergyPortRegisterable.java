package com.ticticboooom.mods.mm.register;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.MachinePartBlock;
import com.ticticboooom.mods.mm.container.EnergyPortContainer;
import com.ticticboooom.mods.mm.container.FluidPortContainer;
import com.ticticboooom.mods.mm.loader.model.MachineEnergyPortModel;
import com.ticticboooom.mods.mm.loader.model.MachineFluidPortModel;
import com.ticticboooom.mods.mm.tile.EnergyPortTile;
import com.ticticboooom.mods.mm.tile.FluidPortTile;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;

public class MachineEnergyPortRegisterable extends Registerable {

    private RegistryObject<Block> inputBlock;
    private RegistryObject<ContainerType<EnergyPortContainer>> inputContainer;
    private RegistryObject<TileEntityType<?>> inputTile;
    private RegistryObject<Item> inputBlockItem;

    private RegistryObject<Block> outputBlock;
    private RegistryObject<ContainerType<EnergyPortContainer>> outputContainer;
    private RegistryObject<TileEntityType<?>> outputTile;
    private RegistryObject<Item> outputBlockItem;

    private final MachineEnergyPortModel model;

    public MachineEnergyPortRegisterable(MachineEnergyPortModel model) {

        this.model = model;
    }

    @Override
    public void register() {
        String inputId = model.getIdPrefix() + "_energy_port_input";
        inputBlock = BLOCKS.register(inputId, () -> new MachinePartBlock(inputTile));
        inputContainer = CONTAINERS.register(inputId, () -> IForgeContainerType.create((x, y, z) -> new EnergyPortContainer(x, y, z, inputContainer.get())));
        inputTile = TILES.register(inputId, () -> TileEntityType.Builder.create(() -> new EnergyPortTile(inputTile.get(), inputContainer.get(), model.getCapacity(), model.getOutputMax(), model.getInputMax(), true), inputBlock.get()).build(null));
        inputBlockItem = ITEMS.register(inputId, () -> new BlockItem(inputBlock.get(), new Item.Properties().group(MM.MM_GROUP)));

        String outputId = model.getIdPrefix() + "_energy_port_output";
        outputBlock = BLOCKS.register(outputId, () -> new MachinePartBlock(outputTile));
        outputContainer = CONTAINERS.register(outputId, () -> IForgeContainerType.create((x, y, z) -> new EnergyPortContainer(x, y, z, outputContainer.get())));
        outputTile = TILES.register(outputId, () -> TileEntityType.Builder.create(() -> new EnergyPortTile(outputTile.get(), outputContainer.get(), model.getCapacity(), model.getOutputMax(), model.getInputMax(), true), outputBlock.get()).build(null));
        outputBlockItem = ITEMS.register(outputId, () -> new BlockItem(outputBlock.get(), new Item.Properties().group(MM.MM_GROUP)));
    }

    public RegistryObject<Block> getInputBlock() {
        return inputBlock;
    }

    public RegistryObject<ContainerType<EnergyPortContainer>> getInputContainer() {
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

    public RegistryObject<ContainerType<EnergyPortContainer>> getOutputContainer() {
        return outputContainer;
    }

    public RegistryObject<Item> getOutputBlockItem() {
        return outputBlockItem;
    }

    public RegistryObject<TileEntityType<?>> getOutputTile() {
        return outputTile;
    }

    public MachineEnergyPortModel getModel() {
        return model;
    }
}
