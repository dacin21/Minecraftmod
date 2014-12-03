package com.dacin21.survivalmod.reactor.block;

import com.dacin21.survivalmod.survivalmod;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileHeatExchanger extends TileMultiBlock implements /*IEnergySource,*/ IFluidHandler,ISidedInventory {

	private boolean icInitialized = false;
	public static int count = 0;
	private int energyOut;
	 protected FluidTank tank ;

	public TileHeatExchanger() {
		energyOut = 0;
		tank = new FluidTank(survivalmod.steam, 0, FluidContainerRegistry.BUCKET_VOLUME*1000);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if(!this.worldObj.isRemote){
			if(this.checkForMaster()) count++;
			if(tank != null && energyOut != 0) tank.fill(new FluidStack(survivalmod.steam, energyOut),true ) ;
			/*if (!icInitialized) {
				MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
				icInitialized = true;
			}*/
		}
	}

	@Override
	public void doMultiBlockStuff() {
	}

	@Override
	public void setupStructure() {
		hasMaster = true;
		this.updateFromMaster();

	}
	public void reset(){
		super.reset();
		this.energyOut = 0;
	}

	@Override
	public void resetStructure() {
	}

	@Override
	public boolean checkForMaster() {
		TileEntity tile = worldObj.getTileEntity(masterX, masterY, masterZ);
		return (tile != null && (tile instanceof TileFusionReactor));
	}

	public void updateFromMaster() {
		TileFusionReactor master = (TileFusionReactor) this.worldObj.getTileEntity(masterX, masterY, masterZ);
		if(master==null){
			System.err.println("master not initializer x:" + masterX + ", y: " + masterY + ", z: " + masterZ + "this: x: " + xCoord + ", y: "+ yCoord + ", z: "+ zCoord );
			return;
		}
		this.energyOut = master.getOutput();

	}

	@Override
	public void invalidate() {
		/*if(!this.worldObj.isRemote){
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			icInitialized = false;
		}*/
		super.invalidate();
	}

	@Override
	public void onChunkUnload() {/*
		if(!this.worldObj.isRemote){
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			icInitialized = false;
		}*/
		super.onChunkUnload();
	}
/*
	@Override
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
		return true;
	}

	@Override
	public double getOfferedEnergy() {
		return energyOut;
	}

	@Override
	public void drawEnergy(double amount) {
	}

	@Override
	public int getSourceTier() {
		return 2;
	}
	*/

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
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        tank.readFromNBT(p_145839_1_);
        this.energyOut = p_145839_1_.getInteger("EnergyOut");
    }

    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        tank.writeToNBT(p_145841_1_);
        p_145841_1_.setInteger("EnergyOut", this.energyOut);

    }
    
    //------------- Steam Container -------------------------

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		if(tank == null) return null;
		if(resource.fluidID != tank.getFluid().fluidID)  return null;
		return tank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if(tank == null) return null;
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		if(fluid == null) return false;
		return fluid.getID() == tank.getFluid().fluidID;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { tank.getInfo() };
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return null;
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		
	}

	@Override
	public String getInventoryName() {
		return "container.HeatExchanger";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord,
				this.zCoord) != this ? false : p_70300_1_.getDistanceSq(
				(double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D,
				(double) this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {
		
	}

	@Override
	public void closeInventory() {
		
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		return new int[]{};
	}

	@Override
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_,
			int p_102007_3_) {
		return false;
	}

	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_,
			int p_102008_3_) {
		return false;
	}
	

	

}
