package com.dacin21.survivalmod.reactor.producion;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
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
		this.blockIcon = par1IconRegister.registerIcon("survivalmod:neutronizer_side");
		this.iconTop = par1IconRegister.registerIcon("survivalmod:neutronizer_top");
		this.iconBot = par1IconRegister.registerIcon("survivalmod:neutronizer_bot");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileElectrolyzer();
	}

}
