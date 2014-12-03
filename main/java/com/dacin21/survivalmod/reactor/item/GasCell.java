package com.dacin21.survivalmod.reactor.item;

import ic2.api.item.IC2Items;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;

import com.dacin21.survivalmod.EntityRune;
import com.dacin21.survivalmod.survivalmod;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GasCell extends Item {

	public GasCell(String unlocalizedName) {
		super();
		setUnlocalizedName(survivalmod.modid+ "_" + unlocalizedName);
		GameRegistry.registerItem(this, survivalmod.modid+ "_" + unlocalizedName);
		this.setMaxStackSize(64);
		this.setCreativeTab(CreativeTabs.tabMisc);
		Recipes.extractor.addRecipe(new RecipeInputItemStack(new ItemStack(this)), null, IC2Items.getItem("cell"));
	}
	
	 
	/**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */

}
