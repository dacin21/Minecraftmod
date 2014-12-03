package com.dacin21.survivalmod.reactor.block;

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
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

public class TileFusionReactor extends TileEntity implements ISidedInventory {
	private static final int[] blockpos = { 20, 20, 20, 20, 19, 19, 19, 19, 18,
			18, 17, 17, 16, 15, 14 };
	private final int[] slotsTop = {0,1};
	private final int[] slotsBot = {2,3};
	private final int[] slotsSide = {0,1};
	private ItemStack[] contents = new ItemStack[4];
	private boolean complete, burning;
	private int frameCounter;
	private int energyOutput;
	public int fuelBurnTime;
	private int maxFuelBurnTime;

	public TileFusionReactor() {
		complete = false;
		frameCounter = 0;
		energyOutput = 0;
		maxFuelBurnTime = 100;
	}
	
	@Override
	public void updateEntity()
    {
		if(!this.worldObj.isRemote){
			frameCounter++;
			/*System.out.print(TileNeutronBoiler.count);
			TileNeutronBoiler.count=0;*/
			if(complete){
				updateBoiler();
				burnFuel();
			}
	    }
    	getDescriptionPacket();
    }
	

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        this.energyOutput = p_145839_1_.getInteger("EnergyOut");
        this.fuelBurnTime = p_145839_1_.getInteger("FuelBurnTime");
        this.frameCounter = p_145839_1_.getShort("FrameCounter");
        this.complete = p_145839_1_.getBoolean("Complete");
        

