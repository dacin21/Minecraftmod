package com.dacin21.survivalmod.reactor.producion;

import com.dacin21.survivalmod.survivalmod;
import com.dacin21.survivalmod.reactor.reactor.TileFusionReactor2;

import ic2.api.energy.tile.IEnergySink;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileElectrolyzer extends TileEntity implements IEnergySink{
	public FluidTank waterTank, hydrogenTank;
	private int energyBuffer;
	private static final int MaxEnergyBuffer = 10000;
	
	public TileElectrolyzer(){
		super();
		waterTank = new FluidTank(FluidRegistry.WATER, 0, 10000);
		hydrogenTank = new FluidTank(survivalmod.hydrogen, 0, 10000);
		energyBuffer = 0;
	}
	@Override
	public void updateEntity()
    {
		super.updateEntity();
		if(!this.worldObj.isRemote){
			if(waterTank.drain(10, false).amount == 10){
				if(hydrogenTank.getCapacity() - hydrogenTank.getFluidAmount() >= 20){
					hydrogenTank.fill(new FluidStack(survivalmod.hydrogen, 20), true);
					waterTank.drain(10, true);
				}
			}
		}
    	getDescriptionPacket();
    }
	
	@Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        waterTank.writeToNBT(p_145841_1_);
        hydrogenTank.writeToNBT(p_145841_1_);
        p_145841_1_.setInteger("energy", energyBuffer);
    }

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        waterTank.readFromNBT(p_145839_1_);
        hydrogenTank.readFromNBT(p_145839_1_);
        energyBuffer = p_145839_1_.getInteger("energy");
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
	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
		return true;
	}

	@Override
	public double getDemandedEnergy() {
		int val = this.MaxEnergyBuffer - this.energyBuffer;
		return val >= 32 ? val : 0;
	}

	@Override
	public int getSinkTier() {
		return 0;
	}

	@Override
	public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
		energyBuffer+=amount;
		if(energyBuffer < MaxEnergyBuffer){
			return 0.0f;
		}
		if(energyBuffer < MaxEnergyBuffer + 32){
			energyBuffer = MaxEnergyBuffer;
			return 0.0f;
		}
		amount-= (MaxEnergyBuffer - energyBuffer);
		energyBuffer = MaxEnergyBuffer;
		return amount;
	}

}
