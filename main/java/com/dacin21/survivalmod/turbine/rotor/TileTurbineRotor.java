package com.dacin21.survivalmod.turbine.rotor;

import ic2.api.energy.event.EnergyTileLoadEvent;

import com.dacin21.survivalmod.turbine.block.IMultiblockTurbineServ;
import com.dacin21.survivalmod.turbine.block.TileTurbineRotorbase;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

public class TileTurbineRotor extends TileEntity implements IMultiblockTurbineServ {
	public int masterX, masterY = -1, masterZ;
	private static int posCount = 0;
	public int pos;
	public int direction;
	
	public TileTurbineRotor(){
		super();
		pos = direction = 0;
	}

	@Override
	public TileTurbineRotorbase getMaster(){
		TileEntity entity = this.worldObj.getTileEntity(masterX, masterY, masterZ);
		if(entity!= null && entity instanceof TileTurbineRotorbase){
			return (TileTurbineRotorbase) entity;
		}
		return null;
	}

	@Override
	public int[] getMasterCoords(){
		return new int[]{masterX, masterY, masterZ};
	}
	
	public void setMasterCoords(int newX, int newY, int newZ){
		masterX = newX;
		masterY = newY;
		masterZ = newZ;
	}

	@Override
	public void initFromMaster(TileTurbineRotorbase masterTile){
		this.masterX = masterTile.xCoord;
		this.masterY = masterTile.yCoord;
		this.masterZ = masterTile.zCoord;
	}
	
	public void setRotationAndPosition(int rotation, int position){
		this.pos = position;
		this.direction = rotation;
	}

	@Override
	public void unloadMaster(){
		masterX=masterY=masterZ=-1;
		direction = pos = 0;
	}

	@Override
	public boolean hasMaster(){
		return this.masterY != -1;
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
        p_145841_1_.setInteger("rotorDirection", direction);
        p_145841_1_.setInteger("rotorPos", pos);
    }

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        masterX = p_145839_1_.getInteger("masterX");
        masterY = p_145839_1_.getInteger("masterY");
        masterZ = p_145839_1_.getInteger("masterZ");
        direction = p_145839_1_.getInteger("rotorDirection");
        pos = p_145839_1_.getInteger("rotorPos");
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
}
