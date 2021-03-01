package com.ticticboooom.mods.mm.loader.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class MachineTierModel {
    public static final Codec<MachineTierModel> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.STRING.fieldOf("idPrefix").forGetter(it -> it.idPrefix),
            Codec.STRING.fieldOf("localizedName").forGetter(it -> it.localizedName),
            Codec.STRING.fieldOf("baseTexture").orElse(null).forGetter(it -> it.baseTexture),
            Codec.STRING.fieldOf("overlayTexture").orElse(null).forGetter(it -> it.overlayTexture)
    ).apply(x, MachineTierModel::new));
    private String idPrefix;
    private String localizedName;
    private final String baseTexture;
    private final String overlayTexture;

    public MachineTierModel(String idPrefix, String localizedName, String baseTexture, String overlayTexture) {
        this.idPrefix = idPrefix;
        this.localizedName = localizedName;
        this.baseTexture = baseTexture;
        this.overlayTexture = overlayTexture;
    }

    public String getIdPrefix() {
        return idPrefix;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public void setIdPrefix(String idPrefix) {
        this.idPrefix = idPrefix;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }

    public String getBaseTexture() {
        return baseTexture;
    }

    public String getOverlayTexture() {
        return overlayTexture;
    }
}

