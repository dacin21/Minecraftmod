package com.dacin21.survivalmod.reactor.block;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotResult extends Slot {

	public SlotResult(IInventory par1IInventory, int par2, int par3, int par4) {
		super(par1IInventory,par2,par3,par4);
	}
	

    public boolean isItemValid(ItemStack p_75214_1_)
    {
        return false;
    }

}
