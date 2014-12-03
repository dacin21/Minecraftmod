package com.dacin21.survivalmod.reactor.block;

import ic2.api.item.IC2Items;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.dacin21.survivalmod.survivalmod;
import com.dacin21.survivalmod.tileentityblock.recipe.ResultCentrifuge;


public class RecipesFusionReactor {
	
	public static final RecipesFusionReactor fusionBase = new RecipesFusionReactor();
	private final float multiplier = 128.0f * 2 * 28; //2 mb Steam = 1 Eu
	private Map fusionList = new HashMap();

	private RecipesFusionReactor() {
		ItemStack emptyCell = IC2Items.getItem("cell");
		addRecipe(new ItemStack(survivalmod.deuteriumCell), new ItemStack(survivalmod.deuteriumCell),new ItemStack(survivalmod.tritiumCell), emptyCell, 0.95f );
		addRecipe(new ItemStack(survivalmod.deuteriumCell), new ItemStack(survivalmod.tritiumCell),emptyCell, emptyCell, 1.0f );
		addRecipe(new ItemStack(survivalmod.tritiumCell), new ItemStack(survivalmod.deuteriumCell),emptyCell, emptyCell, 1.0f );
	}
	
	public void addRecipe(ItemStack input1,ItemStack input2,ItemStack output1,ItemStack output2, float fuelBurnTimeUnscaled)
    {
		this.addRecipe(input1,input2,output1,output2, (int) (fuelBurnTimeUnscaled * multiplier));
    }
	
	public void addRecipe(ItemStack input1,ItemStack input2,ItemStack output1,ItemStack output2, int fuelBurnTime)
    {
        this.addRecipe(new ResultFusionReactor(input1, input2, -1), new ResultFusionReactor(output1, output2, fuelBurnTime));
    }

    public void addRecipe(ResultFusionReactor par1, ResultFusionReactor par2)
    {
        this.fusionList.put(par1 , par2);
    }
    

    public ResultFusionReactor getFusionReactorResult(ResultFusionReactor p_151395_1_)
    {
        Iterator iterator = this.fusionList.entrySet().iterator();
        Entry entry;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            entry = (Entry)iterator.next();
        }
        while (!this.compare(p_151395_1_, (ResultFusionReactor)entry.getKey()));

        return (ResultFusionReactor)entry.getValue();
    }
    
    private boolean compare(ResultFusionReactor par1, ResultFusionReactor par2)
    {
        return (compare(par1.getFirstStack(), par2.getFirstStack()) && compare(par1.getSecondStack(), par2.getSecondStack()));
    }
    
    private boolean compare(ItemStack p_151397_1_, ItemStack p_151397_2_)
    {
        return p_151397_2_.getItem() == p_151397_1_.getItem() && (p_151397_2_.getItemDamage() == 32767 || p_151397_2_.getItemDamage() == p_151397_1_.getItemDamage());
    }

    public Map getFusionReactorList()
    {
        return this.fusionList;
    }
    
    public static boolean isFuel(ItemStack stack){
    	Item item = stack.getItem();
    	if(item == survivalmod.deuteriumCell) return true;
    	if(item == survivalmod.tritiumCell) return true;
    	
    	return false;
    	
    }

}
