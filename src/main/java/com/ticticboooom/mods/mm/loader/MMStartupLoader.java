package com.ticticboooom.mods.mm.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.loader.model.*;
import com.ticticboooom.mods.mm.register.MachineEnergyPortRegisterable;
import com.ticticboooom.mods.mm.register.MachineFluidPortRegisterable;
import com.ticticboooom.mods.mm.register.MachineItemPortRegisterable;
import com.ticticboooom.mods.mm.register.MachineTierRegisterable;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MMStartupLoader {
    public static final List<MachineTierRegisterable> TIERS = new ArrayList<>();
    public static final List<MachineItemPortRegisterable> ITEM_PORTS = new ArrayList<>();
    public static final List<MachineFluidPortRegisterable> FLUID_PORTS = new ArrayList<>();
    public static final List<MachineEnergyPortRegisterable> ENERGY_PORTS = new ArrayList<>();

    public static void load() {
        Path rootConfig = FMLPaths.CONFIGDIR.get().resolve("masterfulmachinery");
        File rootConfigPathFile = MasterfulIO.getOrCreateDirectory(rootConfig);

        File tierConfigFile = MasterfulIO.getOrCreateDirectory(rootConfig.resolve("tiers"));
        List<JsonFileResult> tiers = loadFiles(tierConfigFile);
        for (JsonFileResult tier : tiers) {
            Optional<Pair<MachineTierModel, JsonElement>> model = JsonOps.INSTANCE.withDecoder(MachineTierModel.CODEC).apply(tier.getJsonElement()).resultOrPartial(x -> {
                MM.LOGGER.error("failed to parse file: " + tier.getOriginalFile().getAbsolutePath() + " with error: " + x);
            });
            if (!model.isPresent()) {
                continue;
            }
            MachineTierRegisterable machineTierRegisterable = new MachineTierRegisterable(model.get().getFirst());
            machineTierRegisterable.register();
            TIERS.add(machineTierRegisterable);
        }

        File machinePort = MasterfulIO.getOrCreateDirectory(rootConfig.resolve("machine_port"));
        File machineItemPort = MasterfulIO.getOrCreateDirectory(machinePort.toPath().resolve("item"));
        File machineFluidPort = MasterfulIO.getOrCreateDirectory(machinePort.toPath().resolve("fluid"));
        File machineEnergyPort = MasterfulIO.getOrCreateDirectory(machinePort.toPath().resolve("energy"));

        List<JsonFileResult> itemPortModels = loadFiles(machineItemPort);
        List<JsonFileResult> fluidPortModels = loadFiles(machineFluidPort);
        List<JsonFileResult> energyPortModels = loadFiles(machineEnergyPort);

        for (JsonFileResult itemPortModel : itemPortModels) {
            Optional<Pair<MachineItemPortModel, JsonElement>> model = JsonOps.INSTANCE.withDecoder(MachineItemPortModel.CODEC).apply(itemPortModel.getJsonElement()).resultOrPartial(x -> {
                MM.LOGGER.error("failed to parse file: " + itemPortModel.getOriginalFile().getAbsolutePath() + " with error: " + x);
            });
            if (!model.isPresent()){
                continue;
            }
            MachineItemPortRegisterable machineItemPortRegisterable = new MachineItemPortRegisterable(model.get().getFirst());
            machineItemPortRegisterable.register();
            ITEM_PORTS.add(machineItemPortRegisterable);
        }

        for (JsonFileResult fluidPortModel : fluidPortModels) {
            Optional<Pair<MachineFluidPortModel, JsonElement>> model = JsonOps.INSTANCE.withDecoder(MachineFluidPortModel.CODEC).apply(fluidPortModel.getJsonElement()).resultOrPartial(x -> {
                MM.LOGGER.error("failed to parse file: " + fluidPortModel.getOriginalFile().getAbsolutePath() + " with error: " + x);
            });
            if (!model.isPresent()){
                continue;
            }
            MachineFluidPortRegisterable machineFluidPortRegisterable = new MachineFluidPortRegisterable(model.get().getFirst());
            machineFluidPortRegisterable.register();
            FLUID_PORTS.add(machineFluidPortRegisterable);
        }

        for (JsonFileResult energyPortModel : energyPortModels) {
            Optional<Pair<MachineEnergyPortModel, JsonElement>> model = JsonOps.INSTANCE.withDecoder(MachineEnergyPortModel.CODEC).apply(energyPortModel.getJsonElement()).resultOrPartial(x -> {
                MM.LOGGER.error("failed to parse file: " + energyPortModel.getOriginalFile().getAbsolutePath() + " with error: " + x);
            });
            if (!model.isPresent()){
                continue;
            }
            MachineEnergyPortRegisterable machineEnergyPortRegisterable = new MachineEnergyPortRegisterable(model.get().getFirst());
            machineEnergyPortRegisterable.register();
            ENERGY_PORTS.add(machineEnergyPortRegisterable);
        }
    }

    private static List<JsonFileResult> loadFiles(File directory) {
        List<JsonFileResult> result = new ArrayList<>();
        List<File> files = MasterfulIO.listFiles(directory);
        for (File file : files) {
            try {
                JsonElement model = new JsonParser().parse(new FileReader(file));
                result.add(new JsonFileResult(file, model));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
