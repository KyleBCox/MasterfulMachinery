package com.ticticboooom.mods.mm.loader;


import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MasterfulIO {
    public static List<File> listFiles(File dir) {
        File[] files = dir.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }
        List<File> result = new ArrayList<>();

        for (File file : files) {
            if (file.exists() && file.isFile()) {
                result.add(file);
            }
        }
        return result;
    }

    public static File getOrCreateDirectory(Path path) {
        File tierConfigFile = path.toFile();
        if (!tierConfigFile.exists()){
            tierConfigFile.mkdirs();
        }
        return tierConfigFile;
    }
}
