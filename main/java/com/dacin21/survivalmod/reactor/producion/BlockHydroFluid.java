package com.dacin21.survivalmod.reactor.producion;

import com.dacin21.survivalmod.survivalmod;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockHydroFluid extends BlockFluidClassic {

    @SideOnly(Side.CLIENT)
    protected IIcon stillIconh,stillIcond,stillIcont;
   
    public BlockHydroFluid(Fluid fluid, Material material) {
            super(fluid, material);
            setCreativeTab(survivalmod.tabDacin);
    }
   
    @Override
    public IIcon getIcon(int side, int meta) {
		if(this.density == survivalmod.hydrogen.getDensity()) return stillIconh;
		if(this.density == survivalmod.deuteriumPlasma.getDensity()) return stillIcond;
		if(this.density == survivalmod.tritiumPlasma.getDensity()) return stillIcont;
		return null;
            
    }
   
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
            stillIconh = register.registerIcon("survivalmod:fluidHydrogen");
            stillIcond = register.registerIcon("survivalmod:fluidDeuterium");
            stillIcont = register.registerIcon("survivalmod:fluidTritium");
    }
   
    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
            if (world.getBlock(x,  y,  z).getMaterial().isLiquid()) return false;
            return super.canDisplace(world, x, y, z);
    }
   
    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z) {
            if (world.getBlock(x,  y,  z).getMaterial().isLiquid()) return false;
            return super.displaceIfPossible(world, x, y, z);
    }
   
}
