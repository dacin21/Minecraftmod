package com.dacin21.survivalmod.tileentityblock.recipe;

import ic2.api.item.IC2Items;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.dacin21.survivalmod.survivalmod;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipesCentrifuge {
	private static final RecipesCentrifuge centrifugeBase = new RecipesCentrifuge();
	private Map centrifugeList = new HashMap();
	
	public static RecipesCentrifuge centrifuge(){
		return centrifugeBase;
	}
	
	private RecipesCentrifuge(){
		this.addRecipe(2, IC2Items.getItem("electrolyzedWaterCell"), new ItemStack(IC2Items.getItem("cell").getItem(), 2) , 1 ,new ItemStack(survivalmod.hydrogenCell, 2) , 1, new ItemStack(survivalmod.deuteriumCell, 2), 0.1f);
		this.addRecipe(1, new ItemStack(survivalmod.hydrogenCell), new ItemStack(IC2Items.getItem("cell").getItem(), 1), 1, new ItemStack(survivalmod.deuteriumCell, 1), 1, new ItemStack(survivalmod.tritiumCell, 1), 0.1f);
		this.addRecipe(1, new ItemStack(survivalmod.deuteriumCell), new ItemStack(IC2Items.getItem("cell").getItem(), 1) , 1 ,new ItemStack(survivalmod.hydrogenCell, 1) , 2, new ItemStack(survivalmod.tritiumCell, 1), 1f);
	}
	
	public void addRecipe(int inputSize, ItemStack input, ItemStack r1, float c1, ItemStack r2, float c2, ItemStack r3, float c3)
    {
        this.addRecipe(input, new ResultCentrifuge(inputSize, r1,c1,r2,c2,r3,c3));
    }

    public void addRecipe(ItemStack par1, ResultCentrifuge par2)
    {
        this.centrifugeList.put(par1 , par2);
    }
    

    public ResultCentrifuge getCentrifugeResult(ItemStack p_151395_1_)
    {
        Iterator iterator = this.centrifugeList.entrySet().iterator();
        Entry entry;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            entry = (Entry)iterator.next();
        }
        while (!this.compare(p_151395_1_, (ItemStack)entry.getKey()));

        return (ResultCentrifuge)entry.getValue();
    }
    
    public ItemStack getRandomCentrifugeResult(ItemStack par1){
    	return getCentrifugeResult(par1).getRandomResult();
    }
    
    private boolean compare(ItemStack p_151397_1_, ItemStack p_151397_2_)
    {
        return p_151397_2_.getItem() == p_151397_1_.getItem() && (p_151397_2_.getItemDamage() == 32767 || p_151397_2_.getItemDamage() == p_151397_1_.getItemDamage());
    }

    public Map getCentrifugeList()
    {
        return this.centrifugeList;
    }
    
    
	
}
