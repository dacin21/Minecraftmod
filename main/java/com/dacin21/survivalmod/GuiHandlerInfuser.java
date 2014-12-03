package com.dacin21.survivalmod;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.dacin21.survivalmod.reactor.block.ContainerFusionReactor;
import com.dacin21.survivalmod.reactor.block.ContainerHeatExchanger;
import com.dacin21.survivalmod.reactor.block.ContainerSteamDistributor;
import com.dacin21.survivalmod.reactor.block.GuiFusionReactor;
import com.dacin21.survivalmod.reactor.block.GuiHeatExchanger;
import com.dacin21.survivalmod.reactor.block.GuiSteamDistributor;
import com.dacin21.survivalmod.reactor.block.TileFusionReactor;
import com.dacin21.survivalmod.reactor.block.TileHeatExchanger;
import com.dacin21.survivalmod.reactor.block.TileSteamDistributor;
import com.dacin21.survivalmod.tileentityblock.ContainerCentrifuge;
import com.dacin21.survivalmod.tileentityblock.GuiCentrifuge;
import com.dacin21.survivalmod.tileentityblock.TileCentrifuge;

import cpw.mods.fml.common.network.IGuiHandler;


public class GuiHandlerInfuser implements IGuiHandler
{
	@Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
            TileEntity tile_entity = world.getTileEntity(x, y, z);
            switch(id)
            {
                    case 0: return id == 0 && world.getBlock(x, y, z) == survivalmod.infuserPit ? new ContainerInfuser(player.inventory, world, x, y, z) : null;
                    case 1: return id == 1 && world.getBlock(x, y, z) == survivalmod.fusionReactor ? new ContainerFusionReactor(player.inventory,(TileFusionReactor) tile_entity) : null;
                    case 2: return id == 2 && world.getBlock(x, y, z) == survivalmod.centrifuge ?  new ContainerCentrifuge(player.inventory,(TileCentrifuge) tile_entity) : null;
                    case 3: return id == 3 && world.getBlock(x, y, z) == survivalmod.heatExchanger ?  new ContainerHeatExchanger(player.inventory,(TileHeatExchanger) tile_entity) : null;
                    case 4: return id == 4 && world.getBlock(x, y, z) == survivalmod.steamDistributor ?  new ContainerSteamDistributor(player.inventory,(TileSteamDistributor) tile_entity) : null;
                    
            }
            return null;
           }
	@Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
            TileEntity tile_entity = world.getTileEntity(x, y, z);
            switch(id)
            {
                    case 0: return id == 0 && world.getBlock(x, y, z) == survivalmod.infuserPit ? new GuiInfuser(player.inventory, world, x, y, z) : null;
                    case 1: return id == 1 && world.getBlock(x, y, z) == survivalmod.fusionReactor ? new GuiFusionReactor(player.inventory, (TileFusionReactor) world.getTileEntity(x, y, z)) : null;
                    case 2: return id == 2 && world.getBlock(x, y, z) == survivalmod.centrifuge ?  new GuiCentrifuge(player.inventory,(TileCentrifuge) world.getTileEntity(x, y, z)) : null;
                    case 3: return id == 3 && world.getBlock(x, y, z) == survivalmod.heatExchanger ?  new GuiHeatExchanger(player.inventory,(TileHeatExchanger) tile_entity) : null;
                    case 4: return id == 4 && world.getBlock(x, y, z) == survivalmod.steamDistributor ?  new GuiSteamDistributor(player.inventory,(TileSteamDistributor) tile_entity) : null;
            }
            return null;
    }


		
		
}
