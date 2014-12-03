package com.dacin21.survivalmod.tileentityblock.recipe;

import net.minecraft.item.crafting.IRecipe;

import com.dacin21.survivalmod.tileentityblock.recipe.RecipesCentrifuge;

public class RecipeSorterCentrifuge {

	final RecipesCentrifuge recipesCentrifue;

    public RecipeSorterCentrifuge(RecipesCentrifuge par1CraftingManagerCentrifuge)
    {
        this.recipesCentrifue = par1CraftingManagerCentrifuge;
    }

    public int compareRecipes(IRecipe par1IRecipe, IRecipe par2IRecipe)
    {
    	return ((par2IRecipe.getRecipeSize() < par1IRecipe.getRecipeSize() ? -1 : (par2IRecipe.getRecipeSize() > par1IRecipe.getRecipeSize() ? 1 : 0)));
    }

    public int compare(Object par1Obj, Object par2Obj)
    {
        return this.compareRecipes((IRecipe)par1Obj, (IRecipe)par2Obj);
    }

}
