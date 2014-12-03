package com.dacin21.survivalmod.reactor.block;

import net.minecraft.item.ItemStack;

public class ResultFusionReactor {
	
	private ItemStack s1, s2;
	private int energyOut;

	public ResultFusionReactor(ItemStack stack1, ItemStack stack2, int energyOutput) {
		s1 = stack1;
		s2 = stack2;
		energyOut = energyOutput;
	}
	
	public ItemStack getFirstStack(){
		return s1;
	}
	
	public ItemStack getSecondStack(){
		return s2;
	}
	
	public int getEnergyOutput(){
		return energyOut;
	}
	

}
