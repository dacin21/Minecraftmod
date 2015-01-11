package com.dacin21.survivalmod.turbine.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTank;

import com.dacin21.survivalmod.survivalmod;
import com.dacin21.survivalmod.turbine.rotor.TileTurbineRotor;

public class TileTurbineRotorbase extends TileEntity {
	public FluidTank steamTank;
	private boolean complete, burning;
	private static final double energyOutput = 100;
	public int steamConsumtion;
	public double energyBuffer;
	private int direction = -1;
	
	private boolean isUpdateSceduled = false;

	public TileTurbineRotorbase() {
		complete = false;
		steamConsumtion = 1;
		steamTank = new FluidTank(survivalmod.steam, 0, 1000000);
		energyBuffer = 0;
	}
	
	@Override
	public void updateEntity()
    {
		if(!this.worldObj.isRemote){
			if(isUpdateSceduled){
				this.runMasterUnload();
				checkForMultiBlock();
			}
			if(complete){
				burnFuel();
				if(burning){
					energyBuffer+=energyOutput;
				}
				if(energyBuffer > 10000) energyBuffer = 10000;
			}
	    }
    	getDescriptionPacket();
    }
	

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        this.steamConsumtion = p_145839_1_.getInteger("steamConsumtion");
        this.complete = p_145839_1_.getBoolean("complete");
        this.direction = p_145839_1_.getInteger("direction");

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
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setInteger("SteamConsumtion", this.steamConsumtion);
        p_145841_1_.setBoolean("Complete", complete);
        p_145841_1_.setInteger("direction", direction);

    }

	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord,
				this.zCoord) != this ? false : p_70300_1_.getDistanceSq(
				(double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D,
				(double) this.zCoord + 0.5D) <= 64.0D;
	}
	
	@Override
	public void invalidate(){
		burning = false;
		if(!this.worldObj.isRemote){
			this.runMasterUnload();
		}
		super.invalidate();
	}
	@Override
	public void onChunkUnload() {
		burning = false;
		if(!this.worldObj.isRemote){
			this.runMasterUnload();
		}
		super.onChunkUnload();
	}

	public void sceduleMultiblockCheck(){
		isUpdateSceduled = true;
	}
	
	public void checkForMultiBlock() {
		System.out.println("Checking for Turbine Multiblock");
		this.isUpdateSceduled = false;
		boolean oldComplete = complete;
		complete = false;
		int[] relPos;
		for(direction = 0; direction < 4; direction++){
			System.out.println("Checking Direction: " + direction);
			complete = true;
			for(int i = 0; i< 50 + 16*8; i++){
				if(i == 12) continue;
				relPos = getBlockPos(i);
				switch(direction){
					case 0:
						break;
					case 3:
						relPos[0]*=-1;
						relPos[2]*=-1;
						//-> now call case 1
					case 1:
						int tmp = relPos[0];
						relPos[0] = relPos[2];
						relPos[2] = -tmp;
						break;
					case 2:
						relPos[0]*=-1;
						relPos[2]*=-1;
						break;
				}
				if(!checkTurbineBlock(this.xCoord + relPos[0], this.yCoord + relPos[1], this.zCoord + relPos[2])){
					complete = false;
					System.out.println("Failindex: " + i);
					break;
				}
			}
			for(int i=1;i < 8+1; i++){
				boolean sucess = true;
				switch(direction){
					case 0:
						sucess&=this.checkBlock(survivalmod.turbineRotor, xCoord, yCoord, zCoord+i);
						break;
					case 1:
						sucess&=this.checkBlock(survivalmod.turbineRotor, xCoord+i, yCoord, zCoord);
						break;
					case 2:
						sucess&=this.checkBlock(survivalmod.turbineRotor, xCoord, yCoord, zCoord-i);
						break;
					case 3:
						sucess&=this.checkBlock(survivalmod.turbineRotor, xCoord-i, yCoord, zCoord);
						break;
				}
				if(!sucess){
					complete = false;
					System.out.println("Turbine at Slot: " + i + " was Missing");
					break;
				}
			}
			if(complete) break;
		}
		
		if(oldComplete!=complete){
			this.markDirty();
			if(complete){
				for(int i = 0; i< 50 + 16*8; i++){
					relPos = getBlockPos(i);
					switch(direction){
						case 0:
							break;
						case 3:
							relPos[0]*=-1;
							relPos[2]*=-1;
							//-> now call case 1
						case 1:
							int tmp = relPos[0];
							relPos[0] = relPos[2];
							relPos[2] = -tmp;
							break;
						case 2:
							relPos[0]*=-1;
							relPos[2]*=-1;
							break;
					}
					TileEntity servTile = this.worldObj.getTileEntity(this.xCoord + relPos[0], this.yCoord + relPos[1], this.zCoord + relPos[2]);
					if(servTile == null) continue;
					if(servTile instanceof IMultiblockTurbineServ){
						IMultiblockTurbineServ serv = (IMultiblockTurbineServ) servTile;
						serv.initFromMaster(this);
					}
					
				}
				for(int i=1;i < 8+1; i++){
					TileEntity tmpTile = null;
					switch(direction){
						case 0:
							tmpTile=this.worldObj.getTileEntity(xCoord, yCoord, zCoord+i);
							break;
						case 1:
							tmpTile=this.worldObj.getTileEntity(xCoord+i, yCoord, zCoord);
							break;
						case 2:
							tmpTile=this.worldObj.getTileEntity(xCoord, yCoord, zCoord-i);
							break;
						case 3:
							tmpTile=this.worldObj.getTileEntity(xCoord-i, yCoord, zCoord);
							break;
					}
					if(tmpTile!=null && tmpTile instanceof TileTurbineRotor){
						TileTurbineRotor turbine = (TileTurbineRotor) tmpTile;
						turbine.initFromMaster(this);
						turbine.setRotationAndPosition((direction+3)%4, i-1);
						turbine.getWorldObj().markBlockForUpdate(turbine.xCoord, turbine.yCoord, turbine.zCoord);
					}
				}
			} else {
				this.runMasterUnload();
			}
		}
	}
	
	private boolean checkTurbineBlock(int... absPos){
		if(checkBlock(survivalmod.turbineFrame, absPos)) return true;
		if(checkBlock(survivalmod.turbineGlass, absPos)) return true;
		if(checkBlock(survivalmod.turbinePowerport, absPos)) return true;
		System.out.println("Block failed at: ( " + absPos[0] + " / " + absPos[1] + " / " + absPos[2] + " ) with Block: " + getBlock(absPos).getLocalizedName() +"(not a turbine Block)");
		return false;
	}
	
	private int[] getBlockPos(int index){
		if(index < 0) return null;
		int[] pos = {0,0,0};
		if(index < 50){
			if(index >= 25){
				pos[2]+=9;
				index-=25;
			}
			pos[0]+= (index%5)-2;
			pos[1]+= (index/5)-2;
		} else if((index-=50) < 16 * 8) {
			pos[2]+=index>>4;
			pos[0] = 2;
			pos[1] = (index&3)-1;
			if((index&8)==0){
				pos[0]*=-1;
				pos[1]*=-1;
			}
			if((index&4) == 0){
				int tmp = pos[0];
				pos[0] = pos[1];
				pos[1] = -tmp;
			}
		} else {
			System.out.println("!?! Index Too Large: " + index+50);
			return null;
		}
		return pos;
	}
	
	private Block getBlock(int... absPos){
		return this.worldObj.getBlock(absPos[0], absPos[1], absPos[2]);
	}
	
	private boolean checkBlock(Block block,int... absPos){
		if(absPos == null) return false;
		if(absPos.length!= 3) return false;
		Block worldBlock = this.worldObj.getBlock(absPos[0], absPos[1], absPos[2]);
		if(worldBlock == null) return false;
		if(block == worldBlock){
			return true;
		}
		//System.out.println("Block failed at: ( " + absPos[0] + " / " + absPos[1] + " / " + absPos[2] + " ) with Block: " + worldBlock.getLocalizedName() + ", instead of requested: " + block.getLocalizedName());
		return false;
	}
	
	public void runMasterUnload(){
		int[] relPos;
		for(int i = 0; i< 50 + 16*8; i++){
			relPos = getBlockPos(i);
			switch(direction){
				case 0:
					break;
				case 3:
					relPos[0]*=-1;
					relPos[2]*=-1;
					//-> now call case 1
				case 1:
					int tmp = relPos[0];
					relPos[0] = relPos[2];
					relPos[2] = -tmp;
					break;
				case 2:
					relPos[0]*=-1;
					relPos[2]*=-1;
					break;
			}
			Block block = getBlock(this.xCoord + relPos[0], this.yCoord + relPos[1], this.zCoord + relPos[2]);
			if(block == null) continue;
			if(block instanceof IMultiblockTurbineServ){
				IMultiblockTurbineServ serv = (IMultiblockTurbineServ) block;
				serv.unloadMaster();
			}
			
		}
		for(int i=1;i < 8+1; i++){
			TileEntity tmpTile = null;
			switch(direction){
				case 0:
					tmpTile=this.worldObj.getTileEntity(xCoord, yCoord, zCoord+i);
					break;
				case 1:
					tmpTile=this.worldObj.getTileEntity(xCoord+i, yCoord, zCoord);
					break;
				case 2:
					tmpTile=this.worldObj.getTileEntity(xCoord, yCoord, zCoord-i);
					break;
				case 3:
					tmpTile=this.worldObj.getTileEntity(xCoord-i, yCoord, zCoord);
					break;
			}
			if(tmpTile!=null && tmpTile instanceof TileTurbineRotor){
				TileTurbineRotor turbine = (TileTurbineRotor) tmpTile;
				turbine.unloadMaster();
				turbine.getWorldObj().markBlockForUpdate(turbine.xCoord, turbine.yCoord, turbine.zCoord);
			}
		}
		this.burning = false;
		this.complete = false;
	}
	
	
	public boolean checkCompletion(){
		return complete;
	}
	
	public double getOutput(){
		return energyOutput;
	}
	
	private void burnFuel(){
		
		if (!this.worldObj.isRemote)
        {
				if(!this.useFuel()){
					//if(burning) checkForMultiBlock();
					burning = false;
				} else if (!burning){
					burning = true;
				}
			
        }
        this.worldObj.markBlockForUpdate(this.xCoord, yCoord, zCoord);
	}
	
	private boolean useFuel(){
		if(steamTank.getFluidAmount() < this.steamConsumtion) return false;
		steamTank.drain(this.steamConsumtion, true);
		return true;
	}
	
	
	

}

