package com.dacin21.survivalmod.backpack;

import com.dacin21.survivalmod.survivalmod;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class BackpackProtectedSlot extends Slot {

	public BackpackProtectedSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
		super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
	}
	@Override
    public ItemStack getStack()
    {
        if(super.getStack().getItem() == survivalmod.backpack) return null;
        return super.getStack();
    }

}
