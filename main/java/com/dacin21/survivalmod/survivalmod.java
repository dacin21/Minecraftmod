package com.dacin21.survivalmod;

import ic2.api.item.IC2Items;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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

import com.dacin21.survivalmod.backpack.FunctionNBTChange;
import com.dacin21.survivalmod.backpack.ItemBackpack;
import com.dacin21.survivalmod.backpack.NBTRecipe;
import com.dacin21.survivalmod.mob.EntityMath;
import com.dacin21.survivalmod.mob.ProjectileMath;
import com.dacin21.survivalmod.mob.RenderProjectileMath;
import com.dacin21.survivalmod.reactor.block.BlockSteamDistributor;
import com.dacin21.survivalmod.reactor.block.TileSteamDistributor;
import com.dacin21.survivalmod.reactor.producion.BlockElectrolyzer;
import com.dacin21.survivalmod.reactor.producion.BlockHydroFluid;
import com.dacin21.survivalmod.reactor.producion.BlockNeutronizer;
import com.dacin21.survivalmod.reactor.producion.TileElectrolyzer;
import com.dacin21.survivalmod.reactor.producion.TileNeutronizer;
import com.dacin21.survivalmod.reactor.reactor.BlockFusionReactor2;
import com.dacin21.survivalmod.reactor.reactor.BlockReactorWall;
import com.dacin21.survivalmod.reactor.reactor.BlockSteamPipe;
import com.dacin21.survivalmod.reactor.reactor.TileFusionReactor2;
import com.dacin21.survivalmod.reactor.reactor.TileReactorWall;
import com.dacin21.survivalmod.reactor.reactor.TileSteamPipe;
import com.dacin21.survivalmod.teleStaff.EntityRune;
import com.dacin21.survivalmod.teleStaff.ItemElectricRunicStaff;
import com.dacin21.survivalmod.teleStaff.RenderRunic;
import com.dacin21.survivalmod.teleStaff.RunicStaff;
import com.dacin21.survivalmod.turbine.block.BlockTurbineFluidport;
import com.dacin21.survivalmod.turbine.block.BlockTurbineFrame;
import com.dacin21.survivalmod.turbine.block.BlockTurbineGlass;
import com.dacin21.survivalmod.turbine.block.BlockTurbinePowerport;
import com.dacin21.survivalmod.turbine.block.BlockTurbineRotorbase;
import com.dacin21.survivalmod.turbine.block.TileTurbineFluidport;
import com.dacin21.survivalmod.turbine.block.TileTurbineFrame;
import com.dacin21.survivalmod.turbine.block.TileTurbineGlass;
import com.dacin21.survivalmod.turbine.block.TileTurbinePowerport;
import com.dacin21.survivalmod.turbine.block.TileTurbineRotorbase;
import com.dacin21.survivalmod.turbine.rotor.BlockTurbineRotor;
import com.dacin21.survivalmod.turbine.rotor.TileTurbineRotor;

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

@Mod(modid = survivalmod.modid, name = "dacin_survivalmod", version = "1.0.0")
public class survivalmod {

	public static final String modid = "dacin_survivalmod";
	
	public static final int PlasmaCount = 50;
	public static final int SteamCount = 1000;
	public static final int EuPerSteam = 1000;

	public static Item fleshCluster;
	//public static Item solarBreaker;
	public static Item nenderHeat;
	public static Item doomBlade;
	public static Item runicStaff, electricRunicStaff;
	public static Item runicStaffIcon;
	
	public static Item backpack;
	
	public static ToolMaterial DoomTool;
	
	public static Fluid deuteriumPlasma, tritiumPlasma, hydrogen;
	public static Fluid steam;
	
	public static ItemStack graviSuitMagnetron, graviSuitSuperconductor, graviSuitCoolingCore;



	public static Block infuserPit, steamDistributor;
	public static Block fusionReactor2, reactorWall, steamPipe, neutronizer, electrolyzer;
	public static Block hFluidBlock, dFluidBlock, tFluidBlock;
	public static Block turbineGlass, turbineFrame, turbinePowerport, turbineFluidport, turbineBase, turbineRotor;

	private static int modEntityID = 0;

