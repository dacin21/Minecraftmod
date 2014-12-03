package com.dacin21.survivalmod.reactor.block;

import com.dacin21.survivalmod.tileentityblock.TileCentrifuge;

import ic2.api.item.IC2Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotUpgrade extends Slot {


	public SlotUpgrade(IInventory par1IInventory, int par2, int par3, int par4) {
		super(par1IInventory,par2,par3,par4);
	}
	
	public boolean isItemValid(ItemStack par1)
    {
        if(par1.isItemEqual(IC2Items.getItem("overclockerUpgrade"))) return true;
        if(par1.isItemEqual(IC2Items.getItem("transformerUpgrade"))) return true;
        if(par1.isItemEqual(IC2Items.getItem("energyStorageUpgrade"))) return true;
        return false;
    }
	

    public void onSlotChanged()
    {
        super.onSlotChanged();
        if(this.inventory!= null && this.inventory instanceof TileCentrifuge)((TileCentrifuge)(this.inventory)).checkUpgrades();
    }

}
