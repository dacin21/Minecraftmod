package com.dacin21.survivalmod.client;

import com.dacin21.survivalmod.CommonProxy;
import com.dacin21.survivalmod.survivalmod;
import com.dacin21.survivalmod.mob.EntityMath;
import com.dacin21.survivalmod.mob.ModelMath;
import com.dacin21.survivalmod.mob.ProjectileMath;
import com.dacin21.survivalmod.mob.RenderMath;
import com.dacin21.survivalmod.mob.RenderProjectileMath;
import com.dacin21.survivalmod.teleStaff.EntityRune;
import com.dacin21.survivalmod.teleStaff.RenderRunic;
import com.dacin21.survivalmod.turbine.rotor.TileTurbineRotor;
import com.dacin21.survivalmod.turbine.rotor.TileTurbineRotorRenderer;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
       
	@Override
	public void registerRenderers() {
	RenderingRegistry.registerEntityRenderingHandler(EntityMath.class, new RenderMath(new ModelMath(), 0.5F));
	RenderingRegistry.registerEntityRenderingHandler(EntityRune.class, new RenderRunic(survivalmod.runicStaffIcon));
	RenderingRegistry.registerEntityRenderingHandler(ProjectileMath.class, new RenderProjectileMath());
	ClientRegistry.bindTileEntitySpecialRenderer(TileTurbineRotor.class, new TileTurbineRotorRenderer());
	
	//ClientRegistry.bindTileEntitySpecialRenderer(TileTestMultiBlock.class, new TileTestRenderer());
	}

    

       
}
