package com.dacin21.survivalmod.turbine.block;

import com.dacin21.survivalmod.survivalmod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTurbineGlass extends BlockContainer {

	protected BlockTurbineGlass(Material p_i45394_1_) {
		super(p_i45394_1_);
	}
	
	public BlockTurbineGlass(){
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
        this.blockIcon = par1IconRegister.registerIcon("survivalmod:turbineGlass");
    }
	
	@Override
	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5Player, int par6, float par7, float par8, float par9)
    {
		TileTurbineGlass myTile = (TileTurbineGlass) par1World.getTileEntity(x, y, z);
        if (par1World.isRemote && myTile.hasMaster())
        {
            return true;
        }
        else if(!myTile.hasMaster())
        {
            return false;
        }
        else
        {
        	return false;
        }
    }
	@Override
    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_)
    {
        Block block = p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_);

        if (Block.isEqualTo(block,(p_149646_1_.getBlock(p_149646_2_ - Facing.offsetsXForSide[p_149646_5_], p_149646_3_ - Facing.offsetsYForSide[p_149646_5_], p_149646_4_ - Facing.offsetsZForSide[p_149646_5_]))))
        {
            return false;
        }
        //return true;
        return super.shouldSideBeRendered(p_149646_1_, p_149646_2_, p_149646_3_, p_149646_4_, p_149646_5_);
    }
	

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 0;
    }
    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }
	


	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileTurbineGlass();
	}
	
	

}


