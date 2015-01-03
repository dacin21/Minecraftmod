package com.dacin21.survivalmod;

import ic2.api.item.IC2Items;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.dacin21.survivalmod.mob.EntityMath;
import com.dacin21.survivalmod.mob.ProjectileMath;
import com.dacin21.survivalmod.mob.RenderProjectileMath;
import com.dacin21.survivalmod.reactor.block.BlockFusionReactor;
import com.dacin21.survivalmod.reactor.block.BlockHeatExchanger;
import com.dacin21.survivalmod.reactor.block.BlockNeutronBoiler;
import com.dacin21.survivalmod.reactor.block.BlockSteamDistributor;
import com.dacin21.survivalmod.reactor.block.TileFusionReactor;
import com.dacin21.survivalmod.reactor.block.TileHeatExchanger;
import com.dacin21.survivalmod.reactor.block.TileSteamDistributor;
import com.dacin21.survivalmod.reactor.item.GasCell;
import com.dacin21.survivalmod.reactor.reactor.BlockFusionReactor2;
import com.dacin21.survivalmod.reactor.reactor.BlockReactorWall;
import com.dacin21.survivalmod.reactor.reactor.TileFusionReactor2;
import com.dacin21.survivalmod.reactor.reactor.TileReactorWall;
import com.dacin21.survivalmod.tileentityblock.BlockCentrifuge;
import com.dacin21.survivalmod.tileentityblock.TileCentrifuge;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler; // used in 1.6.2
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid=survivalmod.modid, name="dacin_survivalmod", version="1.0.0")

public class survivalmod {
	
		public static final String modid = "dacin_survivalmod";
	
	    public static Item fleshCluster;
	    public static Item solarBreaker;
	    public static Item nenderHeat;
	    public static Item doomBlade;
	    public static Item runicStaff;
	    public static Item runicStaffIcon;
	    public static ToolMaterial DoomTool;
	    public static Item hydrogenCell, deuteriumCell, tritiumCell;
	    public static Fluid deuteriumPlasma, tritiumPlasma;
	    public static Fluid steam;
	    
	
	    
	    public static Block infuserPit, centrifuge, fusionReactor,  fusionReactor2, reactorWall, heatExchanger, neutronBoiler, steamDistributor;
	    
	    
	    private static int modEntityID = 0;
		
		// The instance of your mod that Forge uses.
	    @Instance(value = survivalmod.modid)
	    public static survivalmod instance;
	    private survivalmodFuelHandler srvHandler;
	    private GuiHandlerInfuser guiHandlerDacin21 = new GuiHandlerInfuser();
	
	   
	    // Says where the client and server 'proxy' code is loaded.
	    @SidedProxy(clientSide="com.dacin21.survivalmod.client.ClientProxy", serverSide="com.dacin21.survivalmod.CommonProxy")
	    public static CommonProxy proxy;
	   
	    @EventHandler // used in 1.6.2
	    public void preInit(FMLPreInitializationEvent event) {
	    }
	   