	// The instance of your mod that Forge uses.
	@Instance(value = survivalmod.modid)
	public static survivalmod instance;
	private survivalmodFuelHandler srvHandler;
	private GuiHandlerInfuser guiHandlerDacin21 = new GuiHandlerInfuser();


	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "com.dacin21.survivalmod.client.ClientProxy", serverSide = "com.dacin21.survivalmod.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	// used in 1.6.2
	public void preInit(FMLPreInitializationEvent event) {
		doFluids();
		graviSuitMagnetron= graviSuitSuperconductor= graviSuitCoolingCore= new ItemStack(Item.getItemById(20));
	}

	@EventHandler
	// used in 1.6.2
	public void load(FMLInitializationEvent event) {
		

		srvHandler = (new survivalmodFuelHandler());
		GameRegistry.registerFuelHandler(srvHandler);
		
		//Reflection hacks to get Gravisuit "Api"
		Class<?> graviSuiteClass;
		try {
			graviSuiteClass = Class.forName("gravisuite.GraviSuite");
			Object obj = graviSuiteClass.getField("magnetron").get(null);
			if(obj!=null) graviSuitMagnetron = (ItemStack) obj;
			Object obj2 = graviSuiteClass.getField("superConductor").get(null);
			if(obj2!=null) graviSuitSuperconductor = (ItemStack) obj2;
			Object obj3 = graviSuiteClass.getField("coolingCore").get(null);
			if(obj3!=null) graviSuitCoolingCore = (ItemStack) obj3;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			graviSuitMagnetron = null;
		}
		//End of Reflection hacks
		DoomTool = EnumHelper.addToolMaterial("DoomTool", 5, 3200, 12.0F, 35.0F, 10);
		doItems();
		doBlocks();
		//doFluids();
		doMobs();



		NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandlerDacin21);


