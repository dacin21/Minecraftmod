package com.dacin21.survivalmod.reactor.reactor;


import com.dacin21.survivalmod.survivalmod;

import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.item.IC2Items;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileFusionReactor2 extends TileEntity {
	private static final int[][] blockpos={{0,16},{0,15},{0,14},
		{1,16},{1,15},{1,14},
		{2,16},{2,15},{2,14},
		{3,16},{3,15},{3,14},
		{4,15},{4,14},{4,13},
		{5,15},{5,14},{5,13},
		{6,15},{6,14},{6,13},
		{7,14},{7,13},{7,12},
		{8,14},{8,13},{8,12},{8,11},
		{9,13},{9,12},{9,11},
		{10,13},{10,12},{10,11},{10,10},
		{11,12},{11,11},{11,10},{11,9},{11,8},
		{12,11},{12,10},{12,9},{12,8},{12,7},
		{13,10},{13,9},{13,8},{13,7},{13,6},{13,5},{13,4},
		{14,8},{14,7},{14,6},{14,5},{14,4},{14,3},{14,2},{14,1},
		{15,6},{15,5},{15,4},{15,3},{15,2},{15,1},
		{16,3},{16,2},{16,1}};
	private static final int[][] blockpos2 ={
		{0,17},{0,13},{1,17},{1,13},{2,17},{2,13},{3,17},{3,13},
		{4,17},{4,16},{4,12},
		{5,16},{5,12},{6,16},{6,12},{6,11},
		{7,16},{7,15},{7,11},{8,15},{8,10},
		{9,14},{9,10},{9,9},
		{10,14},{10,9},{10,8},
		{11,13},{11,7},{11,6},
		{12,12},{12,6},{12,5},{12,4},
		{13,11},{13,3},{13,2},{13,1},
		{14,10},{14,9},{15,8},{15,7},
		{16,7},{16,6},{16,5},{16,4},
		{17,4},{17,3},{17,2},{17,1}};
	private static final int[][] steampos = {
		{0,18},{1,18},{2,18},{3,18},{4,18},
		{5,17},{6,17},{7,17},
		{8,16},{9,16},{9,15},{10,15},
		{11,14},{12,13},{13,13},{13,12},{14,11},
		{15,10},{15,9},{16,9},{16,8},
		{17,7},{17,6},{17,5},
		{18,4},{18,3},{18,2},{18,1}
	};
	public FluidTank deutTank, triTank, steamTank;
	private boolean complete, burning;
	private int energyOutput;
	public int fuelBurnupRate;

	public TileFusionReactor2() {
		complete = false;
		energyOutput = 0;
		fuelBurnupRate = 1;
		deutTank = new FluidTank(survivalmod.deuteriumPlasma, 0, 10000);
		triTank = new FluidTank(survivalmod.tritiumPlasma, 0, 10000);
		steamTank = new FluidTank(survivalmod.steam, 0, 1000000);
	}
	
	@Override
	public void updateEntity()
    {
		if(!this.worldObj.isRemote){
			/*System.out.print(TileNeutronBoiler.count);
			TileNeutronBoiler.count=0;*/
			if(complete){
				burnFuel();
				this.steamTank.fill(new FluidStack(survivalmod.steam,this.energyOutput), true);
				//this.steamTank.fill(new FluidStack(survivalmod.steam,1000), true);
			}
	    }
    	getDescriptionPacket();
    }
	
	public int getRelTri(){
		return this.triTank.getFluidAmount() * 100 / this.triTank.getCapacity();
	}
	public int getRelDeu(){
		return this.deutTank.getFluidAmount() * 100 / this.deutTank.getCapacity();
	}
	

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        this.energyOutput = p_145839_1_.getInteger("EnergyOut");
        this.fuelBurnupRate = p_145839_1_.getInteger("FuelBurnTime");
        this.complete = p_145839_1_.getBoolean("Complete");
        this.deutTank = new FluidTank(survivalmod.deuteriumPlasma, p_145839_1_.getInteger("DeutTankContent"), 10000);
        this.triTank = new FluidTank(survivalmod.tritiumPlasma, p_145839_1_.getInteger("TriTankContent"), 10000);

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
        p_145841_1_.setInteger("EnergyOut", this.energyOutput);
        p_145841_1_.setInteger("FuelBurnTime", this.fuelBurnupRate);
        p_145841_1_.setBoolean("Complete", complete);
        p_145841_1_.setInteger("DeutTankContent", this.deutTank.getFluidAmount());
        p_145841_1_.setInteger("TriTankContent", this.triTank.getFluidAmount());

    }

	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord,
				this.zCoord) != this ? false : p_70300_1_.getDistanceSq(
				(double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D,
				(double) this.zCoord + 0.5D) <= 64.0D;
	}
	
	@Override
	public void invalidate(){
		changeEnergyOutput(0);
		this.runMasterUnload();
		super.invalidate();
	}
	@Override
	public void onChunkUnload() {
		changeEnergyOutput(0);
		this.runMasterUnload();
		super.onChunkUnload();
	}


	public void checkForMultiBlock() {
		System.out.println("Checking for fusionReactor");
		boolean oldComplete = complete;
		complete = true;
		for(int i=0;i<69*4*2 + 3*200;i++){
			if(!checkBlock(i, survivalmod.reactorWall, -1)){
				complete = false;
				break;
			}
		}
		if(complete){
			for(int i = 69*4*2 + 3*200; i< 69*4*2+3*200 + 112 ; i++){
				if(!checkBlock(i, survivalmod.steamPipe,-1)){
					complete = false;
					break;
				}
			}
		}
		
		
		if(oldComplete!=complete){
			if(complete){
				TileReactorWall wall = null;
				for(int i=0;i < 69*4*2 + 3*200; i++){
					wall = getWallTile(i);
					if(wall!= null){
						wall.initFromMaster(this);
					}
				}
				TileSteamPipe pipe = null;
				for(int i=0;i<112;i++){
					pipe = getSteamTile(i);
						if(pipe!= null){
							pipe.initFromMaster(this);
						}
				}
			} else {
				this.runMasterUnload();
			}
		}
	}
	
	private int[] wallPos(int index){
		if(index < 0) return null;
		int x, y,z;
		y=x=z=0;
		y+=this.yCoord;
		x+=this.xCoord;
		z+=this.zCoord;
		//layer 0 and 4
		if(index<69*4*2){
			if(index>=69*4){
				y+=4;
				index-=69*4;
			}
			switch(index/69){
			case 0:
				x+=blockpos[index%69][0];
				z+=blockpos[index%69][1];
				break;
			case 1:
				z-=blockpos[index%69][0];
				x+=blockpos[index%69][1];
				break;
			case 2:
				x-=blockpos[index%69][0];
				z-=blockpos[index%69][1];
				break;
			case 3:
				z+=blockpos[index%69][0];
				x-=blockpos[index%69][1];
				break;
				
			}
			
		} else if(index < 200*3 + 69*4*2){
			index-= 69*4*2;
			//layers 1-3
			y++;
			while(index>=200){
				index-=200;
				y++;
			}
			switch(index/50){
			case 0:
				x+=blockpos2[index%50][0];
				z+=blockpos2[index%50][1];
				break;
			case 1:
				z-=blockpos2[index%50][0];
				x+=blockpos2[index%50][1];
				break;
			case 2:
				x-=blockpos2[index%50][0];
				z-=blockpos2[index%50][1];
				break;
			case 3:
				z+=blockpos2[index%50][0];
				x-=blockpos2[index%50][1];
				break;
			}
		} else{
			index-= 3*200 + 69*4*2;
			System.out.print("Index leftover: " + index);
			return null;
		}
		
		return new int[]{x,y,z};
	}


	
	private BlockReactorWall getWall(int index){
		int[] pos = wallPos(index);
		if(pos == null) return null;
		
		Block wall = this.worldObj.getBlock(pos[0], pos[1], pos[2]);
		if(wall != null && wall instanceof BlockReactorWall){
			return (BlockReactorWall)wall;
		}
		//System.out.print("Wall not Found at[" + x + " , " + y + " , " + z + " ]");
		return null;
	}
	
	private TileReactorWall getWallTile(int index){
		int[] pos = wallPos(index);
		if(pos == null) return null;
		
		TileEntity wall = this.worldObj.getTileEntity(pos[0], pos[1], pos[2]);
		if(wall != null && wall instanceof TileReactorWall){
			return (TileReactorWall)wall;
		}
		//System.out.print("Wall not Found at[" + x + " , " + y + " , " + z + " ]");
		return null;
	}
	
	private int[] getSteamPos(int index){
		int x, y,z;
		y=x=z=0;
		y+=this.yCoord;
		x+=this.xCoord;
		z+=this.zCoord;
		switch(index/28){
			case 0:
				x+= steampos[index%28][0];
				z+= steampos[index%28][1];
				break;
			case 1:
				z-= steampos[index%28][0];
				x+= steampos[index%28][1];
				break;
			case 2:
				x-= steampos[index%28][0];
				z-= steampos[index%28][1];
				break;
			case 3:
				z+= steampos[index%28][0];
				x-= steampos[index%28][1];
				break;
			default:
				System.err.println("!!!invalid index after division!!!");
		}
		y+=2;
		return new int[]{x,y,z};
	}
	
	private TileSteamPipe getSteamTile(int index){
		int[] pos = getSteamPos(index);
		if(pos == null) return null;
		
		TileEntity steamP = this.worldObj.getTileEntity(pos[0], pos[1], pos[2]);
		if(steamP != null && steamP instanceof TileSteamPipe){
			return (TileSteamPipe)steamP;
		}
		//System.out.print("Steam Pipe not Found at[" + x + " , " + y + " , " + z + " ]");
		return null;
	}
	
	private boolean checkBlock(int index, Block block, int metadata){
		if(index < 0) return false;
		int[] pos;
		if(index < 3*200 + 69*4*2){
			pos = wallPos(index);
		} else {
			pos = getSteamPos(index - (3*200 + 69*4*2));
		}
		Block worldBlock = this.worldObj.getBlock(pos[0], pos[1], pos[2]);
		if(worldBlock == null) return false;
		if(metadata==-1){
			if(block == worldBlock){
				return true;
			}
		}else if(metadata == this.worldObj.getBlockMetadata(pos[0], pos[1], pos[2]) && block == worldBlock){
			return true;
		}
		System.out.println("Block failed at: ( " + pos[0] + " / " + pos[1] + " / " + pos[2] + " ) with index: " + index);
		return false;
	}
	
	public void runMasterUnload(){
		TileReactorWall wall = null;
		for(int i=0;i < 69*4*2 + 3*200; i++){
			wall = getWallTile(i);
			if(wall!= null){
				wall.unloadMaster();;
			}
		}
		TileSteamPipe pipe = null;
		for(int i = 0;i < 112; i++){
			pipe = getSteamTile(i);
			if(pipe!=null){
				pipe.unloadMaster();
			}
		}
		this.burning = false;
		this.complete = false;
		this.energyOutput = 0;
	}
	
	
	public boolean checkCompletion(){
		return complete;
	}
	
	public int getOutput(){
		return energyOutput;
	}
	
	private void burnFuel(){
		
		if (!this.worldObj.isRemote)
        {
				if(!this.useFuel()){
					//if(burning) checkForMultiBlock();
					burning = false;
					changeEnergyOutput(0);
				} else if (!burning){
					burning = true;
					//checkForMultiBlock();
				}
			
        }
        this.worldObj.markBlockForUpdate(this.xCoord, yCoord, zCoord);
	}
	
	private boolean useFuel(){
		if(triTank.getFluidAmount() < this.fuelBurnupRate) return false;
		if(deutTank.getFluidAmount() < this.fuelBurnupRate) return false;
		triTank.drain(this.fuelBurnupRate, true);
		deutTank.drain(this.fuelBurnupRate, true);
		changeEnergyOutput(1000);
		return true;
	}
	
	private void changeEnergyOutput(int newOutput){
		if(this.energyOutput != newOutput){
			this.energyOutput = newOutput;

			/*TileHeatExchanger tmpBoiler;
			for(int i = 0; i< 4;  i++){
				tmpBoiler=this.getBoiler(i);
				if(tmpBoiler == null) continue;
				tmpBoiler.updateFromMaster();
			}*/
				
		}
	}
	
	

}
