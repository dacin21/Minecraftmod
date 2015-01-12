package com.dacin21.survivalmod.reactor.producion;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerNeutronizer extends Container
{
    private TileNeutronizer tileNeutronizer;

    public ContainerNeutronizer(InventoryPlayer p_i1812_1_, TileNeutronizer p_i1812_2_)
    {
        this.tileNeutronizer = p_i1812_2_;
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

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int p_75137_1_, int p_75137_2_)
    {

    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return this.tileNeutronizer.isUseableByPlayer(p_75145_1_);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1Player, int par2Slot)
    {
    	ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2Slot);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2Slot >= 0 && par2Slot < 27)
            {
                if (!this.mergeItemStack(itemstack1, 27, 36, false))
                {
                    return null;
                }
            }
            else if (par2Slot >= 27 && par2Slot < 36)
            {
                if (!this.mergeItemStack(itemstack1, 0, 27, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, 36, false))
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

            slot.onPickupFromSlot(par1Player, itemstack1);
        }

        return itemstack;
    }
}


