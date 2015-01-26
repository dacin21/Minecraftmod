package com.dacin21.survivalmod.backpack;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;


public class NBTRecipe implements IRecipe {
	/** How many horizontal slots this recipe is wide. */
	public final int recipeWidth;

	/** How many vertical slots this recipe uses. */
	public final int recipeHeight;

	/** Is a array of ItemStack that composes the recipe. */
	public final ItemStack[] recipeItems;

	/** Is the ItemStack that you get when craft the recipe. */
	private ItemStack recipeOutput;

	/** Is the itemID of the output item that you get when craft the recipe. */
	public final Item recipeOutputItemID;
	
	public final FunctionNBTChange NBTFunc;

	public NBTRecipe(int par1, int par2, ItemStack[] par3ArrayOfItemStack, ItemStack par4ItemStack, FunctionNBTChange par5NBTfunction)
	{
		this.recipeOutputItemID = par4ItemStack.getItem();
		this.recipeWidth = par1;
		this.recipeHeight = par2;
		this.recipeItems = par3ArrayOfItemStack;
		this.recipeOutput = par4ItemStack;
		this.NBTFunc = par5NBTfunction;
	}

	public ItemStack getRecipeOutput()
	{
		return this.recipeOutput;
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	public boolean matches(InventoryCrafting par1InventoryCrafting, World par2World)
	{
		for (int i = 0; i <= 3 - this.recipeWidth; ++i)
		{
			for (int j = 0; j <= 3 - this.recipeHeight; ++j)
			{
				if (this.checkMatch(par1InventoryCrafting, i, j, true))
				{
					return true;
				}

				if (this.checkMatch(par1InventoryCrafting, i, j, false))
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Checks if the region of a crafting inventory is match for the recipe.
	 */
	private boolean checkMatch(InventoryCrafting par1InventoryCrafting, int par2, int par3, boolean par4)
	{
		for (int k = 0; k < 3; ++k)
		{
			for (int l = 0; l < 3; ++l)
			{
				int i1 = k - par2;
				int j1 = l - par3;
				ItemStack itemstack = null;

				if (i1 >= 0 && j1 >= 0 && i1 < this.recipeWidth && j1 < this.recipeHeight)
				{
					if (par4)
					{
						itemstack = this.recipeItems[this.recipeWidth - i1 - 1 + j1 * this.recipeWidth];
					}
					else
					{
						itemstack = this.recipeItems[i1 + j1 * this.recipeWidth];
					}
				}

				ItemStack itemstack1 = par1InventoryCrafting.getStackInRowAndColumn(k, l);

				if (itemstack1 != null || itemstack != null)
				{
					if (itemstack1 == null && itemstack != null || itemstack1 != null && itemstack == null)
					{
						return false;
					}

					if (itemstack.getItem() != itemstack1.getItem())
					{
						return false;
					}

					if (itemstack.getItemDamage() != 32767 && itemstack.getItemDamage() != itemstack1.getItemDamage())
					{
						return false;
					}
				}
			}
		}

		return true;
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting)
	{
		ItemStack itemstack = this.getRecipeOutput().copy();
		NBTTagCompound[] oldTags = new NBTTagCompound[par1InventoryCrafting.getSizeInventory()];

			for (int i = 0; i < par1InventoryCrafting.getSizeInventory(); ++i)
			{
				ItemStack itemstack1 = par1InventoryCrafting.getStackInSlot(i);

				if (itemstack1 != null && itemstack1.hasTagCompound())
				{
					oldTags[i] = itemstack1.stackTagCompound;
				} else {
					oldTags[i] = null;
				}
			}
		itemstack.setTagCompound(this.NBTFunc.call(oldTags));
		return itemstack;
	}

	/**
	 * Returns the size of the recipe area
	 */
	public int getRecipeSize()
	{
		return this.recipeWidth * this.recipeHeight;
	}
	
	public static NBTRecipe addNBTRecipe(ItemStack par1ItemStack,FunctionNBTChange nbtFunc,  Object ... par2ArrayOfObj)
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
	
	         NBTRecipe var17 = new NBTRecipe(var5, var6, var15, par1ItemStack, nbtFunc);
	         CraftingManager.getInstance().getRecipeList().add(var17);
	         return var17;
	}

}
