package com.dacin21.survivalmod.teleStaff;


import com.dacin21.survivalmod.survivalmod;

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.api.item.IC2Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RunicStaff extends Item  {
	public RunicStaff(String unlocalizedName)
	{
		super();
        setCreativeTab(survivalmod.tabDacin);
		setUnlocalizedName(survivalmod.modid+ "_" + unlocalizedName);
		GameRegistry.registerItem(this, survivalmod.modid+ "_" + unlocalizedName);
	    this.setMaxDamage(9);
	}
	
	/**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
	@Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
		 	
    		par1ItemStack.damageItem(1, par3EntityPlayer);
            par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            if (!par2World.isRemote)
            {
                par2World.spawnEntityInWorld(new EntityRune(par2World, par3EntityPlayer));
            }

            return par1ItemStack;

    }

}
