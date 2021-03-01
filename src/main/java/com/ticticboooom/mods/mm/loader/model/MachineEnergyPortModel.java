package com.ticticboooom.mods.mm.loader.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public class MachineEnergyPortModel {
    public static final Codec<MachineEnergyPortModel> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.STRING.fieldOf("idPrefix").forGetter(it -> it.idPrefix),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("capacity").forGetter(it -> it.capacity),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("inputMax").forGetter(it -> it.inputMax),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("outputMax").forGetter(it -> it.outputMax),
            Codec.STRING.optionalFieldOf("baseTexture").orElse(null).forGetter(it -> Optional.ofNullable(it.baseTexture)),
            Codec.STRING.optionalFieldOf("inputOverlayTexture").orElse(null).forGetter(it -> Optional.ofNullable(it.inputOverlayTexture)),
            Codec.STRING.optionalFieldOf("outputOverlayTexture").orElse(null).forGetter(it -> Optional.ofNullable(it.outputOverlayTexture)),
            Codec.STRING.optionalFieldOf("inputDetailTexture").orElse(null).forGetter(it -> Optional.ofNullable(it.inputDetailTexture)),
            Codec.STRING.optionalFieldOf("outputDetailTexture").orElse(null).forGetter(it -> Optional.ofNullable(it.inputDetailTexture))
    ).apply(x, (s, i, i2, i3, s2, s3, s4, s5, s6) ->
            new MachineEnergyPortModel(s, i, i2, i3, s2.orElse(null), s3.orElse(null), s4.orElse(null), s5.orElse(null), s6.orElse(null))));

    private String idPrefix;
    private int capacity;
    private int inputMax;
    private int outputMax;
    private final String baseTexture;
    private final String inputOverlayTexture;
    private final String outputOverlayTexture;
    private final String inputDetailTexture;
    private final String outputDetailTexture;

    public MachineEnergyPortModel(String idPrefix, int capacity, int inputMax, int outputMax, String baseTexture, String inputOverlayTexture, String outputOverlayTexture, String inputDetailTexture, String outputDetailTexture) {
        this.idPrefix = idPrefix;
        this.capacity = capacity;

        this.inputMax = inputMax;
        this.outputMax = outputMax;
        this.baseTexture = baseTexture;
        this.inputOverlayTexture = inputOverlayTexture;
        this.outputOverlayTexture = outputOverlayTexture;
        this.inputDetailTexture = inputDetailTexture;
        this.outputDetailTexture = outputDetailTexture;
    }


    public int getInputMax() {
        return inputMax;
    }

    public int getOutputMax() {
        return outputMax;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setInputMax(int inputMax) {
        this.inputMax = inputMax;
    }

    public void setOutputMax(int outputMax) {
        this.outputMax = outputMax;
    }


    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getIdPrefix() {
        return idPrefix;
    }

    public void setIdPrefix(String idPrefix) {
        this.idPrefix = idPrefix;
    }

    public String getInputOverlayTexture() {
        return inputOverlayTexture;
    }

    public String getOutputOverlayTexture() {
        return outputOverlayTexture;
    }

    public String getBaseTexture() {
        return baseTexture;
    }

    public String getOutputDetailTexture() {
        return outputDetailTexture;
    }

    public String getInputDetailTexture() {
        return inputDetailTexture;
    }
}
