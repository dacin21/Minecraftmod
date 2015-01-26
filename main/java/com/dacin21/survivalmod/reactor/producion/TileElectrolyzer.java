package com.dacin21.survivalmod.reactor.producion;

import com.dacin21.survivalmod.survivalmod;
import com.dacin21.survivalmod.reactor.reactor.TileFusionReactor2;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileElectrolyzer extends TileEntity implements IEnergySink, IFluidHandler{
	public FluidTank waterTank, hydrogenTank;
	private double energyBuffer;
	private boolean ic2Init;
	private static final double MaxEnergyBuffer = 1000.0F;
	
	public TileElectrolyzer(){
		super();
		ic2Init  = false;
		waterTank = new FluidTank(FluidRegistry.WATER, 0, 10000);
		hydrogenTank = new FluidTank(survivalmod.hydrogen, 0, 10000);
		energyBuffer = 0;
	}
	@Override
	public void updateEntity()
    {
		super.updateEntity();
		if(!this.worldObj.isRemote){
			if(!ic2Init){
				ic2Init = true;
				  MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			}
		}
		if(!this.worldObj.isRemote){
			if(energyBuffer > 16.0F){
				energyBuffer-=16.0F;
				if(waterTank.drain(survivalmod.PlasmaCount, false)!= null && waterTank.drain(survivalmod.PlasmaCount, false).amount == survivalmod.PlasmaCount){
					if(hydrogenTank.getCapacity() - hydrogenTank.getFluidAmount() >= survivalmod.PlasmaCount*2){
						hydrogenTank.fill(new FluidStack(survivalmod.hydrogen, survivalmod.PlasmaCount*2), true);
						waterTank.drain(survivalmod.PlasmaCount, true);
					}
					this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			}
			TileEntity tile = null;
			if(this.hydrogenTank.getFluidAmount() > 10){
				for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
					tile = this.worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
					if(tile!=null && tile instanceof TileNeutronizer){
						this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
						this.worldObj.markBlockForUpdate(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
						TileNeutronizer handler = (TileNeutronizer) tile;
						this.hydrogenTank.drain(handler.fill(dir.getOpposite(), this.hydrogenTank.drain(survivalmod.PlasmaCount*2*2, false), true),true);
					}
				}
			}
		}
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    	getDescriptionPacket();
    }
	
	@Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setInteger("waterAmount", waterTank.getFluidAmount());
        p_145841_1_.setInteger("hydroAmount", hydrogenTank.getFluidAmount());
        hydrogenTank.writeToNBT(p_145841_1_);
        p_145841_1_.setDouble("energy", energyBuffer);
    }

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        waterTank = new FluidTank(FluidRegistry.WATER, p_145839_1_.getInteger("waterAmount"), 10000);
        hydrogenTank = new FluidTank(survivalmod.hydrogen, p_145839_1_.getInteger("hydroAmount"), 10000);
        energyBuffer = p_145839_1_.getDouble("energy");
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
	
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord,
				this.zCoord) != this ? false : p_70300_1_.getDistanceSq(
				(double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D,
				(double) this.zCoord + 0.5D) <= 64.0D;
	}

	

	@Override
	public void invalidate(){
		if(!this.worldObj.isRemote){
			if(ic2Init){
				ic2Init = false;
				  MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			}
		}
		super.invalidate();
	}
	
	public void onChunkUnload(){
		if(!this.worldObj.isRemote){
			if(ic2Init){
				ic2Init = false;
				  MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			}
		}
		super.onChunkUnload();
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
		return true;
	}

	@Override
	public double getDemandedEnergy() {
		double val = this.MaxEnergyBuffer - this.energyBuffer;
		return val >= 32 ? val : 0;
	}

	@Override
	public int getSinkTier() {
		return 1;
	}

	@Override
	public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
		energyBuffer+=amount;
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		if(energyBuffer < MaxEnergyBuffer){
			return 0.0f;
		}
		if(energyBuffer < MaxEnergyBuffer + 15){
			energyBuffer = MaxEnergyBuffer;
			return 0.0f;
		}
		amount-= (MaxEnergyBuffer - energyBuffer);
		energyBuffer = MaxEnergyBuffer;
		return amount;
	}
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		return this.waterTank.fill(resource, doFill);
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
		return fluid.getID() == FluidRegistry.WATER.getID();
	}
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
	}
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[]{this.waterTank.getInfo()};
	}

}
