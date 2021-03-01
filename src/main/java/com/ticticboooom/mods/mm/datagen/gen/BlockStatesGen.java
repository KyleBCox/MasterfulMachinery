package com.ticticboooom.mods.mm.datagen.gen;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.loader.MMStartupLoader;
import com.ticticboooom.mods.mm.register.MachineEnergyPortRegisterable;
import com.ticticboooom.mods.mm.register.MachineFluidPortRegisterable;
import com.ticticboooom.mods.mm.register.MachineItemPortRegisterable;
import com.ticticboooom.mods.mm.register.MachineTierRegisterable;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class BlockStatesGen extends BlockStateProvider {
    private static final ResourceLocation CUBE_ALL_OVERLAYED = new ResourceLocation("masterfulmachinery", "block/cube_all_overlayed");
    private static final ResourceLocation CUBE_OVERLAYED = new ResourceLocation("masterfulmachinery", "block/cube_overlayed");
    private static final ResourceLocation NOTHING = new ResourceLocation("masterfulmachinery", "blocks/nothing_transparent");

    public BlockStatesGen(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, MM.ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (MachineTierRegisterable tier : MMStartupLoader.TIERS) {
            BlockModelBuilder builder = this.models()
                    .getBuilder(tier.getControllerBlock().get().getRegistryName().toString())
                    .parent(new ModelFile.UncheckedModelFile(CUBE_OVERLAYED));
            if (tier.getModel().getBaseTexture() != null) {
                builder.texture("base", tier.getModel().getBaseTexture());
            }
            if (tier.getModel().getOverlayTexture() != null) {
                builder.texture("overlay", tier.getModel().getOverlayTexture());
            }
            simpleBlock(tier.getControllerBlock().get(), new ModelFile.UncheckedModelFile(tier.getControllerBlock().get().getRegistryName().getNamespace() + ":block/" + tier.getControllerBlock().get().getRegistryName().getPath()));
            this.itemModels().getBuilder(tier.getControllerBlockItem().get().getRegistryName().toString()).parent(new ModelFile.UncheckedModelFile(tier.getControllerBlock().get().getRegistryName().getNamespace() + ":block/" + tier.getControllerBlock().get().getRegistryName().getPath()));
        }

        for (MachineItemPortRegisterable port : MMStartupLoader.ITEM_PORTS) {
            genPortBlocks(port.getInputBlock(), port.getOutputBlock(), port.getModel().getBaseTexture(), port.getModel().getInputOverlayTexture(), port.getModel().getOutputOverlayTexture(), port.getModel().getInputDetailTexture(), port.getModel().getOutputDetailTexture());
        }

        for (MachineFluidPortRegisterable port : MMStartupLoader.FLUID_PORTS) {
            genPortBlocks(port.getInputBlock(), port.getOutputBlock(), port.getModel().getBaseTexture(), port.getModel().getInputOverlayTexture(), port.getModel().getOutputOverlayTexture(), port.getModel().getInputDetailTexture(), port.getModel().getOutputDetailTexture());
        }

        for (MachineEnergyPortRegisterable port : MMStartupLoader.ENERGY_PORTS) {
            genPortBlocks(port.getInputBlock(), port.getOutputBlock(), port.getModel().getBaseTexture(), port.getModel().getInputOverlayTexture(), port.getModel().getOutputOverlayTexture(), port.getModel().getInputDetailTexture(), port.getModel().getOutputDetailTexture());
        }
    }

    private void genPortBlocks(RegistryObject<Block> inputBlock, RegistryObject<Block> outputBlock, String baseTexture, String inputOverlayTexture, String outputOverlayTexture, String inputDetailTexture, String outputDetailTexture) {
        if (baseTexture != null) {
            BlockModelBuilder inputBuilder = this.models()
                    .getBuilder(inputBlock.get().getRegistryName().toString())
                    .parent(new ModelFile.UncheckedModelFile(CUBE_ALL_OVERLAYED))
                    .texture("base", baseTexture);
            if (inputOverlayTexture != null) {
                inputBuilder.texture("overlay", inputOverlayTexture);
            } else {
                inputBuilder.texture("overlay", NOTHING);
            }
            if (inputDetailTexture != null) {
                inputBuilder.texture("detail", inputDetailTexture);
            } else {
                inputBuilder.texture("detail", NOTHING);
            }
            simpleBlock(inputBlock.get(), new ModelFile.UncheckedModelFile(inputBlock.get().getRegistryName().getNamespace() + ":block/" + inputBlock.get().getRegistryName().getPath()));
        }
        if (baseTexture != null) {
            BlockModelBuilder outputBuilder = this.models()
                    .getBuilder(outputBlock.get().getRegistryName().toString())
                    .parent(new ModelFile.UncheckedModelFile(CUBE_ALL_OVERLAYED))
                    .texture("base", baseTexture);
            if (outputOverlayTexture != null) {
                outputBuilder.texture("overlay", outputOverlayTexture);
            } else {
                outputBuilder.texture("overlay", NOTHING);
            }
            if (outputDetailTexture != null) {
                outputBuilder.texture("detail", outputDetailTexture);
            } else {
                outputBuilder.texture("default", NOTHING);
            }
            simpleBlock(outputBlock.get(), new ModelFile.UncheckedModelFile(outputBlock.get().getRegistryName().getNamespace() + ":block/" + outputBlock.get().getRegistryName().getPath()));
        }
        this.itemModels().getBuilder(inputBlock.get().getRegistryName().toString()).parent(new ModelFile.UncheckedModelFile(inputBlock.get().getRegistryName().getNamespace() + ":block/" + inputBlock.get().getRegistryName().getPath()));
        this.itemModels().getBuilder(outputBlock.get().getRegistryName().toString()).parent(new ModelFile.UncheckedModelFile(outputBlock.get().getRegistryName().getNamespace() + ":block/" + outputBlock.get().getRegistryName().getPath()));

    }
}
