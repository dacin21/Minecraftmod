package com.dacin21.survivalmod.reactor.block;



import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiSteamDistributor extends GuiContainer
{
    private static final ResourceLocation exchangerGuiTextures = new ResourceLocation("survivalmod:textures/gui/SteamDistributor.png");
    private TileSteamDistributor tileDistributor;
    private ContainerSteamDistributor containerDistributor;
    private static final String __OBFID = "CL_00000758";
    
    private static final int GUIYOFFSET = 7;

    public GuiSteamDistributor(InventoryPlayer p_i1091_1_, TileSteamDistributor p_i1091_2_)
    {
        super(new ContainerSteamDistributor(p_i1091_1_, p_i1091_2_));
        this.tileDistributor = p_i1091_2_;
        this.containerDistributor = (ContainerSteamDistributor)this.inventorySlots;
    }
    @Override
    public void initGui(){
    	super.initGui();
	    int posX = (this.width - this.xSize) / 2;
		int posY = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(0, posX + 10, posY + 10 + GUIYOFFSET, 20, 20, "x++"));
		this.buttonList.add(new GuiButton(1, posX + 10, posY + 40 + GUIYOFFSET, 20, 20, "x--"));

		this.buttonList.add(new GuiButton(2, posX + 40, posY + 10 + GUIYOFFSET, 20, 20, "y++"));
		this.buttonList.add(new GuiButton(3, posX + 40, posY + 40 + GUIYOFFSET, 20, 20, "y--"));

		this.buttonList.add(new GuiButton(4, posX + 70, posY + 10 + GUIYOFFSET, 20, 20, "z++"));
		this.buttonList.add(new GuiButton(5, posX + 70, posY + 40 + GUIYOFFSET, 20, 20, "z--"));
		
		this.buttonList.add(new GuiButton(6, posX + 110, posY + 10 + GUIYOFFSET, 50, 20, "Boiler Direction"));
		this.buttonList.add(new GuiButton(7, posX + 110, posY + 40 + GUIYOFFSET, 50, 20, "Fill Direction"));
    }
    
    @Override
    public void actionPerformed(GuiButton button){
    	//enchantPacket used for Client -> Server communication
    	this.mc.playerController.sendEnchantPacket(this.containerDistributor.windowId, button.id);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        String s = I18n.format("Steam Distributor", new Object[0]);
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
       
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(exchangerGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        
        //Button variables
        this.fontRendererObj.drawString(I18n.format(String.valueOf(this.tileDistributor.xCoord + this.tileDistributor.xOff), new Object[0]), (this.width - this.xSize) / 2 + 10, (this.height - this.ySize) / 2 + 30 + GUIYOFFSET, 4210752);

        this.fontRendererObj.drawString(I18n.format(String.valueOf(this.tileDistributor.yCoord + this.tileDistributor.yOff), new Object[0]), (this.width - this.xSize) / 2 + 40, (this.height - this.ySize) / 2 + 30 + GUIYOFFSET, 4210752);

        this.fontRendererObj.drawString(I18n.format(String.valueOf(this.tileDistributor.zCoord + this.tileDistributor.zOff), new Object[0]), (this.width - this.xSize) / 2 + 70, (this.height - this.ySize) / 2 + 30 + GUIYOFFSET, 4210752);

        this.fontRendererObj.drawString(I18n.format(this.tileDistributor.directionIn.toString(), new Object[0]), (this.width - this.xSize) / 2 + 100, (this.height - this.ySize) / 2 + 30 + GUIYOFFSET, 4210752);
        
        this.fontRendererObj.drawString(I18n.format(this.tileDistributor.directionFillOut.getOpposite().toString(), new Object[0]), (this.width - this.xSize) / 2 + 100, (this.height - this.ySize) / 2 + 60 + GUIYOFFSET, 4210752);
    }
}

