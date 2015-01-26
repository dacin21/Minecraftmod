package com.dacin21.survivalmod.teleStaff;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.dacin21.survivalmod.survivalmod;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemElectricRunicStaff extends Item implements IElectricItem {
	private static final double maxEnergy = 1000.0f;
	private static final double useEnergy=100.0f;
	public ItemElectricRunicStaff(String unlocalizedName)
	{
		super();
		this.setMaxDamage(27);
		this.setMaxStackSize(1);
        setCreativeTab(survivalmod.tabDacin);
		setUnlocalizedName(survivalmod.modid+ "_" + unlocalizedName);
		GameRegistry.registerItem(this, survivalmod.modid+ "_" + unlocalizedName);
	}
		
		/**
	     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	     */
		@Override
	    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	    {
			 		
			if(ElectricItem.manager.use(par1ItemStack, useEnergy, par3EntityPlayer)){
	    			par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		
		            if (!par2World.isRemote)
		            {
		            	EntityRune rune = new EntityRune(par2World, par3EntityPlayer);
		            	rune.setVelocity(rune.motionX*5, rune.motionY*5, rune.motionZ*5);
		                par2World.spawnEntityInWorld(rune);
		            }
	    		}

	            return par1ItemStack;

	    }

		@Override
		public boolean canProvideEnergy(ItemStack itemStack) {
			return false;
		}

		@Override
		public Item getChargedItem(ItemStack itemStack) {
			return this;
		}

		@Override
		public Item getEmptyItem(ItemStack itemStack) {
			return this;
		}

		@Override
		public double getMaxCharge(ItemStack itemStack) {
			return this.maxEnergy;
		}

		@Override
		public int getTier(ItemStack itemStack) {
			return 0;
		}

		@Override
		public double getTransferLimit(ItemStack itemStack) {
			return this.maxEnergy/10;
		}

}
