package com.ticticboooom.mods.mm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.ticticboooom.mods.mm.datagen.MMPackFinder;
import com.ticticboooom.mods.mm.datagen.MemoryDataGeneratorFactory;
import com.ticticboooom.mods.mm.datagen.PackType;
import com.ticticboooom.mods.mm.datagen.gen.BlockStatesGen;
import com.ticticboooom.mods.mm.loader.MMStartupLoader;
import com.ticticboooom.mods.mm.net.PacketHandler;
import com.ticticboooom.mods.mm.register.Registerable;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Mod(MM.ID)
public class MM {
    public static final String ID = "masterfulmachinery";
    public static final Logger LOGGER = LogManager.getLogger("masterfulmachinery");


    public static MM instance = null;
    private boolean hasGenerated = false;
    private DataGenerator dataGenerator;

    public MM() {
        instance = this;
        MMStartupLoader.load();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Registerable.BLOCKS.register(bus);
        Registerable.CONTAINERS.register(bus);
        Registerable.ITEMS.register(bus);
        Registerable.TILES.register(bus);
        MemoryDataGeneratorFactory.init();
        setupGenerators();
        MinecraftForge.EVENT_BUS.addListener(MM::onServerStarted);
        bus.addListener(this::clientEvents);
        bus.addListener(this::setup);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            Minecraft.getInstance().getResourcePackList().addPackFinder(new MMPackFinder(PackType.RESOURCE));
        }
    }

    private void clientEvents(final FMLClientSetupEvent event) {
        for (RegistryObject<Block> block : Registerable.BLOCKS.getEntries()) {
            RenderTypeLookup.setRenderLayer(block.get(), layer -> layer == RenderType.getSolid() || layer == RenderType.getTranslucent());
        }
    }

    private void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(PacketHandler::register);
    }

    public static void onServerStarted(FMLServerAboutToStartEvent event) {
        event.getServer().getResourcePacks().addPackFinder(new MMPackFinder(PackType.DATA));
    }

    public static final ItemGroup MM_GROUP = new ItemGroup("masterfulmachinery") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.PAPER);
        }
    };

    private void setupGenerators() {
        dataGenerator = MemoryDataGeneratorFactory.createMemoryDataGenerator();
        ExistingFileHelper existingFileHelper = new ExistingFileHelper(ImmutableList.of(), ImmutableSet.of(), false);

        dataGenerator.addProvider(new BlockStatesGen(dataGenerator, existingFileHelper));
    }

    public static void generate() {
        if (!instance.hasGenerated) {
            try {
                instance.dataGenerator.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
            instance.hasGenerated = true;
        }
    }
}
