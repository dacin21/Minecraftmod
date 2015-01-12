package com.dacin21.survivalmod.turbine.block;


import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTurbine extends GuiContainer
{
    private static final ResourceLocation exchangerGuiTextures = new ResourceLocation("survivalmod:textures/gui/Turbine.png");
    private TileTurbineRotorbase tileTurbine;
    private static final String __OBFID = "CL_00000758";

    public GuiTurbine(InventoryPlayer p_i1091_1_, TileTurbineRotorbase p_i1091_2_)
    {
        super(new ContainerTurbine(p_i1091_1_, p_i1091_2_));
        this.tileTurbine = p_i1091_2_;
    }
    
    public GuiTurbine(InventoryPlayer player, TileTurbineRotorbase turbine, TileEntity serv)
    {
    	super(new ContainerTurbine(player, turbine, serv));
    	this.tileTurbine = turbine;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        String s = I18n.format("Turbine", new Object[0]);
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(exchangerGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        if (this.tileTurbine.steamTank!=null && this.tileTurbine.steamTank.getFluidAmount() != 0)
        {
        	int i1 = (this.tileTurbine.steamTank.getFluidAmount()-1) * 50 / this.tileTurbine.steamTank.getCapacity();
            this.drawTexturedModalRect(k + 109, l + 68 -i1, 0 , 215 - i1, 18, i1+1);
        }
    }
}
