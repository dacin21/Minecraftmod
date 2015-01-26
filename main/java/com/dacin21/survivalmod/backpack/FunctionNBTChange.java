package com.dacin21.survivalmod.backpack;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class FunctionNBTChange {
	public static FunctionNBTChange BackpackBuildup = new FunctionNBTChange(){
		public NBTTagCompound call(NBTTagCompound[] oldtags){
			NBTTagCompound ret = null;
			for(int i=0;i<oldtags.length;i++){
				if(oldtags[i]!= null && oldtags[i].getTagList("items", 10)!=null){
					ret = (NBTTagCompound) oldtags[i].copy();
					ret.setInteger("size", ret.getInteger("size")+1);
					ret.setInteger("ID", ret.getInteger("ID"));

					NBTTagList itemlist = (NBTTagList) oldtags[i].getTagList("items", 10).copy();
			        for(int j=0 ;j<9;j++){
			        	 NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			        	 nbttagcompound1.setBoolean("hasItem", false);
			        	 itemlist.appendTag(nbttagcompound1);
			        }
			        ret.setTag("items", itemlist);
			       
				}
			}
		return ret;
		}
	};
	
	public NBTTagCompound call(NBTTagCompound[] oldtags){
		return null;
	}
}
