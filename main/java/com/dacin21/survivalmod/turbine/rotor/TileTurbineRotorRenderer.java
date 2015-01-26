package com.dacin21.survivalmod.turbine.rotor;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class TileTurbineRotorRenderer extends TileEntitySpecialRenderer {

	// The model of your block
	private final ModelTurbineRotor model;

	public TileTurbineRotorRenderer() {
		this.model = new ModelTurbineRotor();
	}

	private void adjustRotatePivotViaMeta(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		GL11.glPushMatrix();
		GL11.glRotatef(meta * (-90), 0.0F, 0.0F, 1.0F);
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale) {
		int pushcount = 0;
		GL11.glPushMatrix();
		pushcount++;
		ResourceLocation textures = new ResourceLocation("survivalmod", "textures/entity/Block_Rotor.png");
		Minecraft.getMinecraft().renderEngine.bindTexture(textures);
		

		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
    	GL11.glPushMatrix();
		pushcount++;
		int jbase = -1;
	    if(te!= null && te instanceof TileTurbineRotor){
	    	TileTurbineRotor tileRotor = (TileTurbineRotor) te;
	    	if(tileRotor.hasMaster()){
		    	if(tileRotor.getMaster()!= null && tileRotor.getMaster().isBurning()){
		    		float rotation = tileRotor.getMaster().frameCounter;
		    		tileRotor.getMaster().frameCounter++;
		    		switch(tileRotor.direction){
		    			case 0:
				    		GL11.glRotatef(rotation, -1.0F, 0.0F, 0.0F);
		    				break;
		    			case 1:
				    		GL11.glRotatef(rotation, 0.0F, 0.0F, 1.0F);
				    		break;
		    			case 2:
				    		GL11.glRotatef(rotation, 1.0F, 0.0F, 0.0F);
				    		break;
		    			case 3:
				    		GL11.glRotatef(rotation, 0.0F, 0.0F, -1.0F);
				    		break;
		    		}
		    		GL11.glPushMatrix();
		    		pushcount++;
		    	}
		    	GL11.glRotatef(90.0F * tileRotor.direction, 0.0F, 1.0F, 0.0F);
		    	jbase=4 * tileRotor.pos;
	    	}
	    }
		GL11.glPushMatrix();
		pushcount++;
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F); // upside down fix
		
		this.model.render(jbase, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		for(;pushcount>0;pushcount--){
			GL11.glPopMatrix();
		}
	}

	// Set the lighting stuff, so it changes it's brightness properly.
	private void adjustLightFixture(World world, int i, int j, int k, Block block) {
		Tessellator tess = Tessellator.instance;
		// float brightness = block.getBlockBrightness(world, i, j, k);
		// As of MC 1.7+ block.getBlockBrightness() has become
		// block.getLightValue():
		float brightness = block.getLightValue(world, i, j, k);
		int skyLight = world.getLightBrightnessForSkyBlocks(i, j, k, 0);
		int modulousModifier = skyLight % 65536;
		int divModifier = skyLight / 65536;
		tess.setColorOpaque_F(brightness, brightness, brightness);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) modulousModifier, divModifier);
	}
}