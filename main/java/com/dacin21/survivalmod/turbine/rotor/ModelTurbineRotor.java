package com.dacin21.survivalmod.turbine.rotor;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;


public class ModelTurbineRotor extends ModelBase
{
  //fields
    ModelRenderer body;
    ModelRenderer[] rotors = new ModelRenderer[32];
    private static final float Pi16 = (float) (Math.PI * 2 / 16);
  
  public ModelTurbineRotor()
  {
	textureWidth = 64;
	textureHeight = 32;
	
	  body = new ModelRenderer(this, 0, 0);
      body.addBox(-8F, 0F, -1F, 16, 2, 2);
      body.setRotationPoint(0F, 0F, 0F);
      body.setTextureSize(64, 32);
      body.mirror = true;
	
	for(int i = 0; i < 32; i++){
		rotors[i] = new ModelRenderer(this, 0, 0);
		rotors[i].addBox(0.0F, -0.5F, -0F, 3, 1, 8+i/2);
		rotors[i].setRotationPoint(-4.0f * (i& 3) + 4.0f, 0.0f, 0.0f);
		rotors[i].setTextureSize(64, 32);
	}
  }
  
  public void render(int jbase, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render((Entity)null, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, (Entity)null);
    body.render(f5);
    if(jbase != -1){
	    for(float i = 0;i < 16 * Pi16 ; i+= Pi16){
	    	for(int j = jbase; j < 4 + jbase && j < rotors.length; j++){
	    		ModelRenderer r = rotors[j];
	    		setRotation(r,i,0.0f,0.0f);
	    		r.render(f5);
	    	}
	    }
    }
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }

}
