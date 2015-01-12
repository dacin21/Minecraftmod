package com.dacin21.survivalmod.reactor.producion;

import com.dacin21.survivalmod.survivalmod;
import com.dacin21.survivalmod.reactor.block.TileSteamDistributor;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockElectrolyzer extends BlockContainer {

	private IIcon iconTop, iconBot;

	protected BlockElectrolyzer(Material p_i45386_1_) {
		super(p_i45386_1_);
	}

	public BlockElectrolyzer() {
		this(Material.anvil);
	}

	@Override
	public IIcon getIcon(int par1, int par2) {
		return par1 == 1 ? this.iconTop : (par1 == 0 ? this.iconBot
				: this.blockIcon);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("survivalmod:electrolyzer_side");
		this.iconTop = par1IconRegister.registerIcon("survivalmod:electrolyzer_top");
		this.iconBot = par1IconRegister.registerIcon("survivalmod:electrolyzer_bot");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileElectrolyzer();
	}
	
	@Override
	public boolean onBlockActivated(World par1World, int x, int y, int z,
			EntityPlayer player, int var6, float var7, float var8, float var9) {

		if (par1World.isRemote)
		{
			return true;
		}

		TileElectrolyzer inventory = (TileElectrolyzer) par1World.getTileEntity(x, y, z);
		if (inventory == null) return true;
		player.openGui(survivalmod.instance, 6, par1World, x, y, z);
		return true;
	}

}
