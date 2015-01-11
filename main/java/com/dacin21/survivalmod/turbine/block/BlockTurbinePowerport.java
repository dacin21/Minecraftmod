package com.dacin21.survivalmod.turbine.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.dacin21.survivalmod.survivalmod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockTurbinePowerport extends BlockContainer {

	protected BlockTurbinePowerport(Material p_i45394_1_) {
		super(p_i45394_1_);
	}
	
	public BlockTurbinePowerport(){
		this(Material.anvil);
	}
	

    @Override
    public IIcon getIcon(int par1, int par2)
    {
        return this.blockIcon;
    }

    @SideOnly(Side.CLIENT)

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("survivalmod:turbinePowerport");
    }
	
	@Override
	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5Player, int par6, float par7, float par8, float par9)
    {
        	return false;
    }
	


	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileTurbinePowerport();
	}
	
	
	
	


}




