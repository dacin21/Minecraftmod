package com.dacin21.survivalmod.tileentityblock;

import com.dacin21.survivalmod.reactor.block.SlotResult;
import com.dacin21.survivalmod.reactor.block.SlotUpgrade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCentrifuge extends Container {
	
	private static final int Max = 7;
	private TileCentrifuge lowerChestInventory;
    private static final String __OBFID = "CL_00001742";

    public ContainerCentrifuge(IInventory p_i1806_1_, TileCentrifuge p_i1806_2_)
    {
        this.lowerChestInventory = p_i1806_2_;
        p_i1806_2_.openInventory();
        int i = -19;
        int j;
        int k;
        //chest
        int minIndex = /*2*9+9*/ 0;
        this.addSlotToContainer(new Slot(p_i1806_2_, minIndex + 0, 20, 17));
        this.addSlotToContainer(new Slot(p_i1806_2_, minIndex + 1, 20, 53));
        this.addSlotToContainer(new SlotResult(p_i1806_2_, minIndex + 2, 74 , 53));
        this.addSlotToContainer(new SlotResult(p_i1806_2_, minIndex + 3, 92 , 53));
        this.addSlotToContainer(new SlotResult(p_i1806_2_, minIndex + 4, 110 , 53));
        this.addSlotToContainer(new Slot/*Upgrade*/(p_i1806_2_, minIndex + 5, 146 , 17));
        this.addSlotToContainer(new Slot/*Upgrade*/(p_i1806_2_, minIndex + 6, 146 , 35));
        this.addSlotToContainer(new Slot/*Upgrade*/(p_i1806_2_, minIndex + 7, 146 , 53));
        //inventory
        for (j = 0; j < 3; ++j)
        {
            for (k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(p_i1806_1_,k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }
        //hotbar
        for (j = 0; j < 9; ++j)
        {
            this.addSlotToContainer(new Slot(p_i1806_1_,  j, 8 + j * 18, 161 + i));
        }
    }

    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return this.lowerChestInventory.isUseableByPlayer(p_75145_1_);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(p_82846_2_);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (p_82846_2_ < Max)
            {
                if (!this.mergeItemStack(itemstack1, Max, this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, Max, false))
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
        }

        return itemstack;
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer p_75134_1_)
    {
        super.onContainerClosed(p_75134_1_);
        this.lowerChestInventory.closeInventory();
    }

    /**
     * Return this chest container's lower chest inventory.
     */
    public IInventory getLowerChestInventory()
    {
        return this.lowerChestInventory;
    }

}