	    @EventHandler // used in 1.6.2
	    public void load(FMLInitializationEvent event) {
	    	
	    	 
	        	srvHandler = (new survivalmodFuelHandler());
	        	GameRegistry.registerFuelHandler(srvHandler);
	        	
	        	DoomTool =EnumHelper.addToolMaterial("DoomTool", 5, 3200, 12.0F, 35.0F, 10);
	    		doItems();
	    		doBlocks();
	    		doMobs();
	    		
	    		
	    		
	    		NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandlerDacin21);
	    		
	
	    		// Now that we've registered the entity, tell the proxy to register the renderer
	    		proxy.registerRenderers();
	
	           
	    }
	    @EventHandler // used in 1.6.2
	    public void postInit(FMLPostInitializationEvent event) {
	    	steam = FluidRegistry.getFluid("steam");
	    	tritiumPlasma = new Fluid("Tritium_Plasma");
	    	FluidRegistry.registerFluid(tritiumPlasma);
	    	
	    	deuteriumPlasma = new Fluid("Deuterium_Plasma");
	    	FluidRegistry.registerFluid(deuteriumPlasma);
	    }
	    
	    
	    
	    
	    private void doItems(){
	    	fleshCluster= new GenericItem("fleshCluster").setMaxStackSize(64).setCreativeTab(tabDacin).setTextureName("survivalmod:fleshCluster");
	  		
	
	  		nenderHeat= new GenericItem("nenderHeat").setMaxStackSize(1).setCreativeTab(tabDacin).setTextureName("survivalmod:nenderHeat");
	  		
	  		solarBreaker= new GenericItem("solarBreaker").setMaxStackSize(64).setCreativeTab(tabDacin).setTextureName("survivalmod:solarBreaker");
	  		
	  		
	  		doomBlade= new DmSword(DoomTool).setMaxStackSize(1).setCreativeTab(tabDacin).setTextureName("survivalmod:doomBlade");
	  		
	  		runicStaff = new RunicStaff("runicStaff").setMaxStackSize(1).setCreativeTab(tabDacin).setTextureName("survivalmod:runicStaff");
	  		runicStaffIcon= new GenericItem("runicStaffIcon").setMaxStackSize(1).setCreativeTab(tabDacin).setTextureName("survivalmod:runicStaffIcon");
	  	   

	  		hydrogenCell = new GasCell("hydrogenCell").setTextureName("survivalmod:hydrogenCell");
	  		deuteriumCell = new GasCell("deuteriumCell").setTextureName("survivalmod:deuteriumCell");
	  		tritiumCell = new GasCell("tritiumCell").setTextureName("survivalmod:tritiumCell");
	  	   
	
	  		
	  		GameRegistry.addRecipe(new ItemStack(fleshCluster), "xx", "xx",
	  				'x', Items.rotten_flesh);
	  		GameRegistry.addSmelting(fleshCluster, new ItemStack(Items.leather), 0.1f);
   
	  		//GameRegistry.addSmelting(solarBreaker, IC2Items.getItem("solarPanel"), 0.1f);
	  		GameRegistry.addShapelessRecipe(new ItemStack(solarBreaker), Blocks.coal_block, Blocks.iron_block, Blocks.redstone_block, IC2Items.getItem("copperBlock"), IC2Items.getItem("hazmatBoots"), IC2Items.getItem("hazmatBoots"), IC2Items.getItem("tinIngot"), IC2Items.getItem("tinIngot"), IC2Items.getItem("tinIngot"));
   
	  		GameRegistry.addRecipe(new ItemStack(nenderHeat), "xzx", "yzy", "xzx",
	  				'x', Blocks.redstone_block, 'y', Items.lava_bucket, 'z', Items.blaze_rod );
	  		
	       
	   }
	   
	 
	   private void doBlocks(){
		   infuserPit= new BlockInfuser().setHardness(2.0F).setStepSound(Block.soundTypeAnvil).setBlockName("infuserPit").setCreativeTab(tabDacin).setResistance(10.0F).setBlockTextureName("survivalmod:infuserPit");
		   GameRegistry.registerBlock(infuserPit, "infuserPit");
		   
		   GameRegistry.addRecipe(new ItemStack(infuserPit), "w w", "xzx", "xyx",
	               'x', Blocks.iron_block, 'y', Blocks.crafting_table, 'z', Blocks.diamond_block, 'w', Items.golden_pickaxe );
		   
		   
		   
		   centrifuge = new BlockCentrifuge().setHardness(1.0F).setStepSound(Block.soundTypeWood).setBlockName("centrifuge").setCreativeTab(tabDacin).setResistance(10.0F);
		   GameRegistry.registerBlock(centrifuge, "centrifuge");
		   GameRegistry.registerTileEntity(TileCentrifuge.class, modid + ".entity.centrifuge");
		   
		   fusionReactor = new BlockFusionReactor().setHardness(10.0F).setStepSound(Block.soundTypeMetal).setBlockName("fusionReactor").setCreativeTab(tabDacin).setResistance(100.0F);
		   GameRegistry.registerBlock(fusionReactor, "fusionReaktor");
		   GameRegistry.registerTileEntity(TileFusionReactor.class, modid + ".entity.fusionReaktor");
		   
		   fusionReactor2 = new BlockFusionReactor2().setHardness(10.0F).setStepSound(Block.soundTypeMetal).setBlockName("fusionReactor2").setCreativeTab(tabDacin).setResistance(100.0F);
		   GameRegistry.registerBlock(fusionReactor2, "fusionReaktor2");
		   GameRegistry.registerTileEntity(TileFusionReactor2.class, modid + ".entity.fusionReaktor2");
		   
		   reactorWall = new BlockReactorWall().setHardness(10.0F).setStepSound(Block.soundTypeMetal).setBlockName("ReactorWall").setCreativeTab(tabDacin).setResistance(100.0F);
		   GameRegistry.registerBlock(reactorWall, "reactorWall");
		   GameRegistry.registerTileEntity(TileReactorWall.class, modid + ".entity.reactorWall");
		   
		   
		   
		   heatExchanger = new BlockHeatExchanger().setHardness(10.0F).setStepSound(Block.soundTypeMetal).setBlockName("heatExchanger").setCreativeTab(tabDacin).setResistance(100.0F);
		   GameRegistry.registerBlock(heatExchanger, "heatExchanger");
		   GameRegistry.registerTileEntity(TileHeatExchanger.class, modid + ".entity.heatExchanger");
		   

		   neutronBoiler = new BlockNeutronBoiler().setHardness(20.0F).setStepSound(Block.soundTypeAnvil).setBlockName("neutronBoiler").setCreativeTab(tabDacin).setResistance(100.0F).setBlockTextureName("survivalmod:neutronBoiler");
		   GameRegistry.registerBlock(neutronBoiler, "neutronBoiler");
		   
		   GameRegistry.addRecipe(new ItemStack(this.heatExchanger), "zzz", "xyx", "zzz",
				   'x', IC2Items.getItem("waterCell"), 'y', IC2Items.getItem("reactorVent"), 'z', IC2Items.getItem("plateiron"));

	  		GameRegistry.addRecipe(new ItemStack(this.neutronBoiler), "aza", "yxy", "bbb", 
	  				'x', IC2Items.getItem("waterMill"), 'y',  IC2Items.getItem("reactorReflector"), 'z', IC2Items.getItem("reactorCoolantSimple"), 'a', IC2Items.getItem("reactorHeatSwitch"), 'b', IC2Items.getItem("reactorPlating"));
  
	  		GameRegistry.addRecipe(new ItemStack(this.centrifuge), "wzw", "xyx", " z ",
	  				'x', IC2Items.getItem("extractor"), 'y', IC2Items.getItem("advancedMachine"), 'z', IC2Items.getItem("advancedAlloy"), 'w', IC2Items.getItem("elemotor"));
	  		
	  		steamDistributor = new BlockSteamDistributor().setHardness(20.0F).setStepSound(Block.soundTypeAnvil).setBlockName("steamDistributor").setCreativeTab(tabDacin).setResistance(100.0F).setBlockTextureName("survivalmod:steamDistributor");
	  		GameRegistry.registerBlock(steamDistributor, "steamDistributor");
	  		GameRegistry.registerTileEntity(TileSteamDistributor.class, modid + ".entity.steamDistributor");
	  		
	  		GameRegistry.addRecipe(new ItemStack(this.steamDistributor), "ywy", "yxy", "zyz",
	  				'x', Items.ender_pearl, 'y', IC2Items.getItem("denseplateiron"), 'z', IC2Items.getItem("coil"), 'w', IC2Items.getItem("waterCell"));
	   }
	   
	   private void doMobs(){
	
		   
		   	EntityRegistry.registerGlobalEntityID(EntityMath.class, "Math", (modEntityID=EntityRegistry.findGlobalUniqueEntityId()), 3515848, 12102);
		   	EntityRegistry.registerModEntity(EntityMath.class, "Math", modEntityID, this, 80, 3, true);
	       	EntityRegistry.addSpawn(EntityMath.class, 50, 2, 6, EnumCreatureType.monster, BiomeGenBase.plains, BiomeGenBase.forest, BiomeGenBase.forestHills, BiomeGenBase.savanna, BiomeGenBase.taiga, BiomeGenBase.taigaHills);
	//           	LanguageRegistry.instance().addStringLocalization("entity.survivalmod.Math.name", "Math");
	
		   	EntityRegistry.registerGlobalEntityID(EntityRune.class, "Rune", ++modEntityID);
			EntityRegistry.registerModEntity(EntityRune.class, "Rune", modEntityID, this, 64, 10, true);
			
		   	EntityRegistry.registerGlobalEntityID(ProjectileMath.class, "ProjectileMath", ++modEntityID);
			EntityRegistry.registerModEntity(ProjectileMath.class, "ProjectileMath", modEntityID, this, 64, 10, true);
		   
			
	   }
	   
	   
	   
	   
	   
	   public void addRenderer(Map map)
	 	 {
	 		    map.put(ProjectileMath.class, new RenderProjectileMath());
	 		    map.put(EntityRune.class, new RenderRunic(runicStaffIcon));
	 	 }
	   
	   public static CreativeTabs tabDacin = new CreativeTabs("DacinMod") {
			@Override
		    public ItemStack getIconItemStack() {
		        return new ItemStack(fusionReactor, 1, 0);
		    }
			@Override
	        @SideOnly(Side.CLIENT)
			public Item getTabIconItem() {
				return Item.getItemFromBlock(fusionReactor);
			}
		};
	   
	   
	   
}

		
