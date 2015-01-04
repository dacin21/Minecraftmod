package com.dacin21.survivalmod.reactor.reactor;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileReactorWall extends TileEntity {
	public int masterX, masterY = -1, masterZ;
	
	
	
	
	public TileFusionReactor2 getMaster(){
		TileEntity entity = this.worldObj.getTileEntity(masterX, masterY, masterZ);
		if(entity!= null && entity instanceof TileFusionReactor2){
			return (TileFusionReactor2) entity;
		}
		return null;
	}
	
	public int[] getMasterCoords(){
		return new int[]{masterX, masterY, masterZ};
	}
	
	public void setMasterCoords(int newX, int newY, int newZ){
		masterX = newX;
		masterY = newY;
		masterZ = newZ;
	}
	
	@Override
	public void updateEntity()
    {
		super.updateEntity();
    	getDescriptionPacket();
    }
	
	@Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setInteger("masterX", masterX);
        p_145841_1_.setInteger("masterY", masterY);
        p_145841_1_.setInteger("masterZ", masterZ);
    }

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        masterX = p_145839_1_.getInteger("masterX");
        masterY = p_145839_1_.getInteger("masterY");
        masterZ = p_145839_1_.getInteger("masterZ");
    }
	
	@Override
	public Packet getDescriptionPacket()
	{
	 NBTTagCompound var1 = new NBTTagCompound();
	 this.writeToNBT(var1);
	 return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 2, var1);
	}
		
	@Override
	public void onDataPacket(NetworkManager netManager, S35PacketUpdateTileEntity packet)
	{
	 readFromNBT(packet.func_148857_g() );
	}
	
	public void initFromMaster(TileFusionReactor2 masterTile){
		this.masterX = masterTile.xCoord;
		this.masterY = masterTile.yCoord;
		this.masterZ = masterTile.zCoord;
	}
	
	public void unloadMaster(){
		masterX=masterY=masterZ=-1;
	}
	
	public boolean hasMaster(){
		return this.masterY != -1;
	}
	
	@Override
	public void invalidate(){
		super.invalidate();
		if(hasMaster()){
			getMaster().checkForMultiBlock();
		}
	}

}
