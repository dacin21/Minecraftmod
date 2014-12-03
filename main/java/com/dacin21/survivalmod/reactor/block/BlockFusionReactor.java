package com.dacin21.survivalmod.reactor.block;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;

import java.util.Random;

import com.dacin21.survivalmod.survivalmod;
import com.dacin21.survivalmod.tileentityblock.TileCentrifuge;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockFusionReactor extends BlockContainer {

	private Random random = new Random();
	private IIcon iconTop, iconFront, iconBot, iconBack;

	public BlockFusionReactor() {
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileFusionReactor();
	}

	@Override
	public IIcon getIcon(int par1, int par2) {
		return par1 == 1 ? this.iconTop : (par1 == 0 ? this.iconBot
				: (par1 == 2 ? this.iconFront : (par1 == 3 ? this.iconBack
						: this.blockIcon)));
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister
				.registerIcon("survivalmod:fusion_side");
		this.iconTop = par1IconRegister.registerIcon("survivalmod:fusion_top");
		this.iconBot = par1IconRegister.registerIcon("survivalmod:fusion_bot");
		this.iconFront = par1IconRegister
				.registerIcon("survivalmod:fusion_front");
		this.iconBack = par1IconRegister
				.registerIcon("survivalmod:fusion_back");
	}
	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_,
			int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
		TileFusionReactor tileentitychest = (TileFusionReactor) p_149749_1_
				.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);

		if (tileentitychest != null) {
			for (int i1 = 0; i1 < tileentitychest.getSizeInventory(); ++i1) {
				ItemStack itemstack = tileentitychest.getStackInSlot(i1);

				if (itemstack != null) {
					float f = this.random.nextFloat() * 0.8F + 0.1F;
					float f1 = this.random.nextFloat() * 0.8F + 0.1F;
					EntityItem entityitem;

					for (float f2 = this.random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; p_149749_1_
							.spawnEntityInWorld(entityitem)) {
						int j1 = this.random.nextInt(21) + 10;

						if (j1 > itemstack.stackSize) {
							j1 = itemstack.stackSize;
						}

						itemstack.stackSize -= j1;
						entityitem = new EntityItem(p_149749_1_,
								(double) ((float) p_149749_2_ + f),
								(double) ((float) p_149749_3_ + f1),
								(double) ((float) p_149749_4_ + f2),
								new ItemStack(itemstack.getItem(), j1,
										itemstack.getItemDamage()));
						float f3 = 0.05F;
						entityitem.motionX = (double) ((float) this.random
								.nextGaussian() * f3);
						entityitem.motionY = (double) ((float) this.random
								.nextGaussian() * f3 + 0.2F);
						entityitem.motionZ = (double) ((float) this.random
								.nextGaussian() * f3);

						if (itemstack.hasTagCompound()) {
							entityitem.getEntityItem().setTagCompound(
									(NBTTagCompound) itemstack.getTagCompound()
											.copy());
						}
					}
				}
			}

			p_149749_1_.func_147453_f(p_149749_2_, p_149749_3_, p_149749_4_,
					p_149749_5_);
		}

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
		TileFusionReactor entity = ((TileFusionReactor)par1World.getTileEntity(x, y, z));
		entity.checkForMultiBlock();
		if(entity.checkCompletion()){
			TileFusionReactor inventory = (TileFusionReactor) par1World.getTileEntity(x, y, z);
			if(inventory == null) return true;
			player.openGui(survivalmod.instance,1 , par1World, x, y, z);
		}
		else {
			if(entity.checkCompletion())
				MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText("FusionReactor completed successfully"));
			else

				MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText("FusionReactor incomplete"));
		}
		return true;
	}
	
	
	public TileFusionReactor func_149951_m(World p_149951_1_, int p_149951_2_,
			int p_149951_3_, int p_149951_4_) {
		TileFusionReactor object = (TileFusionReactor) p_149951_1_
				.getTileEntity(p_149951_2_, p_149951_3_, p_149951_4_);

		if (object == null) {
			return null;
		} else if (p_149951_1_.isSideSolid(p_149951_2_, p_149951_3_ + 1,
				p_149951_4_, DOWN)) {
			return null;
		} else {
			return object;
		}
	}

}
