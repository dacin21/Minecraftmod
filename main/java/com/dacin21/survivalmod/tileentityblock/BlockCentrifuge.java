package com.dacin21.survivalmod.tileentityblock;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.dacin21.survivalmod.survivalmod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCentrifuge extends BlockContainer {

    private final Random random = new Random();
    
    private IIcon iconTop, iconFront, iconBot;


	public BlockCentrifuge() {
		super(Material.wood);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	@Override
    public IIcon getIcon(int par1, int par2)
    {
        return par1 == 1 ? this.iconTop : (par1 == 0 ? this.iconBot : (par1 == 2  ? this.iconFront : this.blockIcon));
    }

    @SideOnly(Side.CLIENT)

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("survivalmod:centrifuge_side");
        this.iconTop = par1IconRegister.registerIcon("survivalmod:centrifuge_top");
        this.iconBot = par1IconRegister.registerIcon("survivalmod:centrifuge_bot");
        this.iconFront = par1IconRegister.registerIcon("survivalmod:centrifuge_front");
    }
	

	@Override
	/**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileCentrifuge createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
		TileCentrifuge centrifuge = new TileCentrifuge();
		return centrifuge;
    }
	
	
	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_)
    {
        super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
    }
	
	
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
    {
        TileCentrifuge tileentitychest = (TileCentrifuge)p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);

        if (tileentitychest != null)
        {
            for (int i1 = 0; i1 < tileentitychest.getSizeInventory(); ++i1)
            {
                ItemStack itemstack = tileentitychest.getStackInSlot(i1);

                if (itemstack != null)
                {
                    float f = this.random.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.random.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = this.random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; p_149749_1_.spawnEntityInWorld(entityitem))
                    {
                        int j1 = this.random.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize)
                        {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        entityitem = new EntityItem(p_149749_1_, (double)((float)p_149749_2_ + f), (double)((float)p_149749_3_ + f1), (double)((float)p_149749_4_ + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)this.random.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)this.random.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)this.random.nextGaussian() * f3);

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }
                    }
                }
            }

            p_149749_1_.func_147453_f(p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_);
        }

        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
    }
	
	
	/**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer player, int var6, float var7, float var8, float var9)
    {
        if (var1.isRemote)
        {
            return true;
        }
        else
        {
        	TileCentrifuge iinventory = this.func_149951_m(var1, var2,var3 , var4);

            if (iinventory != null)
            {
            	player.openGui(survivalmod.instance,2 , var1, var2, var3, var4);
            	return true;
            }

            return true;
        }
    }
	
	
    public TileCentrifuge func_149951_m(World p_149951_1_, int p_149951_2_, int p_149951_3_, int p_149951_4_)
    {
        TileCentrifuge object = (TileCentrifuge)p_149951_1_.getTileEntity(p_149951_2_, p_149951_3_, p_149951_4_);

        if (object == null)
        {
            return null;
        }
        else if (p_149951_1_.isSideSolid(p_149951_2_, p_149951_3_ + 1, p_149951_4_, DOWN))
        {
            return null;
        }
        else
        {
            return object;
        }
    }
	


	
	
	
	
	
	
	
	

}
