package com.dacin21.survivalmod.reactor.reactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockSteamPipe extends BlockContainer {
	IIcon iconTop, iconBot;

	protected BlockSteamPipe(Material p_i45386_1_) {
		super(p_i45386_1_);
	}
	
	public BlockSteamPipe(){
		this(Material.anvil);
	}
	

    @Override
    public IIcon getIcon(int par1, int par2)
    {
        return par1 == 1 ? this.iconTop : (par1 == 0 ? this.iconBot :  this.blockIcon);
    }

    @SideOnly(Side.CLIENT)

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("survivalmod:steam_side");
        this.iconTop = par1IconRegister.registerIcon("survivalmod:steam_top");
        this.iconBot = par1IconRegister.registerIcon("survivalmod:steam_bot");
    }
	
	

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileSteamPipe();
	}

}
