package com.dacin21.survivalmod;

//import ic2.api.item.Items;

import ic2.api.item.IC2Items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;



public class CraftingManagerInfuser
{
	public static final int WILDCARD_VALUE = Short.MAX_VALUE;
	private static final CraftingManagerInfuser instance = new CraftingManagerInfuser();
	private List recipes = new ArrayList();
	
	public static final CraftingManagerInfuser getInstance()
	{
	         return instance;
	}
	
	private CraftingManagerInfuser()
	{
	recipes = new ArrayList();
		
	
	
			ItemStack fleshStack = new ItemStack(Items.rotten_flesh);
			func_92051_a(new ItemStack(survivalmod.fleshCluster), "xx", "xx",
	                'x', fleshStack);
	
			ItemStack swordD = new ItemStack(Items.diamond_sword);
			ItemStack saberNano = IC2Items.getItem("nanoSaber");
			//ItemStack saberNano = new ItemStack(Items.wooden_sword);
			ItemStack Obsidian = new ItemStack(Blocks.obsidian);
			ItemStack bucketLava = new ItemStack(Items.lava_bucket);
			ItemStack blockRedstone = new ItemStack(Blocks.redstone_block);
			ItemStack pearlEnder = new ItemStack(Items.ender_pearl);
			ItemStack stoneEnder = new ItemStack(Blocks.end_stone);
			
			func_92051_a(new ItemStack(survivalmod.doomBlade), " gdg ","gdcdg","dcbcd","ecace","fcacf",
	               'a', swordD,'b', saberNano,'c', Obsidian,'d', bucketLava,'e', blockRedstone,'f', pearlEnder,'g', stoneEnder);
		
			func_92051_a(new ItemStack(survivalmod.runicStaff),"    b", "  da ", " cad ", " ac  ", "e    ",
					'a',Items.blaze_rod , 'b', Items.ender_pearl ,'c', Items.gold_ingot, 'd', Items.iron_ingot, 'e', Items.stick);
			
			func_92051_a(new ItemStack(survivalmod.fusionReactor), "ddddd", "debed", "dbabd", "debed", "ddcdd",
					'a',IC2Items.getItem("reactorChamber") , 'b',IC2Items.getItem("teslaCoil") , 'c', IC2Items.getItem("evTransformer"), 'd', IC2Items.getItem("reactorReflectorThick") , 'e', IC2Items.getItem("inductionFurnace") );
			
			
	
	Collections.sort(this.recipes, new RecipeSorterInfuser(this));
	         System.out.println(this.recipes.size() + " recipes");
	}
	
	public ShapedRecipesInfuser func_92051_a(ItemStack par1ItemStack, Object ... par2ArrayOfObj)
	{
	         String var3 = "";
	         int var4 = 0;
	         int var5 = 0;
	         int var6 = 0;
	
	         if (par2ArrayOfObj[var4] instanceof String[])
	         {
	                 String[] var7 = (String[])((String[])par2ArrayOfObj[var4++]);
	
	                 for (int var8 = 0; var8 < var7.length; ++var8)
	                 {
	                         String var9 = var7[var8];
	                         ++var6;
	                         var5 = var9.length();
	                         var3 = var3 + var9;
	                 }
	         }
	         else
	         {
	                 while (par2ArrayOfObj[var4] instanceof String)
	                 {
	                         String var11 = (String)par2ArrayOfObj[var4++];
	                         ++var6;
	                         var5 = var11.length();
	                         var3 = var3 + var11;
	                 }
	         }
	
	         HashMap var12;
	
	         for (var12 = new HashMap(); var4 < par2ArrayOfObj.length; var4 += 2)
	         {
	                 Character var13 = (Character)par2ArrayOfObj[var4];
	                 ItemStack var14 = null;
	
	                 if (par2ArrayOfObj[var4 + 1] instanceof Item)
	                 {
	                         var14 = new ItemStack((Item)par2ArrayOfObj[var4 + 1]);
	                 }
	                 else if (par2ArrayOfObj[var4 + 1] instanceof Block)
	                 {
	                         var14 = new ItemStack((Block)par2ArrayOfObj[var4 + 1], 1, -1);
	                 }
	                 else if (par2ArrayOfObj[var4 + 1] instanceof ItemStack)
	                 {
	                         var14 = (ItemStack)par2ArrayOfObj[var4 + 1];
	                 }
	
	                 var12.put(var13, var14);
	         }
	
	         ItemStack[] var15 = new ItemStack[var5 * var6];
	
	         for (int var16 = 0; var16 < var5 * var6; ++var16)
	         {
	                 char var10 = var3.charAt(var16);
	
	                 if (var12.containsKey(Character.valueOf(var10)))
	                 {
	                         var15[var16] = ((ItemStack)var12.get(Character.valueOf(var10))).copy();
	                 }
	                 else
	                 {
	                         var15[var16] = null;
	                 }
	         }
	
	         ShapedRecipesInfuser var17 = new ShapedRecipesInfuser(var5, var6, var15, par1ItemStack);
	         this.recipes.add(var17);
	         return var17;
	}
	
