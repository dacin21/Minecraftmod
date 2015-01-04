package com.dacin21.survivalmod.reactor.reactor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileSteamPipe extends TileEntity implements IFluidHandler {
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
	
	
	
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		if(hasMaster()){
			return getMaster().steamTank.drain(resource.amount, doDrain);
		}
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if(hasMaster()){
			return getMaster().steamTank.drain(maxDrain, doDrain);
		}
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return hasMaster();
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		if(hasMaster()){
			TileFusionReactor2 master = getMaster();
			if(master!= null){
			return new FluidTankInfo[] { master.steamTank.getInfo() };
			}
		}
		return null;
	}

}