        NBTTagList nbttaglist = p_145839_1_.getTagList("Items", 10);
        this.contents = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.contents.length)
            {
                this.contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setInteger("EnergyOut", this.energyOutput);
        p_145841_1_.setInteger("FuelBurnTime", this.fuelBurnTime);
        p_145841_1_.setShort("FrameCounter", (short)this.frameCounter);
        p_145841_1_.setBoolean("Complete", complete);
        
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.contents.length; ++i)
        {
            if (this.contents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.contents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        p_145841_1_.setTag("Items", nbttaglist);

    }
    
    public int getFuelTimeScaled(int scale){
    	return fuelBurnTime * scale / maxFuelBurnTime;
    }

	@Override
	public int getSizeInventory() {
		return contents.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return contents[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (this.contents[index] != null) {
			ItemStack itemstack;

			if (this.contents[index].stackSize <= count) {
				itemstack = this.contents[index];
				this.contents[index] = null;
				this.markDirty();
				return itemstack;
			} else {
				itemstack = this.contents[index].splitStack(count);

				if (this.contents[index].stackSize == 0) {
					this.contents[index] = null;
				}

				// this.markDirty();
				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		if (this.contents[index] != null) {
			ItemStack itemstack = this.contents[index];
			this.contents[index] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.contents[index] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}

		this.markDirty();

	}

	@Override
	public String getInventoryName() {
		return "container.Fusionreaktor";
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
	public void invalidate(){
		changeEnergyOutput(0);
		TileHeatExchanger tmpBoiler;
		for(int i = 0; i< 4;  i++){
			tmpBoiler=this.getBoiler(i);
			if(tmpBoiler!=null){
					tmpBoiler.reset();
			}
		}
		super.invalidate();
	}
	@Override
	public void onChunkUnload() {
		changeEnergyOutput(0);
		TileHeatExchanger tmpBoiler;
		for(int i = 0; i< 4;  i++){
			tmpBoiler=this.getBoiler(i);
			if(tmpBoiler!=null){
					tmpBoiler.reset();
			}
		}
		super.onChunkUnload();
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack p_94041_2_) {
		return slot == 0 ? true : (slot == 1 ? true: false);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		return p_94128_1_ == 0 ? slotsBot : (p_94128_1_ == 1 ? slotsTop
				: slotsSide);
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack item,
			int side) {
		return this.isItemValidForSlot(slot, item);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item, int side) {
		//if (side == 0)
			return (slot == slotsBot[0] || slot == slotsBot[1]);

		//return false;
	}

	public void checkForMultiBlock() {
		System.out.println("Checking for fusionReactor");
		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;
		Block machine = Block.getBlockFromItem(IC2Items.getItem("machine").getItem());
		Block machine2 = Block.getBlockFromItem(IC2Items.getItem("advancedMachine").getItem());
		Block magnetizer = Block.getBlockFromItem(IC2Items.getItem("magnetizer").getItem());
		Block air = Blocks.air;
		int meta = 0;
		int meta2 = 12;
		int meta3 = 9;
		boolean oldComplete = complete;
		complete=true;
		//layer 1
		for (int i = 0; i < blockpos.length; i++) {
			if(!complete) break;
			check( x + i, y , z + blockpos[i], machine, meta);
			check( x - i, y , z + blockpos[i], machine, meta);
			check( x - i, y , z - blockpos[i], machine, meta);
			check( x + i, y , z - blockpos[i], machine, meta);
			
			check( x + blockpos[i], y , z + i, machine, meta);
			check( x - blockpos[i], y , z + i, machine, meta);
			check( x - blockpos[i], y , z - i, machine, meta);
			check( x + blockpos[i], y , z - i, machine, meta);
		}
		
		
		//layer 2
		//System.out.println("Layer 2 Checking ---------------------------------");
		y++;
		for(int i = 0; i < blockpos.length - 1;i++){
			if(!complete) break;
			check( x + i, y , z + blockpos[i]-1, machine2, meta2);
			check( x - i, y , z + blockpos[i]-1, machine2, meta2);
			check( x - i, y , z - blockpos[i]+1, machine2, meta2);
			check( x + i, y , z - blockpos[i]+1, machine2, meta2);
		}
		for(int i=0;i < blockpos.length - 1; i++){
			if(!complete) break;
			check( x + blockpos[i]-1, y , z + i, machine2, meta2);
			check( x - blockpos[i]+1, y , z + i, machine2, meta2);
			check( x - blockpos[i]+1, y , z - i, machine2, meta2);
			check( x + blockpos[i]-1, y , z - i, machine2, meta2);
		}
		
		//air ring
		for (int i = 0; i < blockpos.length; i++) {
			if(!complete) break;
			check( x + i, y , z + blockpos[i], air);
			check( x - i, y , z + blockpos[i], air);
			check( x - i, y , z - blockpos[i], air);
			check( x + i, y , z - blockpos[i], air);
			
			check( x + blockpos[i], y , z + i, air);
			check( x - blockpos[i], y , z + i, air);
			check( x - blockpos[i], y , z - i, air);
			check( x + blockpos[i], y , z - i, air);
		}
		//boilers
		for(int i=0;i<blockpos.length;i++){
			if(!complete) break;
			check(x + i, y, z + blockpos[i]+ 1, survivalmod.neutronBoiler);
			check(x - i, y, z + blockpos[i]+ 1, survivalmod.neutronBoiler);
			check(x - i, y, z - blockpos[i]- 1, survivalmod.neutronBoiler);
			check(x + i, y, z - blockpos[i]- 1, survivalmod.neutronBoiler);
			check(x + blockpos[i]+1, y, z + i, survivalmod.neutronBoiler);
			check(x - blockpos[i]-1, y, z + i, survivalmod.neutronBoiler);
			check(x - blockpos[i]-1, y, z - i, survivalmod.neutronBoiler);
			check(x + blockpos[i]+1, y, z - i, survivalmod.neutronBoiler);
		}

		//HeatExchangers TODO: finish
		check(x + blockpos[0] + 2 , y , z , survivalmod.heatExchanger);
		check(x - blockpos[0] - 2 , y , z , survivalmod.heatExchanger);
		check(x , y , z + blockpos[0] + 2 , survivalmod.heatExchanger);
		check(x , y , z - blockpos[0] - 2 , survivalmod.heatExchanger);
		//Layer 3
		//System.out.println("Layer 3 Checking ---------------------------------");
		y++;
		for (int i = 0; i < blockpos.length; i++) {
			if(!complete) break;
			check( x + i, y , z + blockpos[i], magnetizer, meta3);
			check( x - i, y , z + blockpos[i], magnetizer, meta3);
			check( x - i, y , z - blockpos[i], magnetizer, meta3);
			check( x + i, y , z - blockpos[i], magnetizer, meta3);
			
			check( x + blockpos[i], y , z + i, magnetizer, meta3);
			check( x - blockpos[i], y , z + i, magnetizer, meta3);
			check( x - blockpos[i], y , z - i, magnetizer, meta3);
			check( x + blockpos[i], y , z - i, magnetizer, meta3);
		}
		y-=2;
		if(oldComplete!=complete){
			TileHeatExchanger tmpBoiler;
			for(int i = 0; i< 4;  i++){
				tmpBoiler=this.getBoiler(i);
				if(tmpBoiler!=null){
					if(complete){
						tmpBoiler.setMasterCoords(this.xCoord, this.yCoord, this.zCoord);
						tmpBoiler.setupStructure();
					}else
						tmpBoiler.reset();
				} 
			}
		}
	}


	private void check(int x, int y, int z, Block block, int metadata) {
		if(!complete) return;
		/*if(!this.worldObj.isRemote)
			System.out.println("Block checking at: ( " + x + " / " + y + " / " + z + " ) for: " +  block.toString());
		*/
		Block tmpBlock = this.worldObj.getBlock(x, y, z);
		int tmpMeta = this.worldObj.getBlockMetadata(x, y, z);
		complete = tmpBlock == block && tmpMeta == metadata;
		if(!complete) System.out.println("Block failed at: ( " + x + " / " + y + " / " + z + " )");
	}

	private void check(int x, int y, int z, Block block) {
		if(!complete) return;
		/*if(!this.worldObj.isRemote)
			System.out.println("Block checking at: ( " + x + " / " + y + " / " + z + " ) for: " +  block.toString());
		*/
		Block tmpBlock = this.worldObj.getBlock(x, y, z);
		complete = tmpBlock == block;
		if(!complete) System.out.println("Block failed at: ( " + x + " / " + y + " / " + z + " )");
	}
	
	private void updateBoiler(){
		if(this.frameCounter < 127) return;
			checkForMultiBlock();
			frameCounter = 0;
	}
	
	private TileHeatExchanger getBoiler(int index){
		TileEntity boiler;
		switch(index){
		case 0:
			boiler = this.worldObj.getTileEntity(xCoord + 22, yCoord + 1, zCoord);
			break;
		case 1:
			boiler = this.worldObj.getTileEntity(xCoord - 22, yCoord + 1, zCoord);
			break;
		case 2:
			boiler = this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord + 22);
			break;
		case 3:
			boiler = this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord - 22);
			break;
		default:
			boiler = null;
		
		}
			

		if(boiler != null && boiler instanceof TileHeatExchanger) return (TileHeatExchanger)boiler;
		return null;
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
			if(this.fuelBurnTime == 0){
				if(!this.useNewFuel()){
					if(burning) checkForMultiBlock();
					burning = false;
					changeEnergyOutput(0);
				} else if (!burning){
					burning = true;
					checkForMultiBlock();
				}
			} else{
				this.fuelBurnTime--;
			}
        }
        this.worldObj.markBlockForUpdate(this.xCoord, yCoord, zCoord);
	}
	
	private boolean useNewFuel(){
		if(contents[0] == null) return false;
		if(contents[1] == null) return false;
		ResultFusionReactor result = RecipesFusionReactor.fusionBase.getFusionReactorResult(new ResultFusionReactor(contents[0], contents[1], -1));
		if(result == null) return false;
		if(contents[2]!= null){
			if(!result.getFirstStack().isItemEqual(contents[2])) return false;
			if(result.getFirstStack().stackSize + contents[2].stackSize > contents[2].getMaxStackSize()) return false;
		}
		if(contents[3] != null){
			if(!result.getSecondStack().isItemEqual(contents[3])) return false;
			if(result.getSecondStack().stackSize + contents[3].stackSize > contents[3].getMaxStackSize()) return false;
		}
		--contents[0].stackSize;
		--contents[1].stackSize;
		if(contents[0].stackSize <= 0) contents[0] = null;
		if(contents[1].stackSize <= 0) contents[1] = null;
		if(contents[2] == null) contents[2] = result.getFirstStack().copy();
		else contents[2].stackSize+=result.getFirstStack().stackSize;
		if(contents[3] == null) contents[3] = result.getSecondStack().copy();
		else contents[3].stackSize+=result.getSecondStack().stackSize;
		
		this.fuelBurnTime = this.maxFuelBurnTime;
		changeEnergyOutput(result.getEnergyOutput());
		return true;
	}
	
	private void changeEnergyOutput(int newOutput){
		if(this.energyOutput != newOutput){
			this.energyOutput = newOutput;

			TileHeatExchanger tmpBoiler;
			for(int i = 0; i< 4;  i++){
				tmpBoiler=this.getBoiler(i);
				if(tmpBoiler == null) continue;
				tmpBoiler.updateFromMaster();
			}
				
		}
	}
	
	

}
