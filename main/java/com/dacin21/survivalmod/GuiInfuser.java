package com.dacin21.survivalmod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;



@SideOnly(Side.CLIENT)
public class GuiInfuser extends GuiContainer
{
    public static String GuiTexturePrefix= "survivalmod" + ":" + "textures/gui/";
    public static ResourceLocation infuserTextures = new ResourceLocation(GuiTexturePrefix + "Infuser.png");
    		
    public GuiInfuser(InventoryPlayer par1InventoryPlayer, World par2World, int par3, int par4, int par5)
    {
        super(new ContainerInfuser(par1InventoryPlayer, par2World, par3, par4, par5));
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
             this.fontRendererObj.drawString(StatCollector.translateToLocal("\u00a76Infuser"), 120, 5, 0x404040);
             this.fontRendererObj.drawString(StatCollector.translateToLocal("\u00a76Pit"), 116, 20, 0x404040);
             //this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 - 14, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
             GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
             mc.renderEngine.bindTexture(infuserTextures);
             int l = (width - xSize) / 2;
             int i1 = (height - ySize) / 2;
             drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
    }

}

