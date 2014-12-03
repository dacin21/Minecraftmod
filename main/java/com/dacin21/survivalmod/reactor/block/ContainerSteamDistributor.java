package com.dacin21.survivalmod.reactor.block;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class ContainerSteamDistributor extends Container
{
    private TileSteamDistributor tileSteamDistributor;
    private int lastItemBurnTime;

    public ContainerSteamDistributor(InventoryPlayer p_i1812_1_, TileSteamDistributor p_i1812_2_)
    {
        this.tileSteamDistributor = p_i1812_2_;
        int i;

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(p_i1812_1_, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(p_i1812_1_, i, 8 + i * 18, 142));
        }
    }
    @Override
    //has nothing to do with enchanting, just used for client -> server communication
    public boolean enchantItem(EntityPlayer p_75140_1_, int id)
    {
    	switch(id){
    	case 0: this.tileSteamDistributor.xOff++;
				break;
		case 1: this.tileSteamDistributor.xOff--;
				break;
	
		case 2: this.tileSteamDistributor.yOff++;
				break;
		case 3: this.tileSteamDistributor.yOff--;
				break;
	
		case 4: this.tileSteamDistributor.zOff++;
				break;
		case 5: this.tileSteamDistributor.zOff--;
				break;
			
		case 6: int dirIn = this.tileSteamDistributor.directionIn.ordinal();
				dirIn = (dirIn + 1) % 6;
				this.tileSteamDistributor.directionIn = ForgeDirection.getOrientation(dirIn);
				break;
		case 7: int dirFillOut = this.tileSteamDistributor.directionFillOut.ordinal();
				dirFillOut = (dirFillOut + 1) % 6;
				this.tileSteamDistributor.directionFillOut = ForgeDirection.getOrientation(dirFillOut);
				break;
		default: return false;
    	}
    	this.tileSteamDistributor.getWorldObj().markBlockForUpdate(tileSteamDistributor.xCoord, tileSteamDistributor.yCoord, tileSteamDistributor.zCoord);
    	this.tileSteamDistributor.markDirty();
    	return true;
    }
    
    
    @Override
    public void addCraftingToCrafters(ICrafting p_75132_1_)
    {
        super.addCraftingToCrafters(p_75132_1_);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int p_75137_1_, int p_75137_2_)
    {
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return this.tileSteamDistributor.isUseableByPlayer(p_75145_1_);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(p_82846_2_);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (p_82846_2_ == 2 ||p_82846_2_ == 3 )
            {
                if (!this.mergeItemStack(itemstack1, 4, 40, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (p_82846_2_ != 1 && p_82846_2_ != 0)
            {
                
                if (p_82846_2_ >= 4 && p_82846_2_ < 31)
                {
                    if (!this.mergeItemStack(itemstack1, 31, 40, false))
                    {
                        return null;
                    }
                }
                else if (p_82846_2_ >= 31 && p_82846_2_ < 40 && !this.mergeItemStack(itemstack1, 4, 31, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 4, 40, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(p_82846_1_, itemstack1);
        }

        return itemstack;
    }
}



