package com.dacin21.survivalmod.reactor.block;

import com.dacin21.survivalmod.survivalmod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockSteamDistributor extends BlockContainer {

	private IIcon iconTop, iconBot;

	public BlockSteamDistributor() {
		super(Material.anvil);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileSteamDistributor();
	}
	
	@Override
	public IIcon getIcon(int par1, int par2) {
		return par1 == 1 ? this.iconTop : (par1 == 0 ? this.iconBot
				:  this.blockIcon);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister
				.registerIcon("survivalmod:distributor_side");
		this.iconTop = par1IconRegister.registerIcon("survivalmod:distributor_top");
		this.iconBot = par1IconRegister.registerIcon("survivalmod:distributor_bot");
	}
	
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_,
			int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
		super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_,
				p_149749_5_, p_149749_6_);
	}
	
	@Override
	public boolean onBlockActivated(World par1World, int x, int y, int z,
			EntityPlayer player, int var6, float var7, float var8, float var9) {
		
		if (par1World.isRemote)
        {
            return true;
        }

		TileSteamDistributor inventory = (TileSteamDistributor) par1World.getTileEntity(x, y, z);
		if(inventory == null) return true;
		player.openGui(survivalmod.instance,4 , par1World, x, y, z);
		return true;
	}

}

