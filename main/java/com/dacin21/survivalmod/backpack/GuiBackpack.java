package com.dacin21.survivalmod.backpack;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiBackpack extends GuiContainer{

	private static final ResourceLocation backpackGuiTextures = new ResourceLocation("survivalmod:textures/gui/backpack.png");
    private ItemStack backpackStack;

    public GuiBackpack(EntityPlayer par1, ItemStack stack)
    {
        super(new ContainerBackpack(par1, stack));
        this.backpackStack = stack;
        this.ySize = 195;
    }
    
    @Override
    public void actionPerformed(GuiButton button){
    	//enchantPacket used for Client -> Server communication
    	this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, button.id);
    }
    

    public void initGui(){
    	super.initGui();
	    int posX = (this.width - this.xSize) / 2;
		int posY = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(0, posX + 10, posY + 10, 10, 10, "↑"));
		this.buttonList.add(new GuiButton(1, posX + 10, posY + 91, 10, 11, "↓"));
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        String s = I18n.format("Backpack", new Object[0]);
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        //this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(backpackGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }

}
