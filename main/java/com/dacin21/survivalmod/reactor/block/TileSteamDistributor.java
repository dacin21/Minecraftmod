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

public class TileSteamDistributor extends TileEntity implements ISidedInventory {

	public ForgeDirection directionIn, directionFillOut; // x+;x-;y+;y-;z+;z-;
	public int xOff, yOff, zOff;

	public TileSteamDistributor() {
		directionIn = directionFillOut = ForgeDirection.UP;
		xOff = yOff = zOff = 0;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (!this.worldObj.isRemote) {
			TileEntity steamInput = null;
			steamInput = this.worldObj.getTileEntity(xCoord + directionIn.offsetX, yCoord + directionIn.offsetY, zCoord + directionIn.offsetZ);

			if (steamInput == null) return;
			if (!(steamInput instanceof IFluidHandler)) return;
			TileEntity steamOutput = this.worldObj.getTileEntity(xCoord + xOff, yCoord + yOff, zCoord + zOff);
			if (steamOutput != null && steamOutput instanceof IFluidHandler) {
				if (((IFluidHandler) steamInput).drain(directionIn, 2000, false) != null) {
					((IFluidHandler) steamInput).drain(directionIn, ((IFluidHandler) steamOutput).fill(directionFillOut, ((IFluidHandler) steamInput).drain(directionIn, 2000, false), true), true);
				}
			}
		}
		getDescriptionPacket();
	}


	@Override
	public void invalidate() {
		super.invalidate();
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
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

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_)
	{
		super.readFromNBT(p_145839_1_);
		xOff = p_145839_1_.getInteger("xTurbineOffset");
		yOff = p_145839_1_.getInteger("yTurbineOffset");
		zOff = p_145839_1_.getInteger("zTurbineOffset");
		directionIn = ForgeDirection.getOrientation(p_145839_1_.getShort("steamInputDirection"));
		directionFillOut = ForgeDirection.getOrientation(p_145839_1_.getShort("steamFillOutputDirection"));
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_)
	{
		super.writeToNBT(p_145841_1_);
		p_145841_1_.setInteger("xTurbineOffset", xOff);
		p_145841_1_.setInteger("yTurbineOffset", yOff);
		p_145841_1_.setInteger("zTurbineOffset", zOff);
		p_145841_1_.setShort("steamInputDirection", (short) directionIn.ordinal());
		p_145841_1_.setShort("steamFillOutputDirection", (short) directionFillOut.ordinal());

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
		return "container.SteamDistributor";
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
		return new int[] {};
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
