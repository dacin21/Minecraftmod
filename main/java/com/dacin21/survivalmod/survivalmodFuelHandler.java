package com.dacin21.survivalmod;


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;
import com.dacin21.survivalmod.survivalmod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;


public class survivalmodFuelHandler implements IFuelHandler {
	
	
	
	
	
	@Override
	public int getBurnTime(ItemStack fuel) {
		// TODO Auto-generated method stub
		Item var1=fuel.getItem();
		if (var1 == (survivalmod.nenderHeat)){
			return 204800;
		}
		return 0;
	}

}
