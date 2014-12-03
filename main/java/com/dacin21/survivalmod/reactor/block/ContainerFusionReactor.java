package com.dacin21.survivalmod.reactor.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFusionReactor extends Container
{
    private TileFusionReactor tileFusionReactor;
    private int lastItemBurnTime;

    public ContainerFusionReactor(InventoryPlayer p_i1812_1_, TileFusionReactor p_i1812_2_)
    {
        this.tileFusionReactor = p_i1812_2_;
        this.addSlotToContainer(new Slot(p_i1812_2_, 0, 38, 17));
        this.addSlotToContainer(new Slot(p_i1812_2_, 1, 38, 53));
        this.addSlotToContainer(new SlotResult(p_i1812_2_, 2, 92, 17));
        this.addSlotToContainer(new SlotResult(p_i1812_2_, 3, 92, 53));
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
        p_75132_1_.sendProgressBarUpdate(this, 0, this.tileFusionReactor.fuelBurnTime);
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

            if (this.lastItemBurnTime != this.tileFusionReactor.fuelBurnTime)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.tileFusionReactor.fuelBurnTime);
            }
        }
        this.lastItemBurnTime = this.tileFusionReactor.fuelBurnTime;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int p_75137_1_, int p_75137_2_)
    {

        if (p_75137_1_ == 0)
        {
            this.tileFusionReactor.fuelBurnTime = p_75137_2_;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return this.tileFusionReactor.isUseableByPlayer(p_75145_1_);
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
                if (RecipesFusionReactor.isFuel(itemstack))
                {
                    if (!this.mergeItemStack(itemstack1, 0, 2, false))
                    {
                        return null;
                    }
                }
                else if (p_82846_2_ >= 4 && p_82846_2_ < 31)
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