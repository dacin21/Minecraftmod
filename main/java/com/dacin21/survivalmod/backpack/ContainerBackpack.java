package com.dacin21.survivalmod.backpack;

import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ReportedException;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerBackpack extends Container {


	private InventoryBackpack backpackInventory;
	

	public ContainerBackpack(EntityPlayer par1Player, ItemStack par2ItemStack)
	{
		
		if (!(par2ItemStack.getItem() instanceof ItemBackpack)) {
			throw new ReportedException(new CrashReport("Invalid Item to open Backpack gui", new IllegalArgumentException()));
		}
		backpackInventory = new InventoryBackpack(par2ItemStack, par1Player);


		int i;
		//Player's Inventory
		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(par1Player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		//Hotbar
		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(par1Player.inventory, i, 8 + i * 18, 142));
		}
		// Backpack
		for(i = 0; i < 9; i++){
			for(int j=0; j < backpackInventory.getSizeInventory()/9; j++){
				this.addSlotToContainer(new Slot(backpackInventory, i + j*9, 8 + i*18, 66 - j*18));
			}
		}
	}
	
	public void onContainerClosed(EntityPlayer player) {
		backpackInventory.closeInventory();	
	};


	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_)
	{
		return true;
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or
	 * you will crash when someone does that.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1Player, int par2Slot)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(par2Slot);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2Slot >= 0 && par2Slot < 36)
			{
				if (!this.mergeItemStack(itemstack1, 36, this.inventorySlots.size(), false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 0, 36, false))
			{
				return null;
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(par1Player, itemstack1);
		}

		return itemstack;
	}
}
