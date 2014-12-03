package com.dacin21.survivalmod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.client.renderer.texture.IIconRegister;


public class GenericBlock extends Block
{

        public GenericBlock ()
        {
                super(Material.iron);
                setCreativeTab(CreativeTabs.tabBlock);
        }

}
