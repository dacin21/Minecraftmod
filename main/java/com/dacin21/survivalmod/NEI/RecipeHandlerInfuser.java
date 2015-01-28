package com.dacin21.survivalmod.NEI;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.DefaultOverlayRenderer;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.ShapedRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.dacin21.survivalmod.ContainerInfuser;
import com.dacin21.survivalmod.CraftingManagerInfuser;
import com.dacin21.survivalmod.GuiInfuser;
import com.dacin21.survivalmod.ShapedRecipesInfuser;
import com.dacin21.survivalmod.ShapelessRecipesInfuser;

public class RecipeHandlerInfuser extends TemplateRecipeHandler {


	
	@Override
	public String getRecipeName() {
		return "Infusing Altar";
	}

	@Override
	public String getGuiTexture() {
		return GuiInfuser.infuserTextures.toString();
	}

	public RecipeHandlerInfuser() {
		super();
	}
	
	public void drawBackground(int recipe)
	  {
	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    GuiDraw.changeTexture(getGuiTexture());
	    GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 166, 92);
	  }
	  
	  public void drawForeground(int recipe)
	  {
	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    GL11.glDisable(2896);
	    GuiDraw.changeTexture(getGuiTexture());
	    drawExtras(recipe);
	  }

	public class CachedShapedRecipeInfuser extends TemplateRecipeHandler.CachedRecipe
	{
		public ArrayList<PositionedStack> ingredients;
		public PositionedStack result;

		public CachedShapedRecipeInfuser(int width, int height, Object[] items, ItemStack out)
		{
			super();
			this.result = new PositionedStack(out, 131, 36);
			this.ingredients = new ArrayList();
			setIngredients(width, height, items);
		}

		public CachedShapedRecipeInfuser(ShapedRecipesInfuser recipe) {
			this(recipe.recipeWidth, recipe.recipeHeight, recipe.recipeItems, recipe.getRecipeOutput());
		}



		public void setIngredients(int width, int height, Object[] items)
		{
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (items[(y * width + x)] != null)
					{

						PositionedStack stack = new PositionedStack(items[(y * width + x)], 4 + x * 18, 3 + y * 18, false);
						stack.setMaxSize(1);
						this.ingredients.add(stack);
					}
				}
			}
		}

		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(10, this.ingredients);
		}

		public PositionedStack getResult() {
			return this.result;
		}

		public void computeVisuals() {
			for (PositionedStack p : this.ingredients) {
				p.generatePermutations();
			}
		}
	}

	public void loadTransferRects() {
		this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new java.awt.Rectangle(84, 23, 24, 18), "crafting", new Object[0]));
	}

	public Class<? extends GuiContainer> getGuiClass()
	{
		return com.dacin21.survivalmod.GuiInfuser.class;
	}


	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if ((outputId.equals("Infusing Altar")) /*&& this.getClass() == RecipeHanderInfuser.class*/)
			for (Object e : CraftingManagerInfuser.getInstance().getRecipeList()) {
				IRecipe irecipe = null;
				if(e instanceof IRecipe){
					irecipe = (IRecipe)e;
				}
				CachedShapedRecipeInfuser recipe = null;
				if ((irecipe instanceof ShapedRecipes)) {
					recipe = new CachedShapedRecipeInfuser((ShapedRecipesInfuser) irecipe);
				} /*else if ((irecipe instanceof ShapedOreRecipe)) {
					recipe = forgeShapedRecipe((ShapedOreRecipe) irecipe);
				}*/
				if (recipe != null)
				{

					recipe.computeVisuals();
					this.arecipes.add(recipe);
				}
			}
		else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	public void loadCraftingRecipes(ItemStack result)
	{
		for (Object e : CraftingManagerInfuser.getInstance().getRecipeList()) {
			IRecipe irecipe = (IRecipe) e;
			if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
				CachedShapedRecipeInfuser recipe = null;
				//TODO: add my recipes
				if ((irecipe instanceof ShapedRecipesInfuser)) {
					recipe = new CachedShapedRecipeInfuser((ShapedRecipesInfuser) irecipe);
				} /*else if ((irecipe instanceof ShapedRecipesInfuser)) {
					recipe = forgeShapedRecipe((ShapedRecipesInfuser) irecipe);
				}*/
				if (recipe != null)
				{

					recipe.computeVisuals();
					this.arecipes.add(recipe);
				}
			}
		}
	}

	public void loadUsageRecipes(ItemStack ingredient) {
		for (Object e : CraftingManagerInfuser.getInstance().getRecipeList()) {
			IRecipe irecipe = (IRecipe) e;
			CachedShapedRecipeInfuser recipe = null;
			if ((irecipe instanceof ShapedRecipesInfuser)) {
				recipe = new CachedShapedRecipeInfuser((ShapedRecipesInfuser) irecipe);
			} /*else if ((irecipe instanceof ShapelessRecipesInfuser)) {
				recipe = forgeShapedRecipe((ShapelessRecipesInfuser) irecipe);
			}*/
			if ((recipe != null) && (recipe.contains(recipe.ingredients, ingredient.getItem())))
			{

				recipe.computeVisuals();
				if (recipe.contains(recipe.ingredients, ingredient)) {
					recipe.setIngredientPermutation(recipe.ingredients, ingredient);
					this.arecipes.add(recipe);
				}
			}
		}
	}

	/*public CachedShapedRecipeInfuser forgeShapedRecipe(ShapedOreRecipe recipe) {
		int width;
		int height;
			width = height = 5;
		Object[] items = recipe.getInput();
		for (Object item : items) {
			if (((item instanceof List)) && (((List) item).isEmpty()))
				return null;
		}
		return new CachedShapedRecipeInfuser(width, height, items, recipe.getRecipeOutput());
	}*/

	public String getOverlayIdentifier()
	{
		return "crafting";
	}

	public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
		return container instanceof ContainerInfuser;
		//return (super.hasOverlay(gui, container, recipe)) || ((isRecipe2x2(recipe)) && (RecipeInfo.hasDefaultOverlay(gui, "crafting2x2")));
	}


	//used for ? Button
	public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe)
	{
		IRecipeOverlayRenderer renderer = super.getOverlayRenderer(gui, recipe);
		if (renderer != null) {
			return renderer;
		}
		codechicken.nei.api.IStackPositioner positioner = RecipeInfo.getStackPositioner(gui, "crafting2x2");
		if (positioner == null)
			return null;
		return new DefaultOverlayRenderer(getIngredientStacks(recipe), positioner);
	}

	//used for ? Button
	public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe)
	{
		IOverlayHandler handler = super.getOverlayHandler(gui, recipe);
		if (handler != null) {
			return handler;
		}
		return RecipeInfo.getOverlayHandler(gui, "crafting3x3");
	}

	public boolean isRecipe2x2(int recipe) {
		for (PositionedStack stack : getIngredientStacks(recipe)) {
			if ((stack.relx > 43) || (stack.rely > 24))
				return false;
		}
		return true;
	}

}
