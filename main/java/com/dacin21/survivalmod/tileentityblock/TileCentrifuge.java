package com.dacin21.survivalmod.tileentityblock;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.item.IC2Items;

import java.util.Iterator;
import java.util.List;

import com.dacin21.survivalmod.reactor.block.RecipesFusionReactor;
import com.dacin21.survivalmod.tileentityblock.recipe.RecipesCentrifuge;
import com.dacin21.survivalmod.tileentityblock.recipe.ResultCentrifuge;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class TileCentrifuge extends TileEntity implements IInventory, ISidedInventory, IEnergySink {

    private static final int[] slotsTop = new int[] {0};
    private static final int[] slotsBottom = new int[] {2, 3, 4};
    private static final int[] slotsSides = new int[] {1};
    private static final float energyConsumtion = 2;
    private static final int maxCookTime = 400;
    private float maxEnergyBuffer = 10000;
    private int overclockerCount, transformerCount, storageCount;
	private ItemStack[] chestContents = new ItemStack[8];

    /** The amount of energy stored in the furnace */
    public double EnergyBuffer;
    /** The number of ticks that the current item has been cooking for */
    public float furnaceCookTime;
    private String customName;
    private boolean icInitialized = false;

	public TileCentrifuge() {
		furnaceCookTime = 0.0f;
		EnergyBuffer = 0.0F;
		overclockerCount = transformerCount = storageCount;
		
	}

    /**
     * Returns the number of slots in the inventory.
     */
	@Override
	public int getSizeInventory() {
		return 8;
	}

    /**
     * Returns the stack in slot i
     */
	@Override
	public ItemStack getStackInSlot(int index) {
		return this.chestContents[index];
	}
	
	

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (this.chestContents[index] != null)
        {
            ItemStack itemstack;

            if (this.chestContents[index].stackSize <= count)
            {
                itemstack = this.chestContents[index];
                this.chestContents[index] = null;
                this.markDirty();
                return itemstack;
            }
            else
            {
                itemstack = this.chestContents[index].splitStack(count);

                if (this.chestContents[index].stackSize == 0)
                {
                    this.chestContents[index] = null;
                }

                //this.markDirty();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		if (this.chestContents[index] != null)
        {
            ItemStack itemstack = this.chestContents[index];
            this.chestContents[index] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.chestContents[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
		
	}

	@Override
	public String getInventoryName() {
		return "container.centrifuge";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
    /**
     * Returns an integer between 0 and the passed value representing how close the current item is to being completely
     * cooked
     */
    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int p_145953_1_)
    {
        return (int) (this.furnaceCookTime * p_145953_1_ / maxCookTime) ;
    }

    /**
     * Returns an integer between 0 and the passed value representing how much burn time is left on the current fuel
     * item, where 0 means that the item is exhausted and the passed value means that the item is fresh
     */
    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int p_145955_1_)
    {
        

        return (int) (this.EnergyBuffer * p_145955_1_ / this.maxEnergyBuffer);
    }

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : p_70300_1_.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}
	

	@Override
	public void openInventory() {
		
	}

	@Override
	public void closeInventory() {

	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return p_94041_1_ == 0 ? (RecipesCentrifuge.centrifuge().getCentrifugeResult(p_94041_2_) != null) : (p_94041_1_ == 1 ? RecipesFusionReactor.isFuel(p_94041_2_) : p_94041_1_ == 5 ? p_94041_2_.isItemEqual(IC2Items.getItem("overclockerUpgrade")):true);
	}
	

	@Override
	public void invalidate(){
		if(!this.worldObj.isRemote){
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			icInitialized=false;
		}
		super.invalidate();
		this.updateContainingBlockInfo();
	}
	
	@Override
	public void onChunkUnload(){
		if(!this.worldObj.isRemote){
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			icInitialized=false;
		}
		super.onChunkUnload();
	}
   
    
    
    
    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        this.chestContents = new ItemStack[this.getSizeInventory()];

        if (p_145839_1_.hasKey("CustomName", 8))
        {
            this.customName = p_145839_1_.getString("CustomName");
        }

        NBTTagList nbttaglist = p_145839_1_.getTagList("ItemsCentrifuge", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("SlotCentrifuge") & 255;

            if (j >= 0 && j < this.chestContents.length)
            {
                this.chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        this.furnaceCookTime = p_145839_1_.getFloat("CookTime");
        this.EnergyBuffer = p_145839_1_.getDouble("EnergyBuffer");
        this.overclockerCount = p_145839_1_.getShort("overclock");
        this.transformerCount = p_145839_1_.getShort("transformer");
        this.storageCount = p_145839_1_.getShort("storage");
    }

    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setFloat("CookTime", this.furnaceCookTime);
        p_145841_1_.setDouble("EnergyBuffer", this.EnergyBuffer);
        p_145841_1_.setShort("overclock", (short)this.overclockerCount);
        p_145841_1_.setShort("transformer", (short)this.transformerCount);
        p_145841_1_.setShort("storage", (short)this.storageCount);
        
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i)
        {
        	ItemStack itemstack = getStackInSlot(i);
            if (itemstack != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("SlotCentrifuge", (byte)i);
                itemstack.writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        p_145841_1_.setTag("ItemsCentrifuge", nbttaglist);

        if (this.hasCustomInventoryName())
        {
            p_145841_1_.setString("CustomName", this.customName);
        }
    }
    
    public void updateEntity()
    {
    	super.updateEntity();
    	getDescriptionPacket();
		if(!this.worldObj.isRemote){
	    	if(!icInitialized){
	    		MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
	    		icInitialized=true;
	    	}
		}
        boolean flag = this.EnergyBuffer > 0;
        boolean flag1 = false;

        if (!this.worldObj.isRemote)
        {
            if (this.EnergyBuffer != 0 || this.chestContents[1] != null && this.chestContents[0] != null)
            {

                if (this.isBurning() && this.canSmelt())
                {
                	checkUpgrades();
                    //this.furnaceCookTime += Math.pow(0.7, -overclockerCount);
                    this.furnaceCookTime += Math.pow(0.62, -overclockerCount);
                    this.EnergyBuffer-= this.energyConsumtion * Math.pow(1.6, overclockerCount);

                    if (this.furnaceCookTime > maxCookTime)
                    {
                        this.furnaceCookTime = 0;
                        this.smeltItem();
                        flag1 = true;
                    }
                }
                else
                {
                    this.furnaceCookTime = 0;
                }

	       		 if (flag != this.furnaceCookTime > 0)
	       		 {
	       			 flag1 = true;
	       			 this.validate();
	       		 }
            }
           
        }
        //TODO: make more efficient
        this.worldObj.markBlockForUpdate(this.xCoord, yCoord, zCoord);

    }

    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmelt()
    {
        if (this.chestContents[0] == null)
        {
            return false;
        }
        else
        {
            ResultCentrifuge result = RecipesCentrifuge.centrifuge().getCentrifugeResult(this.chestContents[0]);
            if (result == null) return false;
            if(this.chestContents[0].stackSize < result.getInputSize()) return false;
            for(int a=2; a!=0;a--){
	            if (this.chestContents[a+2] == null) continue;
	            if (!this.chestContents[a+2].isItemEqual(result.getResult(a))) return false;
	            int r = chestContents[a+2].stackSize + result.getResult(a).stackSize;
	            if(r > getInventoryStackLimit() || r > this.chestContents[a+2].getMaxStackSize()) return false;
            }
            return true;
        }
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void smeltItem()
    {
        if (this.canSmelt())
        {
            Object[] recipe = RecipesCentrifuge.centrifuge().getCentrifugeResult(this.chestContents[0]).randomResultIndex();
            
            int i = (Integer)(recipe[0]);
            ItemStack itemstack = (ItemStack)(recipe[1]);
            

            if (this.chestContents[2+i] == null)
            {
                this.chestContents[2+i] = itemstack.copy();
            }
            else if (this.chestContents[2+i].getItem() == itemstack.getItem())
            {
                this.chestContents[2+i].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
            }

            this.chestContents[0].stackSize -= RecipesCentrifuge.centrifuge().getCentrifugeResult(this.chestContents[0]).getInputSize();

            if (this.chestContents[0].stackSize <= 0)
            {
                this.chestContents[0] = null;
            }
        }
    }
    public boolean isBurning()
    {
        return this.EnergyBuffer > 0;
    }

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		 return p_94128_1_ == 0 ? slotsBottom : (p_94128_1_ == 1 ? slotsTop : slotsSides);
	}

	@Override
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_,
			int p_102007_3_) {
		 return this.isItemValidForSlot(p_102007_1_, p_102007_2_);
	}

	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_,
			int p_102008_3_) {
		 return p_102008_3_ != 0 || p_102008_1_ != 1 || p_102008_2_.getItem() == Items.bucket;
	}
	
	/*static{
		addMapping(TileCentrifuge.class, "Centrifuge");
		
	}*/
	
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//XXX: IC2 here

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter,
			ForgeDirection direction) {
		return true;
	}

	@Override
	public double getDemandedEnergy() {
		double leftover = this.maxEnergyBuffer - this.EnergyBuffer;
		//double d = Math.pow(2, 5+this.transformerCount*2);
		//return Math.min(leftover,d );
		return leftover;
	}
	
	@Override
	public int getSinkTier() {
		//Try to fix (insanealy bugged)
		return 0;
	}

	@Override
	public double injectEnergy(ForgeDirection directionFrom, double amount,
			double voltage) {
		this.EnergyBuffer+= amount;
		double filled;
		if((filled =  this.EnergyBuffer - this.maxEnergyBuffer)>0){
			this.EnergyBuffer = this.maxEnergyBuffer;
			return amount - filled;
		}
		//System.out.println("Got" + amount + "Eu");
		return 0;
	}
	
	public void checkUpgrades(){
		this.overclockerCount = storageCount = transformerCount = 0;
		
		for(int i=0;i<3;i++){
			if(chestContents[5+i]==null) continue;
			if(chestContents[5+i].isItemEqual(IC2Items.getItem("overclockerUpgrade"))) overclockerCount+=chestContents[5+i].stackSize;
			if(chestContents[5+i].isItemEqual(IC2Items.getItem("transformerUpgrade"))) transformerCount+=chestContents[5+i].stackSize;
			if(chestContents[5+i].isItemEqual(IC2Items.getItem("energyStorageUpgrade"))) storageCount+=chestContents[5+i].stackSize;
		}
		this.maxEnergyBuffer = 10000 * (this.storageCount+1);
	}

}