	public void addInfuserShapelessRecipe(ItemStack par1ItemStack, Object ... par2ArrayOfObj)
	{
	         ArrayList var3 = new ArrayList();
	         Object[] var4 = par2ArrayOfObj;
	         int var5 = par2ArrayOfObj.length;
	
	         for (int var6 = 0; var6 < var5; ++var6)
	         {
	                 Object var7 = var4[var6];
	
	                 if (var7 instanceof ItemStack)
	                 {
	                         var3.add(((ItemStack)var7).copy());
	                 }
	                 else if (var7 instanceof Item)
	                 {
	                         var3.add(new ItemStack((Item)var7));
	                 }
	                 else
	                 {
	                         if (!(var7 instanceof Block))
	                         {
	                                 throw new RuntimeException("Invalid shapeless recipy!");
	                         }
	
	                         var3.add(new ItemStack((Block)var7));
	                 }
	         }
	
	         this.recipes.add(new ShapelessRecipesInfuser(par1ItemStack, var3));
	}
	
	public ItemStack findMatchingRecipe(InventoryCrafting par1InventoryCrafting, World par2World)
	{
	         int var3 = 0;
	         ItemStack var4 = null;
	         ItemStack var5 = null;
	         int var6;
	
	         for (var6 = 0; var6 < par1InventoryCrafting.getSizeInventory(); ++var6)
	         {
	                 ItemStack var7 = par1InventoryCrafting.getStackInSlot(var6);
	
	                 if (var7 != null)
	                 {
	                         if (var3 == 0)
	                         {
	                                 var4 = var7;
	                         }
	
	                         if (var3 == 1)
	                         {
	                                 var5 = var7;
	                         }
	
	                         ++var3;
	                 }
	         }
	//if (var3 == 2 && var4.itemID == var5.itemID && var4.stackSize == 1 && var5.stackSize == 1 && Item.itemsList[var4.itemID].isRepairable())
	         if (var3 == 2 && var4.getItem() == var5.getItem() && var4.stackSize == 1 && var5.stackSize == 1 && var4.getItem().isRepairable())
	         {
	                // Item var11 = Item.itemsList[var4.itemID];
	                 Item var11 = var4.getItem();
	                 int var13 = var11.getMaxDamage() - var4.getItemDamageForDisplay();
	                 int var8 = var11.getMaxDamage() - var5.getItemDamageForDisplay();
	                 int var9 = var13 + var8 + var11.getMaxDamage() * 5 / 100;
	                 int var10 = var11.getMaxDamage() - var9;
	
	                 if (var10 < 0)
	                 {
	                         var10 = 0;
	                 }
	
	                 return new ItemStack(var4.getItem(), 1, var10);
	         }
	         else
	         {
	                 for (var6 = 0; var6 < this.recipes.size(); ++var6)
	                 {
	                         IRecipe var12 = (IRecipe)this.recipes.get(var6);
	
	                         if (var12.matches(par1InventoryCrafting, par2World))
	                         {
	                                 return var12.getCraftingResult(par1InventoryCrafting);
	                         }
	                 }
	
	                 return null;
	         }
	}
	
	/**
	         * returns the List<> of all recipes
	         */
	public List getRecipeList()
	{
	         return this.recipes;
	}
}