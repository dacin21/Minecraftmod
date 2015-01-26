package com.dacin21.survivalmod.backpack;

import com.dacin21.survivalmod.survivalmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryBackpack implements IInventory {
	private ItemStack[] content;
	private ItemStack BackpackStack;
	private EntityPlayer player;
	

	public InventoryBackpack(ItemStack backpackItem, EntityPlayer player) {
		BackpackStack = backpackItem;
		this.player=player;
		read();
	}
	
	private void reloadBackpack(){
		int ID = BackpackStack.stackTagCompound.getInteger("ID");
		if(ID!=0){
			for (ItemStack item:player.inventory.mainInventory){
				if(item!= null &&item.stackTagCompound!=null){
					if(item.stackTagCompound.getInteger("ID") == ID){
						BackpackStack = item;
						break;
					}
				}
			}
		}
	}
	
	private void write(){
		reloadBackpack();
		writeItemsToNBT(BackpackStack.stackTagCompound);
	}
	
	private void read(){
		reloadBackpack();
		readItemsFromNBT(BackpackStack.getTagCompound());
	}

	private void writeItemsToNBT(NBTTagCompound par1NBTTagCoumpound) {
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < content.length; i++) {
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			if (content[i] != null) {
				nbttagcompound1.setBoolean("hasItem", true);
				content[i].writeToNBT(nbttagcompound1);
			} else {
				nbttagcompound1.setBoolean("hasItem", false);
			}
			nbttaglist.appendTag(nbttagcompound1);
		}
		par1NBTTagCoumpound.setTag("items", nbttaglist);
	}

	private void readItemsFromNBT(NBTTagCompound par1NBTTagCoumpound) {
		content = new ItemStack[par1NBTTagCoumpound.getInteger("size") * 9];
		NBTTagList tagList = par1NBTTagCoumpound.getTagList("items", 10);
		for (int i = 0; i < content.length; i++) {
			NBTTagCompound tag = tagList.getCompoundTagAt(i);
			if (tag.getBoolean("hasItem")) {
				content[i] = ItemStack.loadItemStackFromNBT(tag);
			} else
				content[i] = null;
		}
	}

	@Override
	public int getSizeInventory() {
		return content.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return content[index+scrollOffset()];
	}

	@Override
	public ItemStack decrStackSize(int index, int amount) {
		//read();
		ItemStack ret = content[index+scrollOffset()].splitStack(amount);
		if(content[index+scrollOffset()].stackSize <= 0) content[index+scrollOffset()] = null;
		//write();
		return ret;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		read();
		return content[index+scrollOffset()];
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		read();
		content[index+scrollOffset()] = stack;
		write();

	}

	@Override
	public String getInventoryName() {
		return "Backpack";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		write();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return true;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
		write();
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack p_94041_2_) {
		return true;
	}
	
	public void scollGui(int offset){
		read();
		int pos = this.BackpackStack.stackTagCompound.getInteger("guiOffset");
		int maxpos = this.BackpackStack.stackTagCompound.getInteger("size") - 4;
		pos+=offset;
		if(pos < 0) pos = 0;
		if(pos > maxpos) pos=maxpos;
		this.BackpackStack.stackTagCompound.setInteger("guiOffset", pos);
		write();
	}
	
	public int scrollOffset(){
		if(this.BackpackStack.stackTagCompound!=null){
			return this.BackpackStack.stackTagCompound.getInteger("guiOffset")*9;
		}
		return 0;
	}

}
