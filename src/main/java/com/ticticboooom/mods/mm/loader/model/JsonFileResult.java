package com.ticticboooom.mods.mm.loader.model;

import com.google.gson.JsonElement;

import java.io.File;

public class JsonFileResult {
    private final File originalFile;
    private final JsonElement jsonElement;

    public JsonFileResult(File originalFile, JsonElement jsonElement) {
        this.originalFile = originalFile;
        this.jsonElement = jsonElement;
    }

    public File getOriginalFile() {
        return originalFile;
    }

    public JsonElement getJsonElement() {
        return jsonElement;
    }
}
