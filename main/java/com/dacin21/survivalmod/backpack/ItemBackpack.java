package com.dacin21.survivalmod.backpack;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import com.dacin21.survivalmod.survivalmod;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemBackpack extends Item {
	private Random random;
	public ItemBackpack(String unlocalizedName) {
		super();
		random = new Random();
		setUnlocalizedName(survivalmod.modid+ "_" + unlocalizedName);
		setMaxStackSize(1);
		setCreativeTab(survivalmod.tabDacin);
		setTextureName("survivalmod:doomBlade");
		GameRegistry.registerItem(this, survivalmod.modid+ "_" + unlocalizedName);
	}
	
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10){
		par2EntityPlayer.openGui(survivalmod.modid, 101, par3World, par4, par5, par6);
		return true;
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
	}
	
	private void initNBT(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
		par1ItemStack.stackTagCompound = new NBTTagCompound();
		par1ItemStack.stackTagCompound.setInteger("size", 1);
		par1ItemStack.stackTagCompound.setInteger("ID", random.nextInt());

        NBTTagList nbttaglist = new NBTTagList();
        for(int i=0 ;i<9;i++){
        	 NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        	 nbttagcompound1.setBoolean("hasItem", true);
        	 //new ItemStack(Item.getItemById(i+10),5).writeToNBT(nbttagcompound1);
        	 nbttaglist.appendTag(nbttagcompound1);
        }
        par1ItemStack.stackTagCompound.setTag("items", nbttaglist);
       
	}

	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		super.onCreated(par1ItemStack, par2World, par3EntityPlayer);
		if(par1ItemStack.stackTagCompound==null){
			this.initNBT(par1ItemStack, par2World, par3EntityPlayer);
		}
	}

	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
		super.onPlayerStoppedUsing(par1ItemStack, par2World, par3EntityPlayer, par4);
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
		par3List.add("This is a Backpack");
		if(par1ItemStack.stackTagCompound!=null){
			if(par1ItemStack.stackTagCompound.getInteger("size")!= 0){
				par3List.add("Containing " + par1ItemStack.stackTagCompound.getInteger("size")*9 + " Items!");
			}
		}
	}

	public boolean canItemEditBlocks(){
		return super.canItemEditBlocks();
	}

	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ){
		if(stack.stackTagCompound == null){
			this.initNBT(stack, world, player);
		}
		return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}
}
