package com.dacin21.survivalmod.tileentityblock;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



@SideOnly(Side.CLIENT)
public class GuiCentrifuge extends GuiContainer
{
    public static String GuiTexturePrefix= "survivalmod" + ":" + "textures/gui/";
    private ResourceLocation survivalmod = new ResourceLocation(GuiTexturePrefix + "Centrifuge.png");
    private TileCentrifuge centrifugeTile;
    		
    public GuiCentrifuge(InventoryPlayer par1InventoryPlayer, TileCentrifuge par2)
    {
        super(new ContainerCentrifuge(par1InventoryPlayer, par2));
        centrifugeTile = par2;
        System.out.println(this.centrifugeTile);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    public void onGuiClosed()
    {
             super.onGuiClosed();
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
             this.fontRendererObj.drawString(StatCollector.translateToLocal("\u00a76Protonizer"), 120, 5, 0x404040);
             //this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 - 14, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
             GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
             mc.renderEngine.bindTexture(survivalmod);
             int l = (width - xSize) / 2;
             int i1 = (height - ySize) / 2;
             drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
             

             if (this.centrifugeTile.isBurning())
             {
                 int i3 = this.centrifugeTile.getBurnTimeRemainingScaled(13);
                 this.drawTexturedModalRect(l + 56, i1 + 36 + 12 - i3, 176, 12 - i3, 14, i3 + 1);
                 int i2 = this.centrifugeTile.getCookProgressScaled(59 + 23);
                 //59x4
                 this.drawTexturedModalRect(l + 38, i1 + 23, 0, 166, i2, 4);
                 if((i2-=59)>0) this.drawTexturedModalRect(l + 77, i1 + 27, 39, 170, 45, i2);
                 
             }
    }

}