		// Now that we've registered the entity, tell the proxy to register the
		// renderer
		proxy.registerRenderers();


	}

	@EventHandler
	// used in 1.6.2
	public void postInit(FMLPostInitializationEvent event) {
		steam = FluidRegistry.getFluid("steam");
		if (steam == null) {
			steam = new Fluid("steam");
			FluidRegistry.registerFluid(steam);
		}

	}


	private void doItems() {
		fleshCluster = new GenericItem("fleshCluster").setMaxStackSize(64).setCreativeTab(tabDacin).setTextureName("survivalmod:fleshCluster");


		nenderHeat = new GenericItem("nenderHeat").setMaxStackSize(1).setCreativeTab(tabDacin).setTextureName("survivalmod:nenderHeat");


		doomBlade = new DmSword(DoomTool).setMaxStackSize(1).setCreativeTab(tabDacin).setTextureName("survivalmod:doomBlade");

		runicStaff = new RunicStaff("runicStaff").setMaxStackSize(1).setCreativeTab(tabDacin).setTextureName("survivalmod:runicStaff");
		electricRunicStaff = new ItemElectricRunicStaff("electricRunicStaff").setMaxStackSize(1).setCreativeTab(tabDacin).setTextureName("survivalmod:runicStaff");
		runicStaffIcon = new GenericItem("runicStaffIcon").setMaxStackSize(1).setCreativeTab(tabDacin).setTextureName("survivalmod:runicStaffIcon");

		backpack = new ItemBackpack("backpack");
		NBTRecipe.addNBTRecipe(new ItemStack(backpack), FunctionNBTChange.BackpackBuildup," w ", "yxy","vzv",
				'x', backpack, 'y', Blocks.chest, 'z', IC2Items.getItem("iridiumPlate"), 'w', Blocks.hopper, 'v', Items.string);
		GameRegistry.addRecipe(new ItemStack(backpack), "vwv", "yxy", "zzz",
				'x',Blocks.diamond_block, 'y', Blocks.chest, 'z', Blocks.lapis_block, 'w', Items.comparator, 'v', Blocks.quartz_block);

		GameRegistry.addRecipe(new ItemStack(fleshCluster), "xx", "xx",
				'x', Items.rotten_flesh);
		GameRegistry.addSmelting(fleshCluster, new ItemStack(Items.leather), 0.1f);

		
		GameRegistry.addRecipe(new ItemStack(nenderHeat), "xzx", "yzy", "xzx",
				'x', Blocks.redstone_block, 'y', Items.lava_bucket, 'z', Items.blaze_rod);
	}


	private void doBlocks() {
		infuserPit = new BlockInfuser().setHardness(2.0F).setStepSound(Block.soundTypeAnvil).setBlockName("infuserPit").setCreativeTab(tabDacin).setResistance(10.0F).setBlockTextureName("survivalmod:infuserPit");
		GameRegistry.registerBlock(infuserPit, "infuserPit");

		GameRegistry.addRecipe(new ItemStack(infuserPit), "w w", "xzx", "xyx",
				'x', Blocks.iron_block, 'y', Blocks.crafting_table, 'z', Blocks.diamond_block, 'w', Items.golden_pickaxe);



		
		fusionReactor2 = new BlockFusionReactor2().setBlockName("fusionReactor2").setHardness(10.0F).setStepSound(Block.soundTypeMetal).setCreativeTab(tabDacin).setResistance(100.0F);
		GameRegistry.registerBlock(fusionReactor2, "fusionReaktor2");
		GameRegistry.registerTileEntity(TileFusionReactor2.class, modid + ".entity.fusionReaktor2");

		reactorWall = new BlockReactorWall().setBlockName("reactorWall").setHardness(10.0F).setStepSound(Block.soundTypeMetal).setCreativeTab(tabDacin).setResistance(100.0F);
		GameRegistry.registerBlock(reactorWall, "reactorWall");
		GameRegistry.registerTileEntity(TileReactorWall.class, modid + ".entity.reactorWall");

		steamPipe = new BlockSteamPipe().setBlockName("steamPipe").setHardness(10.0F).setStepSound(Block.soundTypeMetal).setCreativeTab(tabDacin).setResistance(100.0F);
		GameRegistry.registerBlock(steamPipe, "steamPipe");
		GameRegistry.registerTileEntity(TileSteamPipe.class, modid + ".entity.steamPipe");

		neutronizer = new BlockNeutronizer().setBlockName("neutronizer").setHardness(10.0F).setStepSound(Block.soundTypeMetal).setCreativeTab(tabDacin).setResistance(100.0F);
		GameRegistry.registerBlock(neutronizer, "neutronizer");
		GameRegistry.registerTileEntity(TileNeutronizer.class, modid + ".entity.neutronizer");
		
		electrolyzer = new BlockElectrolyzer().setBlockName("electrolyzer").setHardness(10.0F).setStepSound(Block.soundTypeMetal).setCreativeTab(tabDacin).setResistance(100.0F);
		GameRegistry.registerBlock(electrolyzer, "electrolyzer");
		GameRegistry.registerTileEntity(TileElectrolyzer.class, modid + ".entity.electrolyzer");
		

		
		GameRegistry.addRecipe(new ItemStack(this.steamPipe, 28), "zzz", "xyx", "zzz",
				'x', IC2Items.getItem("waterCell"), 'y', IC2Items.getItem("reactorVent"), 'z', IC2Items.getItem("plateiron"));
		GameRegistry.addRecipe(new ItemStack(this.reactorWall, 1), "yyy", "xzx", "yyy",
				'x', IC2Items.getItem("reactorReflector"), 'y', IC2Items.getItem("reactorPlating"), 'z', IC2Items.getItem("magnetizer") );
		GameRegistry.addRecipe(new ItemStack(this.fusionReactor2), "yxy", "wzw", "axa",
				'x',IC2Items.getItem("reactorCoolantSix") , 'y',IC2Items.getItem("cell") , 'z', IC2Items.getItem("reactorChamber"),'w',  IC2Items.getItem("teslaCoil"), 'a', IC2Items.getItem("compressor"));
		if(graviSuitMagnetron!= null){
			GameRegistry.addRecipe(new ItemStack(this.neutronizer), "zyz", "xwx", "zyz",
					'x', IC2Items.getItem("reactorReflectorThick"), 'y', IC2Items.getItem("centrifuge"), 'z', IC2Items.getItem("extractor"), 'w', graviSuitMagnetron);
			
		}
		GameRegistry.addRecipe(new ItemStack(this.neutronizer), "zxz", "xyx", "zxz",
				'x', IC2Items.getItem("reactorReflectorThick"), 'y', IC2Items.getItem("centrifuge"), 'z', IC2Items.getItem("extractor"));
		
		GameRegistry.addRecipe(new ItemStack(this.electrolyzer), "yxy", "w w", "zxz",
				'x', IC2Items.getItem("teslaCoil"), 'y', IC2Items.getItem("pump"), 'z', Items.cauldron, 'w', IC2Items.getItem("electrolyzer"));
		
		steamDistributor = new BlockSteamDistributor().setHardness(20.0F).setStepSound(Block.soundTypeAnvil).setBlockName("steamDistributor").setCreativeTab(tabDacin).setResistance(100.0F).setBlockTextureName("survivalmod:steamDistributor");
		GameRegistry.registerBlock(steamDistributor, "steamDistributor");
		GameRegistry.registerTileEntity(TileSteamDistributor.class, modid + ".entity.steamDistributor");

		GameRegistry.addRecipe(new ItemStack(this.steamDistributor), "ywy", "yxy", "zyz",
				'x', Items.ender_pearl, 'y', IC2Items.getItem("denseplateiron"), 'z', IC2Items.getItem("coil"), 'w', IC2Items.getItem("waterCell"));

		// turbine
			turbineGlass = new BlockTurbineGlass().setBlockName("turbineGlass").setHardness(10.0F).setStepSound(Block.soundTypeMetal).setCreativeTab(tabDacin).setResistance(100.0F);
			GameRegistry.registerBlock(turbineGlass, "turbineGlass");
			GameRegistry.registerTileEntity(TileTurbineGlass.class, modid + ".entity.turbineGlass");
			
			turbineFrame = new BlockTurbineFrame().setBlockName("turbineFrame").setHardness(10.0F).setStepSound(Block.soundTypeMetal).setCreativeTab(tabDacin).setResistance(100.0F);
			GameRegistry.registerBlock(turbineFrame, "turbineFrame");
			GameRegistry.registerTileEntity(TileTurbineFrame.class, modid + ".entity.turbineFrame");
			
			turbinePowerport = new BlockTurbinePowerport().setBlockName("turbinePowerport").setHardness(10.0F).setStepSound(Block.soundTypeMetal).setCreativeTab(tabDacin).setResistance(100.0F);
			GameRegistry.registerBlock(turbinePowerport, "turbinePowerport");
			GameRegistry.registerTileEntity(TileTurbinePowerport.class, modid + ".entity.turbinePowerport");
			
			turbineBase = new BlockTurbineRotorbase().setBlockName("turbineRotorbase").setHardness(10.0F).setStepSound(Block.soundTypeMetal).setCreativeTab(tabDacin).setResistance(100.0F);
			GameRegistry.registerBlock(turbineBase, "turbineBase");
			GameRegistry.registerTileEntity(TileTurbineRotorbase.class, modid + ".entity.turbineRotorbase");
			
			turbineRotor = new BlockTurbineRotor().setBlockName("turbineRotor").setHardness(10.0F).setStepSound(Block.soundTypeMetal).setCreativeTab(tabDacin).setResistance(100.0F);;
			GameRegistry.registerBlock(turbineRotor, "turbineRotor");
			GameRegistry.registerTileEntity(TileTurbineRotor.class, modid + ".entity.turbineRotor");
			
			turbineFluidport = new BlockTurbineFluidport().setBlockName("turbineFluidport").setHardness(10.0F).setStepSound(Block.soundTypeMetal).setCreativeTab(tabDacin).setResistance(100.0F);;
			GameRegistry.registerBlock(turbineFluidport, "turbineFluidport");
			GameRegistry.registerTileEntity(TileTurbineFluidport.class, modid + ".entity.turbineFluidport");
			
			GameRegistry.addRecipe(new ItemStack(turbineFluidport), " z ", " x ", "y y",
					'x', turbineFrame, 'y', IC2Items.getItem("compressor"), 'z', IC2Items.getItem("pump"));
			
			GameRegistry.addRecipe(new ItemStack(turbinePowerport), "yyy", " x ", "   ",
					'x', turbineFrame, 'y', IC2Items.getItem("glassFiberCableItem"));
			
			CraftingManagerInfuser.addRecipe(new ItemStack(turbineBase), "bbbdb", "yyacb", "yxxxz", "yyacb", "bbbdb",
					'x', turbineFrame, 'y', graviSuitCoolingCore, 'z', IC2Items.getItem("iridiumDrill"), 'a', IC2Items.getItem("advancedCircuit"), 'b', IC2Items.getItem("denseplateobsidian"), 'c', IC2Items.getItem("advancedMachine"), 'd', IC2Items.getItem("carbonPlate"));
			
			
			GameRegistry.addRecipe(new ItemStack(turbineFrame), " y ", "yxy", " y ",
					'x', IC2Items.getItem("reinforcedStone"), 'y', IC2Items.getItem("denseplateiron"));
			GameRegistry.addRecipe(new ItemStack(turbineGlass), " y ", "yxy", " y ",
					'x', IC2Items.getItem("reinforcedGlass"), 'y', IC2Items.getItem("denseplateiron"));
			
			CraftingManagerInfuser.addRecipe(new ItemStack(turbineRotor), " zyz ", " ywy ", "ywxwy", " ywy ", " zyz ",
					'x', IC2Items.getItem("advancedMachine"), 'y', IC2Items.getItem("carbonPlate"), 'z', IC2Items.getItem("advancedAlloy"), 'w', IC2Items.getItem("generator"));
	}

	private void doMobs() {
		EntityRegistry.registerGlobalEntityID(EntityMath.class, "Math", (modEntityID = EntityRegistry.findGlobalUniqueEntityId()), 3515848, 12102);
		EntityRegistry.registerModEntity(EntityMath.class, "Math", modEntityID, this, 80, 3, true);
		EntityRegistry.addSpawn(EntityMath.class, 1, 2, 6, EnumCreatureType.monster, BiomeGenBase.plains, BiomeGenBase.forest, BiomeGenBase.forestHills, BiomeGenBase.savanna, BiomeGenBase.taiga, BiomeGenBase.taigaHills);
		// LanguageRegistry.instance().addStringLocalization("entity.survivalmod.Math.name",
		// "Math");

		EntityRegistry.registerGlobalEntityID(EntityRune.class, "Rune", ++modEntityID);
		EntityRegistry.registerModEntity(EntityRune.class, "Rune", modEntityID, this, 64, 10, true);

		EntityRegistry.registerGlobalEntityID(ProjectileMath.class, "ProjectileMath", ++modEntityID);
		EntityRegistry.registerModEntity(ProjectileMath.class, "ProjectileMath", modEntityID, this, 64, 10, true);
	}

	private void doFluids() {
		hydrogen = new Fluid("hydrogen").setDensity(1).setGaseous(true);
		FluidRegistry.registerFluid(hydrogen);
		hydrogen = FluidRegistry.getFluid("hydrogen");
		
		hFluidBlock = new BlockHydroFluid(hydrogen, Material.lava).setBlockName("hydrogenGas");
		GameRegistry.registerBlock(hFluidBlock, "hydrogenGas");
		if(hydrogen.getBlock() == null){
			hydrogen.setBlock(hFluidBlock);
		}

		
		tritiumPlasma = new Fluid("tritium_plasma").setDensity(2).setGaseous(true).setTemperature(100000000);
		FluidRegistry.registerFluid(tritiumPlasma);
		tritiumPlasma = FluidRegistry.getFluid("tritium_plasma");
		
		tFluidBlock = new BlockHydroFluid(tritiumPlasma, Material.lava).setBlockName("tritiumPlasma");
		GameRegistry.registerBlock(tFluidBlock, "tritiumPlasma");
		if(tritiumPlasma.getBlock() == null){
			tritiumPlasma.setBlock(tFluidBlock);
		}

		deuteriumPlasma = new Fluid("deuterium_plasma").setDensity(3).setGaseous(true).setTemperature(100000001);
		FluidRegistry.registerFluid(deuteriumPlasma);
		deuteriumPlasma = FluidRegistry.getFluid("deuterium_plasma");
		
		dFluidBlock = new BlockHydroFluid(deuteriumPlasma, Material.lava).setBlockName("deuteriumPlasma");
		GameRegistry.registerBlock(dFluidBlock, "deuteriumPlasma");
		if(deuteriumPlasma.getBlock() == null){
			tritiumPlasma.setBlock(dFluidBlock);
		}

	}



	public void addRenderer(Map map)
	{
		map.put(ProjectileMath.class, new RenderProjectileMath());
		map.put(EntityRune.class, new RenderRunic(runicStaffIcon));
	}

	public static CreativeTabs tabDacin = new CreativeTabs("DacinMod") {
		@Override
		public ItemStack getIconItemStack() {
			return new ItemStack(fusionReactor2, 1, 0);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Item.getItemFromBlock(fusionReactor2);
		}
	};

}