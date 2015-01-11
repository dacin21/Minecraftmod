package com.dacin21.survivalmod.turbine.block;

import com.dacin21.survivalmod.survivalmod;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileTurbineFluidport extends TileEntity implements IMultiblockTurbineServ, IFluidHandler  {
	public int masterX, masterY = -1, masterZ;

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

	@Override
	public void setMasterCoords(int newX, int newY, int newZ){
		masterX = newX;
		masterY = newY;
		masterZ = newZ;
	}
	
	@Override
	public void updateEntity()
    {
		super.updateEntity();
		if(!this.worldObj.isRemote){
		}
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

	@Override
	public void initFromMaster(TileTurbineRotorbase masterTile){
		this.masterX = masterTile.xCoord;
		this.masterY = masterTile.yCoord;
		this.masterZ = masterTile.zCoord;
	}

	@Override
	public void unloadMaster(){
		masterX=masterY=masterZ=-1;
	}

	@Override
	public boolean hasMaster(){
		if(this.masterY == -1) return false;
		return getMaster()!=null;
	}
	
	@Override
	public void invalidate(){
		if(!this.worldObj.isRemote){
		}
		super.invalidate();
		if(hasMaster()){
			getMaster().sceduleMultiblockCheck();
		}
	}
	
	public void onChunkUnload(){
		if(!this.worldObj.isRemote){
		}
		super.onChunkUnload();
		if(hasMaster()){
			getMaster().sceduleMultiblockCheck();
		}
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(hasMaster() && getMaster().steamTank!= null){
			return getMaster().steamTank.fill(resource, doFill);
		}
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if(hasMaster() && getMaster().steamTank != null){
		//return fluid.getID() == getMaster().steamTank.getFluid().getFluid().getID();
			return fluid.getID() == survivalmod.steam.getID();
		}
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		if(hasMaster()){
			if(getMaster().steamTank!= null){
				return new FluidTankInfo[]{getMaster().steamTank.getInfo()};
			}
		}
		return null;
	}

	


}

