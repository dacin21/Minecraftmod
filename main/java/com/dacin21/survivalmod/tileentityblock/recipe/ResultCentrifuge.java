package com.dacin21.survivalmod.tileentityblock.recipe;

import java.util.Random;

import net.minecraft.item.ItemStack;

public class ResultCentrifuge {
	private final Random random = new Random();
	int inputsize;
	private ItemStack[] resultStacks = new ItemStack[3];
	private float[] resultChance = new float[3];

	public ResultCentrifuge(int inputSize, ItemStack result1, float chance1, ItemStack result2, float chance2,ItemStack result3, float chance3) {
		this.inputsize = inputSize;
		resultStacks[0] = result1;
		resultStacks[1] = result2;
		resultStacks[2] = result3;
		float scale = chance1 + chance2 + chance3;
		chance1/=scale;
		chance2/=scale;
		chance3/=scale;
		resultChance[0] = chance1;
		resultChance[1] = chance2;
		resultChance[2] = chance3;
	}
	
	public int getInputSize(){
		return this.inputsize;
	}
	
	public ItemStack getResult(int index){
		return resultStacks[index];
	}
	
	public ItemStack getRandomResult(){
		float r = random.nextFloat();
		if((r-= resultChance[0])<0) return resultStacks[0];
		if(r < resultChance[1]) return resultStacks[1];
		return resultStacks[2];
	}
	
	public Object[] randomResultIndex(){
		float r = random.nextFloat();
		int c = 2;
		if((r-= resultChance[0])<0) c = 0;
		else if(r < resultChance[1]) c = 1;
		Object[] result = new Object[2];
		result[0] = c;
		result[1] = resultStacks[c];
		return result;
		
	}


}
