package com.dacin21.survivalmod;


import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
public class GenericEntityItem extends Item
{
	public GenericEntityItem(String unlocalizedName)
	{
		super();
		setUnlocalizedName(survivalmod.modid+ "_" + unlocalizedName);
		GameRegistry.registerItem(this, survivalmod.modid+ "_" + unlocalizedName);
	}
	/*
	 * ka, was das macht, break aber de texturename
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) //updateIcons
	{
		this.itemIcon = iconRegister.registerIcon("survivalmod:" + this.getUnlocalizedName().substring(5));
		System.out.println("survivalmod] Registering Icon: survivalmod:" + this.getUnlocalizedName().substring(5));
	}*/
}
