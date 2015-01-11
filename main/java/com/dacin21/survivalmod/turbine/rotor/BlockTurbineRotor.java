package com.dacin21.survivalmod.turbine.rotor;

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

public class BlockTurbineRotor extends BlockContainer {

	protected BlockTurbineRotor(Material p_i45394_1_) {
		super(p_i45394_1_);
	}
	
	public BlockTurbineRotor(){
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
        this.blockIcon = par1IconRegister.registerIcon("survivalmod:turbineRotor");
    }
	
	@Override
	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5Player, int par6, float par7, float par8, float par9)
    {
		TileTurbineRotor myTile = (TileTurbineRotor) par1World.getTileEntity(x, y, z);
        if (par1World.isRemote && myTile.hasMaster())
        {
            return true;
        }
        else if(!myTile.hasMaster())
        {
            //myTile.direction = (myTile.direction + 1) & 3;
        	return false;
        }
        else
        {
        	return false;
        }
    }
	


	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileTurbineRotor();
	}
	
	//No normal renderer
	@Override
    public int getRenderType() {
            return -1;
    }
   
    //It's not an opaque cube, so you need this.
    @Override
    public boolean isOpaqueCube() {
            return false;
    }
   
    //It's not a normal block, so you need this too.
    public boolean renderAsNormalBlock() {
            return false;
    }
	
	


}


