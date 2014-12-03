package com.dacin21.survivalmod;

import java.util.Comparator;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

public class RecipeSorterInfuser implements Comparator
{
    final CraftingManagerInfuser craftingManagerInfuser;

    public RecipeSorterInfuser(CraftingManagerInfuser par1CraftingManagerInfuser)
    {
        this.craftingManagerInfuser = par1CraftingManagerInfuser;
    }

    public int compareRecipes(IRecipe par1IRecipe, IRecipe par2IRecipe)
    {
    	return par1IRecipe instanceof ShapelessRecipesInfuser && par2IRecipe instanceof ShapedRecipesInfuser ? 1 : (par2IRecipe instanceof ShapelessRecipesInfuser && par1IRecipe instanceof ShapedRecipesInfuser ? -1 : (par2IRecipe.getRecipeSize() < par1IRecipe.getRecipeSize() ? -1 : (par2IRecipe.getRecipeSize() > par1IRecipe.getRecipeSize() ? 1 : 0)));
    }

    public int compare(Object par1Obj, Object par2Obj)
    {
        return this.compareRecipes((IRecipe)par1Obj, (IRecipe)par2Obj);
    }
}
