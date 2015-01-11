package com.dacin21.survivalmod.turbine.block;

import ic2.api.energy.tile.IEnergySource;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.dacin21.survivalmod.survivalmod;

public class BlockTurbineRotorbase extends BlockContainer {

	private Random random = new Random();
	private IIcon iconTop, iconBot;

	public BlockTurbineRotorbase() {
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileTurbineRotorbase();
	}

	@Override
	public IIcon getIcon(int par1, int par2) {
		return par1 == 1 ? this.iconTop : (par1 == 0 ? this.iconBot: this.blockIcon);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister
				.registerIcon("survivalmod:turbineRotorbase_side");
		this.iconTop = par1IconRegister.registerIcon("survivalmod:turbineRotorbase_top");
		this.iconBot = par1IconRegister.registerIcon("survivalmod:turbineRotorbase_bot");
	}
	
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5Block, int par6) {
		super.breakBlock(par1World, par2, par3, par4, par5Block, par6);
	}
	
	@Override
	public boolean onBlockActivated(World par1World, int x, int y, int z,
			EntityPlayer player, int var6, float var7, float var8, float var9) {
		
		if (par1World.isRemote)
        {
            return true;
        }
		TileTurbineRotorbase entity = ((TileTurbineRotorbase)par1World.getTileEntity(x, y, z));
		if(entity == null) return true;
		if(entity.checkCompletion()){
			TileTurbineRotorbase inventory = (TileTurbineRotorbase) par1World.getTileEntity(x, y, z);
			player.openGui(survivalmod.instance,5 , par1World, x, y, z);
		}
		else {
			entity.checkForMultiBlock();
			if(entity.checkCompletion())
				MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText("Turbine completed successfully"));
			else

				MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText("Turbine incomplete"));
		}
		return true;
	}

}

