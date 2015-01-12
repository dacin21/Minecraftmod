package com.dacin21.survivalmod.reactor.producion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiElectrolyzer extends GuiContainer
{
    private static final ResourceLocation electroGuiTextures = new ResourceLocation("survivalmod:textures/gui/Electrolyzer.png");
    private TileElectrolyzer tileElectro;
    private static final String __OBFID = "CL_00000758";

    public GuiElectrolyzer(InventoryPlayer p_i1091_1_, TileElectrolyzer p_i1091_2_)
    {
        super(new ContainerElectrolyzer(p_i1091_1_, p_i1091_2_));
        this.tileElectro = p_i1091_2_;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        String s = I18n.format("Electrolyzer", new Object[0]);
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(electroGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        //fluidTanks
        int off = this.tileElectro.waterTank.getFluidAmount() * 54 / tileElectro.waterTank.getCapacity();
        this.drawTexturedModalRect(k+37, l+16 + 54-off, 176, 0 + 54-off, 18, off);
        
        off = this.tileElectro.hydrogenTank.getFluidAmount() * 54 / tileElectro.hydrogenTank.getCapacity();
        this.drawTexturedModalRect(k+91, l+16 + 54-off, 194, 0 + 54-off, 18, off);
        

        
    }
}



