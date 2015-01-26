package com.dacin21.survivalmod.backpack;

import com.dacin21.survivalmod.survivalmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBackpackProtected extends Slot {

	public SlotBackpackProtected(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
		super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
    {
        if(stack == null) return true;
		return stack.getItem() != survivalmod.backpack;
    }

	@Override
	public ItemStack getStack()
    {
		ItemStack ret = super.getStack();
        return ret;
    }

	@Override
	public ItemStack decrStackSize(int p_75209_1_)
    {
		ItemStack ret = super.decrStackSize(p_75209_1_);
		if(ret== null || ret.getItem() == survivalmod.backpack) return null;
        return ret;
    }

	@Override
	public boolean canTakeStack(EntityPlayer p_82869_1_)
    {
        if(super.getStack() == null) return true;
		return super.getStack().getItem() != survivalmod.backpack;
    }

}
