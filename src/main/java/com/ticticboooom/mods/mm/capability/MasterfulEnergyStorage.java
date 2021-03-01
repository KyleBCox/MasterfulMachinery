package com.ticticboooom.mods.mm.capability;

import net.minecraftforge.energy.EnergyStorage;

public class MasterfulEnergyStorage extends EnergyStorage {
    public MasterfulEnergyStorage(int capacity) {
        super(capacity);
    }
    public MasterfulEnergyStorage(int capacity, int insert, int extract) {
        super(capacity, insert, extract);
    }

    public void setStored(int amount) {
        this.energy = amount;
    }
}
