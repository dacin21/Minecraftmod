package com.dacin21.survivalmod;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.dacin21.survivalmod.reactor.block.ContainerSteamDistributor;
import com.dacin21.survivalmod.reactor.block.GuiSteamDistributor;
import com.dacin21.survivalmod.reactor.block.TileSteamDistributor;
import com.dacin21.survivalmod.reactor.producion.ContainerElectrolyzer;
import com.dacin21.survivalmod.reactor.producion.ContainerNeutronizer;
import com.dacin21.survivalmod.reactor.producion.GuiElectrolyzer;
import com.dacin21.survivalmod.reactor.producion.GuiNeutronizer;
import com.dacin21.survivalmod.reactor.producion.TileElectrolyzer;
import com.dacin21.survivalmod.reactor.producion.TileNeutronizer;
import com.dacin21.survivalmod.reactor.reactor.ContainerFusionReactor2;
import com.dacin21.survivalmod.reactor.reactor.GuiFusionReactor2;
import com.dacin21.survivalmod.reactor.reactor.TileFusionReactor2;
import com.dacin21.survivalmod.turbine.block.ContainerTurbine;
import com.dacin21.survivalmod.turbine.block.GuiTurbine;
import com.dacin21.survivalmod.turbine.block.IMultiblockTurbineServ;
import com.dacin21.survivalmod.turbine.block.TileTurbineRotorbase;

import cpw.mods.fml.common.network.IGuiHandler;


public class GuiHandlerInfuser implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile_entity = world.getTileEntity(x, y, z);
		switch (id)
		{
			case 0:
				return world.getBlock(x, y, z) == survivalmod.infuserPit ? new ContainerInfuser(player.inventory, world, x, y, z) : null;
			case 1:
				return world.getBlock(x, y, z) == survivalmod.turbineBase ? new ContainerTurbine(player.inventory, (TileTurbineRotorbase) tile_entity) : null;
			case 2: {
				if (tile_entity != null && tile_entity instanceof IMultiblockTurbineServ) {
					IMultiblockTurbineServ serv = (IMultiblockTurbineServ) tile_entity;
					if (serv.hasMaster() && serv.getMaster() != null) {
						return new ContainerTurbine(player.inventory, serv.getMaster(), tile_entity);
					}
				}
				return null;
			}
			case 3:
				return world.getBlock(x, y, z) == survivalmod.neutronizer ? new ContainerNeutronizer(player.inventory, (TileNeutronizer) tile_entity) : null;
			case 4:
				return world.getBlock(x, y, z) == survivalmod.steamDistributor ? new ContainerSteamDistributor(player.inventory, (TileSteamDistributor) tile_entity) : null;
			case 5:
				return world.getBlock(x, y, z) == survivalmod.fusionReactor2 ? new ContainerFusionReactor2(player.inventory, (TileFusionReactor2) tile_entity) : null;
			case 6:
				return world.getBlock(x, y, z) == survivalmod.electrolyzer ? new ContainerElectrolyzer(player.inventory, (TileElectrolyzer)tile_entity) : null;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile_entity = world.getTileEntity(x, y, z);
		switch (id)
		{
			case 0:
				return world.getBlock(x, y, z) == survivalmod.infuserPit ? new GuiInfuser(player.inventory, world, x, y, z) : null;
			case 1:
				return world.getBlock(x, y, z) == survivalmod.turbineBase ? new GuiTurbine(player.inventory, (TileTurbineRotorbase) tile_entity) : null;
			case 2: {
				if (tile_entity != null && tile_entity instanceof IMultiblockTurbineServ) {
					IMultiblockTurbineServ serv = (IMultiblockTurbineServ) tile_entity;
					if (serv.hasMaster() && serv.getMaster() != null) {
						return new GuiTurbine(player.inventory, serv.getMaster(), tile_entity);
					}
				}
				return null;
			}
			case 3:
				return world.getBlock(x, y, z) == survivalmod.neutronizer ? new GuiNeutronizer(player.inventory, (TileNeutronizer) tile_entity) : null;
			case 4:
				return world.getBlock(x, y, z) == survivalmod.steamDistributor ? new GuiSteamDistributor(player.inventory, (TileSteamDistributor) tile_entity) : null;
			case 5:
				return world.getBlock(x, y, z) == survivalmod.fusionReactor2 ? new GuiFusionReactor2(player.inventory, (TileFusionReactor2) world.getTileEntity(x, y, z)) : null;
			case 6: 
				return world.getBlock(x, y, z) == survivalmod.electrolyzer ? new GuiElectrolyzer(player.inventory, (TileElectrolyzer)tile_entity) : null;
				
		}
		return null;
	}



}
