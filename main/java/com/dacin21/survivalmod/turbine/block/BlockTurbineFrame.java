package com.dacin21.survivalmod.turbine.block;

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

public class BlockTurbineFrame extends BlockContainer {
	IIcon iconTop, iconBot;

	protected BlockTurbineFrame(Material p_i45394_1_) {
		super(p_i45394_1_);
	}
	
	public BlockTurbineFrame(){
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
        this.blockIcon = par1IconRegister.registerIcon("survivalmod:turbineFrame");
    }
	
	@Override
	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5Player, int par6, float par7, float par8, float par9)
    {
		TileTurbineFrame myTile = (TileTurbineFrame) par1World.getTileEntity(x, y, z);
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
        	/*int[] mcoords = myTile.getMasterCoords();
        	TileFusionReactor2 entity = ((TileFusionReactor2)par1World.getTileEntity(mcoords[0], mcoords[1], mcoords[2]));
    		if(entity == null) return true;
    		if(entity.checkCompletion()){
    			par5Player.openGui(survivalmod.instance,5 , par1World, x, y, z);
    		} else {
    			System.err.print("Corrds of uninitialized Master foung! This is a BUG");
    		}*/
        	
        	return false;
        }
    }
	


	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileTurbineFrame();
	}
	
	
	
	


}

