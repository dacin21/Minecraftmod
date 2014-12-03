package com.dacin21.survivalmod;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSword;

public class DmSword extends ItemSword {
	public DmSword (ToolMaterial material){
		super(material);
		setCreativeTab(CreativeTabs.tabCombat);
		setUnlocalizedName(survivalmod.modid+ "_" + "doomBlade");
		GameRegistry.registerItem(this, survivalmod.modid+ "_" + "doomBlade");
		
	}

}
