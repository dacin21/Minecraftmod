package com.dacin21.survivalmod.reactor.producion;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import com.dacin21.survivalmod.survivalmod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockHydroFluid extends BlockFluidClassic {
	private MapColor color;

    @SideOnly(Side.CLIENT)
    public static IIcon stillIconh,stillIcond,stillIcont;
    public static IIcon flowIconh,flowIcond,flowIcont;
   
    public BlockHydroFluid(Fluid fluid, Material material) {
            super(fluid, material);
            setCreativeTab(survivalmod.tabDacin);
    }
    @Override
    public MapColor getMapColor(int index){
    	return super.getMapColor(index);
    }
   
    @Override
    public IIcon getIcon(int side, int meta) {
		if(this.density == survivalmod.hydrogen.getDensity()) return stillIconh;
		if(this.density == survivalmod.deuteriumPlasma.getDensity()) return stillIcond;
		if(this.density == survivalmod.tritiumPlasma.getDensity()) return stillIcont;
		if(this.stack.getFluid().getID() == survivalmod.hydrogen.getID()) return stillIconh;
		if(this.stack.getFluid().getID() == survivalmod.deuteriumPlasma.getID()) return stillIcond;
		if(this.stack.getFluid().getID() == survivalmod.tritiumPlasma.getID()) return stillIcont;
		return null;
            
    }
   
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
    		flowIconh = stillIconh = register.registerIcon("survivalmod:fluidHydrogen");
    		flowIcond = stillIcond = register.registerIcon("survivalmod:fluidDeuterium");
    		flowIcont = stillIcont = register.registerIcon("survivalmod:fluidTritium");
    		
    		survivalmod.hydrogen.setIcons(stillIconh);
    		survivalmod.deuteriumPlasma.setIcons(stillIcond);
    		survivalmod.tritiumPlasma.setIcons(stillIcont);
            
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
