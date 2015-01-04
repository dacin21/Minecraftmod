package com.dacin21.survivalmod.reactor.producion;

import com.dacin21.survivalmod.survivalmod;
import com.dacin21.survivalmod.reactor.reactor.TileFusionReactor2;

import net.minecraft.block.Block;
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

public class TileNeutronizer extends TileEntity implements IFluidHandler {
	public FluidTank hTank, dTank, tTank;

	public TileNeutronizer() {
		super();
		hTank = new FluidTank(survivalmod.hydrogen, 0, 10000);
		dTank = new FluidTank(survivalmod.deuteriumPlasma, 0, 10000);
		tTank = new FluidTank(survivalmod.tritiumPlasma, 0, 10000);
	}

	public void updateEntity()
	{
		if (!this.worldObj.isRemote) {
			//DEBUG:
			hTank.fill(new FluidStack(survivalmod.hydrogen, 100), true);
			// produce
			if (hTank.getFluidAmount() > 10) {
				if (dTank.fill(new FluidStack(survivalmod.deuteriumPlasma, 5), true) == 5 &&
						tTank.fill(new FluidStack(survivalmod.tritiumPlasma, 5), true) == 5) {
					hTank.drain(10, true);
					dTank.fill(new FluidStack(survivalmod.deuteriumPlasma, 5), true);
					tTank.fill(new FluidStack(survivalmod.tritiumPlasma, 5), true);
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
					}
					if (tTank.drain(tTank.getCapacity(), false) != null) {
						tTank.drain(reactor.triTank.fill(tTank.drain(tTank.getCapacity(), false), true), true);
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
		hTank.readFromNBT(p_145841_1_);
		dTank.readFromNBT(p_145841_1_);
		tTank.readFromNBT(p_145841_1_);

	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_)
	{
		super.readFromNBT(p_145839_1_);
		hTank.readFromNBT(p_145839_1_);
		dTank.readFromNBT(p_145839_1_);
		tTank.readFromNBT(p_145839_1_);

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
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if (resource.getFluid().equals(survivalmod.hydrogen)) {
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

}
