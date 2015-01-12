package com.dacin21.survivalmod.reactor.producion;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;

import com.dacin21.survivalmod.survivalmod;
import com.dacin21.survivalmod.reactor.reactor.TileFusionReactor2;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileNeutronizer extends TileEntity implements IEnergySink,IFluidHandler {
	public FluidTank hTank, dTank, tTank;
	private double energyBuffer;
	private boolean ic2Init;
	private static final double MaxEnergyBuffer = 1000.0F;

	public TileNeutronizer() {
		super();
		ic2Init = false;
		hTank = new FluidTank(survivalmod.hydrogen, 0, 10000);
		dTank = new FluidTank(survivalmod.deuteriumPlasma, 0, 10000);
		tTank = new FluidTank(survivalmod.tritiumPlasma, 0, 10000);
	}

	public void updateEntity()
	{
		if (!this.worldObj.isRemote) {

			if(!ic2Init){
				ic2Init = true;
				  MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			}
			// produce
			if(energyBuffer > 16.0F){
				energyBuffer-=16.0F;
				if (hTank.getFluidAmount() > survivalmod.PlasmaCount*2) {
					if (dTank.fill(new FluidStack(survivalmod.deuteriumPlasma, survivalmod.PlasmaCount), true) == survivalmod.PlasmaCount &&
							tTank.fill(new FluidStack(survivalmod.tritiumPlasma, survivalmod.PlasmaCount), true) == survivalmod.PlasmaCount) {
						hTank.drain(survivalmod.PlasmaCount*2, true);
						dTank.fill(new FluidStack(survivalmod.deuteriumPlasma, survivalmod.PlasmaCount), true);
						tTank.fill(new FluidStack(survivalmod.tritiumPlasma, survivalmod.PlasmaCount), true);
						this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					}
	
				}
			}
			// try to move to rector
			int[] off = { 0, 0, 0, 0, 1, -1, 0, 0, 0, 0 };
			for (int i = 0; i < 6; i++) {

				TileEntity tile = this.worldObj.getTileEntity(xCoord + off[i], yCoord + off[i + 2], zCoord + off[i + 4]);
				if (tile != null && tile instanceof TileFusionReactor2) {
					TileFusionReactor2 reactor = (TileFusionReactor2) tile;
					if (dTank.drain(dTank.getCapacity(), false) != null) {
						dTank.drain(reactor.deutTank.fill(dTank.drain(dTank.getCapacity(), false), true), true);
						this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					}
					if (tTank.drain(tTank.getCapacity(), false) != null) {
						tTank.drain(reactor.triTank.fill(tTank.drain(tTank.getCapacity(), false), true), true);
						this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					}
					break;
				}
			}

		}
		getDescriptionPacket();
	}


	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_)
	{
		super.writeToNBT(p_145841_1_);
		p_145841_1_.setInteger("hTankAmount", hTank.getFluidAmount());
		p_145841_1_.setInteger("dTankAmount", dTank.getFluidAmount());
		p_145841_1_.setInteger("tTankAmount", tTank.getFluidAmount());
        p_145841_1_.setDouble("energy", energyBuffer);

	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_)
	{
		super.readFromNBT(p_145839_1_);
		hTank = new FluidTank(survivalmod.hydrogen, p_145839_1_.getInteger("hTankAmount"), 10000);
		dTank = new FluidTank(survivalmod.deuteriumPlasma, p_145839_1_.getInteger("dTankAmount"), 10000);
		tTank = new FluidTank(survivalmod.tritiumPlasma, p_145839_1_.getInteger("tTankAmount"), 10000);
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
		readFromNBT(packet.func_148857_g());
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
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if (resource.getFluid().equals(survivalmod.hydrogen)) {
			this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			return hTank.fill(resource, doFill);
		}
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		if (from.equals(ForgeDirection.DOWN)) {
			if (resource.isFluidEqual(tTank.getFluid())) {
				return tTank.drain(resource.amount, doDrain);
			}
			return null;
		}
		if (resource.isFluidEqual(dTank.getFluid())) {
			return dTank.drain(resource.amount, doDrain);
		}
		return null;
	}


	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if (from.equals(ForgeDirection.DOWN)) {
			return tTank.drain(maxDrain, doDrain);
		}
		return dTank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return fluid.equals(survivalmod.hydrogen);
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		if (from.equals(ForgeDirection.DOWN)) {
			return fluid.equals(tTank.getFluid().getFluid());
		}
		return fluid.equals(dTank.getFluid().getFluid());
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		if (from.equals(ForgeDirection.DOWN)) {
			return new FluidTankInfo[] { hTank.getInfo(), tTank.getInfo() };
		}
		return new FluidTankInfo[] { hTank.getInfo(), tTank.getInfo() };
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

}
