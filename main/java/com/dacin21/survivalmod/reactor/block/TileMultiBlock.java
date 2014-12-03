package com.dacin21.survivalmod.reactor.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class TileMultiBlock extends TileEntity {
    protected boolean hasMaster;
    protected int masterX, masterY, masterZ;

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!worldObj.isRemote) {
            if (hasMaster()) { 
                    doMultiBlockStuff();
            } 
        }
    }

    /**
     * Stuff the multiblock will do when formed
     */
    public abstract void doMultiBlockStuff();


    /**
     * Setup all the blocks in the structure
     */
    public abstract void setupStructure();

    /**
     * Reset method to be run when the master is gone or tells them to
     */
    public void reset() {
        masterX = 0;
        masterY = 0;
        masterZ = 0;
        hasMaster = false;
    }

    /**
     * Check that the master exists
     */
    public boolean checkForMaster() {
        TileEntity tile = worldObj.getTileEntity(masterX, masterY, masterZ);
        return (tile != null && (tile instanceof TileMultiBlock));
    }

    /**
     * Reset all the parts of the structure
     */
    public abstract void resetStructure();

    @Override
    public void writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setInteger("masterX", masterX);
        data.setInteger("masterY", masterY);
        data.setInteger("masterZ", masterZ);
        data.setBoolean("hasMaster", hasMaster);
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        masterX = data.getInteger("masterX");
        masterY = data.getInteger("masterY");
        masterZ = data.getInteger("masterZ");
        hasMaster = data.getBoolean("hasMaster");
    }

    public boolean hasMaster() {
        return hasMaster;
    }


    public int getMasterX() {
        return masterX;
    }

    public int getMasterY() {
        return masterY;
    }

    public int getMasterZ() {
        return masterZ;
    }

    public void setHasMaster(boolean bool) {
        hasMaster = bool;
    }

    public void setMasterCoords(int x, int y, int z) {
        masterX = x;
        masterY = y;
        masterZ = z;
    }
}

