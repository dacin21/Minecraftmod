package com.dacin21.survivalmod;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class GenericItem extends Item {

	public GenericItem(String unlocalizedName) {
        super();
        setMaxStackSize(64);
        setCreativeTab(survivalmod.tabDacin);
		setUnlocalizedName(survivalmod.modid+ "_" + unlocalizedName);
		GameRegistry.registerItem(this, survivalmod.modid+ "_" + unlocalizedName);
	}

}
